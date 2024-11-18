package rs.raf.AuthService.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rs.raf.AuthService.model.Role;
import rs.raf.AuthService.repositories.RoleRepo;

@Service
public class RoleService {
    @Autowired
    RoleRepo roleRepo;

    public void obrisiUlogu(Long id_uloga){
        Role role = roleRepo.findById(id_uloga).orElseThrow(()->
                new EntityNotFoundException("Nije pronadjena uloga sa tim id-jem"));
        roleRepo.delete(role);
    }
}
