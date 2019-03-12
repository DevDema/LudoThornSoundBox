package net.ddns.andrewnetwork.ludothornsoundbox.utils.wrapper;

public class GenericWrapper2<T1, T2> {

    private T1 source1;
    private T2 source2;

    public GenericWrapper2(T1 source1, T2 source2) {
        this.source1 = source1;
        this.source2 = source2;
    }



    public T1 getFirstSource() {
        return source1;
    }

    public T2 getSecondSource() {
        return source2;
    }
}
