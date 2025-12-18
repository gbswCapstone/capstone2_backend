package Capstone.capstoneProject.dto.Chats;

import Capstone.capstoneProject.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ImageUploadRequest {
    private List<MultipartFile> images;
    private String roomId;
}
