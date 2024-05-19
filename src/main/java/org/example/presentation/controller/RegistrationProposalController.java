package org.example.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.example.application.RegistrationProposalService;
import org.example.dto.request.AddRegistrationProposalRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/api/v1/proposal")
@RestController
@RequiredArgsConstructor
public class RegistrationProposalController {
    private final RegistrationProposalService registrationProposalService;

    @GetMapping("/")
    public ResponseEntity<String> listProposals() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> findProposalById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }

    @PostMapping("/register")
    public ResponseEntity<Long> addProposal(
            @RequestBody AddRegistrationProposalRequest proposalRequest,
            Principal principal) {
        Long id = registrationProposalService.addProposal(proposalRequest, principal);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(id);
    }

    //승인
    @PostMapping("/{id}/approval")
    public ResponseEntity<Long> approveProposal(@PathVariable Long id) {
        Long targetId = registrationProposalService.approveProposal(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(targetId);
    }

    //반려
    @PostMapping("/{id}/reject")
    public ResponseEntity<Long> rejectProposal(@PathVariable Long id) {
        Long targetId = registrationProposalService.rejectProposal(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(targetId  );
    }
}
