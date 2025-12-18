package Capstone.capstoneProject.config;


import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            // 시간을 한국시간 기준으로 설정
            builder.timeZone(TimeZone.getTimeZone("Asia/Seoul"));
        };
    }
}
