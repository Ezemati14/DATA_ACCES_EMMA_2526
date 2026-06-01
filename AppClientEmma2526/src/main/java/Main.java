import utils.CommandLineParser;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Llamamos a la clase commando y guardamos lo que le pasemos por consola
        CommandLineParser parser = new CommandLineParser();
        parser.procesarComando(args);
    }
}
