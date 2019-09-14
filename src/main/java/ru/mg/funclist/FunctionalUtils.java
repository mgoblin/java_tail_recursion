package ru.mg.funclist;

import java.util.*;

public class FunctionalUtils {

    private FunctionalUtils() {
    }

    public static  <E> E head(Collection<E> collection) {
        if (Objects.requireNonNull(collection).isEmpty()) {
            throw new NoSuchElementException("empty doesn't have head");
        }
        return collection.iterator().next();
    }

    public static <E> Collection<E> tail(Collection<E> list) {
        if (Objects.requireNonNull(list).isEmpty()) {
            throw new NoSuchElementException("empty doesn't have tail");
        } else if (list.size() == 1){
            return new ArrayList<>();
        } else {
            ArrayList<E> result = new ArrayList<>(list);
            result.remove(0);
            return result;
        }


    }
}
