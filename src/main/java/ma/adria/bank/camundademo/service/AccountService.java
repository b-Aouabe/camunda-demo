package ma.adria.bank.camundademo.service;


import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {


    public String createAccount(Long customerId, String email) {
        System.out.println("🏦 Creating account for customer ID: " + customerId);
        
        // Simulate account creation
        String accountId = "ACC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        System.out.println("✅ Account created: " + accountId);
        return accountId;
    }
}