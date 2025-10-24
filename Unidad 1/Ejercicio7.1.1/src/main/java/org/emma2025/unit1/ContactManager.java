package org.emma2025.unit1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ContactManager {
    private static final String FILE_NAME = "prueba.txt";
    private List<Contact> contacts;

    public ContactManager() {
        contacts = loadContacts();
    }

    public void createContact() throws IOException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Introduce el nombre:");
        String name = sc.nextLine();

        System.out.println("Introduce el apellido:");
        String surname = sc.nextLine();

        System.out.println("Introduce el e-mail:");
        String email = sc.nextLine();

        System.out.println("Introduce el número de teléfono:");
        String phone = sc.nextLine();

        System.out.println("Introduce la descripción:");
        String description = sc.nextLine();

        Contact contact = new Contact(name, surname, phone, email, description);
        contacts.add(contact);
        saveContacts();
        System.out.println("Usuario crado cone exito");
    }

    public void showContacts(){
        if(contacts.isEmpty()){
            System.out.println("Lista de contactos vacia");
        }else {
            System.out.println("Lista de contactos: ");
            //Con una variable Contact buscamos en la lista los contactos que existan
            for (Contact contact : contacts) {
                System.out.println(contact);
                System.out.println("-----------------------------");
            }
        }
    }

    public void searchContact(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Nombre o numero de telefono: ");
        String b = scanner.nextLine();

        boolean encontrado = false;
        for (Contact contact : contacts) {
            if(contact.getFullName().equals(b) || contact.getPhoneNumber().equals(b)) {
                System.out.println("Contacto encontrado:");
                System.out.println(contact);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            System.out.println("No se encontró ningún contacto con ese nombre o número de teléfono.");
        }

    }

    public void saveContacts() throws IOException {
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(FILE_NAME)))) {
            out.writeObject(contacts);
        }catch (IOException e) {
            System.out.println("Error al guardar los contactos: " + e.getMessage());
        }
    }

    public List<Contact> loadContacts() {
        File file = new File(FILE_NAME);
        if(!file.exists()) {
            //si no existe el archivo retorna una lista vacia
            return new ArrayList<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(FILE_NAME)))){
            return (List<Contact>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


}
