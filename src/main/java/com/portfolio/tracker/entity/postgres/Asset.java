package com.portfolio.tracker.entity.postgres;

import com.portfolio.tracker.enums.AssetType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table (name = "asset")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@ToString(exclude = "portfolio")
@EqualsAndHashCode(exclude = "portfolio")
public abstract class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String symbol;

    @NotNull
    @Positive
    private Float quantity;

    @NotNull
    @Positive
    private Float buyPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    public abstract AssetType getAssetType();
}
