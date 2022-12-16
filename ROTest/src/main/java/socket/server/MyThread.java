package socket.server;

import dao.BusDao;
import utility.SerializationUtility;

import java.io.*;
import java.net.Socket;

public class MyThread extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private BusDao busDao = new BusDao();

    public MyThread(Socket clientSocket) {
        this.socket = clientSocket;
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
                    case 0 -> result = busDao.busesByRootNumber((int) SerializationUtility.deSerialize(fields[1]));
                    case 1 -> result = busDao.busesThatUsedMoreThanGivenTerm((int) SerializationUtility.deSerialize(fields[1]));
                    case 2 -> result = busDao.busesThatDroveMoreThanGivenDistance((int) SerializationUtility.deSerialize(fields[1]));
                }
            } catch (NumberFormatException e) {
                comp_code = 3;
            } catch (ClassNotFoundException e) {
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

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream( )));
            out = new PrintWriter(socket.getOutputStream(), true);
            while (processQuery());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
