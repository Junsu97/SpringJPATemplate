package kopo.poly.repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="NOTICE")
@DynamicInsert
@DynamicUpdate
@Builder
@Entity
public class NoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_seq")
    private Long noticeSeq;

    @NonNull
    @Column(name = "title", length = 500, nullable = false)
    private String title;

    @NonNull
    @Column(name = "notice_yn", length = 1, nullable = false)
    private String noticeYn;

    @NonNull
    @Column(name = "contents", nullable = false)
    private String contents;

    @NonNull
    @Column(name = "user_id", nullable = false)
    private String userId;

    @NonNull
    @Column(name = "reg_cnt", nullable = false)
    private String regCnt;

    @NonNull
    @Column(name = "reg_id", nullable = false)
    private String regId;

    @NonNull
    @Column(name = "reg_dt", nullable = false)
    private String regDt;

    @NonNull
    @Column(name = "chg_id", nullable = false)
    private String chgId;

    @NonNull
    @Column(name = "chg_dt", nullable = false)
    private String chgDt;
}
