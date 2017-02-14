package main.java.peer;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.*;
    
/**
 * Interface for peer data transfer methods
 */
public interface PeerInt extends Remote {
    byte[] obtain(String fileName) throws RemoteException, IOException;
    void query (int messageID, int TTL, String fileName)
    void queryhit(int messageID, int TTL, String fileName, String peerIP, String portNumber)
}
