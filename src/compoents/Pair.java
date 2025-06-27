package compoents;

public class Pair <E>{
    private final E first;
    private final E second;

    public Pair(E first, E second) {
        this.first = first;
        this.second = second;
    }

    public E getSecond() {
        return second;
    }

    public E getFirst() {
        return first;
    }
}
