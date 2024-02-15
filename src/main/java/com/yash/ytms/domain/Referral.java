package com.yash.ytms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project ytms-api
 * @since 12-02-2024
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="referral")
public class Referral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referrer_id", referencedColumnName = "email_add")
    private YtmsUser referrer;

    private String referralCode;
    private String email;
    private String status;
    private LocalDateTime referredAt;
}