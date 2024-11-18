package rs.raf003.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import rs.raf003.model.Ucionica;

public interface UcionicaRepo extends CrudRepository<Ucionica,Integer> {
    @Query("select u from Ucionica u where u.oznaka = :oznaka")
    public Ucionica getUcionicaOznaka(String oznaka);
}
