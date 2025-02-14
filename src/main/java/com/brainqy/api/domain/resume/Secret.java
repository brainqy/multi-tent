package com.brainqy.api.domain.resume;

import com.brainqy.api.domain.YtmsUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 09-11-2024
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Secret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String secretKey;
    @Column(length = 10000)
    private String secretValue;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_add")  // Matches the column name of the primary key in YtmsUser
    private YtmsUser owner;
    public Secret(String secretKey, String secretValue) {
        this.secretKey = secretKey;
        this.secretValue = secretValue;
    }
}
