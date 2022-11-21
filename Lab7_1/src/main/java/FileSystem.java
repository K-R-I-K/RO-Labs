import entity.File;
import entity.Folder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileSystem {
    private static Map<String, Folder> folders = new HashMap<>();
    private static Map<String, File> files = new HashMap<>();

    private static void createId(Folder folder) {
        int id = folders.size() + 1;
        String idToString = "fo" + id;
        while(folders.get(idToString) != null) {
            id++;
            idToString = "fo" + id;
        }
        folder.setId(idToString);
    }

    private static void createId(File file) {
        int id = files.size() + 1;
        String idToString = "id" + id;
        while(files.get(idToString) != null) {
            id++;
            idToString = "id" + id;
        }
        file.setId(idToString);
    }

    public void addFolder(Folder folder) {
        folders.put(folder.getId(), folder);
    }

    public void addFile(File file, String folderId) {
        file.setFolderId(folderId);
        files.put(file.getId(), file);
    }

    public void addNewFolder(Folder folder) {
        createId(folder);
        folders.put(folder.getId(), folder);
    }

    public void addNewFile(File file, String folderId) {
        createId(file);
        file.setFolderId(folderId);
        files.put(file.getId(), file);
    }

    public void removeFolder(Folder folder) {
        folders.remove(folder.getId());
        files.values().removeIf(v -> v.getFolderId().equals(folder.getId()));
    }

    public void removeFile(File file) {
        files.remove(file.getId());
    }

    public void changeFilesFolder(File file, Folder folder) {
        files.get(file.getId()).setFolderId(folder.getId());
    }

    public void renameFile(File file, String newName) {
        files.get(file.getId()).setName(newName);
    }

    public void renameFolder(Folder folder, String newName) {
        folders.get(folder.getId()).setName(newName);
    }

    public File getFile(String id) {
        return files.get(id);
    }

    public Folder getFolder(String id) {
        return folders.get(id);
    }

    public List<File> getFolderFiles(String id){
        return files.values().stream().filter(file -> file.getFolderId().equals(id)).toList();
    }

    public Map<String, Folder> getFolders() {
        return folders;//.values().stream().toList();
    }

    public void print(){
        System.out.println(folders);
        System.out.println(files);
    }
}
