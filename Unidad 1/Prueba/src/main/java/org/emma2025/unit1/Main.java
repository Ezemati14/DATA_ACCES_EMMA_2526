package org.emma2025.unit1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main
{
    public static void main( String[] args ) {

        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream("C:\\Users\\Ezemati14\\Desktop\\Acceso a datos\\Unidad 1\\Prueba\\Prueba.txt");
            fos = new FileOutputStream("C:\\Users\\Ezemati14\\Desktop\\Acceso a datos\\Unidad 1\\Prueba\\PruebaOut.txt");

            byte[] datos = new byte[128];
            int byteLeidos;

            while( (byteLeidos = fis.read(datos)) != -1) {
                fos.write(datos, 0 ,byteLeidos);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
