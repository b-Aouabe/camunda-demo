package ma.adria.bank.camundademo.delegate;

import ma.adria.bank.camundademo.enumeration.CustomerStatus;
import ma.adria.bank.camundademo.model.Customer;
import ma.adria.bank.camundademo.service.ValidationService;
import ma.adria.bank.camundademo.repository.CustomerRepository;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidateCustomerDataDelegate implements JavaDelegate {
    
    @Autowired
    private ValidationService validationService;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("\n========================================");
        System.out.println("🚀 STEP 1: VALIDATE CUSTOMER DATA");
        System.out.println("========================================\n");
        
        // Get variables from process
        String firstName = (String) execution.getVariable("firstName");
        String lastName = (String) execution.getVariable("lastName");
        String email = (String) execution.getVariable("email");
        String phoneNumber = (String) execution.getVariable("phoneNumber");

        System.out.println("📋 Customer Info:");
        System.out.println("   Name: " + firstName + " " + lastName);
        System.out.println("   Email: " + email);
        System.out.println("   Phone: " + phoneNumber);
        System.out.println();
        
        // Validate data
        boolean isValid = validationService.validateCustomerData(firstName, lastName, email);
        
        if (!isValid) {
            System.out.println("❌ Validation failed - throwing BPMN error\n");
            throw new BpmnError("VALIDATION_ERROR", "Customer data validation failed");
        }
        
        // Check if customer already exists
        if (customerRepository.findByEmail(email).isPresent()) {
            System.out.println("❌ Customer with this email already exists\n");
            throw new BpmnError("DUPLICATE_EMAIL", "Customer with this email already exists");
        }
        
        // Create customer entity
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPhoneNumber(phoneNumber);
        customer.setStatus(CustomerStatus.PENDING);
        customer.setCreatedAt(LocalDateTime.now());
        
        customer = customerRepository.save(customer);
        
        // Store customer ID in process
        execution.setVariable("customerId", customer.getId());
        
        System.out.println("✅ Customer created in database");
        System.out.println("   Customer ID: " + customer.getId());
        System.out.println("   Status: PENDING\n");
    }
}