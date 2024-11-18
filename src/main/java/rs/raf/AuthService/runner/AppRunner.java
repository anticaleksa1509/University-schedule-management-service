package rs.raf.AuthService.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.raf.AuthService.model.Role;
import rs.raf.AuthService.model.User;
import rs.raf.AuthService.repositories.RoleRepo;
import rs.raf.AuthService.repositories.UserRepo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Component
public class AppRunner implements CommandLineRunner {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;


    @Autowired
    public AppRunner(UserRepo userRepo,RoleRepo roleRepo){
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;

    }

    public static String hashSifre(String sifra) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(sifra.getBytes());

            // Pretvaranje bajtova u heksadecimalni format
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void run(String... args) throws Exception {

        Role role1 = new Role();
        role1.setImeUloge("Nastavnik");
        role1.setOpisUloge("uloga_nastavnika");
        Role role2 = new Role();
        role2.setImeUloge("Administrator");
        role2.setOpisUloge("administratorska uloga");
        roleRepo.save(role1);
        roleRepo.save(role2);

        User user = new User();
        user.setEmail("aleksa@gmail.com");
        user.setSifra(hashSifre("aleksacar123"));
        user.setUsername("aleksacar");
        user.setRole(role1);

        User user2 = new User();
        user2.setEmail("marko@gmail.com");
        user2.setSifra(hashSifre("markocar123"));
        user2.setUsername("markocar");
        user2.setRole(role1);

        User user3 = new User();
        user3.setEmail("stefan@gmail.com");
        user3.setSifra(hashSifre("stefancar123"));
        user3.setUsername("stefancar");
        user3.setRole(role2);

        userRepo.save(user);
        userRepo.save(user2);
        userRepo.save(user3);
    }
}
