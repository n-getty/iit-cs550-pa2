all:
	javadoc -d docs/manual main/java/peer/*
	javac -d . main/java/peer/ClientDriver.java main/java/peer/PeerInt.java main/java/peer/PeerImpl.java main/java/peer/Client.java
clean:
	rm ./main/java/*/*~ ./main/java/*/*.class ./main/java/*/#*# ./*~ ./*/*~
	rm ../topologies/topo/*
	rm -r ./docs/manual/*
	rm -r ./tests/*

