echo "Prepare for compiling"
cd SequenceDiagram
TARGET_DIR=./target
rm -rf $TARGET_DIR 2 >/dev/null
mkdir $TARGET_DIR
echo "*************************************"

echo "Set class path"
CLASSPATH=./lib/plantuml.jar
echo "CP: $CLASSPATH"
echo "*************************************"

# Compile the source
echo "Compiling..."
ajc -1.8  -classpath .:$CLASSPATH  -d $TARGET_DIR src/aspects/*.java src/aspects/*.aj src/code/*.java
echo "**************************************"

echo "Generate sequence diagram"
cd $TARGET_DIR
java  -classpath .:../$CLASSPATH code/Main

# Copy out.png into output directory
mv out.png ../output/
