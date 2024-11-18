package rs.raf003.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Ucionica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_ucionica;
    private String oznaka;
    private int brojMesta;
    private boolean isRacunar;

    @OneToMany(mappedBy = "ucionica")
    private List<Termin> termini;

    public List<Termin> getTermini() {
        return termini;
    }

    public void setTermini(List<Termin> termini) {
        this.termini = termini;
    }

    public String getOznaka() {
        return oznaka;
    }

    public void setOznaka(String oznaka) {
        this.oznaka = oznaka;
    }

    public int getBrojMesta() {
        return brojMesta;
    }

    public void setBrojMesta(int brojMesta) {
        this.brojMesta = brojMesta;
    }

    public boolean isRacunar() {
        return isRacunar;
    }

    public void setRacunar(boolean racunar) {
        isRacunar = racunar;
    }
}
