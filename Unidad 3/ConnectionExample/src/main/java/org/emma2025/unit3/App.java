package org.emma2025.unit3;

import java.sql.*;
import java.util.Properties;

public class App
{
    public static void main( String[] args ) throws SQLException {

        Connection conn = null;
        try {
            // PRIMER EJEMPLO
            //Llamamos al objeto propiedades para obtener el usuario y contraseña
            /**
             * String url = "jdbc:postgresql://localhost:5432/postgres";
             * Properties prop = new Properties();
             * prop.setProperty("user", "postgres");
             * prop.serProperty("password", "boca");
             * conn = DriverManager.getConnection(url, prop);
             */

            // SEGUNDO EJEMPLO
            //Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "boca";
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("SE CONECTO CON LA BBDD");


            Statement stmt = conn.createStatement();
            //Creamos la sentencia que queremos
            String sqlSentencia = "select * from subjects";
            //Executamos la sentencia y la pasamos por parametros son statement
            ResultSet rs = stmt.executeQuery(sqlSentencia);
            System.out.println("CODIGO " + " " + "NOMBRE");
            System.out.println("-------------------------");

            while (rs.next()) {
                System.out.println(rs.getString("code") + "\t" + rs.getString("name"));
            }
            rs.close();

            // TERCER EJEMPLO
            /** Properties props = new Properties();
            props.load(App.class.getClassLoader().getResourceAsStream("./config.properties"));
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            conn = DriverManager.getConnection(url, user, password);*/

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if( conn != null ) {
                conn.close();
            }
        }
    }
}
