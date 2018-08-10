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
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Rx Java Operators
 * <p>
 * Map, FlatMap, ConcatMap, SwitchMap
 * <p>
 * Article summarise the usage of each operator, difference between them and use case scenarios
 * which will help in choosing the best operator that fulfills your requirement.
 * <p>
 * these operators modify the results emitted by the observables.
 * <p>
 * 1) Map: Modifies each item by the source observable and emits the modified item.
 * <p>
 * 2) FlatMap, SwitchMap, ConcatMap: also applies a function on each item emitted by an observable
 * but instead of returning the modified item, it returns the observable which will returns the
 * observable which will emit data again.
 * <p>
 * FlatMap and ConcatMap will work pretty much in the same way. They merges items emitted by multiple
 * observables and returns a Single Observable.
 * <p>
 * the difference between FlatMap and ConcatMap is the order in which they emit the data.
 * <p>
 * FlatMap can interleave items while emitting i.e the emitted items order is not maintained.
 * <p>
 * Concat Map preserves the order of items .But the main Disadvantage of concatMap is, it has to wait
 * for each observable to complete its work thus Asynchronous is not maintained.
 * <p>
 * <p>
 * 3) SwitchMap : it is a bit different from FlatMap, ConcatMap.
 * SwitchMap Unsubscribe from previous source Observable whenever new item started emitting ,
 * thus always emitting from the current Observable.
 * <p>
 * Lets see the Example below of each above, for better understanding.
 */
public class Example15Activity extends AppCompatActivity {

    private final static String TAG = Example15Activity.class.getSimpleName();
    private Disposable disposable;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //map Operator
        //it transform each item and emits the modified item

        //so for example we are considering
        //suppose we are getting a User which contains the age and name as values in a network call
        //but it is missing a value(email) which we require
        //so by map operator we add the email value in User object
        // return this value.
        getUserObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<User, User>() {
                    @Override
                    public User apply(User user) throws Exception {
                        //modifying the User object , adding the email with each user object
                        user.setEmail(String.format("%s@gmail.com", user.getName()));
                        user.setName(user.getName().toUpperCase());
                        return user;
                    }
                }).subscribe(new Observer<User>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(User user) {
                Log.d(TAG, "onNext: User value : \n Name :" + user.getName()
                        + "\n Gender :" + user.getGender()
                        + "\n Email :" + user.getEmail());
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

    /**
     * Suppose this method is returning the User Observable, after making a api call and fetching
     * users from the response
     *
     * @return
     */
    private Observable<User> getUserObservable() {

        String[] users = new String[]{"shubham", "mayur", "mishra"};

        final List<User> userList = new ArrayList<>();
        for (String name : users) {
            User user = new User();
            user.setGender("male");
            user.setName(name);
            userList.add(user);
        }


        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                for (User user : userList) {
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
        String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

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
