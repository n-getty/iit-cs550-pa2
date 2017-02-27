all:
	javadoc -d docs/manual main/java/peer/*
	javac -d . main/java/peer/ClientDriver.java main/java/peer/PeerInt.java main/java/peer/PeerImpl.java main/java/peer/Client.java
clean:
	sudo rm -r ./tests/* ./docs/manual/* ./main/java/*/*~ ./main/java/*/*.class ./main/java/*/#*# ./*~ ./*/*~ ../topologies/topo/*
