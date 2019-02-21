package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.utils.model;

public class ChiaveValore<T> {
    private int chiave;
    private T valore;

    public int getChiave() {
        return chiave;
    }

    public T getValore() {
        return valore;
    }

    public ChiaveValore(int chiave, T valore) {
        this.chiave = chiave;
        this.valore = valore;
    }
}
