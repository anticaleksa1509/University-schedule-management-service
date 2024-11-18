package rs.raf.AuthService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Role {
    //mozda ce nam trebati da i uloga ima listu korisnika
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_role;


    private String imeUloge;

    private String opisUloge;
}
