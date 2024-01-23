package dto;

import config.Config;

import java.io.IOException;
import java.util.Arrays;

public enum GetRequestEnum {
    DEFAULT("/") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) throws IOException {
            return Config.getService.showIndex();
        }
    },
    SIGNUP("/user/create") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) throws IOException {
            return Config.getService.signup(httpRequestDto);
        }
    },
    FILE("file") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) throws IOException {
            return Config.getService.requestFile(httpRequestDto);
        }
    };

    private String url;

    GetRequestEnum(String url) {
        this.url = url;
    }

    // 요청 url에 해당하는 상수 반환
    public static GetRequestEnum getRequest(String url) {
        return Arrays.stream(GetRequestEnum.values())
                .filter(request -> request.url.equals(url))
                .findAny()
                .orElse(FILE);
    }

    // 상수별로 상속받을 함수
    public abstract HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) throws IOException;

}
