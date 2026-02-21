package com.portfolio.tracker.entity.assets;

import com.portfolio.tracker.entity.Asset;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("CRYPTO")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Crypto extends Asset {

    @NotBlank
    @Column(nullable = false)
    private String blockchain;
}
