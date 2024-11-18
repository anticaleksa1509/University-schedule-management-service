package rs.raf003.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf003.model.Ucionica;
import rs.raf003.repositories.UcionicaRepo;
import rs.raf003.service.UcionicaService;

import java.util.Optional;

@RestController
@RequestMapping("/api/ucionica")
public class UcionicaController {
    @Autowired
    UcionicaService ucionicaService;
    @Autowired
    UcionicaRepo ucionicaRepo;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{oznaka}")
    public ResponseEntity<?> getUcionicaOznaka(@PathVariable String oznaka){
        Optional<Ucionica> optionalUcionica = Optional.ofNullable(ucionicaService.getUcionicaPoNazivu(oznaka));
        if(optionalUcionica.isPresent())
            return ResponseEntity.ok(optionalUcionica.get());
        return ResponseEntity.notFound().build();
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Ucionica dodajUcionicu(@RequestBody Ucionica ucionica){
        return ucionicaRepo.save(ucionica);
    }
}
