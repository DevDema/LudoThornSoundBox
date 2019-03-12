package net.ddns.andrewnetwork.ludothornsoundbox.utils.wrapper;

public class GenericWrapper4<T1, T2, T3, T4> extends GenericWrapper3<T1, T2, T3> {

    private T4 source4;

    public GenericWrapper4(T1 source1, T2 source2, T3 source3, T4 source4) {
        super(source1, source2, source3);

        this.source4 = source4;
    }

    public T4 getFourthSource() {
        return source4;
    }
}
