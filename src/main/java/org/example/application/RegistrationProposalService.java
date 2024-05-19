package org.example.application;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.domain.Member;
import org.example.domain.RegistrationProposal;
import org.example.dto.request.AddRegistrationProposalRequest;
import org.example.infrastructure.repository.RegistrationProposalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RegistrationProposalService {
    private final RegistrationProposalRepository registrationProposalRepository;
    private final MemberService memberService;

    @Transactional(readOnly = true)
    public void listProposals(){
        List<RegistrationProposal> proposals = registrationProposalRepository.findAll();
    }

    @Transactional(readOnly = true)
    public void listProposalsByMemberId(){
        return;
    }
    @Transactional(readOnly = true)
    public void getProposalById(Long id){
        return;
    }
    public Long addProposal(AddRegistrationProposalRequest request, Principal principal) {
        Member member = memberService.findByEmail(principal.getName());
        RegistrationProposal registrationProposal = registrationProposalRepository.save(
                request.toEntity(member)
        );
        return registrationProposal.getId();
    }

    public Long approveProposal(Long proposalId) {
        RegistrationProposal registrationProposal = registrationProposalRepository.findById(proposalId)
                .orElseThrow(EntityNotFoundException::new);
        registrationProposal.approve();
        return proposalId;
    }

    public Long rejectProposal(Long proposalId) {
        RegistrationProposal registrationProposal = registrationProposalRepository.findById(proposalId)
                .orElseThrow(EntityNotFoundException::new);
        registrationProposal.reject();
        return proposalId;
    }
}
