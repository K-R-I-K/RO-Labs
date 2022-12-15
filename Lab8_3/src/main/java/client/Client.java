package client;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import db.entity.Folder;
import db.entity.File;
import server.Server;
import util.IOUtility;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class Client implements AutoCloseable {
    private Channel channel;
    private final String host;
    private final int port;

    public Client() {
        this.host = "localhost";
        this.port = 1234;
    }

    public void connect() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        //factory.setPort(port);

        Connection connection = factory.newConnection();
        channel = connection.createChannel();
    }

    private DataInputStream call(byte[] params)
            throws IOException, ExecutionException, InterruptedException {
        final String corrId = UUID.randomUUID().toString();

        String replyQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", "rpc_queue", props, params);

        CompletableFuture<ByteArrayInputStream> response = new CompletableFuture<>();
        String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response.complete(new ByteArrayInputStream(delivery.getBody()));
            }
        }, consumerTag -> {});

        ByteArrayInputStream resultByteArray = response.get();
        channel.basicCancel(ctag);
        return new DataInputStream(resultByteArray);
    }

    public boolean insertFolder(Folder folder) throws IOException, ExecutionException, InterruptedException {
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(outBytes);
        IOUtility.writeString(out, "insertFolder");
        IOUtility.writeFolder(out, folder);
        DataInputStream resultStream = call(outBytes.toByteArray());
        return resultStream.readBoolean();
    }

    public boolean insertFileToFolder(File file, Folder folder) throws IOException, ExecutionException, InterruptedException {
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(outBytes);
        IOUtility.writeString(out, "insertPlayer");
        IOUtility.writeFile(out, file);
        IOUtility.writeFolder(out, folder);
        DataInputStream resultStream = call(outBytes.toByteArray());
        return resultStream.readBoolean();
    }

    public boolean deleteFile(File file) throws IOException, ExecutionException, InterruptedException {
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(outBytes);
        IOUtility.writeString(out, "deleteFile");
        IOUtility.writeFile(out, file);
        DataInputStream resultStream = call(outBytes.toByteArray());
        return resultStream.readBoolean();
    }

    public List<Folder> findAllFolders() throws IOException, ExecutionException, InterruptedException {
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(outBytes);
        IOUtility.writeString(out, "findAllFolders");
        DataInputStream resultStream = call(outBytes.toByteArray());
        return readFolders(resultStream);
    }

    public List<File> findAllFilesInFolder(Folder folder) throws IOException, ExecutionException, InterruptedException {
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(outBytes);
        IOUtility.writeString(out, "findAllFilesInFolder");
        IOUtility.writeFolder(out, folder);
        DataInputStream resultStream = call(outBytes.toByteArray());
        return readFiles(resultStream);
    }

    private List<File> readFiles(DataInputStream in) throws IOException {
        List<File> result = new ArrayList<>();
        int listSize = in.readInt();
        for (int i = 0; i < listSize; i++) {
            result.add(IOUtility.readFile(in));
        }
        return result;
    }

    private List<Folder> readFolders(DataInputStream in) throws IOException {
        List<Folder> result = new ArrayList<>();
        int listSize = in.readInt();
        for (int i = 0; i < listSize; i++) {
            result.add(IOUtility.readFolder(in));
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        channel.close();
    }

    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.connect();
            System.out.println(client.findAllFolders());
            client.insertFolder(new Folder(4, "newFolder"));
            System.out.println(client.findAllFolders());
            client.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}