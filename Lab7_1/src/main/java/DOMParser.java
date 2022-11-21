import entity.Folder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class DOMParser {
    public static class SimpleErrorHandler implements ErrorHandler {
        public void warning(SAXParseException e) {
            System.out.println("Line " +e.getLineNumber() + ":");
            System.out.println(e.getMessage());
        }
        public void error(SAXParseException e) {
            System.out.println("Line " +e.getLineNumber() + ":");
            System.out.println(e.getMessage());
        }
        public void fatalError(SAXParseException e) {
            System.out.println("Line " +e.getLineNumber() + ":");
            System.out.println(e.getMessage());
        }
    }

    public static FileSystem parse(String path) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new SimpleErrorHandler());
        Document doc = builder.parse(new File(path));
        doc.getDocumentElement().normalize();

        FileSystem fileSystem = new FileSystem();
        NodeList nodes = doc.getElementsByTagName("Folder");
        for(int i = 0; i < nodes.getLength(); ++i) {
            Element node = (Element)nodes.item(i);
            Folder folder = new Folder(node.getAttribute("id"), node.getAttribute("name"));
            fileSystem.addFolder(folder);
            for (int j = 0; j < node.getElementsByTagName("File").getLength(); j++) {
                Element element = (Element) node.getElementsByTagName("File").item(j);
                entity.File file = new entity.File(element.getAttribute("id"), element.getAttribute("name"),
                        Integer.parseInt(element.getAttribute("size")), node.getAttribute("id"));
                fileSystem.addFile(file, node.getAttribute("id"));
            }
        }
        return fileSystem;
    }

    public static void write(FileSystem fileSystem, String path) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElement("FileSystem");
        doc.appendChild(root);

        Map<String, entity.Folder> folders = fileSystem.getFolders();
        for(Map.Entry<String, Folder> entry : folders.entrySet()) {
            Element newFolder = doc.createElement("Folder");
            newFolder.setAttribute("id", entry.getValue().getId());
            newFolder.setAttribute("name", entry.getValue().getName());
            root.appendChild(newFolder);

            for(entity.File file: fileSystem.getFolderFiles(entry.getKey())) {
                Element newFile = doc.createElement("File");
                newFile.setAttribute("id", file.getId());
                newFile.setAttribute("name", file.getName());
                newFile.setAttribute("size", String.valueOf(file.getSize()));
                newFolder.appendChild(newFile);
            }
        }
        Source domSource = new DOMSource(doc);
        Result fileResult = new StreamResult(new File(path));
        TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer transformer = tfactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "FileSystem.dtd");
        transformer.transform(domSource, fileResult);
    }
}
