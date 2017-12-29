package com.baselib.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;


import com.baselib.log.NLog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Create by pc-qing
 * On 2017/2/15 11:48
 * Copyright(c) 2017 XunLei
 * Description
 */
public class NetworkHelper {

    private static final String TAG = "NetworkHelper";
    public enum NetworkStatus{
        NetworkNotReachable,
        NetworkReachableViaWWAN,
        NetworkReachableViaWiFi,
    }

    public interface NetworkInductor
    {
        void onNetworkChanged(NetworkStatus status);
    }

    private static class HelperHolder {
        private static final NetworkHelper helper = new NetworkHelper();
    }

    public static NetworkHelper sharedHelper()
    {
        return HelperHolder.helper;
    }


    private boolean mRegistered = false;
    private NetworkStatus mStatus = NetworkStatus.NetworkNotReachable;
    private NetworkBroadcastReceiver mReceiver = new NetworkBroadcastReceiver();
    private  List<WeakReference<NetworkInductor>> mInductors;

    private NetworkHelper(){
        //load();
        mInductors = new LinkedList<>();
    }

    /**
     * request android.permission.ACCESS_NETWORK_STATE
     * @param context
     */
    public void registerNetworkSensor(Context context)
    {
        NLog.v(TAG, "registerNetworkSensor");
        if (mRegistered)
            return;

        mRegistered = true;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert manager != null;
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null || !info.isAvailable())
        {
            NLog.i(TAG, "network not reachable");
            mStatus = NetworkStatus.NetworkNotReachable;
        }
        else if (info.getType() == ConnectivityManager.TYPE_MOBILE)
        {
            NLog.i(TAG, "network reachable via wwan");
            mStatus = NetworkStatus.NetworkReachableViaWWAN;

        }
        else if (info.getType() == ConnectivityManager.TYPE_WIFI)
        {
            NLog.i(TAG, "network reachable via wifi");
            mStatus = NetworkStatus.NetworkReachableViaWiFi;
        }

        //native_set_network_status(mStatus.ordinal());

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(mReceiver, intentFilter);
    }

    public void unregisterNetworkSensor(Context context)
    {
        if (!mRegistered)
            return;

        mRegistered = false;
        context.unregisterReceiver(mReceiver);
    }

    public NetworkStatus getNetworkStatus() {
        return mStatus;
    }

    public boolean isWifiActive() {
        return mStatus.equals(NetworkStatus.NetworkReachableViaWiFi);
    }

    public boolean isNetworkAvailable() {
        return !mStatus.equals(NetworkStatus.NetworkNotReachable);
    }

    public void addNetworkInductor(NetworkInductor inductor)
    {
        final List<WeakReference<NetworkInductor>> list = new ArrayList<WeakReference<NetworkInductor>>(mInductors);
        for (int i = 0; i < list.size(); i++) {
            WeakReference<NetworkInductor> inductorRef = list.get(i);
            NetworkInductor ind = inductorRef.get();
            if ( ind == inductor)
                return;
            else if (ind == null) {
                mInductors.remove(inductorRef);
            }
        }

        mInductors.add(new WeakReference<NetworkInductor>(inductor));
    }

    public void removeNetworkInductor(NetworkInductor inductor)
    {
        final List<WeakReference<NetworkInductor>> list = new ArrayList<WeakReference<NetworkInductor>>(mInductors);
        for (int i = 0; i < list.size(); i++) {
            WeakReference<NetworkInductor> inductorRef = list.get(i);
            NetworkInductor ind = inductorRef.get();
            if ( ind == inductor) {
                mInductors.remove(inductorRef);
                return;
            }else if (ind == null) {
                mInductors.remove(inductorRef);
            }
        }
    }

    protected void onNetworkChanged() {
        if (mInductors.size() == 0)
            return;

        final List<WeakReference<NetworkInductor>> list = new ArrayList<WeakReference<NetworkInductor>>(mInductors);
        for (int i = 0; i < list.size(); i++) {
            WeakReference<NetworkInductor> inductorRef = list.get(i);
            NetworkInductor inductor = inductorRef.get();
            if (inductor != null)
                inductor.onNetworkChanged(mStatus);
            else
                mInductors.remove(inductorRef);
        }
    }

    protected class NetworkBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive (Context context, Intent intent)
        {

            NLog.v("NetworkBroadcastReceiver", "onReceive");
            if (intent == null)
                return;

            String action = intent.getAction();
            if (TextUtils.equals(action,ConnectivityManager.CONNECTIVITY_ACTION))
            {
                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                assert manager != null;
                NetworkInfo info = manager.getActiveNetworkInfo();
                NetworkStatus ns = NetworkStatus.NetworkNotReachable;
                if (info == null || !info.isAvailable())
                {
                    NLog.i("NetworkBroadcastReceiver", "network not reachable");
                    ns = NetworkStatus.NetworkNotReachable;
                }
                else if (info.getType() == ConnectivityManager.TYPE_MOBILE)
                {
                    NLog.i("NetworkBroadcastReceiver", "network reachable via wwan");
                    ns = NetworkStatus.NetworkReachableViaWWAN;

                }
                else if (info.getType() == ConnectivityManager.TYPE_WIFI)
                {
                    NLog.i("NetworkBroadcastReceiver", "network reachable via wifi");
                    ns = NetworkStatus.NetworkReachableViaWiFi;
                }

                if (!mStatus.equals(ns)) {
                    mStatus = ns;
                    //native_set_network_status(mStatus.ordinal());
                    onNetworkChanged();
                }
            }
        }
    }
}
