package umc.crud.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import umc.crud.config.BaseException;
import umc.crud.config.BaseResponse;
import umc.crud.src.user.model.*;
import umc.crud.utils.JwtService;

import java.util.List;
import static umc.crud.config.BaseResponseStatus.*;
import static umc.crud.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/users")

/**
 * Controller
 * 사용자의 Request를 전달받아 요청의 처리를 담당하는 Service, Prodiver 호출
 */
public class UserController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired  // 객체 생성을 스프링에서 자동으로 생성해주는 역할. 주입하려 하는 객체의 타입이 일치하는 객체를 자동으로 주입한다.
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;

    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService) {
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원가입 API
     * [POST] /users
     */
    @ResponseBody
    @PostMapping("/sign-up")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // TODO: 추가 validation 처리 필요
        if (postUserReq.getEmail() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        if (!isRegexEmail(postUserReq.getEmail())) {
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }

        try {
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 로그인 API
     * [POST] /users/logIn
     */
    @ResponseBody
    @PostMapping("/log-in")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq) {
        try {
            // TODO: 로그인 값들에 대한 형식적 validation 추가 처리 필요
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 모든 회원 조회 API
     * [GET] /users
     * 또는
     * 해당 닉네임을 갖는 유저들의 정보 조회 API
     * [GET] /users?NickName=
     * Query String
     */
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/users
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String nickname) {
        try {
            if (nickname == null) { // query string인 nickname이 없을 경우, 전체 유저정보를 불러온다.
                List<GetUserRes> getUsersRes = userProvider.getUsers();
                return new BaseResponse<>(getUsersRes);
            }
            // query string인 nickname 값이 있을 경우, 조건을 만족하는 유저정보들을 불러온다.
            List<GetUserRes> getUsersRes = userProvider.getUsersByNickname(nickname);
            return new BaseResponse<>(getUsersRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 회원 조회 API
     * [GET] /users/:userIdx
     * Path Variable
     */
    @ResponseBody
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/users/:userIdx
    public BaseResponse<GetUserRes> getUser(@PathVariable("userIdx") int userIdx) {
        //  null or 공백값이 들어가는 경우에는 PathVariable X
        //  .(dot)이 포함된 경우, .을 포함한 그 뒤가 잘려서 들어감
        try {
            GetUserRes getUserRes = userProvider.getUser(userIdx);
            return new BaseResponse<>(getUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 정보 변경 API
     * [PATCH] /users/:userIdx
     */
    @ResponseBody
    @PatchMapping("/{userIdx}")
    public BaseResponse<String> modifyUserName(@PathVariable("userIdx") int userIdx, @RequestBody User user) {
        try {
            // jwt에서 idx 추출
            int userIdxByJwt = jwtService.getUserIdx();

            // userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            // 같다면 유저네임 변경
            PatchUserReq patchUserReq = new PatchUserReq(userIdx, user.getNickname());
            userService.modifyUserName(patchUserReq);

            String result = "회원정보가 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}