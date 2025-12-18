package Capstone.capstoneProject.dto.Usages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReceiptPatchRequest {
    private List<ReceiptItemPatchDTO> items;
}
