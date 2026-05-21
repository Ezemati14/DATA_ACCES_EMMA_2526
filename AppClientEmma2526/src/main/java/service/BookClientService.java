package service;

import connection.HttpResponse;
import connection.RestApiConnection;
import model.Book;

import java.util.List;

public class BookClientService {
    private RestApiConnection connection = new RestApiConnection();

    public HttpResponse importBooks(List<Book> books){

        String json = convertirJson(books);
        return connection.sendPostJson("/books/add-books", json);
    }

    private String convertirJson(
            List<Book> books){

        StringBuilder json =
                new StringBuilder();

        json.append("[");

        for(int i=0;i<books.size();i++){

            Book b=books.get(i);

            json.append("{");

            json.append("\"isbn\":\"")
                    .append(b.getIsbn())
                    .append("\",");

            json.append("\"title\":\"")
                    .append(b.getTitle())
                    .append("\",");

            json.append("\"copies\":")
                    .append(b.getCopies())
                    .append(",");

            json.append("\"category\":{");

            json.append("\"code\":\"")
                    .append( b.getCategoryCode())
                    .append("\"");

            json.append("}");

            json.append("}");

            if(i<books.size()-1){
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }
}
