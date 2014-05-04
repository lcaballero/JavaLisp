# Introduction

I'm writing this project as a learning exercised.  I have a number of books
that I've bought over the years all revolving around Lisp, and Lispy things.
I'd like to go through this collection in pain and glorious detail, and so
I've decided to make that a goal for the rest of my life.

Here is a list of the books I'm referring to:

The Definitive Antlr 4 Reference
Clojure Programming
The Joy of Programming
Lisp in Small Peices
Language Implementation Patterns
The Definitive Antlr Refrence: Building Domain Specific Languages
Machine Learning In Action
Programming Game AI by Example
Programming Clojure
Scheme and the Art of Programming
Practical Common Lisp
Common Lisp
Let over Lambda
Compilers: Principles, Techniques, and Tools
...

## Overview

The project is a hand written Lisp lexer and Parser (or hopes to be, it isn't
yet).  Eventually growing into a Lisp interpreter, as I work through a number
of the books in the above list.  It includes many suites of tests (from its
inception.)

## Setup

1. Install Java 8 JDK
1. Install Maven
1. Clone the repo, then run: `%> mvn package` which should pull down some
dependencies and jar up the end code into a fat jar.

