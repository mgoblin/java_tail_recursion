package ru.mg.tailrec;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.mg.funclist.FunctionalUtils.tail;
import static ru.mg.tailrec.TailCalls.done;

class TailCallTest {

    private static class EmptyTailCall<T> implements TailCall<T> {
        @Override
        public TailCall<T> apply() {
            return null;
        }

        @Override
        public boolean isComplete() {
            return true;
        }

        @Override
        public T result() {
            return null;
        }

    }

    private <E> TailCall<Long> lengthLoopTailRec(List<E> list, long acc) {
        if (list.isEmpty()) {
            return done(acc);
        } else {
            return () -> lengthLoopTailRec(tail(list), acc + 1);
        }
    }

    private <E> Long lengthLoopNaive(List<E> list, long acc) {
        if (list.isEmpty()) {
            return acc;
        } else {
            return lengthLoopNaive(tail(list), acc + 1);
        }
    }

    private <E> long length(List<E> list) {
        return lengthLoopTailRec(list, 0).invoke();
    }

    private <E> long lengthNaive(List<E> list) {
        return lengthLoopNaive(list, 0);
    }

    @Test
    void testInvokeEmpty() {
        EmptyTailCall emptyTailCall = new EmptyTailCall();
        assertThat(emptyTailCall.invoke(), is(nullValue()));
    }

    @Test
    void testInvokeRecursive() {
        List<Integer> ints = IntStream.range(0, 15_000).boxed().collect(Collectors.toList());
        assertThat(length(ints), is(15_000L));

        assertThrows(StackOverflowError.class, () -> lengthNaive(ints));
    }
}