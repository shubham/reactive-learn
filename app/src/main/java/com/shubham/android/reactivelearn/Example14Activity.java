package com.shubham.android.reactivelearn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Rx Java Operators which combines result of two or more observable in to a single observable
 * <p>
 * 1) Concat
 * 2) Merge
 * <p>
 * 1)Concat : this combine or concat two or observables one after the other i.e after one observable are emitted
 * then second observable will emit and then so on till last observable is not combined.
 * <p>
 * 2)Merge :this also combine two or observables one after the other but not in a sequential execution.
 */
public class Example14Activity extends AppCompatActivity {
    private final static String TAG = Example14Activity.class.getSimpleName();
    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        //Concat Operator
//        Observable.concat(getMaleUserObservable(), getFeMaleUserObservable())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<User>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        disposable = d;
//                        Log.d(TAG, "onSubscribe: ");
//                    }
//
//                    @Override
//                    public void onNext(User user) {
//                        Log.d(TAG, "onNext: user name :" + user.getName() + ", Gender " + user.getGender());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e(TAG, "onError: ", e);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG, "onComplete: ");
//                    }
//                });


        //Merge operator
        Observable.merge(getMaleUserObservable(), getFeMaleUserObservable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(User user) {
                        Log.d(TAG, "onNext: user name :" + user.getName() + ", Gender " + user.getGender());
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


    private Observable<User> getMaleUserObservable() {
        String[] maleUsers = new String[]{"shubham", "mayur", "mishra"};

        final List<User> users = new ArrayList<>();

        for (String name : maleUsers) {
            User user = new User();
            user.setGender("male");
            user.setName(name);
            users.add(user);
        }


        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                for (User user : users) {
                    if (!emitter.isDisposed()) {
                        Thread.sleep(500);
                        emitter.onNext(user);
                    }
                }

                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io());

    }

    private Observable<User> getFeMaleUserObservable() {
        String[] femaleUsers = new String[]{"Lucy", "april", "november"};

        final List<User> users = new ArrayList<>();


        for (String name : femaleUsers) {
            User user = new User();
            user.setGender("female");
            user.setName(name);
            users.add(user);
        }


        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                for (User user : users) {
                    if (!emitter.isDisposed()) {
                        Thread.sleep(500);
                        emitter.onNext(user);
                    }
                }

                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    class User {
        String name;
        String gender;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
    }
}
