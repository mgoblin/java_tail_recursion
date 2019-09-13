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


