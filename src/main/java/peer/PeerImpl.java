package main.java.peer;

import javafx.util.Pair;

import java.nio.file.Files;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Server part of the Peer
 */
public class PeerImpl implements PeerInt {

    String folder;
    Set<String> fileIndex;
    Map<Pair<String, Integer>, String> upstreamMap;
    String thisIP;
    int thisPort;
    String[] neighbors;

    /**
     * Constructor for exporting each peer to the registry
     */
    public PeerImpl(String folder, String[] neighbors, Set<String> fileIndex, String id) {
        try {
            thisIP = id;
            this.neighbors = neighbors;
            this.fileIndex = fileIndex;
            upstreamMap = new HashMap<Pair<String, Integer>, String>();
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

    public void updateFileIndex(Set<String> fileList){
        fileIndex = fileList;
    }
    /**
     * Pass chunks of the file to the clients remote peer object until the file is written
     */
    public byte[] obtain(String fileName)
	throws IOException {

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

    public void query (Pair<String, Integer> messageID, int TTL, String fileName)
            throws RemoteException {

        try {
            String upstreamIP = RemoteServer.getClientHost();
            if(!upstreamMap.containsKey(messageID) && TTL > 0) {
                upstreamMap.put(messageID, upstreamIP);
                if (fileIndex.contains(fileName)) {
                    queryhit(messageID, fileName, thisIP, thisPort);
                }
                if(TTL > 1)
                    queryNeighbors(fileName, TTL - 1, messageID);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    public void queryhit(Pair<String, Integer> messageID, String fileName, String peerIP, int portNumber)
            throws RemoteException {
        try {
            String upstreamIP = upstreamMap.get(messageID)
            if(upstreamIP.equals(thisIP)){
                Registry registry = LocateRegistry.getRegistry(peerIP, portNumber);
                PeerInt peerStub = (PeerInt) registry.lookup("PeerInt");
                byte[] requestedFile = peerStub.obtain(fileName);
                writeFile(requestedFile, fileName);
            }
            else {
                Registry registry = LocateRegistry.getRegistry(upstreamIP, portNumber);
                PeerInt peerStub = (PeerInt) registry.lookup("PeerInt");
                peerStub.queryhit(messageID, fileName, peerIP, portNumber);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void queryNeighbors(String fileName, int TTL, Pair<String, Integer> messageID){
        try {
            for (String neighbor : neighbors) {
                Registry registry = LocateRegistry.getRegistry(neighbor,1099);
                PeerInt peerStub = (PeerInt) registry.lookup("PeerInt");
                peerStub.query(messageID, TTL, fileName);
            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public void writeFile(byte[] x, String fileName){
        try {
            FileOutputStream out = new FileOutputStream(new File(folder + "/" + fileName));
            out.write(x);
            out.close();

        } catch (IOException e) {
            System.out.println("Exception" + e);
        }
    }
}
