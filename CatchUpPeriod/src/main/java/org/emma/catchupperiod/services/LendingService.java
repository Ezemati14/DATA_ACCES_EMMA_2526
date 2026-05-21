package org.emma.catchupperiod.services;

import org.emma.catchupperiod.entities.Book;
import org.emma.catchupperiod.entities.Lending;
import org.emma.catchupperiod.entities.Reservation;
import org.emma.catchupperiod.entities.User;
import org.emma.catchupperiod.repositorys.IBooks;
import org.emma.catchupperiod.repositorys.ILending;
import org.emma.catchupperiod.repositorys.IReservation;
import org.emma.catchupperiod.repositorys.IUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LendingService {

    @Autowired
    private ILending lendingRepository;
    @Autowired
    private IBooks booksRepository;
    @Autowired
    private IUsers usersRepository;
    @Autowired
    private IReservation reservationRepository;

    private int maxLending = 3;

    //Funcion para reservar un libro pasando por parametro el isbn del libro y el codigo del usuario
    public void reserveBook(String isbn, String userCode){
        //buscamos al usuario por el code que nos llega por parametros
        User user = usersRepository.findById(userCode)
                .orElseThrow(() -> new IllegalArgumentException("No existe el usuario"));

        //buscamos el libro por isbn que se va a reservar
        Book book = booksRepository.findById(isbn)
                .orElseThrow(() -> new IllegalArgumentException("No existe el libro"));

        int prestamosActivos = lendingRepository.countByBookAndReturningdateIsNull(book);
        int copiasDisponibles = book.getCopies() - prestamosActivos;

        if(copiasDisponibles <= 0) {
            throw new IllegalArgumentException("No hay copias disponibles para reservar");
        }

        //Comprobamos en la base de datos de reservas, si existe ese usuario y libro
        //Asi evitamos que exista la misma persona, con el mismo libro
       boolean existeReserva = reservationRepository.existsByBookAndBorrower(book, user);

       //Si existe enviamos error
       if(existeReserva){
           throw new IllegalArgumentException("Ya tienes una reserva para este libro");
       }

       //Si va t0do bien, creamos la reserva, con el libro, el usuario y la fecha de hoy
       Reservation reservation = new Reservation();
       reservation.setBook(book);
       reservation.setBorrower(user);
       reservation.setDate(LocalDate.now());

       //Reducimos una copia cuando reservamos el libro
       //book.setCopies(book.getCopies() - 1);

       reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations(String code) {
        User user = usersRepository.findById(code)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return reservationRepository.findByBorrower(user);
    }

    public List<Lending> getAllLendings(String code) {
        User user = usersRepository.findById(code)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return lendingRepository.findByBorrower(user);
    }

    public String lendBook(String isbn, String userCode, Boolean reservar) {
        // 1. Buscamos usuario
        User user = usersRepository.findById(userCode)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        // 2. Buscar libro
        Book book = booksRepository.findById(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));
        // 3. Comprobar si tiene sancion

        //Comprobamos si el usuario tiene una sancion, y si esta dentro de la fecha de sancion
        if(user.getFined() != null && user.getFined().isAfter(LocalDate.now())) {
            return "Usuario sancionado hasta " + user.getFined();
        }

        //Comprobamos si existe un libro, usuario y que libros que no fueron devueltos.
        if(lendingRepository.existsByBookAndBorrowerAndReturningdateIsNull(book, user)) {
            throw new IllegalArgumentException("Ya tiene este libro prestado");
        }

        // 4. Comprobar prestamos activos del usuario
        int usuarioActivos = lendingRepository.countByBorrowerAndReturningdateIsNull(user);
        if(usuarioActivos >= maxLending) {
            return "Usuario tiene el maximo de prestamos activos";
        }

        // 5. Calcular copias disponibles
        int activosLibro = lendingRepository.countByBookAndReturningdateIsNull(book);
        int disponibles = book.getCopies() - activosLibro;

        // 6. Si no hay copias disponibles, comprobar si se puede reservar
        if(disponibles <= 0) {
            //
            if(Boolean.TRUE.equals(reservar)) {
                if(user.getEmail() == null && user.getPhone() == null){
                    return "No se puede reservar sin datos del contacto";
                }

                Reservation reservation = new Reservation();
                reservation.setBook(book);
                reservation.setBorrower(user);
                reservation.setDate(LocalDate.now());
                reservationRepository.save(reservation);
                return "No hay copias, reserva creada";
            }
        }

        // 7. Comprobamos reservas existentes
        List<Reservation> reservas = reservationRepository.findByBookOrderByDateAsc(book);

        if(!reservas.isEmpty()){
            Reservation primeraReserva = reservas.get(0);

            if(!primeraReserva.getBorrower().getCode().equals(userCode)) {
                 return "El libro esta reservado por otro usuario";
            }
        }
        // 8. Creamos el prestamo
        Lending lending = new Lending();
        lending.setBook(book);
        lending.setBorrower(user);
        lending.setLendingdate(LocalDate.now());
        lendingRepository.save(lending);

        return "Prestamo completo por usuario : " + user.getName();
    }

    public String returnBook(String isbn, String userCode) {
        //Este mensaje se va actualizando segun el resultado de cada paso.
        String mensaje = "Libro devuelvedo correctamente";

        //De esta manera buscamos por el isbn y el id en la base de datos
        User usuario = usersRepository.findById(userCode)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Book libro = booksRepository.findById(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));
        //Usamos este metodo para buscar ese libro, usuario, y que el returningDate sea null
        //Asi sabemos que ese libro no se devolvio.
        Lending prestamo = lendingRepository.findByBookAndBorrowerAndReturningdateIsNull(libro, usuario)
                .orElseThrow(() -> new IllegalArgumentException("No existe un préstamo activo para devolver"));

        //Agarramos la fecha de hoy
        LocalDate today = LocalDate.now();
        //Con set, guardamos la fecha de hoy que esta en today
        //Decimos que hoy se devolvio el libro
        prestamo.setReturningdate(today);
        //Y guardamos en la base de datos, la fecha
        lendingRepository.save(prestamo);
        //Comprobamos retraso de 7 dias
        LocalDate fechaLimite = prestamo.getLendingdate().plusDays(7);

        if(today.isAfter(fechaLimite)) {
            //Le agregamos sancion al usuario de 15 dias
            usuario.setFined(today.plusDays(15));
            usersRepository.save(usuario);
            mensaje += "Usuario sancionado por retraso de 15 dias";
        }
        List<Reservation> reservas = reservationRepository.findByBookOrderByDateAsc(libro);

        if(!reservas.isEmpty()) {
            Reservation primera = reservas.get(0);
            User reservado = primera.getBorrower();
            mensaje += " Libro reservado por: "
                    + reservado.getCode() + " - "
                    + reservado.getName();
        }
        return mensaje;
    }
}
