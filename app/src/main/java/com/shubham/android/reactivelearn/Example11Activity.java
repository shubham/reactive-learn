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
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Rx java Math Operator
 * <p>
 * Implementing the count Operator
 */
public class Example11Activity extends AppCompatActivity {

    private static final String TAG = Example11Activity.class.getSimpleName();
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Observable<User> userObservable = getUserObservable();

        userObservable
                .filter(new Predicate<User>() {
                    @Override
                    public boolean test(User user) throws Exception {
                        return user.getGender().equalsIgnoreCase("male");
                    }
                })
                .count()
                .subscribe(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onSuccess(Long aLong) {
                        Log.d(TAG, "Male user counts: " + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }
                });
    }


    private Observable<User> getUserObservable() {
        String[] maleUsers = new String[]{"shubham", "mayur", "mishra"};
        String[] femaleUsers = new String[]{"Lucy", "april", "november"};

        final List<User> users = new ArrayList<>();

        for (String name : maleUsers) {
            User user = new User();
            user.setGender("male");
            user.setName(name);
            users.add(user);
        }

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
