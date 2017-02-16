package main.java.peer;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.*;
    
/**
 * Interface for peer data transfer methods
 */
public interface PeerInt extends Remote {
    byte[] obtain(String fileName) throws IOException;
    void query (String messageID, int TTL, String fileName) throws RemoteException;
    void queryhit(String messageID, String fileName, String peerIP, int portNumber)throws RemoteException;
}
