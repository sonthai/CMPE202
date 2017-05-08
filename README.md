# UML Parser Project

## Introduction
Creating a Parser which converts Java Source Code into both UML Class Diagram and UML Sequence Diagram.

## Tools
- Maven: Handle package dependency
- JavaParser: Convert Java Source Code to Abstract Syntax Tree
- PlantUML: Generate UML Diagram in PNG format in from the provided string input 
- AspectJ compiler: to capture Java class at defined pointcuts to tracing sequential actions of the Java program

## How to generate Class Diagram and Sequence Diagram from Java source code
A Makefile file will be used to generate both Class Diagram and Sequence Diagram.

````
Makefile supports the following operations:

1. Clean, compile, and build the Class Diagram project

    make build

2. Generate the UML Class Diagram 

    make umlparser INPUT=<source directory> OUTPUT=[<output directory>|<output filename>]
    
    INPUT and OUTPUT arguments must be relative paths.

3. Generate the UML Sequence Diagram
   make aspects OUTPUT=<output directory>
   OUTPUT argument must be a relative path
   
   Notes: The testcase is put in the package sequence under SequenceDiagram/src/sequence/
````
