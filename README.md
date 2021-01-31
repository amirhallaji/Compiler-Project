<!-- About Decaf -->

<p>
    <h1 align="center"><u>Decaf Compiler</u></h1>
    <h3 align="center">Compiler Project Fall 1399 - Shahid Beheshti University</h3>
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

| Scanner 	| [![Scanner](icons/download.svg)](https://github.com/hamedkhaledi/Compiler-Projects/blob/master/Description/ProjcetDcsp.pdf)	|
|:---------:|:-----:|
|  Parser 	| [![Scanner](icons/download.svg)](https://github.com/hamedkhaledi/Compiler-Projects/blob/master/Description/ProjcetDcsp.pdf) 	|
| Codegen 	| [![Scanner](icons/download.svg)](https://github.com/hamedkhaledi/Compiler-Projects/blob/master/Description/ProjcetDcsp.pdf) 	|

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

## Contributers


[Hamed Khademi Khaledi](https://github.com/hamedkhaledi)

[Mohammad Khoddam](https://github.com/mkh2097)

[Amir Hallaji Bidgoli](https://github.com/amirhallaji)