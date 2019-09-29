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
```java
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
Sequential invoke all deferred calls should transform recursion to iteration.

Length calculation may be rewrited as 
```java
private <E> TailCall<Long> lengthLoopTailRec(Collection<E> list, long acc) {
   if (list.isEmpty()) {
     return done(acc); //<-- done is recursion trailing marker
   } else {
     return () -> lengthLoopTailRec(tail(list), acc + 1); //<-- recursive deferred call as lambda 
   }
}

<E> long length(Collection<E> list) {
   return lengthLoopTailRec(list, 0).invokeWhile(); //<-- start to sequentially calling deferred lambdas
}
```

## TailCall details
TailCall is a functional interface for recursive call 
It have abstract method apply() for generate next deferred recursive call.
Recursive algorithm implementers should replace non trailing recursive calls
from f(...) to () -> f(...)

isComplete function returns trailing call flag. 
By default TailCall is not trailing and its isComplete returns false.

result function return final result of recursive call.
By default TailCall is not trailing and result throw exception because 
not trailing call not last call in recursion sequence and doesn't have
final result.  

The trailing recursive call is making by function done(T value). It returns
the TailCall instance with isComplete = true and final result of recursion
computation, method apply throws exception.

TailCall have to methods to run computation: invoke() and invokeWhile().
The difference is in implementation details. 
Method invoke() use java streams and invokeWhile() use while statement for computation.


## How to use it
If you have tail recursive function naive implementation and want to use tail call optimization:


The first step is imports. 
```java
import ru.mg.tailrec.TailCall;
import static u.mg.tailrec.TailCall.done;
```
The second step is to replace function return type to TailCall<E> 
where E is a function raw return type.

Next replace all not tailing calls by lambda. For example if your tail recursion function named
func you should replace non trailing calls of func(<args>) to () -> func(<args>).
Replace trailing calls result by done(result) where done is a TailCall.done method.

The last step is to replace function usage call to invoke() or invokeWhile(). 

# Implementation overhead







