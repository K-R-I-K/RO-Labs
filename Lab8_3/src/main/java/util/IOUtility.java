package util;

import db.entity.File;
import db.entity.Folder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class IOUtility {

    private IOUtility() {}

    public static void writeString(DataOutputStream out, String str) throws IOException {
        out.writeInt(str.length());
        out.writeBytes(str);
    }

    public static String readString(DataInputStream in) throws IOException {
        int length = in.readInt();
        byte[] data = new byte[length];
        in.readFully(data);
        return new String(data);
    }

    public static void writeFile(DataOutputStream out, File file) throws IOException {
        out.writeInt(file.getId());
        writeString(out, file.getName());
        out.writeInt(file.getSize());
        out.writeInt(file.getFolderId());
    }

    public static File readFile(DataInputStream in) throws IOException {
        return new File(in.readInt(), readString(in), in.readInt(), in.readInt());
    }

    public static void writeFolder(DataOutputStream out, Folder folder) throws IOException {
        out.writeInt(folder.getId());
        writeString(out, folder.getName());
    }

    public static Folder readFolder(DataInputStream in) throws IOException {
        return new Folder(in.readInt(), readString(in));
    }
}
