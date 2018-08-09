package com.shubham.android.reactivelearn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;

/**
 * Rx java mathematical operators
 * <p>
 * in this example we will implement the Reduce Operator
 * <p>
 * This applies the function on the item emitted by the observable
 * <p>
 * first it applies to the first item, then take the result and feeds back
 * to same function on second item.this process until the last emission.
 * once all items are emitted, it emits the final result
 */
public class Example12Activity extends AppCompatActivity {
    private final static String TAG = Example12Activity.class.getSimpleName();
    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //reduce operator
        //we are going to give range from 1 to 10 and through reduce operator
        // we will calculate the sum of range
        Observable.range(1, 10)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer number, Integer sum) throws Exception {
                        return sum + number;
                    }
                }).subscribe(new MaybeObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
                disposable = d;
            }

            @Override
            public void onSuccess(Integer integer) {
                Log.d(TAG, "Sum of Range from 1 to 10  is : " + integer);
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
