package com.venia.core.models.commerce.custom;

import com.adobe.cq.commerce.core.components.models.product.Product;
import com.venia.core.models.unsplash.dtos.UnsplashImage;
import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface CustomProductHero extends Product {

    String getImageUrl();

    UnsplashImage getUnsplashImage();

    String getImageAlt();

    String getFeaturedText();

    boolean isEmpty();

    boolean isAIGeneratedImage();

}
