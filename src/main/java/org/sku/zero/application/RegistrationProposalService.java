package org.sku.zero.application;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sku.zero.domain.*;
import org.sku.zero.dto.external.GeoCoderResultDto;
import org.sku.zero.dto.request.AcceptProposalRequest;
import org.sku.zero.dto.request.AddRegistrationProposalRequest;
import org.sku.zero.dto.request.RejectProposalRequest;
import org.sku.zero.dto.response.ListProposalResponse;
import org.sku.zero.dto.response.ProposalDetailResponse;
import org.sku.zero.exception.ErrorCode;
import org.sku.zero.exception.NotFoundException;
import org.sku.zero.infrastructure.repository.IdGeneratorRepository;
import org.sku.zero.infrastructure.repository.RegistrationProposalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final ShopService shopService;
    private final IdGeneratorRepository idGeneratorRepository;
    private final ShopLocationService shopLocationService;

    @Transactional(readOnly = true)
    public void listProposals(){
        List<RegistrationProposal> proposals = registrationProposalRepository.findAll();
    }

    @Transactional(readOnly = true)
    public void listProposalsByMemberId(){
        return;
    }
    @Transactional(readOnly = true)
    public ProposalDetailResponse getProposalById(Long id) {
        RegistrationProposal proposal = registrationProposalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PROPOSAL_NOT_FOUND));
        return ProposalDetailResponse.toDto(proposal, proposal.getMember());
    }

    public Long addProposal(AddRegistrationProposalRequest request, Principal principal) {
        Member member = memberService.findByEmail(principal.getName());
        RegistrationProposal registrationProposal = registrationProposalRepository.save(
                request.toEntity(member)
        );
        return registrationProposal.getId();
    }

    public Long approveProposal(AcceptProposalRequest request) {
        RegistrationProposal registrationProposal = registrationProposalRepository.findById(request.id())
                .orElseThrow(EntityNotFoundException::new);
        IdGenerator idGenerator = idGeneratorRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
        String id = idGenerator.getGeneratedId().toString();
        idGenerator.generateNewId();

        Shop shop = shopService.save(request.toShopEntity(id, registrationProposal));
        GeoCoderResultDto resultDto = shopLocationService.getCoordinateAndRegionId(registrationProposal.getShopAddress());
        ShopLocation shopLocation = shopLocationService.save(resultDto.toEntity(id));

        registrationProposal.editMemo(request.memo());
        registrationProposal.approve();

        return 0L;
    }

    public Long rejectProposal(RejectProposalRequest request) {
        RegistrationProposal registrationProposal = registrationProposalRepository.findById(request.id())
                .orElseThrow(EntityNotFoundException::new);

        registrationProposal.editMemo(request.memo());
        registrationProposal.reject();
        return 0L;
    }

    @Transactional(readOnly = true)
    public Page<RegistrationProposal> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RegistrationProposal> registrationProposals = registrationProposalRepository.findAll(pageable);
        return registrationProposals;
    }

    public List<ListProposalResponse> findByMember(Principal principal) {
        Member member = memberService.findByEmail(principal.getName());
        List<RegistrationProposal> proposals = registrationProposalRepository.findRegistrationProposalsByMember(member);
        return proposals.stream().map(proposal -> ListProposalResponse.toDto(proposal, member)).toList();
    }
}
