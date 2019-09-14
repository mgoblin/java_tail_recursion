package ru.mg.funclist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;

public interface FunctionalUtils {

    static  <E> E head(Collection<E> collection) {
        if (Objects.requireNonNull(collection).isEmpty()) {
            throw new NoSuchElementException("empty doesn't have head");
        }
        return collection.iterator().next();
    }

    static <E> Collection<E> tail(Collection<E> collection) {
        if (Objects.requireNonNull(collection).isEmpty()) {
            throw new NoSuchElementException("empty doesn't have tail");
        } else {
            ArrayList<E> result = new ArrayList<>(collection);
            result.remove(0);
            return result;
        }
    }
}
