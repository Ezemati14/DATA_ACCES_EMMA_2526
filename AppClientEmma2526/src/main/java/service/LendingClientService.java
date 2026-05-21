package service;

import connection.HttpResponse;
import connection.RestApiConnection;

public class LendingClientService {

    //Llamamos c restApiconnection, para abrir la conecxion con la API
    private RestApiConnection connection = new RestApiConnection();

    //Y en esta funcion le pasamos por parametros lo que pusimos por consola
    //Esto contruye esto isbn=0141189207445&userCode=A786543&reservar=false
    public HttpResponse lendBook(String isbn, String userCode, boolean reservar) {
        String parametros = "isbn=" + isbn + "&userCode=" + userCode + "&reservar=" + reservar;
        return connection.sendPost("/lending/lend", parametros);
    }

    public HttpResponse returnBook(String isbn, String userCode) {
        String parametros = "isbn=" + isbn + "&userCode=" + userCode;
        return connection.sendPost("/lending/return", parametros);
    }
}
