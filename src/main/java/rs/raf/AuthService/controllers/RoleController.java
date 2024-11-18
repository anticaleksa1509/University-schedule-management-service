package rs.raf.AuthService.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.AuthService.model.Role;
import rs.raf.AuthService.repositories.RoleRepo;
import rs.raf.AuthService.security.CheckSecurity;
import rs.raf.AuthService.services.RoleService;

import java.util.Optional;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleRepo roleRepo;
    @Autowired
    RoleService roleService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>  kreirajKorisnika(@Valid @RequestBody Role role){
       if(role.getImeUloge() == null || role.getOpisUloge() == null){
           return ResponseEntity.badRequest().body("Morate uneti i ime i opis");
       }else {
           roleRepo.save(role);
           return ResponseEntity.ok("Uspesno kreirana uloga");
       }
    }
    @CheckSecurity(ugole = {"Administrator"})
    @DeleteMapping("/{id_role}")
    public ResponseEntity<?> deleteRole(@RequestHeader("Authorization")
            String authorization,@PathVariable Long id_role) {
        try {
            roleService.obrisiUlogu(id_role);
               return ResponseEntity.ok("Uspesno obrisana uloga");
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
