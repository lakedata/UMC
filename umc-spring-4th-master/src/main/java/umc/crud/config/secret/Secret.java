package umc.crud.config.secret;

public class Secret {
    // 터미널에 $ openssl rand -hex 64 명령어를 입력해 랜덤 문자열 생성해 secret key로 사용
    public static String JWT_SECRET_KEY = "5fb7652d9ffd2098bcdd5434c9a9fbd5a84832a25b3897836fd50ba2f780588d4febfa3be27cd08bcd70d7ffffa8dca268a642a86a0dc838896d613fb52d3114";
    public static String USER_PASSWORD_KEY = "";
}
