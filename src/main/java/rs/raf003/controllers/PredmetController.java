package rs.raf003.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf003.model.Predmet;
import rs.raf003.repositories.PredmetRepo;
import rs.raf003.service.PredmetService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/predmet")
public class PredmetController {
    @Autowired
    PredmetRepo predmetRepo;
    @Autowired
    PredmetService predmetService;
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/all")
    public List<Predmet> dajSvePredmete(){
        return (List<Predmet>)predmetRepo.findAll();
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public Predmet dodajPredmet(@RequestBody Predmet predmet){
        return predmetRepo.save(predmet);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{naziv}")
    public ResponseEntity<?> getPoNazivu(@PathVariable String naziv){
        Optional<Predmet> optionalPredmet = Optional.ofNullable(predmetService.getPoNazivu(naziv));
        if(optionalPredmet.isPresent())
            return ResponseEntity.ok(optionalPredmet.get());
        return ResponseEntity.notFound().build();
    }



}
