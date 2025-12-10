package org.emma2025.fpfa;

import org.emma2025.fpfa.controller.EnrollmentController;
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
        EnrollmentController enrollmentController = new EnrollmentController(session);
        Scanner sc = new Scanner(System.in);

        System.out.println("> vtinstitute para entrar o exit para salir ");
        String input = sc.nextLine().toLowerCase();

        if (input.equalsIgnoreCase("vtinstitute")) {

            boolean running = true;
            while (running) {
                System.out.println("=== VT Institute EMMA 2526 ===");
                System.out.println("-h / --help → mostrar ayuda");
                System.out.println("-a / --add {archivo.xml} → importar estudiantes");
                System.out.println("-e / --enroll → {studentID} {courseId} matricular estudiante");
                System.out.println("-p / --print → {studentID} {courseId} Imprime notas del estudiante");
                System.out.println("-q / --qualify → {studentID} {courseId} Insertamos la nota del estudiante");
                System.out.println("-u / --unenroll → {studentID} Borrar matricula del estudiante de ese curso");
                System.out.println("-d / --delete → {studentID} Borrar estudiante");
                System.out.println("exit → salir");

                System.out.print("> ");
                String cmd = sc.nextLine().trim().toLowerCase();

                if (cmd.equalsIgnoreCase("exit")) {
                    running = false;
                    System.out.println("Cerrando sesión...");
                    session.close();
                }
                else if (cmd.equals("--help") || cmd.equals("-h")) {
                    System.out.println("Opciones disponibles:");
                    System.out.println("-h / --help → mostrar ayuda");
                    System.out.println("-a / --add {archivo.xml} → importar estudiantes");
                    System.out.println("-e / --enroll → {studentID} {courseId} matricular estudiante");
                    System.out.println("-p / --print → {studentID} {courseId} Imprime notas del estudiante");
                    System.out.println("-q / --qualify → {studentID} {courseId} Insertamos la nota del estudiante");
                    System.out.println("-u / --unenroll → {studentID} Borrar matricula del estudiante de ese curso");
                    System.out.println("-d / --delete → {studentID} Borrar estudiante");
                    System.out.println("exit → salir");
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
                else if(cmd.equals("--qualify") || cmd.equals("-q")) {
                    //Cuando ponemos -q, entra a esta funcion y se va al controlador
                    // sc = -q    ... sc vale -q
                    controller.qualify(sc);
                }
                else if (cmd.equals("--print") || cmd.equals("-p")) {
                    controller.printResults(sc);
                }
                else if (cmd.equals("--unenroll") || cmd.equals("-u")) {
                    enrollmentController.eliminaMatricula(sc);
                }
                else if (cmd.equals("--delete") || cmd.equals("-d")) {
                    enrollmentController.deleteStudent(sc);
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
