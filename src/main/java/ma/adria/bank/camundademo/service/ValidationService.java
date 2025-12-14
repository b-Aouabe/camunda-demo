package ma.adria.bank.camundademo.service;

import org.springframework.stereotype.Service;

@Service
public class ValidationService {
    
    public boolean validateEmail(String email) {
        System.out.println("🔍 Validating email: " + email);
        
        if (email == null || email.trim().isEmpty()) {
            System.out.println("❌ Email is empty");
            return false;
        }
        
        boolean valid = email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
        System.out.println(valid ? "✅ Email is valid" : "❌ Email is invalid");
        return valid;
    }
    
    public boolean validatePhoneNumber(String phoneNumber) {
        System.out.println("🔍 Validating phone: " + phoneNumber);
        
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            System.out.println("❌ Phone is empty");
            return false;
        }
        
        // Simple validation: must be 10 digits
        boolean valid = phoneNumber.replaceAll("[^0-9]", "").length() >= 10;
        System.out.println(valid ? "✅ Phone is valid" : "❌ Phone is invalid");
        return valid;
    }
    
    public boolean validateCustomerData(String firstName, String lastName, String email) {
        System.out.println("🔍 Validating customer data...");
        
        boolean valid = firstName != null && !firstName.trim().isEmpty() &&
                       lastName != null && !lastName.trim().isEmpty() &&
                       validateEmail(email);
        
        System.out.println(valid ? "✅ Customer data is valid" : "❌ Customer data is invalid");
        return valid;
    }
}