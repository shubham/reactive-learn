package com.shubham.android.reactivelearn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

// count the number of taps in a time using the buffer operator
public class Example6Activity extends AppCompatActivity {

    private static final String TAG = Example6Activity.class.getSimpleName();

    private Button mTapButton;
    private TextView mTapResultTextView;
    private TextView mTapResultMaxCountTextView;

    private Disposable mDisposable;
    private int mMaxTaps = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTapButton = findViewById(R.id.layout_tap_area);
        mTapResultTextView = findViewById(R.id.tap_result);
        mTapResultMaxCountTextView = findViewById(R.id.tap_result_max_count);


        //using RxView
        RxView.clicks(mTapButton)
                .map(new Function<Object, Integer>() {
                    @Override
                    public Integer apply(Object o) throws Exception {
                        return 1;
                    }
                })
                .buffer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<List<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(List<Integer> integers) {
                        Log.d(TAG, "onNext: " + integers.size() + " Taps Received !");
                        if (integers.size() > 0) {
                            mMaxTaps = integers.size() > mMaxTaps ? integers.size() : mMaxTaps;
                            mTapResultTextView.setText(String.format("Received %d taps in 3 Seconds", integers.size()));
                            mTapResultMaxCountTextView.setText(String.format("Maximum of %d taps received ", mMaxTaps));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });

        mTapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
