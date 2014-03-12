.PHONY : javadump clean

javadump:
	rm -f javadump/*
	python rkftojava.py
	cp javadump/*.java code/android/RejsekortReader/src/info/rejsekort/reader/rkf/blocks/

clean:
	rm -f javadump/*
