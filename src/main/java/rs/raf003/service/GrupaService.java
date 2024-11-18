package rs.raf003.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf003.model.Grupa;
import rs.raf003.repositories.GrupaRepo;

@Service
public class GrupaService {
    @Autowired
    GrupaRepo grupaRepo;
    public Grupa nadjiPoId(Integer id_grupa){
        Grupa grupa = grupaRepo.findById(id_grupa).orElseThrow(()->
                new EntityNotFoundException("Nije nadjena grupa sa unetim id-jem"));

        return grupa;
    }
}
