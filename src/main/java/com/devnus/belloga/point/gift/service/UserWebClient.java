package com.devnus.belloga.point.gift.service;

import com.devnus.belloga.point.common.dto.CommonResponse;
import com.devnus.belloga.point.common.exception.error.NotFoundLabelerIdException;
import com.devnus.belloga.point.gift.dto.ResponseUser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserWebClient {
    private final WebClient userServiceWebClient;
    private final ObjectMapper objectMapper;

    /**
     * 동기 통신을 통해 accountId로 기업 사용자 정보를 가져온다
     */
    public ResponseUser.LabelerInfo getLabelerInfo(String accountId) {
        CommonResponse commonResponse = userServiceWebClient
                .get()
                .uri("/api/user/v1/labelers/{accountId}", accountId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(res -> {
                    if(res.statusCode().value() != HttpStatus.OK.value()){
                        return reactor.core.publisher.Mono.error(new NotFoundLabelerIdException());
                    }
                    return res.bodyToMono(CommonResponse.class);

                })
                .block();

        //LinkedHashMap으로 역직렬화 된 commonResponse의 response 값을 ResponseUser.EnterpriseInfo 형변환
        return objectMapper.convertValue(commonResponse.getResponse(), new TypeReference<ResponseUser.LabelerInfo>() {});
    }
}
