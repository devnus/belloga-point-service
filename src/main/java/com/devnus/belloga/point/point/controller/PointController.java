package com.devnus.belloga.point.point.controller;

import com.devnus.belloga.point.common.aop.annotation.GetAccountIdentification;
import com.devnus.belloga.point.common.aop.annotation.UserRole;
import com.devnus.belloga.point.common.dto.CommonResponse;
import com.devnus.belloga.point.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/point")
public class PointController {
    private final PointService pointService;

    @GetMapping("/v1/info")
    public ResponseEntity<CommonResponse> getMyPointInfo(@GetAccountIdentification(role = UserRole.LABELER) String labelerId) {
        return new ResponseEntity<>(CommonResponse.builder()
                .success(true)
                .response(pointService.getMyPointInfo(labelerId))
                .build(), HttpStatus.OK);
    }
}
