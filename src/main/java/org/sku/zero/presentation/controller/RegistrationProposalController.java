package org.sku.zero.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.sku.zero.application.RegistrationProposalService;
import org.sku.zero.domain.RegistrationProposal;
import org.sku.zero.dto.request.AcceptProposalRequest;
import org.sku.zero.dto.request.AddRegistrationProposalRequest;
import org.sku.zero.dto.request.RejectProposalRequest;
import org.sku.zero.dto.response.ListProposalResponse;
import org.sku.zero.dto.response.ProposalDetailResponse;
import org.sku.zero.dto.response.ProposalPageResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/api/v1/proposal")
@RestController
@RequiredArgsConstructor
public class RegistrationProposalController {
    private final RegistrationProposalService registrationProposalService;

    @GetMapping("/")
    public ResponseEntity<ProposalPageResponse> listProposals(@RequestParam(defaultValue = "10") int size,
                                                              @RequestParam(defaultValue = "0") int page) {
        Page<RegistrationProposal> proposalPage = registrationProposalService.findAll(page, size);
        List<ListProposalResponse> proposalList = proposalPage.getContent()
                .stream().map(proposal -> ListProposalResponse.toDto(proposal, proposal.getMember()))
                .toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ProposalPageResponse.toDto(proposalList, proposalPage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProposalDetailResponse> findProposalById(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(registrationProposalService.getProposalById(id));
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
    @PostMapping("/approval")
    public ResponseEntity<Long> approveProposal(@RequestBody AcceptProposalRequest request) {
        Long targetId = registrationProposalService.approveProposal(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(targetId);
    }

    //반려
    @PostMapping("/reject")
    public ResponseEntity<Long> rejectProposal(@RequestBody RejectProposalRequest request) {
        Long targetId = registrationProposalService.rejectProposal(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(targetId);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ListProposalResponse>> listProposals(Principal principal) {
        List<ListProposalResponse> proposalResponses = registrationProposalService.findByMember(principal);

        return ResponseEntity.status(HttpStatus.OK)
                .body(proposalResponses);
    }
}
