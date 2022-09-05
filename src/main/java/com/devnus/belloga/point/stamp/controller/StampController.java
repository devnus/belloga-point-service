package com.devnus.belloga.point.stamp.controller;

import com.devnus.belloga.point.common.aop.annotation.GetAccountIdentification;
import com.devnus.belloga.point.common.aop.annotation.UserRole;
import com.devnus.belloga.point.common.dto.CommonResponse;
import com.devnus.belloga.point.stamp.service.StampService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stamp")
public class StampController {
    private final StampService stampService;


    public StampController(StampService stampService) {
        this.stampService = stampService;
    }

    /**
     * 포인트를 사용해 도장을 추가한다.
     * @return
     */
    @PostMapping("/v1/add")
    public ResponseEntity<CommonResponse> exchangeToStamp(@GetAccountIdentification(role = UserRole.LABELER) String labelerId) {

        stampService.addStamp(labelerId);

        return new ResponseEntity<>(CommonResponse.builder()
                .success(true)
                .build(), HttpStatus.OK);
    }

}
