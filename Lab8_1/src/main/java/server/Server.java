package server;

import db.DBException;
import db.DBManager;
import db.entity.File;
import db.entity.Folder;
import util.SerializationUtility;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
public class Server {
    private ServerSocket server = null;
    private Socket sock = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private DBManager dbManager = new DBManager();

    public void start(int port) throws IOException {
        server = new ServerSocket(port);
        while (true)
        {
            sock = server.accept();
            in = new BufferedReader(new InputStreamReader(sock.getInputStream( )));
            out = new PrintWriter(sock.getOutputStream(), true);
            while (processQuery());
        }
    }
    private boolean processQuery() {
        Object result = null;
        int comp_code = 0;
        try {
            String query = in.readLine();
            if (query == null) {
                return false;
            }
            String[] fields = query.split("#");
            int oper = Integer.parseInt(fields[0]);
            try {
                switch(oper) {
                    case 0 -> result = dbManager.insertFolder((Folder) SerializationUtility.deSerialize(fields[1]));
                    case 1 -> result = dbManager.deleteFolder((Folder) SerializationUtility.deSerialize(fields[1]));
                    case 2 -> result = dbManager.insertFileToFolder((File) SerializationUtility.deSerialize(fields[1]),
                                (Folder) SerializationUtility.deSerialize(fields[2]));
                    case 3 -> result = dbManager.deleteFile((Folder) SerializationUtility.deSerialize(fields[2]),
                            (File) SerializationUtility.deSerialize(fields[1]));
                    case 4 -> result = dbManager.updateFile((File) SerializationUtility.deSerialize(fields[1]));
                    case 5 -> result = dbManager.transferFile((File) SerializationUtility.deSerialize(fields[1]),
                            (Folder) SerializationUtility.deSerialize(fields[2]));
                    case 6 -> result = dbManager.findAllFolders();
                    case 7 -> result = dbManager.getFilesInFolder((Folder) SerializationUtility.deSerialize(fields[1]));
                }
            } catch (NumberFormatException e) {
                comp_code = 3;
            } catch (DBException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            String response = comp_code + "#" + SerializationUtility.serialize((Serializable) result);
            out.println(response);
            return true;
        }
        catch(IOException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        try {
            Server srv = new Server();
            srv.start(12345);
        }
        catch(IOException e) {
            System.out.println("Error");
        }
    }
}
