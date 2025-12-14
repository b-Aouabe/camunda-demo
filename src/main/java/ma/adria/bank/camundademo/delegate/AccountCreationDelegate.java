package ma.adria.bank.camundademo.delegate;

import ma.adria.bank.camundademo.enumeration.CustomerStatus;
import ma.adria.bank.camundademo.model.Customer;
import ma.adria.bank.camundademo.repository.CustomerRepository;
import ma.adria.bank.camundademo.service.AccountService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("accountCreationDelegate")
public class AccountCreationDelegate implements JavaDelegate {
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("\n========================================");
        System.out.println("🏦 STEP 4: CREATE ACCOUNT");
        System.out.println("========================================\n");
        
        Long customerId = (Long) execution.getVariable("customerId");
        String email = (String) execution.getVariable("email");
        
        String accountId = accountService.createAccount(customerId, email);
        
        // Update customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setAccountId(accountId);
        customer.setStatus(CustomerStatus.APPROVED);
        customer.setApprovedAt(LocalDateTime.now());
        
        // Check if was manually approved
        Boolean manuallyApproved = (Boolean) execution.getVariable("approved");
        if (manuallyApproved != null && manuallyApproved) {
            String approvedBy = (String) execution.getVariable("approvedBy");
            customer.setApprovedBy(approvedBy != null ? approvedBy : "demo");
        }
        
        customerRepository.save(customer);
        
        execution.setVariable("accountId", accountId);
        
        System.out.println("✅ Account creation completed");
        System.out.println("   Account ID: " + accountId);
        System.out.println("   Customer Status: APPROVED\n");
    }
}