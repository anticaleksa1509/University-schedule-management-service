package rs.raf.mailService.listenerHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import rs.raf.mailService.service.EmailService;

@Component
public class Listener {

    @Autowired
    EmailService emailService;

    @JmsListener(destination = "${destination.MessageforEmail}")
    public void sendEmail(String message) throws JmsException {
        String[] delovi = message.split(",");
        String tekstPoruke = delovi[0].trim();
        String subject = delovi[1].trim();
        emailService.sendSimpleMessage("aantic8120ri@raf.rs",subject,tekstPoruke);

    }
}
