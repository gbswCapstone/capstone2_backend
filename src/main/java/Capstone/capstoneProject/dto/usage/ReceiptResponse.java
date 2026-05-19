package Capstone.capstoneProject.dto.usage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReceiptResponse {
    List<ReceiptListDTO> usageResponseList;
    private BigDecimal totalPrice;
    private LocalDate proDate;
}
