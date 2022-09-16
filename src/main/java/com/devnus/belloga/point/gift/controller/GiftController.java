package com.devnus.belloga.point.gift.controller;

import com.devnus.belloga.point.common.aop.annotation.GetAccountIdentification;
import com.devnus.belloga.point.common.aop.annotation.UserRole;
import com.devnus.belloga.point.common.dto.CommonResponse;
import com.devnus.belloga.point.gift.dto.RequestGift;
import com.devnus.belloga.point.gift.service.GiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gift")
public class GiftController {
    private final GiftService giftService;

    /**
     * gift 프로젝트를 생성한다
     * @param adminId, createGiftDto
     * @return
     */
    @PostMapping("/v1")
    public ResponseEntity<CommonResponse> createGift(
            @GetAccountIdentification(role = UserRole.ADMIN) String adminId,
            @Valid @RequestBody RequestGift.CreateGift createGiftDto) {

        giftService.createGiftProject(adminId, createGiftDto.getGiftType(), createGiftDto.getTitle(), createGiftDto.getExpectedDrawDate());

        return new ResponseEntity<>(CommonResponse.builder()
                .success(true)
                .build(), HttpStatus.OK);
    }
    /**
     * gift 프로젝트 리스트를 조회한다.
     * @param
     * @return
     */
    @GetMapping("/v1")
    public ResponseEntity<CommonResponse> getGiftProject(Pageable pageable) {
        return new ResponseEntity<>(CommonResponse.builder()
                .success(true)
                .response(giftService.getAllGiftProject(pageable))
                .build(), HttpStatus.OK);
    }

    /**
     * 이벤트에 응모한다
     */
    @PostMapping("/v1/apply")
    public ResponseEntity<CommonResponse> applyGift(@GetAccountIdentification(role = UserRole.LABELER) String labelerId, @Valid @RequestBody RequestGift.EnterGift request) {

        return new ResponseEntity<>(CommonResponse.builder()
                .success(giftService.createApplyGift(labelerId, request.getGiftId()))
                .build(), HttpStatus.OK);
    }
}
