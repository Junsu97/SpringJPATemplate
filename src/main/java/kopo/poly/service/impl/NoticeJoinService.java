package kopo.poly.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.NoticeDTO;
import kopo.poly.repository.NoticeFetchRepository;
import kopo.poly.repository.NoticeJoinRepository;
import kopo.poly.repository.NoticeNativeQueryRepository;
import kopo.poly.repository.entity.NoticeFetchEntity;
import kopo.poly.repository.entity.NoticeJoinEntity;
import kopo.poly.repository.entity.NoticeNativeQueryEntity;
import kopo.poly.repository.entity.UserInfoEntity;
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
    private final NoticeJoinRepository noticeJoinRepository;
    private final NoticeNativeQueryRepository noticeNativeQueryRepository;
    private final NoticeFetchRepository noticeFetchRepository;

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
}
