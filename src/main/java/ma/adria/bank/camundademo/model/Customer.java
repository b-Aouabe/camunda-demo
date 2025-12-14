package ma.adria.bank.camundademo.model;

import jakarta.persistence.*;
import lombok.*;
import ma.adria.bank.camundademo.enumeration.CustomerStatus;
import ma.adria.bank.camundademo.enumeration.RiskLevel;

import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    private String phoneNumber;
    
//    @Column(nullable = false)
    private Integer creditScore;
    
    @Enumerated(EnumType.STRING)
    private CustomerStatus status;
    
    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;
    
    private String accountId;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime approvedAt;
    
    private String approvedBy;
}