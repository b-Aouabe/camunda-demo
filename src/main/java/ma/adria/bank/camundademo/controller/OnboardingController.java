package ma.adria.bank.camundademo.controller;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingController {
    
    @Autowired
    private RuntimeService runtimeService;
    
    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startOnboarding(@RequestBody CustomerRequest request) {
        
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║   🎯 NEW CUSTOMER ONBOARDING STARTED           ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
        
        // Prepare process variables
        Map<String, Object> variables = new HashMap<>();
        variables.put("firstName", request.getFirstName());
        variables.put("lastName", request.getLastName());
        variables.put("email", request.getEmail());
        variables.put("phoneNumber", request.getPhoneNumber());
//        variables.put("creditScore", request.getCreditScore());

        // Start process instance
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                "customer-onboarding",
                request.getEmail(), // Business key
                variables
        );
        
        Map<String, Object> response = new HashMap<>();
        response.put("processInstanceId", processInstance.getId());
        response.put("businessKey", processInstance.getBusinessKey());
        response.put("message", "Customer onboarding process started successfully");
        
        return ResponseEntity.ok(response);
    }
    
    public static class CustomerRequest {
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
//        private Integer creditScore;

        // Getters and Setters
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

//        public Integer getCreditScore() { return creditScore; }
//        public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }
    }
}