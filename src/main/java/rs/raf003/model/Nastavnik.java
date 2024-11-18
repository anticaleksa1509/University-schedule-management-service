package rs.raf003.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Nastavnik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_nastavnik;
    private String ime;
    private String prezime;
    private String zvanje;



    @OneToMany(mappedBy = "nastavnik")
    private List<Termin> termini;

    public List<Termin> getTermini() {
        return termini;
    }

    public void setTermini(List<Termin> termini) {
        this.termini = termini;
    }

    @OneToMany(mappedBy = "nastavnik")
    private List<Predmet> predmeti;

    public List<Predmet> getPredmeti() {
        return predmeti;
    }


    public void setPredmeti(List<Predmet> predmeti) {
        this.predmeti = predmeti;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getZvanje() {
        return zvanje;
    }

    public void setZvanje(String zvanje) {
        this.zvanje = zvanje;
    }
}
