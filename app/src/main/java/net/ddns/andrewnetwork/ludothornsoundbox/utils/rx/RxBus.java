package net.ddns.andrewnetwork.ludothornsoundbox.utils.rx;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class RxBus {

    private PublishSubject<RxBusEvent> bus;

    public RxBus() {
        bus = PublishSubject.create();
        //da capire come invocare il disposabile al termine della vita del bus
        Disposable d = bus.subscribe(rxBusEvent -> {

        }, throwable -> {

        });
    }

    public void send(RxBusEvent o) {
        try {
            if (o != null)
                bus.onNext(o);
        } catch (Exception e) {
            bus.onError(e.getCause());
        }
    }

    public Disposable subscribeToBus(String eventName, Consumer subscriber) {
        return bus.filter(event -> event.eventName.equals(eventName)
        ).map(event -> event.data).subscribe(subscriber);
    }

    public Disposable subscribeToBus(Consumer<RxBusEvent> subscriber) {
        return bus.subscribe(subscriber);
    }

    public static class RxBusEvent<T> {
        public String eventName;
        public T data;

        public RxBusEvent(String eventName, T data) {
            this.eventName = eventName;
            this.data = data;
        }
    }


}