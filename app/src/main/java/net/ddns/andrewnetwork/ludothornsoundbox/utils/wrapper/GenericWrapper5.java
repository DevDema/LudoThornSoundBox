package net.ddns.andrewnetwork.ludothornsoundbox.utils.wrapper;

public class GenericWrapper5<T1, T2, T3, T4, T5> extends GenericWrapper4<T1, T2, T3, T4> {

    private T5 source5;

    public GenericWrapper5(T1 source1, T2 source2, T3 source3, T4 source4, T5 source5) {
        super(source1, source2, source3, source4);

        this.source5 = source5;
    }

    public T5 getFifthSource() {
        return source5;
    }
}
