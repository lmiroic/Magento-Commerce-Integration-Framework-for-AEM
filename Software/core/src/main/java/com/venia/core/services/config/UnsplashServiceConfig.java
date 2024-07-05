package com.venia.core.services.config;


import org.apache.commons.lang3.StringUtils;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Unsplash Service Configuration", description = "Configuration for calling Unsplash API")
public @interface UnsplashServiceConfig {

    @AttributeDefinition(name = "API Key", description = "API key for Unsplash API call")
    String accessKey() default StringUtils.EMPTY;

}
