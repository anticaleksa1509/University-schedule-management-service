package rs.raf.AuthService.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.raf.AuthService.dto.NastavnikDto;
import rs.raf.AuthService.model.Role;
import rs.raf.AuthService.model.User;
import rs.raf.AuthService.repositories.RoleRepo;
import rs.raf.AuthService.repositories.UserRepo;
import rs.raf.AuthService.request.TokenRequest;
import rs.raf.AuthService.security.CheckSecurity;
import rs.raf.AuthService.services.UserService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    UserRepo userRepo;
    @Autowired
    UserService userService;
    @Autowired
    RoleRepo roleRepo;

    public static String hashSifre(String sifra) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(sifra.getBytes());

            // Pretvaranje bajtova u heksadecimalni format
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,value = "/registruj")
    public ResponseEntity<?> registrujKorisnika(@RequestBody User user){

        try{
            userService.sacuvajKorisnika(user);
            return ResponseEntity.ok("Uspesno dodat korisnik");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,value = "/login")
    public ResponseEntity<?> logovanjeKorisnika(@RequestBody TokenRequest tokenRequest){
        try {
            userService.login(tokenRequest);
            return new ResponseEntity<>(userService.login(tokenRequest),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @CheckSecurity(ugole = {"Nastavnik", "Administrator"})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/{username}/{sifra}")
    public ResponseEntity<?> nadji(@RequestHeader("Authorization") String authorization,
                                       @PathVariable String username,@PathVariable String sifra) {
        Optional<User> optionalUser = userRepo.findUserByUsernameAndSifra(username,hashSifre(sifra));
        if(optionalUser.isPresent())
            return ResponseEntity.ok().body("Nadjen je korisnik" + optionalUser.get());
        return ResponseEntity.badRequest().body("Nije nadjen korsnik");

    }


    @CheckSecurity(ugole = {"Administrator", "Nastavnik"})
    @PostMapping( value = "/dodajUloguKorisniku/{id_user}/{id_role}")
    public ResponseEntity<?> dodajUlogu(@RequestHeader("Authorization") String authorization, @PathVariable Long id_user, @PathVariable
                                        Long id_role, @RequestBody NastavnikDto nastavnikDto){
        try {

            userService.pridruziUloguKorisniku(id_user,id_role,nastavnikDto);
            return ResponseEntity.ok("Uspesno dodata uloga korisniku i " +
                    "korisnik je uspesno dodat u servis za upravljanje rasporedom jer je " +
                    "tipa Nastavnik");

        }catch (Exception e){
            return ResponseEntity.ok().body(e.getMessage());
        }
    }

    @CheckSecurity(ugole = {"Administrator"})
    @DeleteMapping("/{id_user}")
    public ResponseEntity<?> obrisiKorisnika(@RequestHeader ("Authorization")
                                                 String authorization, @PathVariable Long id_user){
        try {
            userService.obrisiKorisnika(id_user);
            return ResponseEntity.ok("Uspesno obrisan korisnik");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value = "/{id_user}/{username}/{sifra}/{email}/{id_role}")
    public ResponseEntity<?> azurirajKorisnika(@PathVariable Long id_user,
                                               @PathVariable String username,
                                               @PathVariable String sifra,
                                               @PathVariable String email,
                                               @PathVariable Long id_role){
        try {
            userService.updateUser(id_user,username,sifra,email,id_role);
            return ResponseEntity.ok("Uspesno azuriran korisnik");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
