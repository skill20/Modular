package com.worldunion.partner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.common.net.HttpManager;
import com.common.net.RxObserver;
import com.common.net.RxSchedulers;
import com.common.network.OKHttpUtils;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNet();
            }
        });
    }

    private void initNet() {
//        OKHttpUtils.getInstance().createInterface(ApiServer.class)
//                .getSearchBook("金瓶梅", null, 0, 1)
//                .subscribeOn(Schedulers.io())
//                .subscribe(new RxObserver<Book>("1") {
//                    @Override
//                    protected void onStartRequest() {
//                        String name = Thread.currentThread().getName();
//                        Log.i(TAG, "onStartRequest"+name);
//                    }
//
//                    @Override
//                    protected void onSuccess(Book book) {
//                        String name = Thread.currentThread().getName();
//                        Log.i(TAG, "onSuccess"+name);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i(TAG, "onError");
//                    }
//                });

        Observable<Book> searchBook = HttpManager.getInstance().createInterface(ApiServer.class).getSearchBook("金瓶梅", null, 0, 1);

        Provider.getUserCache().getSearchBook(searchBook,new DynamicKey("username"), new EvictDynamicKey(false))
                .subscribeOn(Schedulers.io())
                .subscribe(new RxObserver<Book>("1") {
                    @Override
                    public void onStartRequest() {
                        String name = Thread.currentThread().getName();
                        Log.i(TAG, "onStartRequest" + name);
                    }

                    @Override
                    public void onSuccess(Book book) {
                        String name = Thread.currentThread().getName();
                        Log.i(TAG, "onSuccess" + name);
                    }

                    @Override
                    public void onError(String s) {

                    }

                });
    }
}
