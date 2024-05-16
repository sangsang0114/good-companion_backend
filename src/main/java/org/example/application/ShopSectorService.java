package org.example.application;

import lombok.RequiredArgsConstructor;
import org.example.domain.ShopSector;
import org.example.exception.ErrorCode;
import org.example.exception.NotFoundException;
import org.example.infrastructure.repository.ShopSectorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ShopSectorService {
    private final ShopSectorRepository shopSectorRepository;

    public String getSectorNameByID(String sectorID) {
        ShopSector shopSector = shopSectorRepository.findById(sectorID)
                .orElseThrow(() -> new NotFoundException(ErrorCode.SECTOR_NOT_FOUND));
        return shopSector.getName();
    }

    public List<ShopSector> listSectors() {
        return shopSectorRepository.findAll();
    }
}
