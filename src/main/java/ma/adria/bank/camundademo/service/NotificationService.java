package ma.adria.bank.camundademo.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final String EMAIL_SENT_MESSAGE = "✅ Email sent successfully!";

    public void sendWelcomeEmail(String email, String firstName, String accountId) {
        System.out.println("📧 Sending welcome email to: " + email);
        System.out.println("   Subject: Welcome to Our Service!");
        System.out.println("   Body: Dear " + firstName + ",");
        System.out.println("         Welcome! Your account ID is: " + accountId + ", ");
        System.out.println(EMAIL_SENT_MESSAGE);
    }
    
    public void sendRejectionEmail(String email, String firstName, String reason) {
        System.out.println("📧 Sending rejection email to: " + email);
        System.out.println("   Subject: Application Status Update");
        System.out.println("   Body: Dear " + firstName + ",c");
        System.out.println("         Unfortunately, we cannot process your application.");
        System.out.println("         Reason:" + reason);
        System.out.println(EMAIL_SENT_MESSAGE);
    }
    
    public void sendApprovalNotification(String email, String firstName) {
        System.out.println("📧 Sending approval notification to: " + email);
        System.out.println("   Subject: Application Approved!");
        System.out.println("   Body: Dear " + firstName + ", ");
        System.out.println("         Your application has been approved!");
        System.out.println(EMAIL_SENT_MESSAGE);
    }
}