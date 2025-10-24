package org.emma2025.unit1;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Name first file");
        String file1 = sc.nextLine();
        System.out.println("Name second file");
        String file2 = sc.nextLine();

        try {
            BufferedReader bR1 = new BufferedReader(new FileReader(file1));
            BufferedReader bR2 = new BufferedReader(new FileReader(file2));
            BufferedWriter bW1 = new BufferedWriter(new FileWriter("sorted.txt"));

            String line1 = bR1.readLine();
            String line2 = bR2.readLine();

            while (line1 != null || line2 != null){
                if(line1 == null){
                    bW1.write("File second" + line2);
                    bW1.newLine();
                    line2 = bR2.readLine();
                }else if(line2 == null){
                    bW1.write("File first" + line1);
                    bW1.newLine();
                    line1 = bR1.readLine();
                } else if(line1.compareToIgnoreCase(line2) <= 0) {
                    bW1.write("File first" + line1);
                    bW1.newLine();
                    line1 = bR1.readLine();
                } else {
                    bW1.write("File second" + line2);
                    bW1.newLine();
                    line2 = bR2.readLine();
                }
                bR1.close();
                bR2.close();
                bW1.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
