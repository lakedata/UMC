package umc.crud.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import umc.crud.config.BaseException;
import umc.crud.config.secret.Secret;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static umc.crud.config.BaseResponseStatus.*;

@Service
public class JwtService {

    /**
     * JWT 생성
     * @param userIdx
     * @return String
     */
    public String createJwt(int userIdx){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt") // type=jwt로 설정
                .claim("userIdx",userIdx) // payload에 담길 데이터(userIdx로 전달받은 값)
                .setIssuedAt(now) // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*60*24*365))) // 해당 JWT 만료 시간(1년 뒤로 설정)
                .signWith(SignatureAlgorithm.HS256, Secret.JWT_SECRET_KEY) // 서명 알고리즘(HS256), 비밀키
                .compact(); // 토큰 생성
    }

    /**
     * Header에서 X-ACCESS-TOKEN으로 JWT 값 추출
     * @return String
     */
    public String getJwt(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("X-ACCESS-TOKEN"); // 프론트에서 "X-ACCESS-TOKEN"라는 이름의 header에 담아 넘겨주었다고 가정, Postman 돌릴 때 header에 값 넣어주기
    }

    /**
     * JWT에서 userIdx 추출
     * @return int
     * @throws BaseException
     */
    public int getUserIdx() throws BaseException {
        //1. 헤더에서 JWT 추출
        String accessToken = getJwt();
        if(accessToken == null || accessToken.length() == 0){
            throw new BaseException(EMPTY_JWT);
        }

        // 2. JWT parsing
        Jws<Claims> claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(Secret.JWT_SECRET_KEY)
                    .parseClaimsJws(accessToken);
        }catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }

        // 3. userIdx 추출
        return claims.getBody().get("userIdx",Integer.class); // jwt에서 userIdx 추출합
    }
}