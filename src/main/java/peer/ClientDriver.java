package main.java.peer;

import java.io.*;
import java.util.*;


/**
 * Client Driver is the main program on the peer to obtain and return files
 */
public class ClientDriver {

    /**
     * Main function creates client to server, and initiates peer server
     * Input is IP address of host and the directory that the peer will share and download to
     */
    public static void main(String[] args)
            throws IOException {

        /*  create new client object
         *  args[0] is the directory of the files to download to
         *  args[1] is the local ip address of this peer
         */
        String folder = args[0];
        String id = args[1];
        System.out.println("INFO: Initializing Peer...");
        Client peerClient = new Client(folder, id);
        System.out.println("INFO: Client Process initialized...");

        System.out.println("INFO: Indexing Files in: ./" + folder + "/");

        Scanner input = new Scanner(System.in);
        System.out.println("\nInput 'exit' to close the application at anytime");
        String query;

        long old_time = 0;
        long time = System.nanoTime();

        if (args.length > 2) {

            for (int i = 0; i < 1000; i++) {
                List<String> peers = peerClient.lookup("file10.txt");
                byte[] x = peerClient.retrieve("file10.txt", peers.get(0));
                try {
                    FileOutputStream out = new FileOutputStream(new File(folder + "/" + "file10_" + i + ".txt"));
                    out.write(x);
                    out.close();
                } catch (IOException e) {
                    System.out.println("Exception" + e);
                }
                old_time = time;
                time = System.nanoTime();
                System.out.println(time - old_time);
            }
            System.exit(0);
        }


        while (true) {
            System.out.println("\nInput name of file you want to obtain:\n");
            query = input.nextLine();
            if (query.equals("exit")) {
                System.out.println("\nALERT: Process exiting... \n Goodbye.");
                System.exit(0);
            }
            List<String> peers = peerClient.lookup(query);
            if (peers == null) {
                System.out.println("WARNING: No peers for that file\n\ttry a new filename");
            } else {
                byte[] x = peerClient.retrieve(query, peers.get(0));
                try {
                    FileOutputStream out = new FileOutputStream(new File(folder + "/" + query));
                    out.write(x);
                    out.close();

                } catch (IOException e) {
                    System.out.println("Exception" + e);
                }
                // save file in directory

            }
        }
    }
}
