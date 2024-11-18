package rs.raf003.controllers;

import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import rs.raf003.model.Termin;
import rs.raf003.repositories.TerminRepository;
import rs.raf003.security.CheckSecurity;
import rs.raf003.security.CheckSecurity1;
import rs.raf003.security.tokenService.TokenService;
import rs.raf003.service.TerminService;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/termin")
public class TerminController {
    @Autowired
    TerminRepository terminRepository;
    @Autowired
    TerminService terminService;
    @Autowired
    TokenService tokenService;


    @PostMapping(value = "/dodajTermin/{pocTermin}/{krajTermin}/{danUnedelji}/{tipNastave}" +
            "/{id_predmet}/{id_ucionica}/{id_nastavnik}/{id_grupa}" )
    @CheckSecurity1(uloge = {"Administrator","Nastavnik"})
    //CheckSecurity1 provera samo da li je korisnik tipa administrator i nastavnik
    public ResponseEntity<?> dodajTermin(@RequestHeader("Authorization") String authorization,
                                         @PathVariable LocalTime pocTermin, @PathVariable LocalTime krajTermin,
                                         @PathVariable DayOfWeek danUnedelji, @PathVariable String tipNastave,
                                         @PathVariable Integer id_predmet, @PathVariable Integer id_ucionica,
                                         @PathVariable Integer id_nastavnik, @ PathVariable Integer id_grupa){
    try{
        String token = authorization.substring(7);
        Claims claims = tokenService.parseToken(token);
        Long id_korisnika = Long.valueOf(claims.get("id", Integer.class));
        terminService.dodajTermin(pocTermin,krajTermin,danUnedelji,tipNastave,id_predmet,id_ucionica,
                id_nastavnik,id_grupa,id_korisnika);
        return ResponseEntity.ok("Uspesno dodat termin, dodao ga je korisnik sa id-jem " + id_korisnika);
//
    } catch (Exception e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/terminiZaGrupu/{id_grupa}")
    public ResponseEntity<List<Termin>> nadjiTermin(@PathVariable Integer id_grupa){
        try {
            List<Termin> termins = terminService.getTerminezaGrupu(id_grupa);
            return ResponseEntity.status(HttpStatus.OK).body(termins);
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/terminiZaNastavnika/{id_nastavnik}")
    public ResponseEntity<List<Termin>> getTermineZaNastavnika(@PathVariable Integer id_nastavnik){

        List<Termin> terminiNastavik = terminService.getTerminPoNasvniku(id_nastavnik);
        if(terminiNastavik.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(terminiNastavik,HttpStatus.OK);
        }

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/terminiZaPredmet/{id_predmet}")
    public ResponseEntity<List<Termin>> getTerminZaPredmet(@PathVariable Integer id_predmet){
        List<Termin> termins = terminRepository.findPredmetById(id_predmet);
        if(termins.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(termins,HttpStatus.OK);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value =
            "/terminiZaDanIucionicu/{danUnedelji}/{id_ucionica}")
    public ResponseEntity<List<Termin>> getTermine(@PathVariable DayOfWeek danUnedelji,
                                                   @PathVariable Integer id_ucionica){
        List<Termin> termins = terminService.getTerminPoDanuIucionici(DayOfWeek.valueOf(String.valueOf(danUnedelji)),id_ucionica);
        if(termins.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(termins,HttpStatus.OK);
    }



    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/PoDanuRastuce")
    public ResponseEntity<List<Termin>> getRastuceDan(){
        List<Termin> termins = terminService.terminiRastucePoDanu();
        if(termins.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(termins,HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/PoDanuOpadajuce")
    public ResponseEntity<List<Termin>> getOpadajuceDan(){
        List<Termin> termins = terminService.terminiOpadajucePoDanu();
        if(termins.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(termins,HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/PoUcioniciRastuce")
    public ResponseEntity<List<Termin>> getRastucePoUcionici(){
        List<Termin> termins = terminService.terminiRastucePoUcionici();
        if(termins.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(termins,HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/PoUcioniciOpadajuce")
    public ResponseEntity<List<Termin>> getOpadajucePoUcionici(){
        List<Termin> termins = terminService.terminiOpadajucePoUcionici();
        if(termins.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(termins,HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/PoVremenuRastuce")
    public ResponseEntity<List<Termin>> getRastucePoVremenu(){
        List<Termin> termins = terminService.terminiPoVremenuRastuce();
        if(termins.isEmpty())
            return (ResponseEntity<List<Termin>>) ResponseEntity.status(HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.OK).body(termins);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/PoVremenuOpadajuce")
    public ResponseEntity<List<Termin>> getPoVremenuOpadajuce(){
        List<Termin> termins = terminService.terminiPoVremenuOpadajuce();
        if(termins.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        return ResponseEntity.status(HttpStatus.OK).body(termins);

    }

    @CheckSecurity(ugole = {"Administrator","Nastavnik"})
    //brisanje po parametrima takodje moze da radi samo nastavnik koji predaje
    //predmet za koji je termin vezan i administrator
    @DeleteMapping(value = "/{pocetakTermina}/{danUnedelji}/{id_ucionica}")
    public ResponseEntity<?> obrisiPoParametrima(@RequestHeader("Authorization") String authorization,
                                                 @PathVariable LocalTime pocetakTermina,
                                                 @PathVariable DayOfWeek danUnedelji,
                                                 @PathVariable Integer id_ucionica){
        try {
            terminService.obrisiTerminPoParametrima(pocetakTermina,danUnedelji,id_ucionica);
            return ResponseEntity.status(HttpStatus.OK).body("Uspesno obrisan termin");
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @DeleteMapping(value = "/poIdTermina/{id_termin}")
    @CheckSecurity(ugole = {"Administrator","Nastavnik"})
    //CheckSecurity provera da li nastavnik predaje predmet za koji je vezan termin
    //moze admin ili nastavnik koji predaje
    public ResponseEntity<?> obrisiPoId(@RequestHeader ("Authorization")
                                            String authorization,
                                        @PathVariable Integer id_termin){
        String token = authorization.substring(7);
        Claims claims = tokenService.parseToken(token);
        Long id_korisnika = Long.valueOf(claims.get("id", Integer.class));
        try {
            terminService.obrisiPoIdTermina(id_termin,id_korisnika);
            return ResponseEntity.ok().body("Uspesno obrisan termin sa id-jem" + id_termin + "." +
                    "Termin je obrisao korisnik sa id-jem" + id_korisnika);
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping(value = "/{pocetakTermina}/{krajTermina}/{id_termin}")
    @CheckSecurity(ugole = {"Administrator","Nastavnik"})
    public ResponseEntity<?> izmeniTermin(@RequestHeader ("Authorization")
                                              String authorization,
                                              @PathVariable LocalTime pocetakTermina,
                                          @PathVariable LocalTime krajTermina,
                                          @PathVariable Integer id_termin) throws Exception{
        String token = authorization.substring(7);
        Claims claims = tokenService.parseToken(token);
        Long id_korisnika = Long.valueOf(claims.get("id", Integer.class));

        try {
            terminService.izmeniTermin(pocetakTermina,krajTermina, id_termin,id_korisnika);
            return ResponseEntity.status(HttpStatus.OK).body("Uspesno ste izmenili termin " +
                    "sa id-jem " + id_termin + ".Korisnik koji je napravio izmenu je korisnik" +
                    " sa id-jem " + id_korisnika + ".");
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/nadjiPoId/{id_termin}")
    public ResponseEntity<Optional<Termin>> nadjiTermin(@PathVariable Long id_termin){
        Optional<Termin> termin = terminRepository.findById(Math.toIntExact(id_termin));
        if(termin.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(termin);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }









}
