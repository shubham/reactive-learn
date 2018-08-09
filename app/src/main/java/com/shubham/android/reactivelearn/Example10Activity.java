package com.shubham.android.reactivelearn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import rx.Observable;
import rx.Subscriber;
import rx.observables.MathObservable;

/**
 * RxJava  Mathematical Operators ,
 * <p>
 * Mathematical Operators come under a separate Rx java Library
 * <p>
 * implementation 'io.reactivex:rxjava-math:1.0.0'
 * <p>
 * In this example we are going to perform some of the mathematical operators
 * <p>
 * max,min,sum,Average
 * <p>
 * and in the last, mathematical operators for custom data type
 * <p>
 * PS: here use the Observable from rx.observable not from the Rx
 */
public class Example10Activity extends AppCompatActivity {

    private static final String TAG = Example10Activity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Integer[] numbers = {5, 101, 404, 22, 3, 1024, 65};

        Observable<Integer> observable = Observable.from(numbers);
        //max operator
        MathObservable
                .max(observable)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "Max value: " + integer);
                    }
                });

        //Min operator
        MathObservable.min(observable)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "Min Value: " + integer);
                    }
                });

        //Sum operator
        MathObservable.sumInteger(observable)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "Sum : " + integer);
                    }
                });

        //average Operator
        MathObservable.averageInteger(observable)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "Average: " + integer);
                    }
                });


    }
}
