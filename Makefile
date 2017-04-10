clean:
	scripts/build.sh

umlparser:
	scripts/umlparser.sh ${INPUT} ${OUTPUT}

compile_seq:
	cd scripts; ./sequence_diagram.sh; cd ..


