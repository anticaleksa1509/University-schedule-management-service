package rs.raf003.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class Termin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_termin;
    private LocalTime pocetakTermina;
    private LocalTime krajTermina;

    private DayOfWeek danUnedelji;
    private String tipNastave;


    public DayOfWeek getDanUnedelji() {
        return danUnedelji;
    }

    public void setDanUnedelji(DayOfWeek danUnedelji) {
        this.danUnedelji = danUnedelji;
    }

    public String getTipNastave() {
        return tipNastave;
    }

    public void setTipNastave(String tipNastave) {
        this.tipNastave = tipNastave;
    }

    @JsonIgnore
    @ManyToOne//(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_nastavnik")
    private Nastavnik nastavnik;

    @JsonIgnore
    @ManyToOne//(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_predmet")
    private Predmet predmet;


    @JsonIgnore
    @ManyToOne//(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_ucionica")
    private Ucionica ucionica;


    @JsonIgnore
    @ManyToOne//(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_grupa")
    private Grupa grupa;

    public Grupa getGrupa() {
        return grupa;
    }

    public void setGrupa(Grupa grupa) {
        this.grupa = grupa;
    }

    public Ucionica getUcionica() {
        return ucionica;
    }

    public void setUcionica(Ucionica ucionica) {
        this.ucionica = ucionica;
    }

    public Predmet getPredmet() {
        return predmet;
    }

    public void setPredmet(Predmet predmet) {
        this.predmet = predmet;
    }

    public Long getId_termin() {
        return id_termin;
    }

    public void setId_termin(Long id_termin) {
        this.id_termin = id_termin;
    }

    public Nastavnik getNastavnik() {
        return nastavnik;
    }

    public void setNastavnik(Nastavnik nastavnik) {
        this.nastavnik = nastavnik;
    }

    public LocalTime getPocetakTermina() {
        return pocetakTermina;
    }

    public void setPocetakTermina(LocalTime pocetakTermina) {
        this.pocetakTermina = pocetakTermina;
    }

    public LocalTime getKrajTermina() {
        return krajTermina;
    }

    public void setKrajTermina(LocalTime krajTermina) {
        this.krajTermina = krajTermina;
    }
}
