package kopo.poly.service.impl;

import kopo.poly.dto.UserInfoDTO;
import kopo.poly.repository.UserInfoRepository;
import kopo.poly.repository.entity.UserInfoEntity;
import kopo.poly.service.IUserInfoService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service("UserInfoService")
public class UserInfoService implements IUserInfoService {
    private final UserInfoRepository userInfoRepository;

    @Override
    public UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".getUserIdExists Start!!");

        UserInfoDTO rDTO = null;

        String userId = CmmUtil.nvl(pDTO.userId());

        log.info("userId : " + userId);

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(userId);

        if(rEntity.isPresent()){
            rDTO = UserInfoDTO.builder().existYn("Y").build();
        }else{
            rDTO = UserInfoDTO.builder().existYn("N").build();
        }

        log.info(this.getClass().getName() + ".getUserIdExists Exit!!");
        return rDTO;
    }

    @Override
    public int insertUserInfo(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".insertUserInfo Start!!");

        int res = 0;

        String userId = CmmUtil.nvl(pDTO.userId());
        String userName = CmmUtil.nvl(pDTO.userName());
        String password = CmmUtil.nvl(pDTO.password());
        String email = CmmUtil.nvl(pDTO.email());
        String addr1 = CmmUtil.nvl(pDTO.addr1());
        String addr2 = CmmUtil.nvl(pDTO.addr2());

        log.info("userId : " + userId);
        log.info("userName : " + userName);
        log.info("password : " + password);
        log.info("email : " + email);
        log.info("addr1 : " + addr1);
        log.info("addr2 : " + addr2);

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(userId);

        if(rEntity.isPresent()){
            res = 2;
        }else{
            UserInfoEntity pEntity = UserInfoEntity.builder()
                    .userId(userId).userName(userName)
                    .password(password)
                    .email(email)
                    .addr1(addr1).addr2(addr2)
                    .regId(userId).regDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                    .chgId(userId).chgDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                    .build();

            userInfoRepository.save(pEntity);

            rEntity = userInfoRepository.findByUserId(userId);

            if(rEntity.isPresent()){
                res = 1;
            }else{
                res = 0;
            }

            log.info(this.getClass().getName() + ".insertUserInfo End!!");

            return res;
        }

        log.info(this.getClass().getName() + ".insertUserInfo End!!");
        return 0;
    }

    @Override
    public int getUserLogin(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".getUserLogin Start!!!");

        int res = 0;

        String userId = CmmUtil.nvl(pDTO.userId());
        String password = CmmUtil.nvl(pDTO.password());

        log.info("userId : " + userId);
        log.info("password : " + password);
        return 0;
    }

    @Override
    public void deleteUserInfo(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".deleteUserInfo Start!!");

        String userId = pDTO.userId();
        log.info("userId : " + userId);

        userInfoRepository.deleteById(userId);

        log.info(this.getClass().getName() + ".deleteUserInfo End!!");
    }
}
