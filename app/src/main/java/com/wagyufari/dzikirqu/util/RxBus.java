package com.wagyufari.dzikirqu.util;


import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Aditya Naufal on 2/06/2017.
 */
public class RxBus {

    private static RxBus rxBus;
    private final PublishSubject<Object> bus = PublishSubject.create();

    public static RxBus getDefault(){
        if (rxBus==null){
            rxBus = new RxBus();
        }
        return rxBus;
    }

    public void send(Object o) {
        bus.onNext(o);
    }

    public Observable<Object> toObservables() {
        return bus;
    }
}
