package com.devnus.belloga.point.point.controller;

import com.devnus.belloga.point.common.aop.annotation.GetAccountIdentification;
import com.devnus.belloga.point.common.aop.annotation.UserRole;
import com.devnus.belloga.point.common.dto.CommonResponse;
import com.devnus.belloga.point.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/point")
public class PointController {
    private static final Logger logger = LoggerFactory.getLogger(PointController.class);
    private final PointService pointService;

    @GetMapping("/v1/info")
    public ResponseEntity<CommonResponse> getMyPointInfo(@GetAccountIdentification(role = UserRole.LABELER) String labelerId) {
        Object result = null;
        try {
            result = pointService.getMyPointInfo(labelerId);
        } catch (DataIntegrityViolationException de) {
            // write skew 동시성으로 인한 중복 발생, 보상 트랜잭션
            logger.info("write skew 보상 트랜잭션");
            result = pointService.getMyPointInfo(labelerId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return new ResponseEntity<>(CommonResponse.builder()
                .success(true)
                .response(result)
                .build(), HttpStatus.OK);
    }
}
