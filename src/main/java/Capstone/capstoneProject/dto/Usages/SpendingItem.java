package Capstone.capstoneProject.dto.Usages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SpendingItem {
    private String name;
    private int count;
}
