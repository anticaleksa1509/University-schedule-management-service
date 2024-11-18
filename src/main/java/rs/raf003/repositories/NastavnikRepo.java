package rs.raf003.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import rs.raf003.model.Nastavnik;

public interface NastavnikRepo extends CrudRepository<Nastavnik,Integer> {

    @Query("select n from Nastavnik n where n.ime = :ime and n.prezime = :prezime")
    public Nastavnik getNastavnikPoImenuIprezimenu(String ime, String prezime);

    @Query("SELECT n from Nastavnik n where n.zvanje = :zvanje")
    public Nastavnik getPoZvanju(String zvanje);
}
