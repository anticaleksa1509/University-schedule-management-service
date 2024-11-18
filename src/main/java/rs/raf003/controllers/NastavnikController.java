package rs.raf003.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf003.model.Nastavnik;
import rs.raf003.model.Predmet;
import rs.raf003.repositories.NastavnikRepo;
import rs.raf003.security.CheckSecurity;
import rs.raf003.security.CheckSecurity1;
import rs.raf003.service.NastavnikService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/nastavnik")
public class NastavnikController {

    @Autowired
    NastavnikRepo nastavnikRepo;
    @Autowired
    NastavnikService nastavnikService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/all")
    public List<Nastavnik> getAllNastavnike() {
        return (List<Nastavnik>) nastavnikRepo.findAll();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{ime}/{prezime}")
    public ResponseEntity<?> getNastavnikaImeIprezime(@PathVariable String ime,
                                                      @PathVariable String prezime) {
        Optional<Nastavnik> optionalNastavnik = Optional.ofNullable(nastavnikService.getPoImenuIprezimenu(ime, prezime));
        if (optionalNastavnik.isPresent())
            return ResponseEntity.ok(optionalNastavnik.get());
        return ResponseEntity.notFound().build();

    }

    @CheckSecurity1(uloge = {"Nastavnik", "Administrator"})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,value = "/dodajNastavnika")
    public Nastavnik dodajNastavnika(@RequestHeader("Authorization") String authorization,
                                     @RequestBody Nastavnik nastavnik) {
       return nastavnikRepo.save(nastavnik);
    }

    @PostMapping(value = "/dodajPredmet/{id_nastavnik}/{id_predmet}")
    public ResponseEntity<?> dodajPredmetProfesoru(@PathVariable Integer id_nastavnik,
                                                   @PathVariable Integer id_predmet){

        try {
            nastavnikService.dodajPredmet(id_nastavnik, id_predmet);
            return ResponseEntity.ok("Predmet uspesno dodat");

        }catch (EntityNotFoundException e){
            return ResponseEntity.ok(e.getMessage());

        }
    }



    @DeleteMapping(value = "/{id_nastavnik}")
    public ResponseEntity<?> deleteNastavnik(@PathVariable Integer id_nastavnik){
        try {
            nastavnikService.deleteNastavnik(id_nastavnik);
            return ResponseEntity.ok("Uspesno obrisan nastavnik");
        }catch (EntityNotFoundException e){
            return ResponseEntity.ok(e.getMessage());
        }

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/zvanjeNastavnika/{zvanje}")
    public Nastavnik uzmi(@PathVariable String zvanje){
        return nastavnikService.getPoZvanju(zvanje);
    }


}


