import utils.CommandLineParser;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        //Llamamos a la clase commando y guardamos lo que le pasemos por consola
        CommandLineParser parser = new CommandLineParser();

        System.out.print("=== BIBLIOTECA === ");

        while(true) {
            System.out.println();
            System.out.print("Escriba el comando: ");
            String linea = scanner.nextLine();

            if(linea.equalsIgnoreCase("salir")) {
                System.out.println("Programa finalizado");
                break;
            }
            //Dividimos lo que escribirmos por consola, para un mejor manejo
            // ["-a", "libros.xml"] o ["-l", "1234567890", "A1234567"]
            String[] comando = linea.split(" ");
            //Y le pasamos el comando a la clase parser, para que lo procese
            parser.procesarComando(comando);
        }
    }
}
