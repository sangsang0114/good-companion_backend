package org.sku.zero.application;

import lombok.RequiredArgsConstructor;
import org.sku.zero.domain.ShopPending;
import org.sku.zero.infrastructure.repository.ShopPendingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopPendingService {
    private final ShopPendingRepository shopPendingRepository;

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
}
