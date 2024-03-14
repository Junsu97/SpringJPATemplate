package kopo.poly.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import kopo.poly.dto.NoticeDTO;
import kopo.poly.repository.NoticeFetchRepository;
import kopo.poly.repository.NoticeJoinRepository;
import kopo.poly.repository.NoticeNativeQueryRepository;
import kopo.poly.repository.NoticeRepository;
import kopo.poly.repository.entity.*;
import kopo.poly.service.INoticeJoinService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeJoinService implements INoticeJoinService {
    private final NoticeRepository noticeRepository;
    private final NoticeJoinRepository noticeJoinRepository;
    private final NoticeNativeQueryRepository noticeNativeQueryRepository;
    private final NoticeFetchRepository noticeFetchRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<NoticeDTO> getNoticeListUsingJoinColumn() {
        log.info(this.getClass().getName() + ".getNoticeListUsingJoinColumn Start!!!");

        List<NoticeJoinEntity> rList = noticeJoinRepository.findAllByOrderByNoticeSeqDesc();

        List<NoticeDTO> list = new LinkedList<>();

        rList.forEach(rEntity -> {
            long noticeSeq = rEntity.getNoticeSeq();
            String noticeYn = CmmUtil.nvl(rEntity.getNoticeYn());
            String title = CmmUtil.nvl(rEntity.getTitle());
            long readCnt = rEntity.getReadCnt();

            // Check if UserInfoEntity is not null before accessing its properties
            String userName = "";
            UserInfoEntity userInfoEntity = rEntity.getUserInfoEntity();
            if (userInfoEntity != null) {
                userName = CmmUtil.nvl(userInfoEntity.getUserName());
            }

            String regDt = CmmUtil.nvl(rEntity.getRegDt());

            log.info("noticeSeq : " + noticeSeq);
            log.info("noticeYn : " + noticeYn);
            log.info("title : " + title);
            log.info("readCnt : " + readCnt);
            log.info("userName : " + userName);
            log.info("regDt : " + regDt);
            log.info("-------------------------");

            NoticeDTO rDTO = NoticeDTO.builder()
                    .noticeSeq(noticeSeq)
                    .noticeYn(noticeYn)
                    .title(title)
                    .readCnt(readCnt)
                    .userName(userName)
                    .regDt(regDt)
                    .build();

            list.add(rDTO);
        });

        log.info(this.getClass().getName() + ".getNoticeListUsingJoinColumn End!!!");

        return list;
    }

    @Override
    public List<NoticeDTO> getNoticeListUsingNativeQuery() {
        log.info(this.getClass().getName() + ".getNoticeListUsingNativeQuery Start!!!!");
        List<NoticeNativeQueryEntity> rList = noticeNativeQueryRepository.getNoticeNativeQueryList();

        List<NoticeDTO> nList = new ObjectMapper().convertValue(rList, new TypeReference<List<NoticeDTO>>(){});

        log.info(this.getClass().getName() + ".getNoticeListUsingNativeQuery End!!!!");

        return nList;
    }

    @Override
    public List<NoticeDTO> getNoticeListUsingJPQL() {
        log.info(this.getClass().getName() + ".getNoticeListUsingJPQL Start!!!!");

        List<NoticeFetchEntity> rList = noticeFetchRepository.getListFetchJoin();

        List<NoticeDTO> nList = new ArrayList<>();

        rList.forEach(e -> {
            NoticeDTO rDTO = NoticeDTO.builder()
                    .noticeSeq(e.getNoticeSeq())
                    .title(e.getTitle())
                    .noticeYn(e.getNoticeYn())
                    .readCnt(e.getReadCnt())
                    .userId(e.getUserId())
                    .userName(e.getUserInfo().getUserName())
                    .build();

            nList.add(rDTO);
        });

        log.info(this.getClass().getName() + ".getNoticeListUsingJPQL End!!!!");
        return nList;
    }

    @Transactional
    @Override
    public List<NoticeDTO> getNoticeListForQueryDSL() {
        log.info(this.getClass().getName() + ".getNoticeListForQueryDSL Start!!!!");

        QNoticeFetchEntity ne = QNoticeFetchEntity.noticeFetchEntity;
        QUserInfoEntity ue = QUserInfoEntity.userInfoEntity;

        List<NoticeFetchEntity> rList = queryFactory
                .selectFrom(ne)
                .join(ne.userInfo, ue)
                .orderBy(ne.noticeYn.desc(), ne.noticeSeq.desc())
                .fetch();

        List<NoticeDTO> nList = new ArrayList<>();
        rList.forEach(e -> {
            NoticeDTO rDTO = NoticeDTO.builder()
                    .noticeSeq(e.getNoticeSeq())
                    .title(e.getTitle())
                    .noticeYn(e.getNoticeYn())
                    .readCnt(e.getReadCnt())
                    .userId(e.getUserId())
                    .userName(e.getUserInfo().getUserName())
                    .build();

            nList.add(rDTO);
        });
        log.info(this.getClass().getName() + ".getNoticeListForQueryDSL End!!!!");

        return nList;
    }

    @Override
    public NoticeDTO getNoticeInfoForQueryDSL(NoticeDTO pDTO, boolean type) throws Exception {
        log.info(this.getClass().getName() + ".getNoticeInfoForQueryDSL Start!!!");

        if (type){
            int res = noticeRepository.updateReadCnt(pDTO.noticeSeq());
            log.info("res : " + res);
        }

        QNoticeEntity ne = QNoticeEntity.noticeEntity;
        NoticeEntity rEntity = queryFactory
                .selectFrom(ne)
                .where(ne.noticeSeq.eq(pDTO.noticeSeq()))
                .fetchOne();

        NoticeDTO rDTO = NoticeDTO.builder()
                .noticeSeq(rEntity.getNoticeSeq())
                .title(rEntity.getTitle())
                .noticeYn(rEntity.getNoticeYn())
                .regDt(rEntity.getRegDt())
                .userId(rEntity.getUserId())
                .readCnt(rEntity.getReadCnt())
                .contents(rEntity.getContents())
                .build();


        log.info(this.getClass().getName() + ".getNoticeInfoForQueryDSL End!!!");
        return rDTO;
    }
}
