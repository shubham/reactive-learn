package com.shubham.android.reactivelearn;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.observables.MathObservable;

/**
 * RxJava Mathematical Operators
 * <p>
 * Mathematical operations on custom data types
 * <p>
 * in this example we are going to find the elderly person in a list.
 * for this we create a person data type with name and age attributes.
 * <p>
 * using the Comparator.comparing() we can easily creates an Observable
 * that emits the max aged person in the list
 */
public class Example13Activity extends AppCompatActivity {

    private static final String TAG = Example13Activity.class.getSimpleName();

    private List<Person> getPersons() {

        List<Person> people = new ArrayList<>();
        Person person1 = new Person("shubham", 22);
        people.add(person1);

        Person person2 = new Person("Mishra", 23);
        people.add(person2);

        Person person = new Person("Mayur", 24);
        people.add(person);

        return people;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Person> personList = new ArrayList<>(getPersons());

        Observable<Person> personObservable = Observable.from(personList);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            MathObservable.from(personObservable)
                    .min(Comparator.comparing(Person::getAge))
                    .subscribe(new Observer<Person>() {
                        @Override
                        public void onCompleted() {
                            Log.d(TAG, "onCompleted: ");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError: ", e);
                        }

                        @Override
                        public void onNext(Person person) {
                            Log.d(TAG, "Person with max age: " + person.getName()
                                    + " , " + person.getAge() + " yrs");
                        }
                    });
        }

    }

    class Person {
        String name;
        Integer age;

        Person(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
}
