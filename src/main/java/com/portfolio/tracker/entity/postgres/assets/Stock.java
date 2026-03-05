package com.portfolio.tracker.entity.postgres.assets;

import com.portfolio.tracker.entity.postgres.Asset;
import com.portfolio.tracker.enums.AssetType;
import com.portfolio.tracker.enums.Sector;
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

    @Override
    public AssetType getAssetType() {
        return AssetType.STOCK;
    }
}
