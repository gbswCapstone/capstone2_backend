package Capstone.capstoneProject.dto.Usages;

import Capstone.capstoneProject.enums.UsageCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CategorySummariesDTO {
    private UsageCategory category;
    private BigDecimal price;
    private int amount;
}
