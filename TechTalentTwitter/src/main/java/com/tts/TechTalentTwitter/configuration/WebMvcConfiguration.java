package com.tts.TechTalentTwitter.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

        // this will allow to encrypt user passwords
        // it can be injected into our service
        @Bean
        public BCryptPasswordEncoder passwordEncoder() {

            // bellow we are instantiating BCryptPasswordEncoder
            // assigning it to variable
            // and we will need to return it
//            BCryptPasswordEncoder bCryptPasswordEncoder =
//                    new BCryptPasswordEncoder();
//            return bCryptPasswordEncoder;

            // instead we can simply return the instantiation
        return new BCryptPasswordEncoder();

        }
    }

