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

    /**
     * Initiates a new customer onboarding process.
     *
     * <p>This endpoint serves as the entry point for the customer onboarding workflow.
     * It accepts customer information via HTTP POST, converts it into process variables,
     * and starts a new instance of the Camunda BPMN process.</p>
     *
     * <h3>Process Flow Triggered:</h3>
     * <ol>
     *   <li>Validate Customer Data - Validates email, phone, and checks for duplicates</li>
     *   <li>Perform Credit Check - Calls credit bureau API to get credit score</li>
     *   <li>Assess Risk Level - Determines risk (LOW/MEDIUM/HIGH) based on credit score</li>
     *   <li>Decision Gateway:
     *     <ul>
     *       <li>If HIGH risk → Manual Approval Required (user task)</li>
     *       <li>If LOW/MEDIUM risk → Auto-approved, skip to account creation</li>
     *     </ul>
     *   </li>
     *   <li>Create Account - Generates account ID and updates customer status</li>
     *   <li>Send Welcome Email - Sends welcome notification with account details</li>
     * </ol>
     *
     * <h3>Process Variables Initialized:</h3>
     * <ul>
     *   <li><b>firstName</b> (String) - Customer's first name</li>
     *   <li><b>lastName</b> (String) - Customer's last name</li>
     *   <li><b>email</b> (String) - Customer's email address (also used as business key)</li>
     *   <li><b>phoneNumber</b> (String) - Customer's phone number</li>
     * </ul>
     *
     * <p><b>Note:</b> Additional variables are set during process execution by delegates:
     * customerId, creditScore, riskLevel, accountId, approved, etc.</p>
     *
     * <h3>Business Key:</h3>
     * <p>The customer's email is used as the business key to:</p>
     * <ul>
     *   <li>Uniquely identify the process instance in business terms</li>
     *   <li>Enable easy process instance lookup by email</li>
     *   <li>Correlate external events (e.g., payment confirmations) to the correct process</li>
     * </ul>
     *
     * <h3>Camunda Integration:</h3>
     * <p>This method uses {@link RuntimeService#startProcessInstanceByKey(String, String, Map)}:</p>
     * <ul>
     *   <li><b>Process Definition Key:</b> "customer-onboarding" - Must match the id in BPMN file</li>
     *   <li><b>Business Key:</b> Email address - Human-readable identifier</li>
     *   <li><b>Variables:</b> Initial process data accessible throughout the workflow</li>
     * </ul>
     *
     * <h3>What Happens After This Call:</h3>
     * <ol>
     *   <li>Camunda creates a new process instance in the database</li>
     *   <li>Process execution begins immediately (synchronous by default)</li>
     *   <li>Service tasks execute automatically until a wait state is reached:
     *     <ul>
     *       <li>User task (manual approval)</li>
     *       <li>Message event (waiting for external signal)</li>
     *       <li>Timer event (waiting for time to pass)</li>
     *     </ul>
     *   </li>
     *   <li>If no wait states: Process completes before method returns</li>
     *   <li>If wait state reached: Method returns, process continues later</li>
     * </ol>
     *
     * <h3>Example Request:</h3>
     * <pre>{@code
     * POST /api/onboarding/start
     * Content-Type: application/json
     *
     * {
     *   "firstName": "John",
     *   "lastName": "Doe",
     *   "email": "john.doe@example.com",
     *   "phoneNumber": "1234567890"
     * }
     * }</pre>
     *
     */
    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startOnboarding(@RequestBody CustomerRequest request) {

        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║   🎯 NEW CUSTOMER ONBOARDING STARTED           ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        // Prepare process variables
        // These variables will be accessible throughout the entire process lifecycle
        // and can be retrieved/updated by any delegate or expression
        Map<String, Object> variables = new HashMap<>();
        variables.put("firstName", request.getFirstName());
        variables.put("lastName", request.getLastName());
        variables.put("email", request.getEmail());
        variables.put("phoneNumber", request.getPhoneNumber());

        // Start process instance
        // - Process Definition Key: "customer-onboarding" must match the id in BPMN XML
        // - Business Key: Email serves as human-readable identifier for this instance
        // - Variables: Initial data that flows through the process
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                "customer-onboarding",    // Process definition key from BPMN
                request.getEmail(),        // Business key for easy lookup
                variables                  // Initial process variables
        );

        // Build response with process instance details
        Map<String, Object> response = new HashMap<>();
        response.put("processInstanceId", processInstance.getId());           // Camunda's unique ID
        response.put("businessKey", processInstance.getBusinessKey());        // Our business identifier
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