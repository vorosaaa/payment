package com.vorosati.payment.project.component.account;
import com.vorosati.payment.project.component.transaction.Transaction;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column
    private String name;

    @Column(nullable = false)
    private Double balance;

    @Version
    private Long version;

    // One account can be sender in multiple transactions
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Transaction> sentTransactions;

    // One account can be recipient in multiple transactions
    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL)
    private List<Transaction> receivedTransactions;

    // Constructors
    public Account() {}

    public Account(String name, Double balance) {
        this.name = name;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public List<Transaction> getSentTransactions() {
        return sentTransactions;
    }

    public void setSentTransactions(List<Transaction> sentTransactions) {
        this.sentTransactions = sentTransactions;
    }

    public List<Transaction> getReceivedTransactions() {
        return receivedTransactions;
    }

    public void setReceivedTransactions(List<Transaction> receivedTransactions) {
        this.receivedTransactions = receivedTransactions;
    }

    @PrePersist
    public void prePersist() {
        if (this.balance == null) {
            this.balance = 0.0;
        }
    }

    public void setId(Long id) {
        this.id = id;
    }
}
