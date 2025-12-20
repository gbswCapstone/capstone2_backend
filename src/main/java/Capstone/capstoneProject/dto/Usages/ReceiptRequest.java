package Capstone.capstoneProject.dto.Usages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReceiptRequest {
    private MultipartFile image;
}
