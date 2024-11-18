package rs.raf003.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf003.model.Grupa;
import rs.raf003.repositories.GrupaRepo;
import rs.raf003.service.GrupaService;

@RestController
@RequestMapping("/api/grupa")
public class GrupaController {

    @Autowired
    GrupaRepo grupaRepo;
    @Autowired
    GrupaService grupaService;
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public Grupa dodajGrupu(@RequestBody Grupa grupa){
        return grupaRepo.save(grupa);
    }

    @DeleteMapping(value = "/{id_grupa}")
    public void deleteGrupa(@PathVariable Integer id_grupa){
         grupaRepo.deleteById(id_grupa);
    }

    @GetMapping(value = "/{id_grupa}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getGrupa(@PathVariable Integer id_grupa){
        try {
            return ResponseEntity.ok().body(grupaService.nadjiPoId(id_grupa));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }
}
