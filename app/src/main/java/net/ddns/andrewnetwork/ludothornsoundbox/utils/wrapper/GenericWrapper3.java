package net.ddns.andrewnetwork.ludothornsoundbox.utils.wrapper;

public class GenericWrapper3<T1, T2, T3> extends GenericWrapper2<T1, T2> {

    private T3 source3;

    public GenericWrapper3(T1 source1, T2 source2, T3 source3) {
        super(source1, source2);

        this.source3 = source3;
    }

    public T3 getThirdSource() {
        return source3;
    }
}
