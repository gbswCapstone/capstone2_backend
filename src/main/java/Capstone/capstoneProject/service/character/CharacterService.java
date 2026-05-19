package Capstone.capstoneProject.service.character;

import Capstone.capstoneProject.dto.character.CharacterResponse;
import Capstone.capstoneProject.entity.user.UserCharacters;
import Capstone.capstoneProject.entity.user.Users;
import Capstone.capstoneProject.exceptions.notfound.UserCharacterNotFoundException;
import Capstone.capstoneProject.repository.user.UserCharacterRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CharacterService {

    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final UserCharacterRepository userCharacterRepository;

    public CharacterResponse getMyCharacter() {
        Users user = authenticatedUserUtils.getCurrentUser();

        UserCharacters userCharacters = userCharacterRepository.findByUser(user)
                .orElseThrow(() -> new UserCharacterNotFoundException("해당 유저의 캐릭터(등급)을 찾을 수 없습니다."));

        return CharacterResponse.from(userCharacters);
    }
}
