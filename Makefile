clean:
	scripts/build.sh

umlparser:
	scripts/umlparser.sh ${INPUT} ${OUTPUT}

aspects:
	scripts/sequence_diagram.sh ${OUTPUT}



