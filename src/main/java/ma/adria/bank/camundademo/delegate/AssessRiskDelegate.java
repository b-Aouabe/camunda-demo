package ma.adria.bank.camundademo.delegate;

import ma.adria.bank.camundademo.enumeration.RiskLevel;
import ma.adria.bank.camundademo.model.Customer;
import ma.adria.bank.camundademo.repository.CustomerRepository;
import ma.adria.bank.camundademo.service.CreditCheckService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("assessRiskDelegate")
public class AssessRiskDelegate implements JavaDelegate {
    
    @Autowired
    private CreditCheckService creditCheckService;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("\n========================================");
        System.out.println("⚠️ STEP 3: ASSESS RISK LEVEL");
        System.out.println("========================================\n");
        
        Integer creditScore = (Integer) execution.getVariable("creditScore");
        Long customerId = (Long) execution.getVariable("customerId");
        
        RiskLevel riskLevel = creditCheckService.assessRisk(creditScore);
        
        // Update customer in database
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setCreditScore(creditScore);
        customer.setRiskLevel(riskLevel);
        customerRepository.save(customer);
        
        // Store in process
        execution.setVariable("riskLevel", riskLevel.name());
        
        System.out.println("✅ Risk assessment completed");
        System.out.println("   Risk Level: " + riskLevel);
        System.out.println("   Requires manual approval: " + (riskLevel == RiskLevel.HIGH));
        System.out.println();
    }
}