package Capstone.capstoneProject.dto.Chats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UsageShareRequest {
    // 단건 다건 다됨
    private List<Long> usageIds;
}
