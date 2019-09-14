package ru.mg.tailrec;



import java.util.stream.Stream;

@FunctionalInterface
public interface TailCall<T> {

    TailCall<T> apply();

    default boolean isComplete() {
        return false;
    }

    default T result() {
        throw new Error("not implemented");
    }

    default T invoke() {
        return Stream.iterate(this, TailCall::apply)
                .filter(TailCall::isComplete)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .result();
    }

    default T invokeWhile() {
        TailCall<T> call = this.apply();
        while (!call.isComplete()) {
            call = call.apply();
        }
        return call.result();
    }

    static <T> TailCall<T> done(final T value) {
        return new TailCall<T>() {
            @Override
            public boolean isComplete() {
                return true;
            }

            @Override
            public T result() {
                return value;
            }

            @Override
            public TailCall<T> apply() {
                throw new Error("not implemented");
            }
        };
    }
}