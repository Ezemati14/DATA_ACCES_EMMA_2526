package org.emma2025.unit1;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        ContactManager manager = new ContactManager();
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\nMenú de Contactos:");
            System.out.println("1. Crear nuevo contacto");
            System.out.println("2. Mostrar todos los contactos");
            System.out.println("3. Buscar contacto");
            System.out.println("4. Salir");
            System.out.print("Selecciona una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1 -> manager.createContact();
                case 2 -> manager.showContacts();
                case 3 -> manager.searchContact();
                case 4 -> System.out.println("Saliendo del programa...");
                default -> System.out.println("Opción inválida. Intenta de nuevo.");
            }
        } while (opcion != 4);
    }
}
