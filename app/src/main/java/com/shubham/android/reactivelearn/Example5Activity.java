package com.shubham.android.reactivelearn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Basic Observable, Observer, Subscriber example
 * Introduced CompositeDisposable and DisposableObserver
 * The observable emits custom data type (Note) instead of primitive data types
 * ----
 * .map() operator is used to turn the note into all uppercase letters
 * ----
 * You can also notice we got rid of the below declarations
 * Observable<Note> notesObservable = getNotesObservable();
 * DisposableObserver<Note> notesObserver = getNotesObserver();
 */
public class Example5Activity extends AppCompatActivity {
    private static final String TAG = Example5Activity.class.getSimpleName();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable<String> animalObservable = Observable.fromArray(
                "Ant", "Cat", "Dog", "Elephant",
                "Ant", "Ape",
                "Bat", "Bee", "Bear", "Butterfly",
                "Cat", "Crab", "Cod",
                "Dog", "Dove",
                "Fox", "Frog");

//        DisposableObserver<String> animalsObserver = getAnimalsObserver();
//
//        DisposableObserver<String> secondAnimalObserver = getAnimalsObserver();

        /*
         * filter() is used to filter out the animal names starting with `b`
         */
        compositeDisposable.add(
                getNotesObservable().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Notes, Notes>() {
                            @Override
                            public Notes apply(Notes notes) throws Exception {
                                //making the note to all caps

                                notes.setNote(notes.getNote().toUpperCase());
                                return notes;
                            }
                        })
                .subscribeWith(getNotesObserver())
        );

//        /*filter() is used to filter out the animals names start with "c"
//         *map() is used to transform all characters to Upper case
//         */
//        compositeDisposable.add(
//                animalObservable.subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .filter(new Predicate<String>() {
//                            @Override
//                            public boolean test(String s) throws Exception {
//                                return s.toLowerCase().startsWith("c");
//                            }
//                        })
//                        .map(new Function<String, String>() {
//
//                            @Override
//                            public String apply(String s) throws Exception {
//
//                                return s.toUpperCase();
//                            }
//                        })
//                        .subscribeWith(secondAnimalObserver)
//        );


    }

    private DisposableObserver<Notes> getNotesObserver() {
        return new DisposableObserver<Notes>() {

            @Override
            public void onNext(Notes notes) {
                Log.d(TAG, "onNext: Note: " + notes.getNote());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: All notes are emitted");
            }
        };
    }


    private Observable<Notes> getNotesObservable() {
        final List<Notes> notes = prepareNotes();

        return Observable.create(
                new ObservableOnSubscribe<Notes>() {
                    @Override
                    public void subscribe(ObservableEmitter<Notes> emitter) throws Exception {

                        for (Notes note : notes) {
                            if (!emitter.isDisposed())
                                emitter.onNext(note);
                        }

                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }
                    }
                }
        );
    }

    private List<Notes> prepareNotes() {
        List<Notes> notes = new ArrayList<>();
        notes.add(new Notes(1, "buy tooth paste!"));
        notes.add(new Notes(2, "call brother"));
        notes.add(new Notes(3, "watch anime tonight"));
        notes.add(new Notes(5, "pay for taxi"));
        return notes;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    class Notes {
        int id;
        String note;

        public Notes(int id, String note) {
            this.id = id;
            this.note = note;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }
    }

}
