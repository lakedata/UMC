package umc.crud.src.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLoginReq {
    private String email;
    private String password;
    private String nickname;
}
