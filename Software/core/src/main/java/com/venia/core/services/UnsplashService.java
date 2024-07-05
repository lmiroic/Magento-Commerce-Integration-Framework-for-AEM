package com.venia.core.services;

import com.venia.core.models.unsplash.dtos.UnsplashImage;

import java.util.List;
import java.util.Optional;

public interface UnsplashService {

    Optional<List<UnsplashImage>> promptUnsplash(final String searchTerm, final int limit, final String backupSearchTerm);

}
