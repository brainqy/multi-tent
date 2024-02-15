package com.yash.ytms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project ytms-api
 * @since 12-02-2024
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "coin_transaction")
public class CoinTransaction {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_add", nullable = false)
    private YtmsUser user;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType = TransactionType.REFERRAL;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "source")
    private String source;

    @Column(name = "created_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();

    public CoinTransaction(YtmsUser user, TransactionType transactionType, Integer amount) {
        this.user = user;
        this.transactionType = transactionType;
        this.amount = amount;
    }
}