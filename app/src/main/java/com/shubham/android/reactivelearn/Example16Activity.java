package com.shubham.android.reactivelearn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Example for single Observable with Single Observer
 * <p>
 * Single will always emit values or throws an error.
 * The same job can be done using observable too with a single emission but
 * single always make sure there is an emission.
 * <p>
 * A use case of single would be making a network call to get response as the response will be
 * fetched once.
 * <p>
 * The below example always emits a single Note. Another example could be fetching
 * a Note from database by its Id. Also we need to make sure that the Note is present
 * in database as Single should always emit a value.
 * <p>
 * Notice : Single Observer doesn't have onNext to emit data, instead the data will be received
 * in onSuccess method.
 */
public class Example16Activity extends AppCompatActivity {
    private static final String TAG = Example16Activity.class.getSimpleName();
    private Disposable disposable;

    /**
     * Single Observable emitting single Note
     * Single Observable is more useful in making network calls
     * where you expect a single response object to be emitted
     * -
     * Single : SingleObserver
     */
    // TODO - link to Retrofit  tutorial
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getNoteSingleObservable()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(getSingleObserver());

    }

    private Single<Note> getNoteSingleObservable() {
        return Single.create(new SingleOnSubscribe<Note>() {
            @Override
            public void subscribe(SingleEmitter<Note> emitter) throws Exception {
                Note note = new Note("Hello Shubham", 1);
                emitter.onSuccess(note);
            }
        });
    }

    private SingleObserver<Note> getSingleObserver() {
        return new SingleObserver<Note>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onSuccess(Note note) {
                Log.d(TAG, "onSuccess: " + note.getData());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ", e);
            }
        };
    }

    class Note {
        String data;
        int id;

        public Note(String data, int id) {
            this.data = data;
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
