package com.uwaterloo.uwbubbles.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode
public class Pair<F extends Comparable<F>, S extends Comparable<S>> implements Comparable<Pair<F, S>> {
    public final F first;
    public final S second;

    @Override
    public int compareTo(Pair<F, S> o) {
        var x = first.compareTo(o.first);
        if (x == 0) {
            return second.compareTo(o.second);
        } else {
            return x;
        }
    }

    public static <A extends Comparable<A>, B extends Comparable<B>> Pair<A, B> of(A a, B b) {
        return new Pair<>(a, b);
    }

}
