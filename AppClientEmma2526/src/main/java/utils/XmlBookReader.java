package utils;

import model.Book;
import model.Category;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

//Esta clase se encarga de leer el XML de libros y validarlo.
//Si encuentra algo mal, lanza una IllegalArgumentException con el mensaje
//y los datos que produjeron el error.
public class XmlBookReader {

    public List<Book> readBooks(String ruta) {

        List<Book> books = new ArrayList<>();

        try {

            File file = new File(ruta);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            NodeList lista = document.getElementsByTagName("book");

            for(int i=0;i<lista.getLength();i++){

                Element elemento =
                        (Element) lista.item(i);

                Book book = new Book();

                book.setIsbn( elemento
                                .getElementsByTagName("isbn")
                                .item(0)
                                .getTextContent()
                );

                book.setTitle( elemento
                                .getElementsByTagName("title")
                                .item(0)
                                .getTextContent()
                );

                book.setCopies( Integer.parseInt(elemento
                                        .getElementsByTagName("copies")
                                        .item(0)
                                        .getTextContent()
                        )
                );

                Category category = new Category();

                category.setCode( elemento
                                .getElementsByTagName("code")
                                .item(0)
                                .getTextContent()
                );

                book.setCategory(category.getCode());
                books.add(book);
            }

        } catch(Exception e){
            throw new RuntimeException( e.getMessage());
        }
        return books;
    }
}
