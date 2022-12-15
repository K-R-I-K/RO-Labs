package server;

import com.rabbitmq.client.*;
import db.DBException;
import db.DBManager;
import db.entity.File;
import db.entity.Folder;
import util.IOUtility;

import java.io.*;
import java.util.List;
import java.util.concurrent.TimeoutException;


public class Server {
    private Channel channel;
    DBManager dbManager;

    public void start() throws IOException, TimeoutException {
        dbManager = new DBManager();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        //factory.setPort(1234);

        Connection connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare("rpc_queue", false, false, false, null);
        channel.queuePurge("rpc_queue");
        channel.basicQos(1);

        System.out.println("Awaiting requests");

        processQuery();
    }

    private void processQuery() throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
                    .correlationId(delivery.getProperties().getCorrelationId())
                    .build();

            DataInputStream in = new DataInputStream(new ByteArrayInputStream(delivery.getBody()));
            ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(outBytes);

            try {
                String query = IOUtility.readString(in);
                System.out.println(query);
                switch (query) {
                    case "insertFileToFolder" -> {
                        File file = IOUtility.readFile(in);
                        Folder folder = IOUtility.readFolder(in);
                        boolean result = dbManager.insertFileToFolder(file, folder);
                        out.writeBoolean(result);
                    }

                    case "insertFolder" -> {
                        Folder folder = IOUtility.readFolder(in);
                        boolean result = dbManager.insertFolder(folder);
                        out.writeBoolean(result);
                    }

                    case "deleteFile" -> {
                        File file = IOUtility.readFile(in);
                        boolean result = dbManager.deleteFiles(file);
                        out.writeBoolean(result);
                    }

                    case "findAllFolders" -> {
                        List<Folder> result = dbManager.findAllFolders();
                        writeListOfFolders(out, result);
                    }

                    case "findAllFilesInFolder" -> {
                        Folder folder = IOUtility.readFolder(in);
                        List<File> result = dbManager.getFilesInFolder(folder);
                        writeListOfFiles(out, result);
                    }
                }
            } catch (RuntimeException | DBException e) {
                System.out.println("[.]" + e);
            } finally {
                channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, outBytes.toByteArray());
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        channel.basicConsume("rpc_queue", false, deliverCallback, consumerTag -> {});
    }

    private void writeListOfFolders(DataOutputStream out, List<Folder> folders) throws IOException {
        out.writeInt(folders.size());
        for (Folder folder : folders) {
            IOUtility.writeFolder(out, folder);
        }
    }

    private void writeListOfFiles(DataOutputStream out, List<File> files) throws IOException {
        out.writeInt(files.size());
        for (File file : files) {
            IOUtility.writeFile(out, file);
        }
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.start();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}