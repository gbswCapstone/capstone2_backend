package Capstone.capstoneProject.dto.character;


import Capstone.capstoneProject.entity.user.UserCharacters;
import Capstone.capstoneProject.enums.CharacterType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CharacterResponse {
    private Long id;
    private int level;
    private CharacterType type;
    private String rating;
    private int experience;

    public static CharacterResponse from(UserCharacters userCharacters) {
        return CharacterResponse.builder()
                .id(userCharacters.getId())
                .level(userCharacters.getLevel())
                .type(userCharacters.getType())
                .rating(userCharacters.getRating())
                .experience(userCharacters.getExperience())
                .build();
    }
}
