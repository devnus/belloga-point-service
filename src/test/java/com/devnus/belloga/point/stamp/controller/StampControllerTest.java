package com.devnus.belloga.point.stamp.controller;

import com.devnus.belloga.point.gift.service.GiftService;
import com.devnus.belloga.point.point.service.PointService;
import com.devnus.belloga.point.stamp.dto.ResponseStamp;
import com.devnus.belloga.point.stamp.service.StampService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@ActiveProfiles("test")
@WebMvcTest
@MockBean(JpaMetamodelMappingContext.class)
class StampControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PointService pointService;
    @MockBean
    private GiftService giftService;
    @MockBean
    private StampService stampService;

    @Test
    @DisplayName("포인트를 스탬프로 교환 API 성공 테스트")
    void exchangePointToStampSuccess () throws Exception {
        String labelerId = "gildong";

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/stamp/v1/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("labeler-id", labelerId) // 라벨링 수행하는 유저의 식별아이디, api gateway에서 받아온다.
                )

                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("exchange-to-stamp",
                        responseFields(
                                fieldWithPath("id").description("logging을 위한 api response 고유 ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("정상 응답 여부"),
                                fieldWithPath("response").description("null"),
                                fieldWithPath("error").description("error 발생 시 에러 정보")
                        )
                ));
    }
    @Test
    @DisplayName("내 스탬프 조회 API 성공 테스트")
    void getMyStampInfoSuccess () throws Exception {
        String labelerId = "gildong";

        given(this.stampService.getMyStampInfo(Mockito.any())).willReturn(ResponseStamp.MyStampInfo.builder()
                        .stampValue(100)
                .build());

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/stamp/v1/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("labeler-id", labelerId) // 라벨링 수행하는 유저의 식별아이디, api gateway에서 받아온다.
                )

                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-my-stamp-info",
                        responseFields(
                                fieldWithPath("id").description("logging을 위한 api response 고유 ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("정상 응답 여부"),
                                fieldWithPath("response.stampValue").description("스탬프 갯수"),
                                fieldWithPath("error").description("error 발생 시 에러 정보")
                        )
                ));
    }

}