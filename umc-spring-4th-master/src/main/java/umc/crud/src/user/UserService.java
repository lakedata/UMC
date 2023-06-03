package umc.crud.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umc.crud.config.BaseException;
import umc.crud.config.secret.Secret;
import umc.crud.src.user.model.PatchUserReq;
import umc.crud.src.user.model.PostUserReq;
import umc.crud.src.user.model.PostUserRes;
import umc.crud.utils.AES128;
import umc.crud.utils.JwtService;

import static umc.crud.config.BaseResponseStatus.*;

/**
 * Service
 * Controller에 의해 호출되어 실제 비즈니스 로직과 트랜잭션을 처리: Create, Update, Delete 의 로직 처리
 * 요청한 작업을 처리하는 과정을 하나의 작업으로 묶음
 * dao를 호출하여 DB CRUD를 처리 후 Controller로 반환
 */
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
    }

    // 회원가입(POST)
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        // 중복 확인: 해당 이메일을 가진 유저가 있는지 확인. 중복될 경우 에러 메시지
        if (userProvider.checkEmail(postUserReq.getEmail()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        String pwd;

        try {
            // 암호화: postUserReq에서 제공받은 비밀번호를 암호화해 DB에 저장
            // ex) password123 -> dfhsjfkjdsnj4@!$!@chdsnjfwkenjfnsjfnjsd.fdsfaifsadjfjaf
            pwd = new AES128(Secret.USER_PASSWORD_KEY).encrypt(postUserReq.getPassword()); // 암호화
            postUserReq.setPassword(pwd);
        } catch (Exception ignored) { // 암호화에 실패할 경우 에러 발생
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try {
            int userIdx = userDao.createUser(postUserReq);
            // jwt 발급
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(userIdx, jwt);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 회원정보 수정(Patch)
    public void modifyUserName(PatchUserReq patchUserReq) throws BaseException {
        try {
            int result = userDao.modifyUserName(patchUserReq); // 해당 과정 성공=True(1), 실패=False(0) return
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}