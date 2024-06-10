package org.sku.zero.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SubwayStationResponse {
    private String name;
    private String line;
    private Double distance;
}
