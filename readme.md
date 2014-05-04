# Introduction

I'm writing this project as a learning exercise.  I have a number of books
that I've bought over the years all revolving around Lisp, and Lispy things.
I'd like to go through this collection in painful and glorious detail, and so
I've decided to make that a goal -- for the rest of my life.

Here is a list of the books I'm referring to:

- [The Definitive Antlr 4 Reference] [Antlr4-Ref]
- [Clojure Programming] [Clojure-Prog]
- [The Joy of Clojure] [Clojure-Joy]
- [Lisp in Small Peices] [Lisp-Small]
- [Language Implementation Patterns] [Lang-Patterns]
- [The Definitive Antlr Reference: Building Domain Specific Languages][Building-DSLs]
- [Machine Learning In Action][Machine-Action]
- [Programming Game AI by Example][AI-Example]
- [Scheme and the Art of Programming][Scheme-Art], [Pdf][Scheme-Art-Pdf]
- [Practical Common Lisp][Practical-Lisp]
- [Common Lisp][Common-Lisp]
- [Let over Lambda][LoL]
- [Compilers: Principles, Techniques, and Tools][Compilers]
- ...


## Overview

The project is a hand written Lisp lexer and Parser (or hopes to be, it isn't
yet).  Eventually growing into a Lisp interpreter, as I work through a number
of the books in the above list.

It includes many suites of tests (from its inception.)

## Setup

1. Install Java 8 JDK
1. Install Maven
1. Clone the repo, then run: `%> mvn package` which should pull down some
dependencies and jar up the end code into a fat jar.

[Antlr4-Ref]: http://pragprog.com/book/tpantlr2/the-definitive-antlr-4-reference
[Clojure-Prog]: http://shop.oreilly.com/product/0636920013754.do
[Clojure-Joy]: http://www.manning.com/fogus2/
[Lisp-Small]: http://books.google.com/books/about/LISP_in_Small_Pieces.html?id=zxp9QgAACAAJ
[Lang-Patterns]: http://pragprog.com/book/tpdsl/language-implementation-patterns
[Building-DSLs]: http://shop.oreilly.com/product/9780978739256.do
[Machine-Action]: http://www.manning.com/pharrington/
[AI-Example]: http://books.google.com/books/about/Programming_Game_AI_by_Example.html?id=gDLpyWtFacYC
[Scheme-Art]: http://www.amazon.com/Scheme-Art-Programming-George-Springer/dp/0262192888
[Scheme-Art-Pdf]: http://www.cs.unm.edu/~williams/cs357/springer-friedman.pdf
[Practical-Lisp]: http://books.google.com/books/about/Practical_Common_Lisp.html?id=gwyZ4jdn_jMC
[Common-Lisp]: http://www.amazon.com/Common-LISP-Language-Second-Edition/dp/1555580416
[LoL]: http://letoverlambda.com/
[Compilers]: http://dragonbook.stanford.edu/
