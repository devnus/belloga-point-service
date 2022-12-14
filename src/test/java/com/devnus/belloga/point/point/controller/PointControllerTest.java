package com.devnus.belloga.point.point.controller;

import com.devnus.belloga.point.gift.service.GiftService;
import com.devnus.belloga.point.point.event.ResponsePoint;
import com.devnus.belloga.point.point.service.PointService;
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
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@ActiveProfiles("test")
@WebMvcTest
@MockBean(JpaMetamodelMappingContext.class)
class PointControllerTest {
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
        @DisplayName("??? ????????? ?????? API ?????? ?????????")
        void getMyPointInfoSuccess () throws Exception {
                String labelerId = "gildong";

                given(this.pointService.getMyPointInfo(Mockito.any())).willReturn(ResponsePoint.MyPointInfo.builder()
                                .tempPointValue(300L)
                                .pointValue(1000000L)
                        .build());

                mockMvc.perform(RestDocumentationRequestBuilders.get("/api/point/v1/info")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("labeler-id", labelerId) // ????????? ???????????? ????????? ???????????????, api gateway?????? ????????????.
                        )

                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.response.pointValue", is(1000000))) // seed ??????
                        .andExpect(jsonPath("$.response.tempPointValue", is(300)))

                        .andDo(print())
                        .andDo(document("get-my-point-info",
                                responseFields(
                                        fieldWithPath("id").description("logging??? ?????? api response ?????? ID"),
                                        fieldWithPath("dateTime").description("response time"),
                                        fieldWithPath("success").description("?????? ?????? ??????"),
                                        fieldWithPath("response.pointValue").description("?????? ?????????"),
                                        fieldWithPath("response.tempPointValue").description("?????? ?????????????????? ???"),
                                        fieldWithPath("error").description("error ?????? ??? ?????? ??????")
                                )
                        ));
        }
}
