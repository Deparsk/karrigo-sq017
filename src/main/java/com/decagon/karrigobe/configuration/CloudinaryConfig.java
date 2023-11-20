package com.decagon.karrigobe.configuration;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    //    @Value("${CLOUDINARY_NAME}")
    private final String CLOUD_NAME = "dzq6l92bt";

    //    @Value("${CLOUDINARY_API_KEY}")
    private final String API_KEY = "615671161187452";

    //    @Value("${CLOUDINARY_SECRET}")
    private final String API_SECRET = "EOAY5aVDFnUSayet9TyDvGPp8fc";


    @Bean
    public Cloudinary cloudinary(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name",CLOUD_NAME);
        config.put("api_key",API_KEY);
        config.put("api_secret",API_SECRET);

        return new Cloudinary(config);
    }
}
