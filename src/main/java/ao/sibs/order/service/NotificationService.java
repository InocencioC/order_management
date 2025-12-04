package ao.sibs.order.service;

import ao.sibs.order.entity.Order;
import ao.sibs.order.entity.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    private final String senderEmail = "cordeiroturorias@gmail.com";

    public void sendOrderCompleteEmail(User user, Order order) {
        if (user == null || user.getEmail() == null) {
            log.error("--- LOG EMAIL ERROR --- Cannot send email. User or email is missing for Order ID: {}", order.getId());
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();

        try {
            message.setFrom(senderEmail);
            message.setTo(user.getEmail());
            message.setSubject("[SIBS Order Manager] Pedido Concluído: " + order.getId());
            message.setText(
                    "Olá " + user.getName() + ",\n\n" +
                            "Seu pedido (" + order.getId() + ") para o item '" + order.getItem().getName() +
                            "' na quantidade de " + order.getQuantity() + " unidades foi COMPLETO.\n\n" +
                            "Agradecemos a sua preferência.\n" +
                            "Equipe SIBS Order Manager"
            );

            mailSender.send(message);

            log.info("--- LOG EMAIL SENT --- Notification sent successfully for Order ID: {} to User: {}",
                    order.getId(), user.getEmail());

        } catch (MailException e) {
            log.error("--- LOG EMAIL ERROR --- Failed to send email for Order ID: {} to {}. Error: {}",
                    order.getId(), user.getEmail(), e.getMessage());
        }
    }
}
