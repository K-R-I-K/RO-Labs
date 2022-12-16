package socket;

import dao.Bus;
import utility.SerializationUtility;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class Client {
    private Socket sock;
    private PrintWriter out;
    private BufferedReader in;
    public Client(String ip, int port) throws IOException {
        sock = new Socket(ip,port);
        in = new BufferedReader(new InputStreamReader(sock.getInputStream( )));
        out = new PrintWriter(sock.getOutputStream(), true);
    }

    private Object sendQuery(int operation, Object ...values) throws IOException {
        StringBuilder query = new StringBuilder(String.valueOf(operation));
        for (Object o: values) {
            query.append("#").append(SerializationUtility.serialize((Serializable) o));
        }
        out.println(query);

        String response = in.readLine();
        String[] fields = response.split("#");
        try {
            int comp_code = Integer.parseInt(fields[0]);
            Object result = SerializationUtility.deSerialize(fields[1]);
            if (comp_code == 0) {
                return result;
            } else {
                throw new IOException("Error while processing query");
            }
        } catch(NumberFormatException e) {
            throw new IOException("Invalid response from server");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Bus> busesByRootNumber(int rootNumber) throws IOException{
        return (List<Bus>) sendQuery(0, rootNumber);
    }
    public List<Bus> busesThatUsedMoreThanGivenTerm(int year) throws IOException{
        return (List<Bus>) sendQuery(1, year);
    }
    public List<Bus> busesThatDroveMoreThanGivenDistance(int distance) throws IOException{
        return (List<Bus>) sendQuery(2, distance);
    }

    public void disconnect() throws IOException {
        sock.close();
    }

    public static void main(String[] args) {
        try {
            ClientSocketTask11 client = new ClientSocketTask11("localhost",12345);

            System.out.println("\nGet buses by root number");
            System.out.println(client.busesByRootNumber(1));
            Thread.sleep(10000);
            System.out.println("\nGet buses that used more than given term");
            System.out.println(client.busesThatUsedMoreThanGivenTerm(2001));

            System.out.println("\nGet buses that drove more than given distance");
            System.out.println(client.busesThatDroveMoreThanGivenDistance(9999));

            client.disconnect();
        } catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
