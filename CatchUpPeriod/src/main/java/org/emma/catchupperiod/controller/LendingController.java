package org.emma.catchupperiod.controller;

import org.emma.catchupperiod.entitiesDTO.LendingAñoDto;
import org.emma.catchupperiod.entitiesDTO.LendingDTO;
import org.emma.catchupperiod.entitiesDTO.LendingInfoDTO;
import org.emma.catchupperiod.services.LendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lending")
public class LendingController {

    @Autowired
    private LendingService lendingService;

    //Funcion para presta un libro, por parametro pasamos isb, codeUser y reservar
    //http://localhost:8080/lending/lend?isbn=0141189207445&userCode=A786543
    @PostMapping("/lend")
    public ResponseEntity<String> lendBook(
            @RequestParam String isbn,
            @RequestParam String userCode,
            @RequestParam(required = false) Boolean reservar){
        try {
            String resultado = lendingService.lendBook(isbn, userCode, reservar);
            return ResponseEntity.ok(resultado);
        }catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body( e.getMessage());
        }
    }

    @GetMapping("/lending-info")
    public LendingInfoDTO getLendingInfo(@RequestParam String isbn, @RequestParam String userCode) {
        return lendingService.getLendingInfo(isbn, userCode);

    }

    //Pasamos por parametros el isbn del libro, y el codigo del usuario
    //EJEMPLO = http://localhost:8080/lending/return?isbn=0141189207888&userCode=S976543
    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@RequestParam String isbn,
                                             @RequestParam String userCode){
        String resultado = lendingService.returnBook(isbn, userCode);
        return ResponseEntity.ok(resultado);
    }
    //EJEMPLO = http://localhost:8080/lending/reservation?isbn=0141189207888&userCode=A787878
    @PostMapping("/reservation")
    public ResponseEntity<String> reserveBook(@RequestParam String isbn,
                                              @RequestParam String userCode){
        try {
            lendingService.reserveBook(isbn, userCode);
            return ResponseEntity.ok("Libro Reservado");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Esto muestra una lista con todos los prestamos activos digamos los que no devolvieron
    // el libro todavia, eso lo sabemos por el returningDate que es null
    //https://localhost:8080/lending/active
    @GetMapping("/active")
    public ResponseEntity<List<LendingDTO>> getActiveBook(){
        return ResponseEntity.ok(lendingService.getActiveLendings());
    }

    //Funcion para buscar los lendings por fecha, pasandole el año solamente
    @GetMapping("/lendings-by-year")
    public List<LendingAñoDto> getLendingsByYear(@RequestParam Integer from, @RequestParam Integer to) {
        return lendingService.obtenerPorAño(from, to);
    }
}
