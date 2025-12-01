package Capstone.capstoneProject.dto.Usages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReceiptRequest {
    private String imageUrl;
}
