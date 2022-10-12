package com.devnus.belloga.point.stamp.controller;

import com.devnus.belloga.point.common.aop.annotation.GetAccountIdentification;
import com.devnus.belloga.point.common.aop.annotation.UserRole;
import com.devnus.belloga.point.common.dto.CommonResponse;
import com.devnus.belloga.point.stamp.service.StampService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.GetMapping;
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

        try {
            stampService.addStamp(labelerId);
        } catch (ObjectOptimisticLockingFailureException oe) {
            // 낙관적 락 재시도
            stampService.addStamp(labelerId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(CommonResponse.builder()
                .success(true)
                .build(), HttpStatus.OK);
    }

    /**
     * stamp value를 반환한다.
     * @param labelerId
     * @return
     */
    @GetMapping("/v1/info")
    public ResponseEntity<CommonResponse> getMyStampInfo(@GetAccountIdentification(role = UserRole.LABELER) String labelerId) {
        Object result = null;

        try {
            result = stampService.getMyStampInfo(labelerId);
        } catch (ObjectOptimisticLockingFailureException oe) {
            // 낙관적 락 재시도
            result = stampService.getMyStampInfo(labelerId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(CommonResponse.builder()
                .success(true)
                .response(result)
                .build(), HttpStatus.OK);
    }

}
