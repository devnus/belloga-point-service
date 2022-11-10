package com.devnus.belloga.point.gift.controller;

import com.devnus.belloga.point.common.aop.annotation.GetAccountIdentification;
import com.devnus.belloga.point.common.aop.annotation.UserRole;
import com.devnus.belloga.point.common.dto.CommonResponse;
import com.devnus.belloga.point.gift.domain.GiftType;
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
     * gift 프로젝트에 기프티콘을 추가한다
     * @param adminId
     * @param createGifticonDto
     * @return
     */
    @PostMapping("/v1/gifticon")
    public ResponseEntity<CommonResponse> createGifticon(
            @GetAccountIdentification(role = UserRole.ADMIN) String adminId,
            @Valid @RequestBody RequestGift.CreateGifticon createGifticonDto) {

        giftService.addGifticonToGiftProject(
                createGifticonDto.getGiftId(),
                createGifticonDto.getTitle(),
                createGifticonDto.getCode(),
                createGifticonDto.getExpectedDate()
                );

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
                .success(true)
                .response(giftService.createApplyGift(labelerId, request.getGiftId()))
                .build(), HttpStatus.OK);
    }

    /**
     * 자신이 응모한 응모 내용 조회
     */
    @GetMapping("/v1/apply")
    public ResponseEntity<CommonResponse> getApplyGift(@GetAccountIdentification(role = UserRole.LABELER) String labelerId, Pageable pageable) {

        return new ResponseEntity<>(CommonResponse.builder()
                .success(true)
                .response(giftService.findApplyGiftInfoByLabelerId(pageable, labelerId))
                .build(), HttpStatus.OK);
    }
    /**
     * 이벤트 추첨을 진행한다. Gift 상태를 Done으로 바꾸고 응모자에게 win, lose를 줌
     * @param dto
     * @return
     */
    @PostMapping("/v1/draw")
    public ResponseEntity<CommonResponse> drawGift(@GetAccountIdentification(role = UserRole.ADMIN) String adminId, @Valid @RequestBody RequestGift.DrawGift dto) {
        if(dto.getGiftType().equals(GiftType.GIFTICON)) {
            giftService.drawGifticonEvent(adminId, dto.getGiftId());
        }
        return new ResponseEntity<>(CommonResponse.builder()
                .success(true)
                .build(), HttpStatus.OK);
    }

    /**
     * 해당 Gift에 대한 당첨자 정보 조회
     */
    @GetMapping("/v1/{giftId}/winners")
    public ResponseEntity<CommonResponse> getGiftWinners(@GetAccountIdentification(role = UserRole.ADMIN) String adminId, @PathVariable Long giftId, Pageable pageable) {

        return new ResponseEntity<>(CommonResponse.builder()
                .success(true)
                .response(giftService.findGiftWinners(pageable, giftId))
                .build(), HttpStatus.OK);
    }
}
