package com.portfolio.priceservice.entity.postgres.assets;

import com.portfolio.priceservice.entity.postgres.Asset;
import com.portfolio.common.enums.AssetType;
import com.portfolio.priceservice.enums.Sector;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("STOCK")
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock extends Asset {

    @NotBlank
    private String exchange;

    @Enumerated(value = EnumType.STRING)
    private Sector sector;
}
