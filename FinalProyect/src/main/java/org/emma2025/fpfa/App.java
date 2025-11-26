package org.emma2025.fpfa;

import org.emma2025.fpfa.controller.StudentController;
import org.emma2025.fpfa.model.entities.Student;
import org.emma2025.fpfa.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        // Creamos la sesión una única vez para toda la aplicación
        Session session = HibernateUtil.getSessionFactory().openSession();
        StudentController controller = new StudentController(session);
        Scanner sc = new Scanner(System.in);

        System.out.println("> ");
        String input = sc.nextLine();

        if (input.equalsIgnoreCase("vtinstitute")) {

            boolean running = true;
            while (running) {
                System.out.println("=== VT Institute EMMA 2526 ===");
                System.out.println("-h / --help → mostrar ayuda");
                System.out.println("-a / --add {archivo.xml} → importar estudiantes");
                System.out.println("-e / --enroll → {studentID} {courseId} matricular estudiante");
                System.out.println("exit → salir");

                System.out.print("> ");
                String cmd = sc.nextLine().trim();

                if (cmd.equalsIgnoreCase("exit")) {
                    running = false;
                    System.out.println("Cerrando sesión...");
                    session.close();
                    System.out.println("Session cerrada. Adiós.");
                }
                else if (cmd.equals("--help") || cmd.equals("-h")) {
                    System.out.println("Opciones disponibles:");
                    System.out.println("-a / --add {archivo.xml} → importar estudiantes desde XML");
                    System.out.println("-e / --enroll → {studentID} {courseId} matricular estudiante en un curso");
                    System.out.println("exit → salir de la aplicación");
                }
                else if (cmd.startsWith("--add")) {
                    String[] parts = cmd.split(" ");
                    if (parts.length == 2) {
                        controller.addFromXML(parts[1]);
                    } else {
                        System.out.println("Uso correcto: --add archivo.xml");
                    }
                }
                else if (cmd.equals("--enroll") || cmd.equals("-e")) {
                    //Aca si se selecciona alguno de estos
                    //Se llamada a la funcion del controlador,
                    // que esta conectado con el service y repository
                    controller.enrollStudent(sc);
                }
                else {
                    System.out.println("Comando no reconocido. Usa --help para ver opciones.");
                }
            }
        } else if (input.equalsIgnoreCase("exit")) {
            session.close();
            System.out.println("Session cerrada.");
        } else {
            System.out.println("Comando no reconocido. Escribe 'vtinstitute' para iniciar o 'exit' para salir.");
        }

        sc.close();
    }
}
