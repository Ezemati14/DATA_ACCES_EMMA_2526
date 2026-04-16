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
}
