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
ajc -1.8  -classpath .:$CLASSPATH  -d $TARGET_DIR src/myaspect/*.java src/myaspect/*.aj src/sequence/*.java
echo "**************************************"

echo "Generate sequence diagram"
echo "Output Directory: " $1
cd $TARGET_DIR
java  -classpath .:../$CLASSPATH sequence/Main $1
