package ru.mg.tailrec;



import java.util.stream.Stream;

@FunctionalInterface
public interface TailCall<T> {

    /**
     * Make next recursive call.
     * <p>The result is trailing call as {@link TailCall#done} or next deferred recursive call</p>
     *
     * @return next recursive call
     */
    TailCall<T> apply();

    /**
     * Is this the trailing call of recursion and therefore recursion complete?
     * Only trailing recursive call should return true.
     *
     * @return false
     */
    default boolean isComplete() {
        return false;
    }

    /**
     * Result of the recursion computation
     * @return throw Error for not trailing call else return the result of computation
     */
    default T result() {
        throw new Error("Calling result for non trailing recursive call");
    }

    /**
     * Invoke sequence of recursive calls for the making result of computation.
     * <p>This implementation is based on streams. Alternately you
     * can use {@link TailCall#invokeWhile()}</p>
     *
     * @return recursive algorithm computation result
     */
    default T invoke() {
        return Stream.iterate(this, TailCall::apply)
                .filter(TailCall::isComplete)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .result();
    }

    /**
     * Invoke sequence of recursive calls for the making result of computation.
     * <p>This implementation is based on while statement. Alternately you
     * can use {@link TailCall#invoke()}</p>
     *
     * @return recursive algorithm computation result
     */
    default T invokeWhile() {
        TailCall<T> call = this.apply();
        while (!call.isComplete()) {
            call = call.apply();
        }
        return call.result();
    }

    /**
     * Make trailing recursive call
     *
     * @param value result of trailing recursive call
     * @param <T> type of result
     * @return trailing recursive call with final result of recursion.
     */
    static <T> TailCall<T> done(final T value) {
        return new TailCall<T>() {
            /**
             * The trailing call is complete
             *
             * @return true
             */
            @Override
            public boolean isComplete() {
                return true;
            }

            @Override
            public T result() {
                return value;
            }

            /**
             * Trailing call should not generate next recursion.
             *
             * @return exception
             */
            @Override
            public TailCall<T> apply() {
                throw new Error("Calling apply for trailing recursive call");
            }
        };
    }
}