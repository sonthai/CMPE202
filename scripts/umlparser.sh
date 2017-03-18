echo "Source Directory: " $1
echo "Output Directory: " $2
mvn exec:java -Dexec.mainClass=uml.classdiagram.Main -Dexec.args="$1 $2"
