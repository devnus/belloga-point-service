package com.devnus.belloga.point.gift.controller;

import com.devnus.belloga.point.gift.domain.GiftStatus;
import com.devnus.belloga.point.gift.domain.GiftType;
import com.devnus.belloga.point.gift.dto.ResponseGift;
import com.devnus.belloga.point.gift.dto.ResponseUser;
import com.devnus.belloga.point.gift.service.GiftService;
import com.devnus.belloga.point.point.service.PointService;
import com.devnus.belloga.point.stamp.service.StampService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@ActiveProfiles("test")
@WebMvcTest
@MockBean(JpaMetamodelMappingContext.class)
class GiftControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GiftService giftService;
    @MockBean
    private PointService pointService;
    @MockBean
    private StampService stampService;

    @Test
    @DisplayName("이벤트 프로젝트 조회 API 성공 테스트")
    void getAllGiftProjectList () throws Exception {
        List<ResponseGift.GiftProject> list = new ArrayList<>();
        list.add(ResponseGift.GiftProject.builder()
                        .giftType(GiftType.GIFTICON)
                        .giftStatus(GiftStatus.WAITING)
                        .title("바나나 기프티콘 이벤트")
                .build());
        list.add(ResponseGift.GiftProject.builder()
                .giftType(GiftType.GIFTICON)
                .giftStatus(GiftStatus.WAITING)
                .title("초콜릿 기프티콘 이벤트")
                .build());
        Page<ResponseGift.GiftProject> pages = new PageImpl<>(list);

        given(this.giftService.getAllGiftProject(Mockito.any(Pageable.class))).willReturn(pages);

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
                                fieldWithPath("response.content.[].id").description("이벤트 식별값"),
                                fieldWithPath("response.content.[].title").description("이벤트 제목"),
                                fieldWithPath("response.content.[].giftType").description("이벤트 타입(기프티콘 등)"),
                                fieldWithPath("response.content.[].expectedDrawDate").description("추첨일"),
                                fieldWithPath("response.content.[].giftStatus").description("gift 추첨 여부"),
                                fieldWithPath("response.content.[].odds").description("응모하면 당첨될 확률"),
                                fieldWithPath("response.content.[].gifticonCount").description("기프티콘 개수"),
                                fieldWithPath("response.content.[].applyCount").description("응모자 수"),
                                fieldWithPath("response.pageable").description("pageable 정보"),
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

    @Test
    @DisplayName("기프티콘 추가 API 성공 테스트")
    void createGifticonToGiftProject () throws Exception {
        //given
        Long giftId = 1L;
        String title = "스타벅스 아메리카노";
        String code = "123-123-123-123-123";
        Date expectedDrawDate = new Date();
        Map<String, String> input = new HashMap<>();
        input.put("giftId", String.valueOf(giftId));
        input.put("title", title);
        input.put("code", code);
        input.put("expectedDrawDate", "2023-11-11");

        String adminId = "dusik";

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/gift/v1/gifticon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .header("admin-id", adminId) // 유저의 식별아이디, api gateway에서 받아온다.
                )
                //then
                .andExpect(status().isOk())
                .andDo(print())

                //docs
                .andDo(document("add-gifticon",
                        requestFields(
                                fieldWithPath("giftId").description("추가하고자하는 giftId"),
                                fieldWithPath("title").description("기프티콘 제목"),
                                fieldWithPath("code").description("기프티콘 코드"),
                                fieldWithPath("expectedDrawDate").description("만료일")
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


    @Test
    @DisplayName("이벤트 프로젝트 생성 API 성공 테스트")
    void createGiftProjectList () throws Exception {
        //given
        String adminId = "dusik";
        String giftType = "GIFTICON";
        String title = "치킨 기프티콘 이벤트";

        Map<String, String> input = new HashMap<>();
        input.put("giftType", giftType);
        input.put("title", title);
        input.put("expectedDrawDate", "2023-11-11");

        doNothing().when(this.giftService).createGiftProject(adminId, GiftType.GIFTICON, title, new Date());

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

    @Test
    @DisplayName("이벤트 응모 API 성공 테스트")
    void applyGiftTest () throws Exception {
        //given
        Map<String, String> input = new HashMap<>();
        input.put("giftId", "1");
        String labelerId = "jisung";

        given(this.giftService.createApplyGift(labelerId, 1L)).willReturn(true);

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

    @Test
    @DisplayName("응모한 이벤트 조회 성공 테스트")
    void getApplyGiftTest () throws Exception {
        //given
        String labelerId = "gildong";

        List<ResponseGift.ApplyGiftInfo> list = new ArrayList<>();
        list.add(ResponseGift.ApplyGiftInfo.builder()
                .giftType(GiftType.GIFTICON)
                .title("바나나 기프티콘 이벤트")
                .build());
        list.add(ResponseGift.ApplyGiftInfo.builder()
                .giftType(GiftType.GIFTICON)
                .title("초콜릿 기프티콘 이벤트")
                .build());
        Page<ResponseGift.ApplyGiftInfo> pages = new PageImpl<>(list);

        given(this.giftService.findApplyGiftInfoByLabelerId(Mockito.any(Pageable.class), Mockito.any())).willReturn(pages);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/gift/v1/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("labeler-id", labelerId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.content.[0].title", is("바나나 기프티콘 이벤트")))
                .andExpect(jsonPath("$.response.content.[1].title", is("초콜릿 기프티콘 이벤트")))

                .andDo(print())
                .andDo(document("get-labeler-apply-gift",
                        responseFields(
                                fieldWithPath("id").description("logging을 위한 api response 고유 ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("정상 응답 여부"),
                                fieldWithPath("response.content.[].id").description("내가 응모한 apply 식별 id"),
                                fieldWithPath("response.content.[].giftId").description("이벤트 gift 식별 id"),
                                fieldWithPath("response.content.[].title").description("이벤트 제목"),
                                fieldWithPath("response.content.[].giftType").description("이벤트 타입(기프티콘 등)"),
                                fieldWithPath("response.content.[].expectedDrawDate").description("추첨일"),
                                fieldWithPath("response.content.[].applyStatus").description("당첨 여부"),

                                fieldWithPath("response.pageable").description("페이징 처리 정보"),
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
    @Test
    @DisplayName("이벤트 추첨 API 성공 테스트")
    void drawGiftTest () throws Exception {
        //given
        Map<String, String> input = new HashMap<>();
        input.put("giftId", "1");
        input.put("giftType", "GIFTICON");
        String adminId = "dodo";

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/gift/v1/draw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .header("admin-id", adminId)
                )
                //then
                .andExpect(status().isOk())
                .andDo(print())

                //docs
                .andDo(document("draw-gift",
                        requestFields(
                                fieldWithPath("giftId").description("추첨할 이벤트(Gift) 아이디"),
                                fieldWithPath("giftType").description("추첨할 이벤트(Gift) 타입")
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

    @Test
    @DisplayName("이벤트 당첨자 정보 조회 성공 테스트")
    void getGiftWinnersTest () throws Exception {
        //given
        String adminId = "test_admin";
        Long GiftId = 1L;

        List<ResponseUser.LabelerInfo> list = new ArrayList<>();
        list.add(ResponseUser.LabelerInfo.builder()
                .phoneNumber("123-123")
                .birthYear("2022-11-11")
                .email("asd@naver.com")
                .name("길동")
                .build());
        list.add(ResponseUser.LabelerInfo.builder()
                    .phoneNumber("123-123")
                    .birthYear("2022-11-11")
                    .email("asd@naver.com")
                    .name("길동")
                .build());
        Page<ResponseUser.LabelerInfo> pages = new PageImpl<>(list);

        given(this.giftService.findGiftWinners(Mockito.any(Pageable.class), Mockito.any())).willReturn(pages);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/gift/v1/{giftId}/winners",GiftId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("admin-id", adminId)
                )
                .andExpect(status().isOk())

                .andDo(print())
                .andDo(document("get-gift-winners",
                        responseFields(
                                fieldWithPath("id").description("logging을 위한 api response 고유 ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("정상 응답 여부"),
                                fieldWithPath("response.content.[].phoneNumber").description("이벤트 당첨자의 전화번호"),
                                fieldWithPath("response.content.[].email").description("이벤트 당첨자의 이메일"),
                                fieldWithPath("response.content.[].name").description("이벤트 당첨자의 이름"),
                                fieldWithPath("response.content.[].birthYear").description("이벤트 당첨자의 생년월일"),

                                fieldWithPath("response.pageable").description("페이징 처리 정보"),
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
}

