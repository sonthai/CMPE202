echo "Clean up sequence diagram source code"
rm ../SequenceDiagram/src/aspectj/code/*.class ../SequenceDiagram/src/aspectj/sequencediagram/*.class
echo "Compile sequence diagram source code"
ajc -1.8 ../SequenceDiagram/src/aspectj/code/*.java ../SequenceDiagram/src/aspectj/sequencediagram/*.aj 
