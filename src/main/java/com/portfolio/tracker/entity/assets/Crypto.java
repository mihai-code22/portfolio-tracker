package com.portfolio.tracker.entity.assets;

import com.portfolio.tracker.entity.Asset;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("CRYPTO")
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Crypto extends Asset {

    @NotBlank
    private String blockchain;
}
