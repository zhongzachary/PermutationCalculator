# Permutation Calculator

This is a small Java project created to help me studying [Advanced Group Theory - Fall 2019](https://sites.google.com/view/rob-silversmith/home/F19-3275) with [Prof. Rob Silversmith](https://sites.google.com/view/rob-silversmith/) in Northeastern University. The purpose of this project is to create an easy command-line tool to calculate inverse and composition of elements in permutation group (a.k.a. symmetry group). It helped me to understand the permutation group better.

## How to Start up Permutation Calculator

1. Make sure Java is installed in your computer and you are using Java 8 or later. 

2. Download this project.

3. Open your terminal, (make sure your current directory is whereever you stored this project) run

   ```
   java -jar PermutationCalculator.jar
   ```

4. If you see some instruction of using Permutation Calculator, it is successfully started up. You should see something like the following.

   <img src="support/start_calculator.png" alt="start_calculator" style="zoom: 50%;" />

5. Following that instructor for using the calculator. Use Ctrl+D to terminate the program.

### Some Clarifications

#### Defining a Permutation

You can define a permutation by using either [one-line notation](https://en.wikipedia.org/wiki/Permutation#One-line_notation) or [cycle notation](https://en.wikipedia.org/wiki/Permutation#Cycle_notation).

For example, for let *s* be a permutation where *s(2) = 4, s(4) = 5, s(5) = 2, s(3) = 6, s(6) = 3*, and *s(x) = x* for any other *x*'s. 

- In *one-line notation*, *s = (145623)*, i.e., 1 will be sent to 1, 2 will be sent to 4, etc.
- In *cycle notation*, however, *s=(245)(36)*,  i.e., 2 will be sent to 4, 4 will be sent to 5, 5 will be sent to 2, etc.

To define a permutation using *one-line notation*, use 

```json
a = [1,4,5,6,2,3]
```

Note that one-line notation is represented as an integer array.

To define a permutation using *cycle notatio*n, use

```json
a = [[2,4,5],[3,6]]
```

Note that cycle notation is represented as an array of interger arrays, each of which represents a cycle.

#### Composition of Permutations

Keep in mind [composition of permutations](https://en.wikipedia.org/wiki/Permutation#Composition_of_permutations), *a \* b*,  is evaluated from right to left, i.e., *a \* b(x) = a(b(x))*. The calculator follows this rule, too. When you typed `a*b`, you are indeed calculating *a \* b*.

## Building the Project

The project is managed using maven. Make sure maven is installed. Then, under the project directory, run

```
mvn package
```

The target jar file is located in `target/PermutationCalculator-1.0-SNAPSHOT-jar-with-dependencies.jar`.

