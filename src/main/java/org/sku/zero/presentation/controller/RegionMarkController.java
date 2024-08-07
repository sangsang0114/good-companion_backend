package org.sku.zero.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.sku.zero.application.RegionMarkService;
import org.sku.zero.dto.request.RegionMarkRequest;
import org.sku.zero.dto.response.RegionNotificationTargetResponse;
import org.sku.zero.exception.ErrorCode;
import org.sku.zero.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/regionMark")
@RestController
public class RegionMarkController {
    private final RegionMarkService regionMarkService;

    @GetMapping("/")
    public ResponseEntity<List<String>> findRegionMarkIdsByEmail(Principal principal) {
        if (principal == null) throw new UnauthorizedException(ErrorCode.FORBIDDEN_CLIENT);
        List<String> regionIds = regionMarkService.getRegionMarkIdsByMember(principal);
        return ResponseEntity.status(HttpStatus.OK)
                .body(regionIds);
    }

    @PostMapping("/edit")
    public ResponseEntity<Boolean> editMark(@RequestBody RegionMarkRequest regionMarkRequest,
                                        Principal principal) {
        regionMarkService.editRegionMark(regionMarkRequest, principal);
        return ResponseEntity.status(HttpStatus.OK)
                .body(true);
    }
}
