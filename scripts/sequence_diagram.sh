echo "Prepare for compiling"
cd SequenceDiagram
rm -rf target 2 >/dev/null
CLASS_TARGET=./target
mkdir $CLASS_TARGET
echo "*************************************"

echo "Set class path"
CLASSPATH=./lib/plantuml.jar
echo "CP: $CLASSPATH"
echo "*************************************"

# Compile the source
echo "Compiling..."
ajc -1.8  -classpath .:$CLASSPATH  -d $CLASS_TARGET src/aspects/*.java src/aspects/*.aj src/code/*.java
echo "**************************************"

echo "Generate sequence diagram"
cd $CLASS_TARGET
java  -classpath .:../$CLASSPATH code/Main

#echo "Clean up sequence diagram source code"
#rm ../SequenceDiagram/src/aspectj/sequencediagram/*.class
#echo "Compile sequence diagram source code"
#ajc -1.8 ../SequenceDiagram/src/aspectj/sequencediagram/*.java ../SequenceDiagram/src/aspectj/sequencediagram/*.aj 
