package rs.raf003.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf003.model.Nastavnik;
import rs.raf003.model.Predmet;
import rs.raf003.repositories.NastavnikRepo;
import rs.raf003.repositories.PredmetRepo;

import java.util.Optional;

@Service
public class NastavnikService {

    @Autowired
    NastavnikRepo nastavnikRepo;
    @Autowired
    PredmetRepo predmetRepo;

    public Nastavnik getPoImenuIprezimenu(String ime, String prezime){
        Nastavnik nastavnik = nastavnikRepo.getNastavnikPoImenuIprezimenu(ime,prezime);
        return nastavnik;
    }

    public void dodajPredmet(Integer id_profesor, Integer id_predmet){

        Nastavnik nastavnik1 =
                nastavnikRepo.findById(id_profesor).orElseThrow(() -> new EntityNotFoundException("Nastavnik ne postoji."));
        Predmet predmet1 = predmetRepo.findById(id_predmet).orElseThrow(() -> new EntityNotFoundException("Predmet ne postoji"));

        predmet1.setNastavnik(nastavnik1);
        nastavnik1.getPredmeti().add(predmet1);
        predmetRepo.save(predmet1);
        //nastavnik1.getTermini().addAll(predmet1.getTermini());
        nastavnikRepo.save(nastavnik1);
    }

    public void deleteNastavnik(Integer id_nastavnik){
       Nastavnik nastavnik = nastavnikRepo.findById(id_nastavnik).orElseThrow(()->
               new EntityNotFoundException("Nastavnik nije pronadjen sa tim Id-jem"));
       nastavnikRepo.deleteById(id_nastavnik);

    }

    public Nastavnik getPoZvanju(String zvanje){
        return nastavnikRepo.getPoZvanju(zvanje);
    }



}
