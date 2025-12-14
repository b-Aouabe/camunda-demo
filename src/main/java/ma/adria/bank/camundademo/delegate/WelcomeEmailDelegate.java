package ma.adria.bank.camundademo.delegate;

import ma.adria.bank.camundademo.enumeration.CustomerStatus;
import ma.adria.bank.camundademo.model.Customer;
import ma.adria.bank.camundademo.repository.CustomerRepository;
import ma.adria.bank.camundademo.service.NotificationService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("welcomeEmailDelegate")
public class WelcomeEmailDelegate implements JavaDelegate {
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("\n========================================");
        System.out.println("📧 STEP 5: SEND WELCOME EMAIL");
        System.out.println("========================================\n");
        
        Long customerId = (Long) execution.getVariable("customerId");
        String email = (String) execution.getVariable("email");
        String firstName = (String) execution.getVariable("firstName");
        String accountId = (String) execution.getVariable("accountId");
        
        notificationService.sendWelcomeEmail(email, firstName, accountId);
        
        // Update customer status to ACTIVE
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setStatus(CustomerStatus.ACTIVE);
        customerRepository.save(customer);
        
        System.out.println("✅ Welcome email sent");
        System.out.println("   Customer Status: ACTIVE");
        System.out.println("\n🎉 ONBOARDING COMPLETED! 🎉\n");
    }
}