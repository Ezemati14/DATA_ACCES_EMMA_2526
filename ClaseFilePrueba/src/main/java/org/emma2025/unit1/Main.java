package org.emma2025.unit1;

import java.io.*;
import javax.swing.*;

public class Main
{
    public static void main( String[] args ) throws FileNotFoundException, IOException
    {
        File file = new File("prueba.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String ruta = file.getParent();
            String line = null;
            while ( (line = br.readLine()) != null )
            {
                File newDir = new File(ruta + File.separator + line);
                if(!newDir.exists()) {
                    newDir.mkdir();
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
