package kopo.poly.repository.entity;

import jakarta.persistence.*;
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
@Cacheable
public class NoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_seq")
    private Long noticeSeq;

    @NonNull
    @Column(name = "title", length = 500, nullable = false)
    // nullable = false 는 DB에서 null을 허용하지 않음
    // @NonNull은 자바 파일에서 Null을 허용하지 않음
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


    @Column(name = "read_cnt", nullable = false)
    private Long readCnt;


    @Column(name = "reg_id", nullable = false)
    private String regId;


    @Column(name = "reg_dt", nullable = false)
    private String regDt;


    @Column(name = "chg_id", nullable = false)
    private String chgId;


    @Column(name = "chg_dt", nullable = false)
    private String chgDt;
}
