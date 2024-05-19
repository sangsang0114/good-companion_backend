package org.example.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.example.application.RegionMarkService;
import org.example.dto.request.AddRegionMarkRequest;
import org.example.dto.request.RegionIdRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/api/v1/regionMark")
@RestController
public class RegionMarkController {
    private final RegionMarkService regionMarkService;

    @PostMapping("/add")
    public ResponseEntity<Long> addMark(@RequestBody AddRegionMarkRequest regionMarkRequest,
                                        Principal principal) {
        Long id = regionMarkService.addRegionMark(regionMarkRequest, principal);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(id);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> removeMark(@RequestBody RegionIdRequest request, Principal principal) {
        System.out.println(request.region());
        Boolean response = regionMarkService.removeRegionMark(request.region(), principal);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
