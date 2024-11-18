package rs.raf.AuthService.request;

import lombok.Data;

@Data
public class TokenRequest {

    private String username;
    private String sifra;

    public TokenRequest(String username, String sifra) {
        this.username = username;
        this.sifra = sifra;
    }
}
