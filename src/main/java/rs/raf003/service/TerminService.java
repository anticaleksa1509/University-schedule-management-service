package rs.raf003.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import rs.raf003.model.*;
import rs.raf003.repositories.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

@Service
public class TerminService {
    @Autowired
    GrupaRepo grupaRepo;
    @Autowired
    PredmetRepo predmetRepo;
    @Autowired
    UcionicaRepo ucionicaRepo;
    @Autowired
    NastavnikRepo nastavnikRepo;
    @Autowired
    TerminRepository terminRepository;
    @Autowired
    JmsTemplate jmsTemplate;

    private String destination;

    public TerminService(TerminRepository terminRepository, JmsTemplate jmsTemplate, @Value("${destination.Message}") String destination) {
        this.terminRepository = terminRepository;
        this.jmsTemplate = jmsTemplate;
        this.destination = destination;
    }


    public void dodajTermin(LocalTime pocTermin, LocalTime krajTermin, DayOfWeek danUnedelji, String tipNastave,
                            Integer id_predmet, Integer id_ucionica,
                            Integer id_nastavnik, Integer id_grupa, Long id_korisnika) throws Exception {
        Termin termin1 = new Termin();
        termin1.setPocetakTermina(pocTermin);
        termin1.setKrajTermina(krajTermin);
        termin1.setDanUnedelji(danUnedelji);
        termin1.setTipNastave(tipNastave);
        Nastavnik nastavnik1 = nastavnikRepo.findById(id_nastavnik).orElseThrow(() ->
                new EntityNotFoundException("Nastavnik sa datim id podatkom nije pronadjen"));
        Grupa grupa1 = grupaRepo.findById(id_grupa).orElseThrow(() ->
                new EntityNotFoundException("Grupa sa datim id podatkom ne postoji"));
        Predmet predmet1 = predmetRepo.findById(id_predmet).orElseThrow(()->
                new EntityNotFoundException("Predmet sa datim id podatkom ne postoji"));
        Ucionica ucionica1 = ucionicaRepo.findById(id_ucionica).orElseThrow(()->
                new EntityNotFoundException("Ucionica sa datim id podatkom ne postoji"));

        termin1.setGrupa(grupa1);
        grupa1.getTermini().add(termin1);
        termin1.setUcionica(ucionica1);
        ucionica1.getTermini().add(termin1);
        termin1.setNastavnik(nastavnik1);
        nastavnik1.getTermini().add(termin1);
        termin1.setPredmet(predmet1);
        predmet1.getTermini().add(termin1);


        List<Termin> mojiTermini = (List<Termin>) terminRepository.findAll();
        for(Termin t: mojiTermini){

            if(t.getDanUnedelji().equals(danUnedelji) ) {
                if (t.getUcionica().equals(ucionica1)){
                    ///ako je isti dan i ista ucionica moram da proverim vreme
                    if (termin1.getKrajTermina().isAfter(t.getPocetakTermina())
                                    && termin1.getKrajTermina().isBefore(t.getKrajTermina())) {
                        throw new Exception("Termin je tada vec u toku");
                    }
                    if(termin1.getPocetakTermina().isAfter(t.getPocetakTermina())
                        && termin1.getPocetakTermina().isBefore(t.getKrajTermina())){
                        throw new Exception("Termin je tada vec u toku");

                    }
                    if(termin1.getPocetakTermina().compareTo(t.getPocetakTermina()) == 0){
                        throw new Exception("Termin je tada vec u toku");
                    }
                    if(termin1.getKrajTermina().compareTo(t.getPocetakTermina()) == 0){
                        throw new Exception("Termin je tada vec u toku");
                    }
                    if(termin1.getPocetakTermina().isBefore(t.getPocetakTermina()) &&
                    termin1.getKrajTermina().isAfter(t.getKrajTermina())){
                        throw new Exception("Termin je tada vec u toku");
                    }
                    terminRepository.save(termin1);
                }
                if(termin1.getNastavnik().equals(t.getNastavnik())){
                        if (termin1.getKrajTermina().isAfter(t.getPocetakTermina())
                                && termin1.getKrajTermina().isBefore(t.getKrajTermina())){
                            throw new Exception("Nastavnik tad predaje");
                        }
                    if(termin1.getPocetakTermina().isAfter(t.getPocetakTermina())
                            && termin1.getPocetakTermina().isBefore(t.getKrajTermina())){
                        throw new Exception("Nastavnik tad predaje");

                    }

                        if(termin1.getPocetakTermina().compareTo(t.getPocetakTermina()) == 0){
                            throw new Exception("Nastavnik tada predaje");
                        }
                        if(termin1.getKrajTermina().compareTo(t.getPocetakTermina()) == 0){
                            throw new Exception("Nastavnik tada predaje");
                        }

                    if(termin1.getPocetakTermina().isBefore(t.getPocetakTermina()) &&
                            termin1.getKrajTermina().isAfter(t.getKrajTermina())){
                        throw new Exception("Nastavnik tada predaje");
                    }
                    terminRepository.save(termin1);

                    }
                if(termin1.getGrupa().equals(t.getGrupa())){

                    if(termin1.getPocetakTermina().isAfter(t.getPocetakTermina())
                            && termin1.getPocetakTermina().isBefore(t.getKrajTermina())){
                        throw new Exception("Grupa vec nesto slusa");

                    }

                    if (termin1.getKrajTermina().isAfter(t.getPocetakTermina())
                            && termin1.getKrajTermina().isBefore(t.getKrajTermina())){
                        throw new Exception("Grupa vec nesto slusa");
                    }

                    if(termin1.getPocetakTermina().compareTo(t.getPocetakTermina()) == 0){
                        throw new Exception("Grupa vec nesto slusa");
                    }
                    if(termin1.getKrajTermina().compareTo(t.getPocetakTermina()) == 0){
                        throw new Exception("Grupa vec nesto slusa");
                    }
                    if(termin1.getPocetakTermina().isBefore(t.getPocetakTermina()) &&
                            termin1.getKrajTermina().isAfter(t.getKrajTermina())){
                        throw new Exception("Grupa vec nesto slusa");
                    }
                }

            }
        }
        if(!nastavnik1.getPredmeti().contains(predmet1)) {
            nastavnik1.getPredmeti().add(predmet1);//pitati za ovo!
            predmet1.setNastavnik(nastavnik1);
        }
        terminRepository.save(termin1);
        String poruka = "Dodali ste termin sa id-jem " + termin1.getId_termin() + ",Dodavanje," + id_korisnika;
        jmsTemplate.convertAndSend(destination,poruka);
    }

    public void izmeniTermin(LocalTime pocetakTermina, LocalTime krajTermina, Integer id_termin, Long id_korisnika) throws Exception {

        Termin termin = terminRepository.findById(id_termin).orElseThrow(()->
                new EntityNotFoundException("Termin sa unetim id-jem ne postoji"));

        List<Termin> mojiTermini = (List<Termin>) terminRepository.findAll();
        for(Termin t : mojiTermini){
            if(t.getId_termin().equals(termin.getId_termin())){
                continue;
            }else{
                if(t.getDanUnedelji().equals(termin.getDanUnedelji())){
                    if(t.getUcionica().equals(termin.getUcionica())){
                        if (termin.getKrajTermina().isAfter(t.getPocetakTermina())
                                && termin.getKrajTermina().isBefore(t.getKrajTermina())) {
                            throw new Exception("Termin je tada vec u toku");
                        }
                        if(termin.getPocetakTermina().isAfter(t.getPocetakTermina())
                                && termin.getPocetakTermina().isBefore(t.getKrajTermina())){
                            throw new Exception("Termin je tada vec u toku");

                        }
                        if(termin.getPocetakTermina().compareTo(t.getPocetakTermina()) == 0){
                            throw new Exception("Termin je tada vec u toku");
                        }
                        if(termin.getKrajTermina().compareTo(t.getPocetakTermina()) == 0){
                            throw new Exception("Termin je tada vec u toku");
                        }
                        if(termin.getPocetakTermina().isBefore(t.getPocetakTermina()) &&
                                termin.getKrajTermina().isAfter(t.getKrajTermina())){
                            throw new Exception("Termin je tada vec u toku");
                        }
                        termin.setPocetakTermina(pocetakTermina);
                        termin.setKrajTermina(krajTermina);
                        terminRepository.save(termin);
                    }
                    if(t.getNastavnik().equals(termin.getNastavnik())){
                        if (termin.getKrajTermina().isAfter(t.getPocetakTermina())
                                && termin.getKrajTermina().isBefore(t.getKrajTermina())) {
                            throw new Exception("Nastavnik tada predaje");
                        }
                        if(termin.getPocetakTermina().isAfter(t.getPocetakTermina())
                                && termin.getPocetakTermina().isBefore(t.getKrajTermina())){
                            throw new Exception("Nastavnik tada predaje");

                        }
                        if(termin.getPocetakTermina().compareTo(t.getPocetakTermina()) == 0){
                            throw new Exception("Nastavnik tada predaje");
                        }
                        if(termin.getKrajTermina().compareTo(t.getPocetakTermina()) == 0){
                            throw new Exception("Nastavnik tada predaje");
                        }
                        if(termin.getPocetakTermina().isBefore(t.getPocetakTermina()) &&
                                termin.getKrajTermina().isAfter(t.getKrajTermina())){
                            throw new Exception("Nastavnik tada predaje");
                        }
                        termin.setPocetakTermina(pocetakTermina);
                        termin.setKrajTermina(krajTermina);
                        terminRepository.save(termin);
                    }
                    if(t.getGrupa().equals(termin.getGrupa())){
                        if (termin.getKrajTermina().isAfter(t.getPocetakTermina())
                                && termin.getKrajTermina().isBefore(t.getKrajTermina())) {
                            throw new Exception("Grupa tada vec nesto slusa");
                        }
                        if(termin.getPocetakTermina().isAfter(t.getPocetakTermina())
                                && termin.getPocetakTermina().isBefore(t.getKrajTermina())){
                            throw new Exception("Grupa tada vec nesto slusa");

                        }
                        if(termin.getPocetakTermina().compareTo(t.getPocetakTermina()) == 0){
                            throw new Exception("Grupa tada vec nesto slusa");
                        }
                        if(termin.getKrajTermina().compareTo(t.getPocetakTermina()) == 0){
                            throw new Exception("Grupa tada vec nesto slusa");
                        }
                        if(termin.getPocetakTermina().isBefore(t.getPocetakTermina()) &&
                                termin.getKrajTermina().isAfter(t.getKrajTermina())){
                            throw new Exception("Grupa tada vec nesto slusa");
                        }
                        termin.setPocetakTermina(pocetakTermina);
                        termin.setKrajTermina(krajTermina);
                        terminRepository.save(termin);
                    }
                }
            }
        }
        termin.setPocetakTermina(pocetakTermina);
        termin.setKrajTermina(krajTermina);
        terminRepository.save(termin);
        String poruka = "Izmenili ste termin sa id-jem " + id_termin + ",Izmena," + id_korisnika;
        jmsTemplate.convertAndSend(destination,poruka);
    }

    public List<Termin> getTerminezaGrupu(Integer id_grupa){
        Grupa grupa = grupaRepo.findById(id_grupa).orElseThrow(()->
            new EntityNotFoundException("Nije nadjen ni jedan termin sa tim ID-jem grupe"));
        return terminRepository.findGrupaById(id_grupa);
    }
    public List<Termin> getTerminPoNasvniku(Integer id_nastavnika){
        return terminRepository.findNastavnikaById(id_nastavnika);
    }

    public List<Termin> getTerminPoPredmetu(Integer id_predmet){
        return terminRepository.findPredmetById(id_predmet);
    }

    public List<Termin> getTerminPoDanuIucionici(DayOfWeek danUnedelji, Integer id_ucionica){
        return terminRepository.findPoDanuIucionici(DayOfWeek.valueOf(String.valueOf(danUnedelji)),id_ucionica);
    }



    public List<Termin> terminiOpadajucePoDanu(){
        return terminRepository.terminiOpadajucePoDanu();
    }

    public List<Termin> terminiRastucePoDanu(){
        return terminRepository.terminiRastucePoDanu();
    }

    public List<Termin> terminiRastucePoUcionici(){
        return terminRepository.terminiPoUcioniciRastuce();
    }
    public List<Termin> terminiOpadajucePoUcionici(){
        return terminRepository.terminiPoUcioniciOpadajuce();
    }
    public List<Termin> terminiPoVremenuRastuce(){
        return terminRepository.terminiPoVremenuRastuce();
    }

    public List<Termin> terminiPoVremenuOpadajuce(){
        return terminRepository.terminiPoVremenuOpadajuce();
    }


   public void obrisiTerminPoParametrima(LocalTime pocetakTermina,DayOfWeek danUnedelji,
                                                      Integer id_ucionica) {
       Ucionica ucionica = ucionicaRepo.findById(id_ucionica).orElseThrow(() ->
               new EntityNotFoundException("Nije nadjena ucionica sa Id-jem"));


       List<Termin> termins = terminRepository.obrisiPoParametrima(pocetakTermina, danUnedelji,
               id_ucionica);
       for (Termin t : termins) {
           if (t.getPocetakTermina().equals(pocetakTermina) &&
                   t.getDanUnedelji().equals(danUnedelji) && t.getUcionica().equals(ucionica)) {
               terminRepository.delete(t);
           }
       }
   }

    public void obrisiPoIdTermina(Integer id_termin, Long id_korisnika){

        Termin termin = terminRepository.findById(id_termin).orElseThrow(()->
                new EntityNotFoundException("Termin sa unetim id-jem ne postoji"));

            terminRepository.deleteById(id_termin);
            Integer id_termina = id_termin;
        String poruka = "Obrisali ste termin sa id-jem " + id_termina + ",Brisanje," + id_korisnika;
        jmsTemplate.convertAndSend(destination,poruka);
        //Obrisali ste termin,Brisanje,1
    }








}
