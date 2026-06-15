package org.emma.catchupperiod.services;

import org.emma.catchupperiod.entities.Book;
import org.emma.catchupperiod.entities.Reservation;
import org.emma.catchupperiod.entities.User;
import org.emma.catchupperiod.repositorys.IBooks;
import org.emma.catchupperiod.repositorys.IReservation;
import org.emma.catchupperiod.repositorys.IUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    @Autowired
    private IReservation reservationRepository;
    @Autowired
    private IUsers usersRepository;
    @Autowired
    private IBooks booksRepository;

    //Creamos el metodo para luego llamar desde el controller, y cancelar la reserva
    public String cancelReservation(String isbn, String userCode) {
        //Primer buscamos al usuario
        User user = usersRepository.findById(userCode).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        //Luego el libro
        Book book = booksRepository.findById(isbn).orElseThrow(() -> new IllegalArgumentException("Book no encontrado"));
        //Cuando tenemos los 2, libro y usuario, buscamos en la tabla de Reservation
        Reservation reservation = reservationRepository.findByBookAndBorrower(book, user);
        //Luego con un if, comprobamos si existe la reserva
        //Si no existe la reserva, enviamos un mensaje de error, que lo agarra
        //el try catch del controller
        if(reservation == null) {
            throw new IllegalArgumentException("No existe la reserva para este libro y usuario");
        }
        //Aca va a venir la reserva, entonces la eliminamos de la base de datos, y devolvemos un mensaje de exito
        reservationRepository.delete(reservation);
        //Retornamos un mensaje
        return "Reserva cancelada exitosamente";
    }
}
