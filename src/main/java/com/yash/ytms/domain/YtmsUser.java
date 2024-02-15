package com.yash.ytms.domain;

import com.yash.ytms.constants.UserAccountStatusTypes;
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

    @Column(name = "full_name")
    private String fullName;

    @Id
    @Column(name = "email_add")
    private String emailAdd;

    @Column(name = "password")
    private String password;

    @Transient
    private String confirmPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status")
    private UserAccountStatusTypes accountStatus;

    private Integer coins=0;

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

}
