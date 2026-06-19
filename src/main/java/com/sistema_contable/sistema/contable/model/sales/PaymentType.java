package com.sistema_contable.sistema.contable.model.sales;

import com.sistema_contable.sistema.contable.model.accounting.BalanceAccount;
import jakarta.persistence.*;

@Entity
@Table (name = "payment_types")
public class PaymentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_payment_type")
    private Long id;

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "payment_type_account_id")
    private BalanceAccount account;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BalanceAccount getAccount() {
        return account;
    }

    public void setAccount(BalanceAccount account) {
        this.account = account;
    }
}
