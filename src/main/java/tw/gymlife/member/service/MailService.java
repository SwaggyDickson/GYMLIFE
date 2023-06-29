package tw.gymlife.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class MailService {
 
    private JavaMailSender mailSender;
 
    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
 
    public boolean prepareAndSend(String recipient, String subject, String message) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("qqww5576843@gmail.com");
            messageHelper.setTo(recipient);
            messageHelper.setSubject(subject);  // set the subject
            messageHelper.setText(message);  // set the content
        };
      
        try {
            mailSender.send(messagePreparator);
            return true; // return true if the mail was sent successfully
        } catch (MailException e) {
            return false; // return false if the mail was not sent due to an exception
        }
    }

}
