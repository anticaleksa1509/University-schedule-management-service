package rs.raf003.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import rs.raf003.model.Grupa;
import rs.raf003.model.Termin;

import java.util.ArrayList;
import java.util.List;

public interface GrupaRepo extends CrudRepository<Grupa,Integer> {


}
