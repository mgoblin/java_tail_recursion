package ru.mg.funclist;

import java.util.*;

public class FunctionalUtils {

    private FunctionalUtils() {
    }

    public static  <E> E head(Collection<E> collection) {
        if (Objects.requireNonNull(collection).isEmpty()) {
            throw new IllegalArgumentException("empty doesn't have head");
        }
        return collection.iterator().next();
    }

    public static <E> List<E> tail(List<E> list) {
        if (Objects.requireNonNull(list).isEmpty()) {
            throw new IllegalArgumentException("empty doesn't have tail");
        } else if (list.size() == 1){
            return new ArrayList<>();
        } else {
            ArrayList<E> result = new ArrayList<>(list);
            result.remove(0);
            return result;
        }


    }
}