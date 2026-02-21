package com.portfolio.tracker.entity.assets;

import com.portfolio.tracker.entity.Asset;
import com.portfolio.tracker.enums.Sector;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("STOCK")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock extends Asset {

    @NotBlank
    @Column(nullable = false)
    private String exchange;

    @Enumerated(value = EnumType.STRING)
    private Sector sector;
}
