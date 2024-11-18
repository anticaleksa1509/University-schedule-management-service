package rs.raf003.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf003.model.Predmet;
import rs.raf003.repositories.PredmetRepo;

@Service
public class PredmetService {
    @Autowired
    PredmetRepo predmetRepo;
    public Predmet dodajPredmet(Predmet predmet){
        return predmetRepo.save(predmet);
    }

    public Predmet getPoNazivu(String naziv){
        Predmet predmet = predmetRepo.getPredmetPoNazivu(naziv);
        return predmet;
    }

}
