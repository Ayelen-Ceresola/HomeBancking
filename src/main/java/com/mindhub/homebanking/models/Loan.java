package com.mindhub.homebanking.models;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Loan {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native" )
    private Long id;
    private String name;
    private Double maxAmount;
    private Double percentage;

    @ElementCollection
    private List<Integer> payments;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans= new HashSet<>();


    public Loan (){}

    public Loan(String name, Double maxAmount, List<Integer> payments,Double percentage) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.percentage = percentage;
    }

    public Set<Client> getClient() {
        return clientLoans.stream()
                .map(clientLoan -> clientLoan.getClient())
                .collect(Collectors.toSet());

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

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public Double getPercentage() {return percentage;}

    public void setPercentage(Double percentage) {this.percentage = percentage;}

    public void addClientLoan(ClientLoan clientLoan){clientLoan.setLoan(this);clientLoans.add(clientLoan);
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maxAmoun=" + maxAmount +
                ", payments=" + payments +
                '}';
    }
}
