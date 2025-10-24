package org.emma2025.unit1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main
{
    public static void main( String[] args )
    {
        //FileWriter("ejemplo.txt", true) si no existe el archivo lo crea, y al estar en true
        //no borra el archvivo, lo que se escriba lo escribe al final.
        //Si esta en false, borra el archivo

        //BufferedWriter esto se usa para escribir por bloques, y no caracter por caracter

        //BufferedReader esto se usa para leer con el metodo readline()

        //PrintWriter podemos utilizar metodos como print.println
        try(PrintWriter print = new PrintWriter( new BufferedWriter(new FileWriter("ejemplo.txt", true))))
        {
            print.println( "Hello World!" );
            print.println( "y...." );
            print.println("Hasta luego");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
