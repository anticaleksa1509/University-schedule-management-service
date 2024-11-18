package rs.raf003.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Predmet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_predmet;
    private String naziv;
    private String studProgram;
    private int brojEspb;

    @OneToMany(mappedBy = "predmet")
    private List<Termin> termini;

    public List<Termin> getTermini() {
        return termini;
    }

    public void setTermini(List<Termin> termini) {
        this.termini = termini;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_nastavnik")
    private Nastavnik nastavnik;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "predmet_grupa",
        joinColumns = @JoinColumn(name = "id_predmet"),
        inverseJoinColumns = @JoinColumn(name = "id_grupa")

    )
    private List<Grupa> grupe = new ArrayList<>();

    public List<Grupa> getGrupe() {
        return grupe;
    }

    public void setGrupe(List<Grupa> grupe) {
        this.grupe = grupe;
    }

    public Nastavnik getNastavnik() {
        return nastavnik;
    }

    public void setNastavnik(Nastavnik nastavnik) {
        this.nastavnik = nastavnik;
    }

    public Long getId_predmet() {
        return id_predmet;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getStudProgram() {
        return studProgram;
    }

    public void setStudProgram(String studProgram) {
        this.studProgram = studProgram;
    }

    public int getBrojEspb() {
        return brojEspb;
    }

    public void setBrojEspb(int brojEspb) {
        this.brojEspb = brojEspb;
    }


}
