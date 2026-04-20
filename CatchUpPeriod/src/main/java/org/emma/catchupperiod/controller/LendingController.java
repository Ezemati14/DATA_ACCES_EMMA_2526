package org.emma.catchupperiod.controller;

import org.emma.catchupperiod.services.LendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lending")
public class LendingController {

    @Autowired
    private LendingService lendingService;

    @PostMapping("/lend")
    public ResponseEntity<String> lendBook(
            @RequestParam String isbn,
            @RequestParam String userCode,
            @RequestParam(required = false) Boolean reservar){
        String resultado = lendingService.lendBook(isbn, userCode, reservar);
        return ResponseEntity.ok(resultado);
    }

    //Pasamos por parametros el isbn del libro, y el codigo del usuario
    //EJEMPLO = http://localhost:8080/lending/return?isbn=0141189207888&userCode=S976543
    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@RequestParam String isbn,
                                             @RequestParam String userCode){
        String resultado = lendingService.returnBook(isbn, userCode);
        return ResponseEntity.ok(resultado);
    }
}
