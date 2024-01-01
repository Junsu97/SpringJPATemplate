package kopo.poly.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import kopo.poly.dto.NoticeDTO;
import kopo.poly.repository.NoticeRepository;
import kopo.poly.repository.entity.NoticeEntity;
import kopo.poly.service.INoticeService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeService implements INoticeService {
    /**
     * RequiredArgsConstructor 어노테이션으로 생성자를 자동생성
     * noticeRepository 변쉥 이미 메모리에 올라간 NoticeRepository 객체를 넣어줌
     * 예전에는 autowired 어노테이션을 통해 설정했지만, 이젠 생성자를 통해 객체를 주입한다.
     * */
    private final NoticeRepository noticeRepository;
    @Override
    public List<NoticeDTO> getNoticeList() {
        log.info(this.getClass().getName() + ".getNoticeList Start!!");

        // 공지사항 전체 리스트 조회
        List<NoticeEntity> rList = noticeRepository.findAllByOrderByNoticeSeqDesc();
        // 엔티티의 값을 DTO에 맞게 넣어주기
        /**
         * Jackson의 ObjectMapper를 통해 json 객체를 자바 객체로 변환
         * TypeReference를 통해 변환하고자 하는 타입을 지정함
         * */

        List<NoticeDTO> nList = new ObjectMapper().convertValue(rList,
                new TypeReference<List<NoticeDTO>>() {
        });

        log.info(this.getClass().getName() + ".getNoticeList End!!!");
        return nList;
    }

    @Transactional
    @Override
    public NoticeDTO getNoticeInfo(NoticeDTO pDTO, boolean type) throws Exception {
        log.info(this.getClass().getName() + ".getNoticeInfo Start!!!");
        if(type){
            int res = noticeRepository.updateReadCnt(pDTO.getNoticeSeq());

            log.info("res : " + res);
        }
        
        // 공지사항 상세내역 가져오기
        NoticeEntity rEntity = noticeRepository.findByNoticeSeq(pDTO.getNoticeSeq());
        
        // 엔티티의 값들을 DTO에 맞게 넣어주기
        NoticeDTO rDTO = new ObjectMapper().convertValue(rEntity, NoticeDTO.class);
        log.info(this.getClass().getName() + ".getNoticeInfo End!!!");
        return rDTO;
    }

    @Transactional
    @Override
    public void updateNoticeInfo(NoticeDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".updateInfo Start!!!");

        
        /**
         * 수정할 값들 변수로 받음
         * */
        Long noticeSeq = pDTO.getNoticeSeq();

        String title = CmmUtil.nvl(pDTO.getTitle());
        String noticeYn = CmmUtil.nvl(pDTO.getNoticeYn());
        String contents = CmmUtil.nvl(pDTO.getContents());
        String userId = CmmUtil.nvl(pDTO.getUserId());

        log.info("noticeSeq : " + noticeSeq);
        log.info("title : " + title);
        log.info("noticeYn : " + noticeYn);
        log.info("contents : " + contents);
        log.info("userId : " + userId);

        // 현재 게시글 조회수 가져오긴
        NoticeEntity rEntity = noticeRepository.findByNoticeSeq(noticeSeq);

        // 수정할 값들을 빌더를 통해 엔티티에 저장하기
        NoticeEntity pEntity = NoticeEntity.builder()
                                .noticeSeq(noticeSeq)
                                .title(title)
                                .noticeYn(noticeYn)
                                .contents(contents)
                                .userId(userId)
                                .readCnt(rEntity.getReadCnt())
                                .build();

        // 데이터 수정하기
        //JPA는 수정과 등록함수를 구분하지 않고 save함수를 사용함
        //캐시에 저장된 Entity의 값과 비교한 뒤, 값이 다르면 Update 쿼리 실행
        //값이 없으면 Insert쿼리 실행
        noticeRepository.save(pEntity);
        
        log.info(this.getClass().getName() + ".updateInfo End!!!");
    }

    @Override
    public void deleteNoticeInfo(NoticeDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".deleteNoticeInfo Start!!!");
        Long noticeSeq = pDTO.getNoticeSeq();

        log.info("noticeSeq : " + noticeSeq);

        // 데이터 수정하기
        noticeRepository.deleteById(noticeSeq);

        log.info(this.getClass().getName() + ".deleteNoticeInfo End!!!");
    }

    @Override
    public void insertNoticeInfo(NoticeDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".insertNoticeInfo Start!!!");


        String title = CmmUtil.nvl(pDTO.getTitle());
        String noticeYn = CmmUtil.nvl(pDTO.getNoticeYn());
        String contents = CmmUtil.nvl(pDTO.getContents());
        String userId = CmmUtil.nvl(pDTO.getUserId());

        log.info("title : " + title);
        log.info("noticeYn : " + noticeYn);
        log.info("contents : " + contents);
        log.info("userId : " + userId);


        /**
         * 게시글 저장을 위해서는 PK 값은 빌더에 추가하지 않음
         * JPA에 자종 증가 설정 해둠
         * */
        NoticeEntity pEntity = NoticeEntity.builder()
                                .title(title)
                                .contents(contents)
                                .userId(userId).readCnt(0L)
                                .regId(userId)
                                .regDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                                .chgId(userId)
                                .chgDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                                .build();

        // 공지사항 저장
        noticeRepository.save(pEntity);
        log.info(this.getClass().getName() + ".insertNoticeInfo End!!!");
    }
}
