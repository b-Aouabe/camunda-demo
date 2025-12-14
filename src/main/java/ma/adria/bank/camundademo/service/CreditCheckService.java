package ma.adria.bank.camundademo.service;

import ma.adria.bank.camundademo.enumeration.RiskLevel;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CreditCheckService {
    
    private final Random random = new Random();

    public int performCreditCheck(String firstName, String lastName, String email) {
        System.out.println("💳 Performing credit check for: " + firstName + lastName);
        
        // Simulate API call delay
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Simulate credit score (300-850)
        int creditScore = 300 + random.nextInt(551);

        System.out.println("📊 Credit score: " + creditScore);
        return creditScore;
    }
    
    public RiskLevel assessRisk(int creditScore) {
        System.out.println("⚠️ Assessing risk level...");
        
        RiskLevel riskLevel;
        if (creditScore >= 700) {
            riskLevel = RiskLevel.LOW;
            System.out.println("✅ Risk Level: LOW");
        } else if (creditScore >= 600) {
            riskLevel = RiskLevel.MEDIUM;
            System.out.println("⚠️ Risk Level: MEDIUM");
        } else {
            riskLevel = RiskLevel.HIGH;
            System.out.println("🚨 Risk Level: HIGH");
        }
        
        return riskLevel;
    }
}