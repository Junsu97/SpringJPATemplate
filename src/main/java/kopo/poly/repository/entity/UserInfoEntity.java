package kopo.poly.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@Table(name ="USER_INFO")
@Cacheable
//@Cacheable
public class UserInfoEntity {
    @Id
    @Column(name = "USER_ID")
    private String userId;

    @NonNull
    @Column(name = "USER_NAME", length = 500, nullable = false)
    private String userName;

    @NonNull
    @Column(name = "PASSWORD", length = 100, nullable = false)
    private String password;

    @NonNull
    @Column(name = "EMAIL", nullable = false)
    private String email;

    @NonNull
    @Column(name = "ADDR1", nullable = false)
    private String addr1;

    @NonNull
    @Column(name = "ADDR2", nullable = false)
    private String addr2;

    @Column(name = "REG_ID", updatable = false)
    private String regId;

    @Column(name = "REG_DT", updatable = false)
    private String regDt;

    @Column(name = "CHG_ID")
    private String chgId;

    @Column(name = "CHG_DT")
    private String chgDt;
}
