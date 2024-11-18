package rs.raf.AuthService.dto;

import lombok.Data;

@Data
public class NastavnikDto {

    private String ime;
    private String prezime;
    private String zvanje;

    public NastavnikDto(String ime, String prezime, String zvanje) {
        this.ime = ime;
        this.prezime = prezime;
        this.zvanje = zvanje;
    }

    public NastavnikDto() {

    }
}
