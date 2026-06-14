package utils;

import connection.HttpResponse;
import model.Book;
import service.BookClientService;
import service.LendingClientService;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.Scanner;

//Este es el que entiende lo que le pasamos por consola, y lo procesa
public class CommandLineParser {

    private LendingClientService lendingService = new LendingClientService();
    private Scanner scanner = new Scanner(System.in);

    //En esta funcion por parametro nos llega algo parecido a esto: ["-l", "1234567890", "A1234567"]
    public void procesarComando(String[] args) {
        //Si el array llega vacio, enviamos un mensaje de error y salimos
        if (args.length == 0) {
            System.out.println("No se indicó comando");
            return;
        }
        //De esto["-l", "1234567890", "A1234567"] elegimos la primera
        String opcion = args[0];
        //Y dependiendo de lo que sea, ejecutamos una funcion u otra
        switch (opcion) {
            case "-l":
            case "--lend":
                //Si se escribio alguno de estos 2 casos
                //ejecutamos esta funcion
                prestarLibro(args);
                break;
            case "-r":
            case "--return":
                String isbn = args[1];
                String userCode = args[2];
                //Si se escribio alguno de estos 2 casos
                //ejecutamos esta funcion
                HttpResponse respuestaReturn = lendingService.returnBook(isbn, userCode);
                System.out.println( respuestaReturn.getBody() );
                break;
            case "-a":
            case "--add":
                XmlBookReader reader = new XmlBookReader();
                List<Book> books = reader.readBooks(args[1]);

                BookClientService service = new BookClientService();

                HttpResponse respuestaAddBook = service.importBooks(books );

                System.out.println( "Código: "+ respuestaAddBook.getStatus());
                System.out.println( respuestaAddBook.getBody() );
                break;
            case "-t":
            case "--txt":
                exportarPrestamosActivos();
                break;
            default:
                System.out.println("Comando desconocido");
        }
    }
    //Funcion para prestar un libro, y le pasamos lo que escribimos por consola
    private void prestarLibro(String[] args) {
        //Si son menos de 3, es que no se escribio tod0 lo necesario.
        if (args.length < 3) {
            System.out.println("Uso: -l ISBN CODIGO");
            return;
        }
        //Aca lo que hacemos es guardar en estas posiciones
        //el isbn del libro y el codigo del usuario
        String isbn = args[1];
        String userCode = args[2];
        boolean exportar = false;

        if(args.length >= 4 && args[3].equals("-w")) {
            exportar = true;
        }

        //Primer intento: prestar sin reservar
        HttpResponse respuesta = lendingService.lendBook(isbn, userCode, false);

        String cuerpo = respuesta.getBody();
        System.out.println(cuerpo);

        //Si la respuesta indica que no hay copias, le preguntamos al usuario
        if (cuerpo != null && cuerpo.toLowerCase().contains("no hay copias")) {
            //Le pregutamos al usuario si quiere reservar
                System.out.print("Quiere reservar? (si/no): ");

                String opcion = scanner.nextLine().trim().toLowerCase();

            if (opcion.equals("si") || opcion.equals("s")) {
                    //Como entro al if, volvemos a llamar a la api, y le pasamos true en el parametro de reservar
                    HttpResponse respuestaReserva = lendingService.lendBook(isbn, userCode, true);
                    //y mostramos la respuesta de la reserva
                    System.out.println(respuestaReserva.getBody());
            } else {
                System.out.println("Reserva cancelada");
            }
        }
        if(exportar) {
            HttpResponse infoPrestamo = lendingService.getLendingInfo(isbn, userCode);
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("prestamosInfo.txt"));
                writer.write(infoPrestamo.getBody());
                writer.close();
                System.out.println("Archivo prestamo.txt creado");
            }catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void exportarPrestamosActivos() {
        HttpResponse respuesta = lendingService.getActiveLendings();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("prestamos.txt"));
            writer.write(respuesta.getBody());
            writer.close();
            System.out.println("Archivo prestamos.txt creado");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
