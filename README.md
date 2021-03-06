# CMPE202: UML Diagram Parser Project

### Introduction
Creating a Parser which converts Java Source Code into both UML Class Diagram and UML Sequence Diagram.

### Tools
- Maven: Handle package dependency
- JavaParser: Convert Java Source Code to Abstract Syntax Tree
- PlantUML: Generate UML Diagram in PNG format in from the provided string input 
- AspectJ compiler: to capture Java class at defined pointcuts to tracing sequential actions of the Java program

### How to generate Class Diagram and Sequence Diagram from Java source code
A Makefile file will be used to generate both Class Diagram and Sequence Diagram.

````
Makefile supports the following operations:

1. Clean, compile, and build the Class Diagram project

    make build

2. Generate the UML Class Diagram 

    make umlparser INPUT=<source directory> OUTPUT=[<output directory>|<output filename>]
    
    INPUT and OUTPUT arguments must be absolute paths.

3. Generate the UML Sequence Diagram
   make aspects OUTPUT=<output directory>
   OUTPUT argument must be a absolute path.
   
   Notes: The testcase is put in the package sequence under SequenceDiagram/src/sequence/
````

### Test Cases and Results
**UML Class Diagram**

The outputs for the Class Diagram testcases are located at [Class Diagram Output](https://github.com/sonthai/CMPE202/tree/master/ClassDiagram/output).

**UML Sequence Diagram**

The output for the Sequence Diagram testcase is located at [Sequence Diagram Output](https://github.com/sonthai/CMPE202/tree/master/SequenceDiagram/output).

### Demo
I integrated my UML Class Diagram with the 2 other students from CMPE 281. Below is URL Links to YouTube: 

    - By Kang Hua Wu https://youtu.be/8xh2-LDrM_k (Between 7:35 and 8:15)
    
    - By Tuan Ung: https://www.youtube.com/watch?v=J1p6r9cU8-8 (Between 10:15 and 12:00)
