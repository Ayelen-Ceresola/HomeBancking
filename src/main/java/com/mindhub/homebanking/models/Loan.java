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
    private long id;
    private String name;
    private Double maxAmoun;

    @ElementCollection
    private List<Integer> payments;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans= new HashSet<>();


    public Loan (){}

    public Loan(String name, Double maxAmoun, List<Integer> payments) {
        this.name = name;
        this.maxAmoun = maxAmoun;
        this.payments = payments;
    }

    public Set<Client> getClient() {
        return clientLoans.stream()
                .map(clientLoan -> clientLoan.getClient())
                .collect(Collectors.toSet());

    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMaxAmoun() {
        return maxAmoun;
    }

    public void setMaxAmoun(Double maxAmoun) {
        this.maxAmoun = maxAmoun;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setLoan(this);
        clientLoans.add(clientLoan);

    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maxAmoun=" + maxAmoun +
                ", payments=" + payments +
                '}';
    }
}
