package org.emma.catchupperiod.services;

import org.emma.catchupperiod.entities.Book;
import org.emma.catchupperiod.entities.Lending;
import org.emma.catchupperiod.entities.Reservation;
import org.emma.catchupperiod.entities.User;

import org.emma.catchupperiod.entitiesDTO.LendingDTO;
import org.emma.catchupperiod.entitiesDTO.LendingInfoDTO;
import org.emma.catchupperiod.entitiesDTO.UserDetailsDto;
import org.emma.catchupperiod.repositorys.IBooks;
import org.emma.catchupperiod.repositorys.ILending;
import org.emma.catchupperiod.repositorys.IReservation;
import org.emma.catchupperiod.repositorys.IUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<LendingDTO> getActiveLendings() {
        //Buscamos todos los prestamos activos, que el returningDate sea null
        //todavia no se devolvio el libro
        List<Lending> lendings = lendingRepository.findByReturningdateIsNull();
        //Lista que devolvemos
        List<LendingDTO> resultadoLendings = new ArrayList<>();

        //Aca recorremos la lista donde los prestamos sean activos
        for(Lending lending : lendings) {
            //El dto ahora no viene vacio
            //y se va llenando mientras recorre la lista de prestamos activos
            LendingDTO dto = new LendingDTO();
            //Aca queremos setear el isbn del objeto dto
            //y con el get, llegamos hasta el dato del libro que es el isbn
            //y luego lo guardamos en el dto con el set
            dto.setIsbnDto(lending.getBook().getIsbn()); //dto.setIsbn("9780199535897");
            dto.setTitleDto(lending.getBook().getTitle()); //dto.setTitle("The Great Gatsby");
            dto.setUserNameDto(lending.getBorrower().getName()); //dto.setUserName("John Doe");
            dto.setUserCodeDto(lending.getBorrower().getCode());
            //Obtenemos la fecha en la que se presto el libro
            //y lo guardamos en el dto
            dto.setLendingDateDto(lending.getLendingdate()); //dto.setLendingDate(LocalDate.of(2023, 10, 1));
            //Aca le agregamos 7 dias a la fecha que obtenemos del prestamo
            LocalDate dueDate = lending.getLendingdate().plusDays(7); //dto.setDueDate(LocalDate.of(2023, 10, 8));
            //Y guardamos esa fecha en el dto
            dto.setDueDateDto(dueDate);
            //Obtenemos la fecha de hoy, con LocalDate.now(). y pregutamos si se paso de la fehca limite
            //devuelve tru o false.
            //Al estar dentro de la fecha, nos va a mostrar false, si se pasa de la fecha, pasa a true
            dto.setDelayedDto(LocalDate.now().isAfter(dueDate));

            resultadoLendings.add(dto);
        }
        return resultadoLendings;
    }

    public List<UserDetailsDto> getUserDetails(String userCode){
        //Primero buscamos al usuario con el que queremos trabajar
        User user = usersRepository.findById(userCode)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        System.out.println("Usuario: " + userCode);
        //Una vez encontrado el usario, buscamos en la tabla lendinsg ese Usuario
        List<Lending> lendings = lendingRepository.findByBorrower_Code(userCode);
        System.out.println("Cantidad de préstamos: " + lendings.size());
        //Creamos una lista en donde se va a guardar ese usuario
        List<UserDetailsDto> resultado = new ArrayList<>();
        //Recorremos el lendings que encontramos, y vamos guardando esos datos
        //en el dto que creamos dentro del for, y luego lo guardamos en la lista resultado, que es la que devolvemos
        for(Lending lending : lendings) {
            UserDetailsDto dto = new UserDetailsDto();
            dto.setUserCodeDto(user.getCode());
            dto.setUserNameDto(user.getName());
            dto.setUserEmailDto(user.getEmail());
            dto.setIsbnDto(lending.getBook().getIsbn());
            dto.setBookTitleDto(lending.getBook().getTitle());
            dto.setRetturningDateDto(lending.getReturningdate());
            resultado.add(dto);
        }
        return resultado;
    }



    //--------------------- FUNCIONES PARA APLICACION CLIENTE -----------------------------

    public LendingInfoDTO getLendingInfo(String isbn, String userCode) {
        Book book = booksRepository.findById(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));
        User user = usersRepository.findById(userCode)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        Optional<Lending> lending = lendingRepository.findByBookAndBorrowerAndReturningdateIsNull(book, user);

        System.out.println("ISBN: " + book.getIsbn());
        System.out.println("USER: " + user.getCode());
        System.out.println("LENDING: " + lending);

        if(lending.isEmpty()) {
            throw new IllegalArgumentException("No existe préstamo activo");
        }
        LendingInfoDTO dto = new LendingInfoDTO();
        dto.setLendingId(lending.get().getId());
        dto.setUserCode(lending.get().getBorrower().getCode());
        dto.setIsbn(lending.get().getBook().getIsbn());
        dto.setLendingDate(lending.get().getLendingdate());
        return dto;
    }
}
