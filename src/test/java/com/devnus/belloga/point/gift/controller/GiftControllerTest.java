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
    @DisplayName("????????? ???????????? ?????? API ?????? ?????????")
    void getAllGiftProjectList () throws Exception {
        List<ResponseGift.GiftProject> list = new ArrayList<>();
        list.add(ResponseGift.GiftProject.builder()
                        .giftType(GiftType.GIFTICON)
                        .giftStatus(GiftStatus.WAITING)
                        .title("????????? ???????????? ?????????")
                .build());
        list.add(ResponseGift.GiftProject.builder()
                .giftType(GiftType.GIFTICON)
                .giftStatus(GiftStatus.WAITING)
                .title("????????? ???????????? ?????????")
                .build());
        Page<ResponseGift.GiftProject> pages = new PageImpl<>(list);

        given(this.giftService.getAllGiftProject(Mockito.any(Pageable.class))).willReturn(pages);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/gift/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.content.[0].title", is("????????? ???????????? ?????????"))) // seed ??????
                .andExpect(jsonPath("$.response.content.[1].title", is("????????? ???????????? ?????????")))

                .andDo(print())
                .andDo(document("get-all-gift-list",
                        responseFields(
                                fieldWithPath("id").description("logging??? ?????? api response ?????? ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("response.content.[].id").description("????????? ?????????"),
                                fieldWithPath("response.content.[].title").description("????????? ??????"),
                                fieldWithPath("response.content.[].giftType").description("????????? ??????(???????????? ???)"),
                                fieldWithPath("response.content.[].expectedDrawDate").description("?????????"),
                                fieldWithPath("response.content.[].giftStatus").description("gift ?????? ??????"),
                                fieldWithPath("response.content.[].odds").description("???????????? ????????? ??????"),
                                fieldWithPath("response.content.[].gifticonCount").description("???????????? ??????"),
                                fieldWithPath("response.content.[].applyCount").description("????????? ???"),
                                fieldWithPath("response.pageable").description("pageable ??????"),
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
                                fieldWithPath("error").description("error ?????? ??? ?????? ??????")

                        )
                ));
    }

    @Test
    @DisplayName("???????????? ?????? API ?????? ?????????")
    void createGifticonToGiftProject () throws Exception {
        //given
        Long giftId = 1L;
        String title = "???????????? ???????????????";
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
                        .header("admin-id", adminId) // ????????? ???????????????, api gateway?????? ????????????.
                )
                //then
                .andExpect(status().isOk())
                .andDo(print())

                //docs
                .andDo(document("add-gifticon",
                        requestFields(
                                fieldWithPath("giftId").description("????????????????????? giftId"),
                                fieldWithPath("title").description("???????????? ??????"),
                                fieldWithPath("code").description("???????????? ??????"),
                                fieldWithPath("expectedDrawDate").description("?????????")
                        ),
                        responseFields(
                                fieldWithPath("id").description("logging??? ?????? api response ?????? ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("response").description("null"),
                                fieldWithPath("error").description("error ?????? ??? ?????? ??????")
                        )
                ));

    }


    @Test
    @DisplayName("????????? ???????????? ?????? API ?????? ?????????")
    void createGiftProjectList () throws Exception {
        //given
        String adminId = "dusik";
        String giftType = "GIFTICON";
        String title = "?????? ???????????? ?????????";

        Map<String, String> input = new HashMap<>();
        input.put("giftType", giftType);
        input.put("title", title);
        input.put("expectedDrawDate", "2023-11-11");

        doNothing().when(this.giftService).createGiftProject(adminId, GiftType.GIFTICON, title, new Date());

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/gift/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .header("admin-id", adminId) // ????????? ???????????????, api gateway?????? ????????????.
                )
                //then
                .andExpect(status().isOk())
                .andDo(print())

                //docs
                .andDo(document("create-gift-project",
                        requestFields(
                                fieldWithPath("giftType").description("????????? ?????? enum (GIFICON)"),
                                fieldWithPath("title").description("????????? ??????"),
                                fieldWithPath("expectedDrawDate").description("?????????")
                        ),
                        responseFields(
                                fieldWithPath("id").description("logging??? ?????? api response ?????? ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("response").description("null"),
                                fieldWithPath("error").description("error ?????? ??? ?????? ??????")
                        )
                ));

    }

    @Test
    @DisplayName("????????? ?????? API ?????? ?????????")
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
                                fieldWithPath("giftId").description("????????? ?????????(Gift) ?????????")
                        ),
                        responseFields(
                                fieldWithPath("id").description("logging??? ?????? api response ?????? ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("response").description("null"),
                                fieldWithPath("error").description("error ?????? ??? ?????? ??????")
                        )
                ));

    }

    @Test
    @DisplayName("????????? ????????? ?????? ?????? ?????????")
    void getApplyGiftTest () throws Exception {
        //given
        String labelerId = "gildong";

        List<ResponseGift.ApplyGiftInfo> list = new ArrayList<>();
        list.add(ResponseGift.ApplyGiftInfo.builder()
                .giftType(GiftType.GIFTICON)
                .title("????????? ???????????? ?????????")
                .build());
        list.add(ResponseGift.ApplyGiftInfo.builder()
                .giftType(GiftType.GIFTICON)
                .title("????????? ???????????? ?????????")
                .build());
        Page<ResponseGift.ApplyGiftInfo> pages = new PageImpl<>(list);

        given(this.giftService.findApplyGiftInfoByLabelerId(Mockito.any(Pageable.class), Mockito.any())).willReturn(pages);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/gift/v1/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("labeler-id", labelerId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.content.[0].title", is("????????? ???????????? ?????????")))
                .andExpect(jsonPath("$.response.content.[1].title", is("????????? ???????????? ?????????")))

                .andDo(print())
                .andDo(document("get-labeler-apply-gift",
                        responseFields(
                                fieldWithPath("id").description("logging??? ?????? api response ?????? ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("response.content.[].id").description("?????? ????????? apply ?????? id"),
                                fieldWithPath("response.content.[].giftId").description("????????? gift ?????? id"),
                                fieldWithPath("response.content.[].title").description("????????? ??????"),
                                fieldWithPath("response.content.[].giftType").description("????????? ??????(???????????? ???)"),
                                fieldWithPath("response.content.[].expectedDrawDate").description("?????????"),
                                fieldWithPath("response.content.[].applyStatus").description("?????? ??????"),

                                fieldWithPath("response.pageable").description("????????? ?????? ??????"),
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
                                fieldWithPath("error").description("error ?????? ??? ?????? ??????")

                        )
                ));

    }
    @Test
    @DisplayName("????????? ?????? API ?????? ?????????")
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
                                fieldWithPath("giftId").description("????????? ?????????(Gift) ?????????"),
                                fieldWithPath("giftType").description("????????? ?????????(Gift) ??????")
                        ),
                        responseFields(
                                fieldWithPath("id").description("logging??? ?????? api response ?????? ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("response").description("null"),
                                fieldWithPath("error").description("error ?????? ??? ?????? ??????")
                        )
                ));

    }

    @Test
    @DisplayName("????????? ????????? ?????? ?????? ?????? ?????????")
    void getGiftWinnersTest () throws Exception {
        //given
        String adminId = "test_admin";
        Long GiftId = 1L;

        List<ResponseUser.LabelerInfo> list = new ArrayList<>();
        list.add(ResponseUser.LabelerInfo.builder()
                .phoneNumber("123-123")
                .birthYear("2022-11-11")
                .email("asd@naver.com")
                .name("??????")
                .build());
        list.add(ResponseUser.LabelerInfo.builder()
                    .phoneNumber("123-123")
                    .birthYear("2022-11-11")
                    .email("asd@naver.com")
                    .name("??????")
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
                                fieldWithPath("id").description("logging??? ?????? api response ?????? ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("response.content.[].phoneNumber").description("????????? ???????????? ????????????"),
                                fieldWithPath("response.content.[].email").description("????????? ???????????? ?????????"),
                                fieldWithPath("response.content.[].name").description("????????? ???????????? ??????"),
                                fieldWithPath("response.content.[].birthYear").description("????????? ???????????? ????????????"),

                                fieldWithPath("response.pageable").description("????????? ?????? ??????"),
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
                                fieldWithPath("error").description("error ?????? ??? ?????? ??????")

                        )
                ));

    }
}

