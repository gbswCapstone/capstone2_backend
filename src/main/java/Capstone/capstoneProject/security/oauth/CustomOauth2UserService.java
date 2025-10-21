package Capstone.capstoneProject.security.oauth;

import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.enums.UserRole;
import Capstone.capstoneProject.repository.UserRepository;
import Capstone.capstoneProject.repository.oauth.OAuth2UserInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;


    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}",oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = null;

        // 구글 로그인 구분
        if(provider.equals("google")){
            log.info("구글 로그인");
            oAuth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());

        }
        else if (provider.equals("kakao")) {
            log.info("카카오 로그인");
            oAuth2UserInfo = new KakaoUserDetails(oAuth2User.getAttributes());
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();


        Optional<Users> optionalUser = userRepository.findByEmail(email);
        Users user;

        if (optionalUser.isEmpty()) { // db에 사용자가 없으면
            user = Users.builder()
                    .email(email) // 로그인 아이디
                    .password("SOCIAL_LOGIN") // 소셜 로그인용 임의 비번
                    .provider(provider)
                    .providerId(providerId)
                    .role(UserRole.USER)
                    .build();
            userRepository.save(user);
        } else{
            user = optionalUser.get();
            // 기존 유저라면 provider 정보 업데이트
            if (!user.getProvider().equals(provider)) {
                user.setProvider(provider);
                user.setProviderId(providerId);
            }

        }

        return new CustomOauth2UserDetails(user, oAuth2User.getAttributes());
    }
}
