package rs.raf003.bootstrap;

import org.hibernate.dialect.function.ListaggGroupConcatEmulation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.raf003.model.*;
import rs.raf003.repositories.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class BootstrapClass implements CommandLineRunner {

    @Autowired
    GrupaRepo grupaRepo;
    @Autowired
    PredmetRepo predmetRepo;
    @Autowired
    NastavnikRepo nastavnikRepo;
    @Autowired
    UcionicaRepo ucionicaRepo;
    @Autowired
    TerminRepository terminRepository;


    @Override
    public void run(String... args) throws Exception {

        Ucionica ucionica1 = new Ucionica();
        ucionica1.setOznaka("RAF1");
        ucionica1.setRacunar(true);
        ucionica1.setBrojMesta(30);
        ucionica1.setTermini(new ArrayList<>());

        Ucionica ucionica2 = new Ucionica();
        ucionica2.setOznaka("RAF6");
        ucionica2.setRacunar(false);
        ucionica2.setBrojMesta(15);
        ucionica2.setTermini(new ArrayList<>());

        ucionicaRepo.save(ucionica1);
        ucionicaRepo.save(ucionica2);

        Nastavnik nastavnik1 = new Nastavnik();
        nastavnik1.setIme("Aleksa");
        nastavnik1.setPrezime("Antic");
        nastavnik1.setZvanje("Profesor");
        nastavnik1.setPredmeti(new ArrayList<>());
        nastavnik1.setTermini(new ArrayList<>());

        Nastavnik nastavnik2 = new Nastavnik();
        nastavnik2.setIme("Marko");
        nastavnik2.setPrezime("Markovic");
        nastavnik2.setZvanje("Vandredni profesor");
        nastavnik2.setPredmeti(new ArrayList<>());
        nastavnik2.setTermini(new ArrayList<>());

        Nastavnik nastavnik3 = new Nastavnik();
        nastavnik3.setIme("Stefan");
        nastavnik3.setPrezime("Stefanovic");
        nastavnik3.setZvanje("Profesor");
        nastavnik3.setPredmeti(new ArrayList<>());
        nastavnik3.setTermini(new ArrayList<>());

        Grupa grupa1 = new Grupa();
        grupa1.setOznaka(423);
        grupa1.setPredmeti(new ArrayList<>());
        grupa1.setTermini(new ArrayList<>());

        Grupa grupa2 = new Grupa();
        grupa2.setOznaka(421);
        grupa2.setPredmeti(new ArrayList<>());
        grupa2.setTermini(new ArrayList<>());

        grupaRepo.save(grupa1);
        grupaRepo.save(grupa2);

        Predmet predmet1 = new Predmet();
        predmet1.setNaziv("OOP");
        predmet1.setNastavnik(nastavnik1);
        predmet1.setBrojEspb(8);
        predmet1.setStudProgram("RI");
        predmet1.setGrupe(new ArrayList<>());
        predmet1.setTermini(new ArrayList<>());

        Predmet predmet2 = new Predmet();
        predmet2.setNaziv("Uvod u programiranje");
        predmet2.setBrojEspb(8);
        predmet2.setNastavnik(nastavnik1);
        predmet2.setStudProgram("RN");
        predmet2.setGrupe(new ArrayList<>());
        predmet2.setTermini(new ArrayList<>());


        Predmet predmet3 = new Predmet();
        predmet3.setNaziv("Analiza1");
        predmet3.setBrojEspb(6);
        predmet3.setStudProgram("RI");
        predmet3.setNastavnik(nastavnik2);
        predmet3.setGrupe(new ArrayList<>());
        predmet3.setTermini(new ArrayList<>());

        Termin termin1 = new Termin();
        termin1.setPocetakTermina(LocalTime.of(10,15));
        termin1.setKrajTermina(LocalTime.of(12,0));
        termin1.setDanUnedelji(DayOfWeek.MONDAY);
        termin1.setTipNastave("Demonstracije");
        termin1.setPredmet(predmet3);
        termin1.setNastavnik(nastavnik2);
        termin1.setUcionica(ucionica1);
        termin1.setGrupa(grupa1);

        Termin termin2 = new Termin();
        termin2.setPocetakTermina(LocalTime.of(14,30));
        termin2.setKrajTermina(LocalTime.of(16,15));
        termin2.setDanUnedelji(DayOfWeek.WEDNESDAY);
        termin2.setTipNastave("Predavanja");
        termin2.setPredmet(predmet1);
        termin2.setNastavnik(nastavnik1);
        termin2.setUcionica(ucionica2);
        termin2.setGrupa(grupa2);

        nastavnik2.getTermini().add(termin1);
        nastavnik1.getTermini().add(termin2);


        ucionica1.getTermini().add(termin1);
        ucionicaRepo.save(ucionica1);
        ucionica2.getTermini().add(termin2);
        ucionicaRepo.save(ucionica2);

        grupa1.getTermini().add(termin1);
        grupa2.getTermini().add(termin2);
        grupaRepo.save(grupa1);
        grupaRepo.save(grupa2);

        predmet1.getTermini().add(termin2);
        predmet3.getTermini().add(termin1);

        //Povezivanje predmeta i nastavnika
        nastavnik1.getPredmeti().add(predmet1);
        nastavnik1.getPredmeti().add(predmet2);
        nastavnik2.getPredmeti().add(predmet3);


        nastavnikRepo.save(nastavnik1);
        nastavnikRepo.save(nastavnik2);

        //////////////////////////////////////////////////////////

        /////////////////////////////////////////////

        //Povezivanje predmeta i grupe
        List<Predmet> predmetiZaGrupu1 = new ArrayList<>();
        predmetiZaGrupu1.add(predmet1);
        predmetiZaGrupu1.add(predmet2);

        List<Predmet> predmetiZaGrupu2 = new ArrayList<>();
        predmetiZaGrupu2.add(predmet1);
        predmetiZaGrupu2.add(predmet3);
        predmetiZaGrupu2.add(predmet2);

        List<Grupa> grupaZaPredmet1 = new ArrayList<>();
        grupaZaPredmet1.add(grupa1);
        grupaZaPredmet1.add(grupa2);

        List<Grupa> grupaZaPredmet2 = new ArrayList<>();
        grupaZaPredmet2.add(grupa1);
        grupaZaPredmet2.add(grupa2);

        List<Grupa> grupaZaPredmet3 = new ArrayList<>();
        grupaZaPredmet3.add(grupa2);

        grupa1.setPredmeti(predmetiZaGrupu1);
        grupa2.setPredmeti(predmetiZaGrupu2);

        predmet1.setGrupe(grupaZaPredmet1);
        predmet2.setGrupe(grupaZaPredmet2);
        predmet3.setGrupe(grupaZaPredmet3);

        predmetRepo.save(predmet1);
        predmetRepo.save(predmet2);
        predmetRepo.save(predmet3);

        terminRepository.save(termin2);
        terminRepository.save(termin1);


    }
}
