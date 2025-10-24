package org.emma2025.unit1;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("File name: ");
        String nameFile = sc.nextLine();

        PrintWriter print = null;


        try {
            //we confirm that the file exists
            boolean lines = true;
            String answer;
            //EXISTS FILE
            if (new File(nameFile).exists()) {
                //If it exists, we ask if you want to overwrite the file
                System.out.println("exists Overwrite the file, or append it?? [Y]overwrite Are [N]append");
                answer = sc.nextLine();

                if (answer == "y" || answer != "n") {
                    System.out.println("exists Overwrite the file, or append it?? [Y]overwrite Are [N]append");
                } else {
                    throw new IllegalArgumentException();
                }

                //OVERWRITE = SOBREESCRIBIR
                if (answer.equals("y")) {
                    print = new PrintWriter(new BufferedWriter(new FileWriter(answer)));

                    while (lines) {
                        System.out.println(" OVERWRITE How many lines do you want to write : ");
                        String line = sc.nextLine();
                        long lineCount = Files.lines(Paths.get(nameFile)).count();
                        print.println(lineCount + ": " + line);

                        System.out.println("OVERWRITE More lines? [y] and [n]");
                        String yesOrNo = sc.nextLine();
                        if (yesOrNo.equals("y")) {
                            //If the user enters y, repeat the while loop again
                            lines = true;
                        } else if (yesOrNo.equals("n")) {
                            lines = false;
                        } else {
                            throw new IllegalArgumentException();
                        }
                    } //cierre while

                }
                //OVERWRITE = SOBREESCRIBIR
                //APPEND = AÑADIR
                else {
                    print = new PrintWriter(new BufferedWriter(new FileWriter(answer, true)));

                    while (lines) {
                        System.out.println("APPEND How many lines do you want to write : ");
                        String line = sc.nextLine();
                        long lineCount = Files.lines(Paths.get(nameFile)).count();
                        print.println(lineCount + ": " + line);

                        System.out.println("APPEND More lines? [y] and [n]");
                        String yesOrNo = sc.nextLine();
                        if (yesOrNo.equals("y")) {
                            //If the user enters y, repeat the while loop again
                            lines = true;
                        } else if (yesOrNo.equals("n")) {
                            lines = false;
                        } else {
                            throw new IllegalArgumentException();
                        }
                    } //cierre while
                }
                //APPEND = AÑADIR

            }
            //EXISTS FILE

            //NO EXISTS FILE
            else {
                print = new PrintWriter(new BufferedWriter(new FileWriter(nameFile, true)));

                while (lines) {
                    System.out.println(" NO EXISTS FILE How many lines do you want to write : ");
                    String line = sc.nextLine();
                    long lineCount = Files.lines(Paths.get(nameFile)).count();
                    print.println(lineCount + ": " + line);

                    System.out.println("NO EXISTS FILE More lines? [y] and [n]");
                    String yesOrNo = sc.nextLine();
                    if (yesOrNo.equals("y")) {
                        //If the user enters y, repeat the while loop again
                        lines = true;
                    } else if (yesOrNo.equals("n")) {
                        lines = false;
                    } else {
                        throw new IllegalArgumentException();
                    }
                } //cierre while
            }
            //NO EXISTS FILE

            print.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
