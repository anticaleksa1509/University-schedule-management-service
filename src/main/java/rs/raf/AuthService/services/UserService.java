package rs.raf.AuthService.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import rs.raf.AuthService.client.ClientConfig;
import rs.raf.AuthService.dto.NastavnikDto;
import rs.raf.AuthService.model.Role;
import rs.raf.AuthService.model.User;
import rs.raf.AuthService.model.UserDto;
import rs.raf.AuthService.repositories.RoleRepo;
import rs.raf.AuthService.repositories.UserRepo;
import rs.raf.AuthService.request.TokenRequest;
import rs.raf.AuthService.response.TokenResponse;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;
    @Autowired
    RoleRepo roleRepo;
    @Autowired
    TokenService tokenService;

    @Autowired
    RestTemplate authServiceRestTemplate;

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
    public void sacuvajKorisnika(User user){
        user.setSifra(hashSifre(user.getSifra()));
        userRepo.save(user);
    }

    public void pridruziUloguKorisniku(Long id_user, Long id_role,NastavnikDto nastavnikDto) throws Exception {
        User user = userRepo.findById(id_user).orElseThrow(()->
                new EntityNotFoundException("Nije pronadjen user sa tim id-jem"));
        Role role = roleRepo.findById(id_role).orElseThrow(()->
                new EntityNotFoundException("Nije pronadjen role sa tim id-jem"));
        user.setRole(role);
        userRepo.save(user);
        if(role.getImeUloge().equals("Nastavnik")){

            nastavnikDto.setIme(nastavnikDto.getIme());
            nastavnikDto.setPrezime(nastavnikDto.getPrezime());
            nastavnikDto.setZvanje(nastavnikDto.getZvanje());
            try {
                ResponseEntity<NastavnikDto> response =
                        authServiceRestTemplate.postForEntity("/dodajNastavnika",nastavnikDto, NastavnikDto.class);
            }catch (HttpClientErrorException e){
                e.getStatusCode();
            }
        }else{
            throw new Exception("Uloga je dodata korisniku ali korsisnik cije " +
                    "je telo uneto u zahtev nije dodat u servis za upravljanje rasporedom jer korisnik nije tipa Nastavnik");
        }
    }
    public void obrisiKorisnika(Long id_user){
        User user = userRepo.findById(id_user).orElseThrow(()->
                new EntityNotFoundException("Nije pronadjen user sa tim id-jem"));
        userRepo.delete(user);
    }

    public void updateUser(Long id_user,String username,String sifra,String email,Long id_role){
        User user = userRepo.findById(id_user).orElseThrow(()->
                new EntityNotFoundException("Nije pronadjen korisnik sa tim id-jem"));
        Role role = roleRepo.findById(id_role).orElseThrow(()->
                new EntityNotFoundException("Nije pronadjena uloga sa tim id-jem"));
        user.setUsername(username);
        user.setSifra(hashSifre(sifra));
        user.setEmail(email);
        user.setRole(role);
        userRepo.save(user);
    }

    public TokenResponse login(TokenRequest tokenRequest){
        User user =  userRepo.findUserByUsernameAndSifra(tokenRequest.getUsername(),
                hashSifre(tokenRequest.getSifra())).orElseThrow(()->
                new EntityNotFoundException("Nije pronadjen korisnik sa unetim kredencijalima"));
        Claims claims = Jwts.claims();
        claims.put("id",user.getId_user());
        claims.put("role",user.getRole().getImeUloge());
        return new TokenResponse(tokenService.generateToken(claims));

    }
}
