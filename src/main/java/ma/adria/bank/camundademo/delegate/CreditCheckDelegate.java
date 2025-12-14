package ma.adria.bank.camundademo.delegate;

import ma.adria.bank.camundademo.service.CreditCheckService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("creditCheckDelegate")
public class CreditCheckDelegate implements JavaDelegate {
    
    @Autowired
    private CreditCheckService creditCheckService;
    
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("\n========================================");
        System.out.println("💳 STEP 2: PERFORM CREDIT CHECK");
        System.out.println("========================================\n");
        
        String firstName = (String) execution.getVariable("firstName");
        String lastName = (String) execution.getVariable("lastName");
        String email = (String) execution.getVariable("email");
        
        int creditScore = creditCheckService.performCreditCheck(firstName, lastName, email);
        
        execution.setVariable("creditScore", creditScore);
        
        System.out.println("✅ Credit check completed");
        System.out.println("   Credit Score stored in process: " + creditScore + "\n");
    }
}