# Example of java tail recursion calls support

This work is based on Vinisha Sharma's blog post 
https://blog.knoldus.com/tail-recursion-in-java-8/

and https://github.com/jonckvanderkogel/java-tail-recursion code

## Recursion calls problem
Recursion is function's to call of itself.
Recursion is widely known concept in a
functional programming design. 
Each function's call allocate stack frame. 
Therefore, we have limited by the size of stack 
in a recursive calls. 
A large recursive query can actually cause a stack overflow.

A tail recursion is a recursive function where 
the function calls itself at the end ("tail") 
of the function in which no computation is done 
after the return of recursive call.
Basically Tail recursions are able to be optimized 
into iteration.

## Java support of tail recursion optimization
Some JVM based support tail recursion call optimization.
Clojure have special recur function, in Scala @tailrec
method annotation force optimization on compilation. 

Java have not supported tail recursion call optimization.
But it may be implemented at code level. This repository 
contains example of implementation tail recursion call
optimization.

# Implementation details
## Idea
Сonsider the naive recursive implementation of the method 
length.
```
private <E> Long lengthLoopNaive(Collection<E> list, long acc) {
  if (list.isEmpty()) {
    return acc;
  } else {
    return lengthLoopNaive(tail(list), acc + 1);
  }
}

public <E> long lengthNaive(Collection<E> list) {
  return lengthLoopNaive(list, 0);
}
```
Algorithm is the fully correct, 
but on long lists (more than 15 000 elements)
calling lengthNaive throws StackOverflowException.

The reason of StackOverflowException is immediate
calling lengthLoopNaive leading to allocation of 
stack frame.

The idea is, instead of immediately calling a function in recursive position, 
return as a result deferred call in the form of a lambda.
Also we need to have a trailing marker for recursion breakdown 
 and the method to sequentially call lambda until
trailing marker.
Sequential calling transform recursion to iteration.

Length calculation may be rewrited as 
```
private <E> TailCall<Long> lengthLoopTailRec(Collection<E> list, long acc) {
   if (list.isEmpty()) {
     return done(acc); //<-- done is recursion trailing marker
   } else {
     return () -> lengthLoopTailRec(tail(list), acc + 1); //<-- recursive deferred call as lambda 
   }
}

private <E> long length(Collection<E> list) {
   return lengthLoopTailRec(list, 0).invokeWhile(); //<-- start to sequentially calling deferred lambdas
}
```


