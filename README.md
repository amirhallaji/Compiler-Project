<!-- About Decaf -->
<link href="icons/style.css" rel="stylesheet"/>


<p align="center">
    <h1 align="center"><u>Decaf Compiler</u></h1>
    <h4 align="center">Compiler Project Fall 1399 - Shahid Beheshti University</h4>
</p>
<p align="center">
        <img src="icons/decaf.jpg" height="320" width="480" align="center">
</p>
<!-- About Decaf -->

## About Decaf Language
Decaf is a simple object oriented language similar to C/C++/Java.
The features of this language has been simplified to be implemented easier. Yet, it's still powerful
enough to be used to write some interesting programs.

----------

## Project Description

| Description|
|:--------------------:|
|[![Scanner](icons/1.svg)](https://github.com/hamedkhaledi/Compiler-Projects/blob/master/Description/ProjcetDcsp.pdf)    |    


## Phases

| Scanner 	| [![Scanner](icons/download.svg)](https://github.com/hamedkhaledi/Compiler-Projects/blob/master/Description/Scanner.pdf)	|
|:---------:|:-----:|
|  Parser 	| [![Scanner](icons/download.svg)](https://github.com/hamedkhaledi/Compiler-Projects/blob/master/Description/Parser.pdf) 	|
| Codegen 	| [![Scanner](icons/download.svg)](https://github.com/hamedkhaledi/Compiler-Projects/blob/master/Description/CodeGeneration.pdf) 	|

# How To Run

### 1- Compile JFlex
First, you must install Jflex 
```commandline
jflex src/Scanner.flex
```
or you can use jar file in the project
```commandline
java -jar src/jflex-1.8.2.jar src/Scanner.flex
```

### 2- Compile CUP
```commandline
cd src/
java -jar java-cup-11b.jar parser.cup
```

## 3- Compile Compiler
```commandline
javac -cp "java-cup-11b.jar:java-cup-11b-runtime.jar:jflex-full-1.7.0.jar Main.java"
```
## Contributers


[Hamed Khademi Khaledi](https://github.com/hamedkhaledi)

[Mohammad Khoddam](https://github.com/mkh2097)

[Amir Hallaji Bidgoli](https://github.com/amirhallaji)