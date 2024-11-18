package rs.raf003.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rs.raf003.model.Ucionica;
import rs.raf003.repositories.UcionicaRepo;

@Service
public class UcionicaService {
    @Autowired
    UcionicaRepo ucionicaRepo;

    public Ucionica getUcionicaPoNazivu(String naziv){
        return ucionicaRepo.getUcionicaOznaka(naziv);
    }
}
