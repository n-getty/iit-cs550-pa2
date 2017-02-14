package main.java.peer;

import java.nio.file.Files;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.io.*;
import java.nio.file.Paths;

/**
 * Server part of the Peer
 */
public class PeerImpl implements PeerInt {

    String folder;
    Set fileIndex = new HashSet<String>();
    Map upstreamMap = new HashMap<String, String>
    String thisIP;
    String thisPort;
    String[] neighbors;
    int maxTTL = 5;

    /**
     * Constructor for exporting each peer to the registry
     */
    public PeerImpl(String fold) {
        folder = fold;
        try {
            PeerInt stub = (PeerInt) UnicastRemoteObject.exportObject(this, 0);
            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("PeerInt", stub);
            System.err.println("PeerImpl ready");
        } catch (Exception e) {
            System.err.println("PeerImpl exception: " + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Pass chunks of the file to the clients remote peer object until the file is written
     */
    public byte[] retrieve(String fileName)
	throws IOException, RemoteException {

        try {
            byte[] requestedFile = Files.readAllBytes(Paths.get(folder+"/"+fileName));
            return requestedFile;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        byte[] x = "x".getBytes();
        return x;
    }

    public void query (int messageID, int TTL, String fileName)
            throws RemoteException {

        try {
            if(!upstreamMap.contains(messageID) && TTL > 0) {
                if (fileIndex.contains(fileName)) {
                    queryhit(messageID, TTL - 1, fileName, thisIP, thisPort)
                }
                queryNeighbors(fileName)
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    public void queryhit(int messageID, int TTL, String fileName, String peerIP, String portNumber)
            throws RemoteException {
        try {

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void queryNeighbors(String fileName){
        try {
            for (String neighbor : neighbors) {
                Registry registry = LocateRegistry.getRegistry(neighbor,1099);
                PeerInt peerStub = (PeerInt) registry.lookup("PeerInt");
                peerStub.query(messageID, maxTTL, fileName)
            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

}
