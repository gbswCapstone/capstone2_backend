package Capstone.capstoneProject.dto.usage;

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
