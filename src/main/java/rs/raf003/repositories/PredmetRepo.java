package rs.raf003.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import rs.raf003.model.Predmet;

public interface PredmetRepo extends CrudRepository<Predmet,Integer> {
    @Query("select p from Predmet p where p.naziv = :naziv")
    public Predmet getPredmetPoNazivu(String naziv);
}
