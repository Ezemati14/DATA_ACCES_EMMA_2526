package org.emma2025.fpfa.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    //Trabajamos con SessionFactory que son sesiones para interactuar con la bbdd
    //Creamos una sola instancia de ssesionFactory para toda la aplicacion
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {

        //Lo hacemos en un try, porque si hay error de configuracion, lo muestra y detiene
        try{
            //Con esto Configuration().configure() leemos el archivo hibernate.cfg.xml donde estan
            //los datos de la bbdd

            //Y con buildSessionFactory creamos la sesion utilizando los datos
            // de configuracion
            return new Configuration().configure().buildSessionFactory();
        } catch (Exception ex) {
            System.out.println("Error al inicializar SessionFactory" + ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
    }
    //Podemos usar este metodo desde cualquier parte de la aplicacion,
    //para obtener la sesion
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
