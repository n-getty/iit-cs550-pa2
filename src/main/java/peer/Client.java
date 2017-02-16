package main.java.peer;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.io.File;


/**
 * Client class creates client to index server 
 */
public class Client {
    // ID of this peer
    String id;
    // contains the file objects
    List<File> files = new ArrayList<File>();
    // contains the list of file names ( for registering )
    Set<String> fileList = new HashSet<String>();
    int maxTTL = 5;
    PeerImpl peerServ;
    int messageID = 0;

    public Client(String folder) {
	try {
	    createFileIndex(folder);
        peerServ = new PeerImpl(folder, getNeighbors(folder), fileList);
	} catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }


    public  String[] getNeighbors(String folder){
        String[] neighbors = null;
        try {
            Set<String> neighborSet = new HashSet<String>();
            File fold = new File("./" + folder + "/" + "neighbors.txt");
            Scanner fileReader = new Scanner(fold);
            while(fileReader.hasNextLine()){
                neighborSet.add(fileReader.nextLine());
            }
            neighbors = (String[]) neighborSet.toArray();
        }
        catch(IOException e){
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
        return neighbors;
    }

    /**
     * Store all the file names of a given directory
     */
    public void createFileIndex(String folder) {
        // currently we only support files inside of folders (i.e. no folders inside folders)

        // read in files from given folder
        File fold = new File("./"+folder);
        File[] listOfFiles = fold.listFiles();

        // convert list of files into ArrayList of strings
        // the strings are the names

        files = Arrays.asList(listOfFiles);
        int i;
	    for(i=0;i<listOfFiles.length;i++) {
            fileList.add(listOfFiles[i].getName());
        }
    }

    public void register(String fileName){
        fileList.add(fileName);
        peerServ.updateFileIndex(fileList);
    }

    public void deregister(String fileName){
        fileList.remove(fileName);
        peerServ.updateFileIndex(fileList);
    }

    /**
     * Retrieve a file from a given peer
     */
    public byte[] retrieve(String fileName){
        try {
	        peerServ.queryNeighbors(fileName, maxTTL, messageID++;);
	    
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
	    byte[] x = "x".getBytes();
	    return x;
    }


}
