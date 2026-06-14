import utils.CommandLineParser;

public class Main {
    public static void main(String[] args) {
        //Llamamos a la clase commando y guardamos lo que le pasemos por consola
        CommandLineParser parser = new CommandLineParser();
        parser.procesarComando(args);
    }
}
// Para prestar un libro, y crear un txt Run -> Edit Configurartions -> -l 9780195062052 T654322 -w