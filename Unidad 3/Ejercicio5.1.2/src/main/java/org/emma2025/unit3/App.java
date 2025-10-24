package org.emma2025.unit3;

import java.sql.*;
import java.util.Scanner;

public class App
{
    public static void main( String[] args )
    {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "boca";

        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Ingrese NOMBRE de asignatura");
            String nombre = sc.nextLine();

            System.out.println("Ingrese AÑO de asignatura");
            int anio = sc.nextInt();

            try (Connection con = DriverManager.getConnection(url, user, password);
                 Statement st = con.createStatement()) {

                String sentenciaSql = "INSERT INTO subjects (name, year) VALUES ('" + nombre + "', " + anio + ")";
                String sentenciaMostrar = "SELECT * FROM subjects";

                st.executeUpdate(sentenciaSql);

                ResultSet rs = st.executeQuery(sentenciaMostrar);
                System.out.println("NOMBRE AÑO");
                System.out.println("-------------------------");

                while (rs.next()) {
                    System.out.println(rs.getString("name") + "\t" + rs.getInt("year"));
                }

                sc.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
