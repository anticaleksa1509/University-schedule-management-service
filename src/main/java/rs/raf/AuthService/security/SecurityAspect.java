package rs.raf.AuthService.security;

import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.raf.AuthService.model.Role;
import rs.raf.AuthService.services.TokenService;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Configuration
public class SecurityAspect {

    @Autowired
    TokenService tokenService;

    @Around("@annotation(rs.raf.AuthService.security.CheckSecurity)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

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

        CheckSecurity checkSecurity = method.getAnnotation(CheckSecurity.class);
        String role = claims.get("role", String.class);
        if (Arrays.asList(checkSecurity.ugole()).contains(role)) {
            return joinPoint.proceed();
        }

        return new ResponseEntity<>("Korisnik nema permisiju za trazeni zahtev" +
                "",HttpStatus.FORBIDDEN);
    }
}
