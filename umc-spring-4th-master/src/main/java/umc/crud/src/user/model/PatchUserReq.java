package umc.crud.src.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
/**
 * PATCH
 * 회원 정보 수정 요청
 */
public class PatchUserReq {
    private int userIdx;
    private String nickname;
}
