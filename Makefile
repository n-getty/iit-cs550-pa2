all:
	javadoc -d src/docs/manual src/main/java/peer/*
	javac -d bin/ -cp bin/ src/main/java/peer/*.java
clean:
	sudo rm -r src/tests/* src/docs/manual/* bin/main topologies/topo/*
