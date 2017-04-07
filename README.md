# CMPE202

### Notes

The coding of Class Diagram is done, and I will some unit tests and refactor the code

Currently, I'm working on sequence diagram. 

### Requirements for the project
- Installing maven
- Install AspectJ in Eclipse.

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
