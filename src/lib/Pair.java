package lib;

/**
 * Pair
 */
public class Pair<K1, K2> {
    private K1 first;
    private K2 second;

    public Pair(K1 k1, K2 k2) {
        first = k1;
        second = k2;
    }

    public K1 getFirst() {
        return first;
    }

    public K2 getSecond() {
        return second;
    }

    public void setFirst(K1 s) {
        first = s;
    }

    public void setSecond(K2 s) {
        second = s;
    }

}