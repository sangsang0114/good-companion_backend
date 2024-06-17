package org.sku.zero.application;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sku.zero.domain.Shop;
import org.sku.zero.domain.ShopPending;
import org.sku.zero.dto.external.GeoCoderResultDto;
import org.sku.zero.dto.request.AcceptShopPendingRequest;
import org.sku.zero.dto.request.RejectShopPendingRequest;
import org.sku.zero.event.NewShopAddedEvent;
import org.sku.zero.infrastructure.repository.ShopPendingRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopPendingService {
    private final ShopPendingRepository shopPendingRepository;
    private final ShopService shopService;
    private final ShopLocationService shopLocationService;
    private final FranchiseService franchiseService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public Page<ShopPending> findAll(int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ShopPending> shopPendings = shopPendingRepository.findAll(pageable);
        return shopPendings;
    }

    @Transactional
    public void save(ShopPending shopPending) {
        shopPendingRepository.save(shopPending);
    }

    @Transactional(readOnly = true)
    public List<ShopPending> findAll() {
        return shopPendingRepository.findAll();
    }

    @Transactional
    public Boolean acceptShopPending(AcceptShopPendingRequest request) {
        String id = request.id();
        ShopPending shopPending = shopPendingRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        GeoCoderResultDto geoCoderResultDto = shopLocationService.getCoordinateAndRegionId(request.newAddress());

        String x = geoCoderResultDto.longitude();
        String y = geoCoderResultDto.latitude();
        String zipcode = shopLocationService.getZipcode(x, y);
        Integer isFranchise = franchiseService.isFranchise(zipcode, shopPending.getName()) ? 1 : 0;

        String regionId = geoCoderResultDto.regionId().substring(0, 5);

        Shop shop = shopService.save(request.toShopEntity(shopPending, isFranchise, regionId));
        shopLocationService.save(
                request.toShopLocationEntity(
                        shopPending,
                        new BigDecimal(y),
                        new BigDecimal(x)
                )
        );
        eventPublisher.publishEvent(new NewShopAddedEvent(this, shop, shopPending.getImgUrlPublic()));
        shopPending.editMemo(request.memo());
        shopPending.updateStatus("ACCEPTED");
        return true;
    }

    @Transactional
    public Boolean rejectShopPending(RejectShopPendingRequest request) {
        String id = request.id();

        ShopPending shopPending = shopPendingRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        shopPending.updateStatus("REJECTED");
        shopPending.editMemo(request.memo());
        return true;
    }
}
