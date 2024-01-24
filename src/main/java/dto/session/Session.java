package dto.session;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

public class Session {

    private String id;
    private String userId;          // 해당 유저의 아이디
    private LocalDateTime createTime;   // 세션 생성 시간
    private LocalDateTime lastAccessTime;     // 마지막 접속 시간 (로그인 했을 떄 갱신)
    private LocalDateTime expires;      // 세션 만료 시간 : 1시간
    private static long maxAccessTime = 3600;          // 최대 접속 가능 시간 : 1시간 (3600초)

    public Session(String userId) {
        this.id = createSessionID();
        this.userId = userId;
        this.createTime = LocalDateTime.now();
        this.lastAccessTime = LocalDateTime.now();
        this.expires = createTime.plusHours(1);
    }

    public String getId() {
        return this.id;
    }
    public String getUserId() {
        return this.userId;
    }
    // 만료기간을 GMT 형식에 맞춰 반환
    public String getExpires() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'").withLocale(Locale.ENGLISH);
        return expires.format(formatter);
    }

    // 로그인 시 마지막 접속 시간 업데이트
    public void setLastAccessTime(LocalDateTime time) {
        this.lastAccessTime = time;
    }

    // 해당 세션이 expires와 비교했을 때 유효한지 검증
    public boolean isValidWithExpires() {
        LocalDateTime currentTime = LocalDateTime.now();
        if(currentTime.isBefore(expires))       // 만료 시간보다 이전일 때
            return true;
        else if(currentTime.isAfter(expires))       // 만료 시간이 지났을 때
            return false;
        else        // 두 시간이 동일할 때
            return false;
    }

    // 해당 세션이 maxAccessTime과 비교했을 때 유효한지 검증
    public boolean isValidWithMaxAccessTime() {
        LocalDateTime currentTime = LocalDateTime.now();

        Duration duration = Duration.between(lastAccessTime, currentTime);
        long AccessTimeSeconds = duration.toSeconds();

        if(AccessTimeSeconds < maxAccessTime)       // 유저의 접속 시간이 1시간보다 적을 경우
            return true;
        return false;                               // 유저의 접속 시간이 1시간 이상이 되었을 경우
    }

    // 세션 ID 생성
    public String createSessionID() {
        // randomUUID 메서드를 사용하여 랜덤한 UUID 생성
        UUID uuid = UUID.randomUUID();
        // UUID를 문자열로 변환하여 세션 ID로 반환
        return uuid.toString();
    }

}
