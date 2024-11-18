package rs.raf003.security;

import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.raf003.model.Nastavnik;
import rs.raf003.model.Predmet;
import rs.raf003.model.Termin;
import rs.raf003.repositories.NastavnikRepo;
import rs.raf003.repositories.PredmetRepo;
import rs.raf003.repositories.TerminRepository;
import rs.raf003.security.tokenService.TokenService;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Aspect
@Configuration
public class SecurityAspect {

    @Autowired
    TokenService tokenService;
    @Autowired
    NastavnikRepo nastavnikRepo;
    @Autowired
    PredmetRepo predmetRepo;
    @Autowired
    TerminRepository terminRepository;


    @Around("@annotation(rs.raf003.security.CheckSecurity1)")
    public Object around1(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        String token = null;
        for (int i = 0; i < methodSignature.getParameterNames().length; i++) {
            if (methodSignature.getParameterNames()[i].equals("authorization")) {

                if (joinPoint.getArgs()[i].toString().startsWith("Bearer")) {

                    token = joinPoint.getArgs()[i].toString().split(" ")[1];
                }
            }
        }

        if (token == null) {
            return new ResponseEntity<>("Korisnik nije autorizovan",HttpStatus.UNAUTHORIZED);
        }

        Claims claims = tokenService.parseToken(token);

        if (claims == null) {
            return new ResponseEntity<>("Korisnik nije autorizovan",HttpStatus.UNAUTHORIZED);
        }

        CheckSecurity1 checkSecurity1 = method.getAnnotation(CheckSecurity1.class);
        String role = claims.get("role", String.class);
        if (Arrays.asList(checkSecurity1.uloge()).contains(role)) {
            return joinPoint.proceed();
        }

        return new ResponseEntity<>("Korisnik nema permisiju za trazeni zahtev" +
                "",HttpStatus.FORBIDDEN);
    }


    @Around("@annotation(rs.raf003.security.CheckSecurity)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        Object[] arguments = joinPoint.getArgs();
        Integer id_termina = null;
        for (Object argument: arguments){
            if(argument instanceof Integer){
                id_termina = (Integer) argument;
                break;
            }
        }
        Termin termin = terminRepository.findById(id_termina).orElseThrow();

        String token = null;
        for (int i = 0; i < methodSignature.getParameterNames().length; i++) {
            if (methodSignature.getParameterNames()[i].equals("authorization")) {
                //Check bearer schema
                if (joinPoint.getArgs()[i].toString().startsWith("Bearer")) {
                    //Get token
                    token = joinPoint.getArgs()[i].toString().split(" ")[1];

                }
            }
        }

        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Claims claims = tokenService.parseToken(token);

        if (claims == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        CheckSecurity checkSecurity = method.getAnnotation(CheckSecurity.class);
        String role = claims.get("role", String.class);

        if (Arrays.asList(checkSecurity.ugole()).contains(role)) {
            if(role.equals("Administrator")) {
                return joinPoint.proceed();
            }else{
                //ovde cemo da probamo da odradimo logiku za brisanje termina
                Integer id_user = claims.get("id",Integer.class);
                Nastavnik nastavnik = nastavnikRepo.findById(id_user).orElseThrow();
                List<Predmet> predmets = nastavnik.getPredmeti();
                //sada imam listu predmeta ulogovanog nastavnika
                if(predmets.contains(termin.getPredmet())){
                    return joinPoint.proceed();
                }else{
                    return new ResponseEntity<>("Nastavnik ne predaje predmet " +
                            "za koji je termin vezan tako da nema permisiju za trazeni zahtev",
                            HttpStatus.FORBIDDEN);
                }
            }
        }

        return new ResponseEntity<>("Korisnik nema permisiju za trazeni zahtev" +
                "",HttpStatus.FORBIDDEN);
    }


}
