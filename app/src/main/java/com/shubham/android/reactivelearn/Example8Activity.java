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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Filter operator
 * <p>
 * example : we are going to create an observable which emits only the female users only
 */
public class Example8Activity extends AppCompatActivity {

    private static final String TAG = Example8Activity.class.getSimpleName();
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable<User> userObservable = getUserObservable();
        disposable.add(
                userObservable
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter(new Predicate<User>() {
                            @Override
                            public boolean test(User user) throws Exception {

                                return user.getGender().equalsIgnoreCase("female");
                            }
                        }).subscribeWith(new DisposableObserver<User>() {
                    @Override
                    public void onNext(User user) {
                        Log.d(TAG, "onNext: user Name " + user.getName() + " \n user gender " + user.getGender());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                }));
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
