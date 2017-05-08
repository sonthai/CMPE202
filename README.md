# UML Parser Project

## Introduction
Creating a Parser which converts Java Source Code into both UML Class Diagram and UML Sequence Diagram

## Tools
- Maven: Handle package dependency
- JavaParser: Convert Java Source Code to Abstract Syntax Tree
- PlantUML: Generate UML Diagram in PNG format in from the provided string input 
- AspectJ compiler: to capture Java class at defined pointcuts to tracing sequential actions of the Java program

## How to generate Class Diagram and Sequence Diagram from Java source code
A Makefile file will be used to generate both Class Diagram and Sequence Diagram.

Makefile supports the following operations:

1. Clean, compile, and build the Class Diagram project

    make build

2. Generate the UML Class Diagram 

    make umlparser INPUT=< source directory> OUTPUT=[< output directory>|< output filename>]


### How to run the project without the IDE
There are 2 scripts in the  scripts directory
- build.sh is used to clean, compile, and build project
- umlparser.sh is used to run and generate UML class diagram:

  ./umlparser.sh < source directory> < output directory >

### Instructions on how to run sequence diagram without IDE
- Download Aspectj compiler from Install ajc https://eclipse.org/aspectj/downloads.php
- Install Aspectj compiler: java -java < aspectj jar file>
- Add aspectjrt.jar into CLASSPATH
- Modify the PATH to include ~/aspectj1.x/bin
- Compile sequence digram code using ~/scripts/sequence_diagram.sh script
   
   ./sequence_diagram.sh < output directory>
