package net.ddns.andrewnetwork.ludothornsoundbox.utils.wrapper;

public class GenericWrapper6<T1, T2, T3, T4, T5, T6> extends GenericWrapper5<T1, T2, T3, T4, T5> {

    private T6 source6;

    public GenericWrapper6(T1 source1, T2 source2, T3 source3, T4 source4, T5 source5, T6 source6) {
        super(source1, source2, source3, source4,source5);

        this.source6 = source6;
    }

    public T6 getSixthSource() {
        return source6;
    }
}
