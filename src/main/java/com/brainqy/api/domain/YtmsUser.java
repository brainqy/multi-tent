package com.brainqy.api.domain;

import com.brainqy.api.constants.UserAccountStatusTypes;
import com.brainqy.api.domain.resume.Secret;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Project Name - ytms-api
 * <p>
 * IDE Used - IntelliJ IDEA
 *
 * @author - yash.raj
 * @since - 25-01-2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ytms_user")
public class YtmsUser {

    @Id
    @Column(name = "email_add")
    private String emailAdd;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "password")
    private String password;

    @Transient
    private String confirmPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status")
    private UserAccountStatusTypes accountStatus;

    private Integer coins = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "user_role")
    private UserRole userRole;

    @OneToMany(mappedBy = "userHistory", cascade = CascadeType.ALL)
    private List<LoginHistory> loginHistoryList = new ArrayList<>();

    public void addLoginHistory(LoginHistory loginHistory) {
        loginHistoryList.add(loginHistory);
        loginHistory.setUserHistory(this);
    }

    @OneToMany(mappedBy = "referrer", cascade = CascadeType.ALL)
    private List<Referral> referrals = new ArrayList<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Secret> secrets = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_badge",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "badge_id")
    )
    private List<Badge> earnedBadges;
    private int xpPoints;
    private int tasksCompleted;
    private int coursesCompleted;
}