package org.emma2025.fpfa;

import org.emma2025.fpfa.controller.StudentController;
import org.emma2025.fpfa.model.entities.Student;
import org.emma2025.fpfa.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {

            //Creamos la sesion una unica vez para la aplicacion
            //Nos conectamos con la base de datos
            Session session = HibernateUtil.getSessionFactory().openSession();
            //Le pasamos la sesion porque no la crea por si sola
            //Asi nos ahorramos tener que abrir multiples sesiones
            StudentController controller = new StudentController(session);
            Scanner sc = new Scanner(System.in);

            System.out.println("> ");
            String input = sc.nextLine();

            if (input.equalsIgnoreCase("vtinstitute")) {

                while (true) {
                    System.out.println("=== VT Institute ===");
                    System.out.println("-h / --help → mostrar ayuda");
                    System.out.println("-a / --add {archivo.xml} → importar estudiantes");
                    System.out.println("exit → salir");

                    System.out.print("> ");
                    String cmd = sc.nextLine();

                    if (cmd.equalsIgnoreCase("exit")) {
                        session.close();
                        System.out.println("Session cerrada. Adiós.");
                        break;
                    }

                    if (cmd.startsWith("--add")) {
                        String[] parts = cmd.split(" ");
                        if (parts.length == 2) {
                            controller.addFromXML(parts[1]);
                        } else {
                            System.out.println("Uso correcto: --add archivo.xml");
                        }
                    } else if (cmd.equals("--help") || cmd.equals("-h")) {
                        System.out.println("Ayuda...");
                    } else {
                        System.out.println("Comando no reconocido.");
                    }
                }
            }
        }
}
