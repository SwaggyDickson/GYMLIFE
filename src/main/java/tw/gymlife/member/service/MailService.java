package tw.gymlife.member.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
@Service
public class MailService {
 
    private JavaMailSender mailSender;
 
    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
   
    @Async
    public CompletableFuture<Boolean> prepareAndSend(String recipient, String subject, String message) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("qqww5576843@gmail.com");
            messageHelper.setTo(recipient);
            messageHelper.setSubject(subject);  // set the subject
            messageHelper.setText(message);  // set the content
        };
      
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                mailSender.send(messagePreparator);
                future.complete(true);
            } catch (MailException e) {
                future.complete(false);
            }
        });

        return future;
    }
//    @Async
//    public static void codeSend(String recipient, String subject, String message) {
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setTo(recipient);
//        msg.setSubject(subject);
//        msg.setText(message);
//
//        mailSender.send(msg);
//    }

}
