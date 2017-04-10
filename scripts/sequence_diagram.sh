echo "Clean up sequence diagram source code"
rm ../SequenceDiagram/src/aspectj/sequencediagram/*.class
echo "Compile sequence diagram source code"
ajc -1.8 ../SequenceDiagram/src/aspectj/sequencediagram/*.java ../SequenceDiagram/src/aspectj/sequencediagram/*.aj 
