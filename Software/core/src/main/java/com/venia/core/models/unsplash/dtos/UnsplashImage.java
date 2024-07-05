package com.venia.core.models.unsplash.dtos;

import lombok.Getter;

@Getter
public class UnsplashImage {

    private String description;
    private String alt_description;
    private UnsplashUrls urls;

}
