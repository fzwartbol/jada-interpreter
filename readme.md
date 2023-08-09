# JADA - Dynamic Java
## Introduction
Jada is a language that compiles to Java. It is a dynamic language with a syntax similar to Java.
It is created for learning about interpreters and compilers and is not intended to be used in production. 
It follows the book [Crafting Interpreters](http://craftinginterpreters.com/) by Bob Nystrom for creating an interpreter and a compiler.

It supports the following features:
### Variables
- Jada uses type inference to determine the type of a variable
    - e.g. 
    - ```var a = 0; ```
    - ```var b = "Hello";```

### Functions
- Jada supports functions with multiple parameters and return values
    - e.g.
    - ``` fun add(a, b) { return a + b; }  ```
    - ``` var result = add(1, 2);  ```

### Classes

### Inheritance
- Jada supports classes with inheritance
    - e.g.
  ```
    class Animal {
       var name;
       fun speak() {
           print("I am an animal");
       }
     }
     class Dog extends Animal {
       fun speak() {
           print("Woof");
       }
     }
     var dog = Dog();
   - dog.speak(); // prints "Woof"
  ```
### Control flow
- Jada supports if statements and while loops
    - e.g.
    ```
    var a = 0;
    if (a == 0) {
        print("a is 0");
    } else {
        print("a is not 0");
    }
    while (a < 10) {
        print(a);
        a = a + 1;
    }
    ```
- 

## Extra features:
Besides the features from the book, Jada also supports the following features:
### Anonymous functions
- Jada supports anonymous functions
    - e.g.
    ```
    var add = fun(a, b) {
        return a + b;
    }
    ```
### Modules
- Jada support modules
    - e.g.
    ```
    // module.jada
    fun add(a, b) {
        return a + b;
    }
    ```
    ```
    // main.jada
    import modulename "module.jada";
    var result = add(1, 2);
    ```
- 