package kopo.poly.service;

import kopo.poly.dto.NoticeDTO;

import java.util.List;

public interface INoticeService {
    /**
     * 
     * @return 공지사항 전체 리턴
     */
    List<NoticeDTO> getNoticeList();

    /**
     *
     * @param pDTO 공지사항 상세정보 가져오기를 위한 정보
     * @param type 조회수 증가여부(true: 증가, false: 증가안함ㄴ)
     * @return 공지사항 상세정보
     * @throws Exception
     */
    NoticeDTO getNoticeInfo(NoticeDTO pDTO, boolean type) throws Exception;

    /**
     * 공지사항 수정
     * @param pDTO 공지사항 수정을 위한 정보
     * @throws Exception
     */
    void updateNoticeInfo(NoticeDTO pDTO) throws Exception;

    /**
     * 해당 게시글 삭젠
     * @param pDTO 게시글 삭제를 위한 정보
     * @throws Exception
     */
    void deleteNoticeInfo(NoticeDTO pDTO) throws Exception;

    /**
     * 해당 게시글 저장
     * @param pDTO 해당 게시글 정보
     * @throws Exception
     */
    void insertNoticeInfo(NoticeDTO pDTO) throws Exception;
}
