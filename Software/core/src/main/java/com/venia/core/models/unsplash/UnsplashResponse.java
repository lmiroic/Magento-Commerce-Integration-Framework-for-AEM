package com.venia.core.models.unsplash;

import com.venia.core.models.unsplash.dtos.UnsplashImage;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UnsplashResponse {

    private final List<UnsplashImage> results = new ArrayList<>();

}
