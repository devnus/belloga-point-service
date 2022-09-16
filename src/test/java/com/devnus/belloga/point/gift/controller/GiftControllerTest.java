package com.devnus.belloga.point.gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@EmbeddedKafka(
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092"
        },
        ports = { 9092 })
class GiftControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("이벤트 프로젝트 조회 API 성공 테스트")
    void getAllGiftProjectList () throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/gift/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.content.[0].title", is("바나나 기프티콘 이벤트"))) // seed 참고
                .andExpect(jsonPath("$.response.content.[1].title", is("초콜릿 기프티콘 이벤트")))

                .andDo(print())
                .andDo(document("get-all-gift-list",
                        responseFields(
                                fieldWithPath("id").description("logging을 위한 api response 고유 ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("정상 응답 여부"),
                                fieldWithPath("response.content.[].title").description("이벤트 제목"),
                                fieldWithPath("response.content.[].giftType").description("이벤트 타입(기프티콘 등)"),
                                fieldWithPath("response.content.[].expectedDrawDate").description("추첨일"),
                                fieldWithPath("response.content.[].odds").description("응모하면 당첨될 확률"),

                                fieldWithPath("response.pageable.sort.unsorted").description("페이징 처리 sort 정보"),
                                fieldWithPath("response.pageable.sort.sorted").description("페이징 처리 sort 정보"),
                                fieldWithPath("response.pageable.sort.empty").description("페이징 처리 sort 정보"),
                                fieldWithPath("response.pageable.pageNumber").description("page number"),
                                fieldWithPath("response.pageable.pageSize").description("page size"),
                                fieldWithPath("response.pageable.offset").description("page offset"),
                                fieldWithPath("response.pageable.paged").description("paged"),
                                fieldWithPath("response.pageable.unpaged").description("unpaged"),
                                fieldWithPath("response.totalPages").description("total pages"),
                                fieldWithPath("response.totalElements").description("total elements"),
                                fieldWithPath("response.last").description("last"),
                                fieldWithPath("response.numberOfElements").description("numberOfElements"),
                                fieldWithPath("response.size").description("size"),
                                fieldWithPath("response.sort.unsorted").description("unsorted"),
                                fieldWithPath("response.sort.sorted").description("sorted"),
                                fieldWithPath("response.sort.empty").description("empty"),
                                fieldWithPath("response.number").description("number"),
                                fieldWithPath("response.first").description("first"),
                                fieldWithPath("response.empty").description("empty"),
                                fieldWithPath("error").description("error 발생 시 에러 정보")

                        )
                ));
    }

    @Transactional
    @Test
    @DisplayName("이벤트 프로젝트 생성 API 성공 테스트")
    void createGiftProjectList () throws Exception {
        //given
        Map<String, String> input = new HashMap<>();
        input.put("giftType", "GIFTICON");
        input.put("title", "치킨 기프티콘 이벤트");
        input.put("expectedDrawDate", "2023-11-11");

        String adminId = "dusik";

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/gift/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .header("admin-id", adminId) // 유저의 식별아이디, api gateway에서 받아온다.
                )
                //then
                .andExpect(status().isOk())
                .andDo(print())

                //docs
                .andDo(document("create-gift-project",
                        requestFields(
                                fieldWithPath("giftType").description("이벤트 타입 enum (GIFICON)"),
                                fieldWithPath("title").description("이벤트 제목"),
                                fieldWithPath("expectedDrawDate").description("추첨일")
                        ),
                        responseFields(
                                fieldWithPath("id").description("logging을 위한 api response 고유 ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("정상 응답 여부"),
                                fieldWithPath("response").description("null"),
                                fieldWithPath("error").description("error 발생 시 에러 정보")
                        )
                ));

    }

    @Transactional
    @Test
    void applyGiftTest () throws Exception {
        //given
        Map<String, String> input = new HashMap<>();
        input.put("giftId", "1");
        String labelerId = "jisung";

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/gift/v1/apply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input))
                .header("labeler-id", labelerId)
        )
                //then
                .andExpect(status().isOk())
                .andDo(print())

                //docs
                .andDo(document("apply-gift",
                        requestFields(
                                fieldWithPath("giftId").description("지원할 이벤트(Gift) 아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").description("logging을 위한 api response 고유 ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("정상 응답 여부"),
                                fieldWithPath("response").description("null"),
                                fieldWithPath("error").description("error 발생 시 에러 정보")
                        )
                ));

    }
}
