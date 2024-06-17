package org.sku.zero.dto.request;

import org.sku.zero.domain.RegistrationProposal;
import org.sku.zero.domain.Shop;

public record AcceptProposalRequest(Long id, String phone, String info, String boast, String memo,
                                    String businessHours, Integer isLocalFranchise) {
    public Shop toShopEntity(String shopId, RegistrationProposal proposal) {
        return Shop.builder()
                .id(shopId)
                .phone(phone)
                .name(proposal.getShopName())
                .address(proposal.getShopAddress())
                .zipcode(proposal.getZipcode())
                .shopRegion(proposal.getShopAddress())
                .shopSector(proposal.getSectorId())
                .boast(boast)
                .isLocalFranchise(isLocalFranchise)
                .businessHours(businessHours)
                .info(info)
                .build();
    }
}
