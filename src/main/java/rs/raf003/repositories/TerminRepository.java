package rs.raf003.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import rs.raf003.model.Grupa;
import rs.raf003.model.Termin;
import rs.raf003.model.Ucionica;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public interface TerminRepository extends CrudRepository<Termin,Integer> {


    @Query("select t from  Termin t where t.grupa.id_grupa = :id_grupa")
    public List<Termin> findGrupaById(Integer id_grupa);

    @Query("select t from Termin t where t.nastavnik.id_nastavnik = :id_nastavnik")
    public List<Termin> findNastavnikaById(Integer id_nastavnik);

    @Query("select t from Termin t where t.predmet.id_predmet = :id_predmet")
    public List<Termin> findPredmetById(Integer id_predmet);

    @Query("select t from Termin t where t.danUnedelji = :danUnedelji and t.ucionica.id_ucionica = :id_ucionica")
    public List<Termin> findPoDanuIucionici(DayOfWeek danUnedelji, Integer id_ucionica);



    @Query("select t from Termin t order by t.danUnedelji desc ")
    public List<Termin> terminiOpadajucePoDanu();

    @Query("select t from Termin t order by t.danUnedelji asc ")
    public List<Termin> terminiRastucePoDanu();

    @Query("select t from Termin t order by t.ucionica.id_ucionica asc")
    public List<Termin> terminiPoUcioniciRastuce();

    @Query("select t from Termin t order by t.ucionica.id_ucionica desc")
    public List<Termin> terminiPoUcioniciOpadajuce();

    @Query("SELECT t from Termin t order by t.pocetakTermina asc")
    public List<Termin> terminiPoVremenuRastuce();

    @Query("select t from Termin t order by t.pocetakTermina desc")
    public List<Termin> terminiPoVremenuOpadajuce();

    @Query("select t from Termin t where t.pocetakTermina = :pocetakTermina and t.danUnedelji = :danUnedelji and t.ucionica.id_ucionica = :id_ucionica")
    public List<Termin> obrisiPoParametrima(LocalTime pocetakTermina,DayOfWeek danUnedelji,
                                                Integer id_ucionica);



}
