package com.github.jfwilson.jsonpp;

import com.github.jfwilson.rxjson.CharSequenceSubscriber;
import com.github.jfwilson.rxjson.JavaObjectTypeHandler;
import rx.Observable;
import rx.Subscriber;

public class RxJson {
    public static Observable.Operator<Object, CharSequence> fromCharSequencesToJavaObject() {
        return JavaObjectCharSequenceSubscriber::new;
    }

    public static class JavaObjectCharSequenceSubscriber extends CharSequenceSubscriber {

        private final Subscriber<? super Object> subscriber;

        public JavaObjectCharSequenceSubscriber(Subscriber<? super Object> subscriber) {
            super(new JavaObjectTypeHandler(subscriber::onNext));
            this.subscriber = subscriber;
        }

        @Override
        public void onError(Throwable e) {
            subscriber.onError(e);
        }

        @Override
        public void onCompleted() {
            super.onCompleted();
            subscriber.onCompleted();
        }
    }
}
