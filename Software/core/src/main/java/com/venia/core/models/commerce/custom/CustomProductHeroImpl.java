package com.venia.core.models.commerce.custom;

import com.adobe.cq.commerce.core.components.models.common.Price;
import com.adobe.cq.commerce.core.components.models.product.*;
import com.adobe.cq.commerce.core.components.models.retriever.AbstractProductRetriever;
import com.adobe.cq.commerce.core.components.storefrontcontext.ProductStorefrontContext;
import com.day.cq.dam.commons.util.DamUtil;
import com.venia.core.models.unsplash.dtos.UnsplashImage;
import com.venia.core.services.UnsplashService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Model(adaptables = SlingHttpServletRequest.class, adapters = CustomProductHero.class,
        resourceType = CustomProductHeroImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CustomProductHeroImpl implements CustomProductHero {

    static final String RESOURCE_TYPE = "venia/components/commerce/customproducthero";

    private static final String INTERNAL_IMAGE_OPTION = "internalImage";

    @Self
    @Via(type = ResourceSuperType.class)
    private Product product;

    @SlingObject
    private ResourceResolver resourceResolver;

    @Getter
    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    private String featuredText;

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    private String imageOption;

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    private String fileReference;

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    private String promptText;

    @Getter
    private UnsplashImage unsplashImage;

    @Getter
    private String imageAlt = StringUtils.EMPTY;

    @Getter
    private String imageUrl = StringUtils.EMPTY;

    @OSGiService
    private UnsplashService unsplashService;

    @PostConstruct
    private void init() {
        try {
            final Optional<UnsplashImage> optionalUnsplashImage = this.getOptionalUnsplashImage();
            if(!INTERNAL_IMAGE_OPTION.equals(this.imageOption) && optionalUnsplashImage.isPresent()) {
                this.unsplashImage = optionalUnsplashImage.get();
            }
            this.imageUrl = INTERNAL_IMAGE_OPTION.equals(this.imageOption) ? this.fetchAssetUrl() : "";
            this.imageAlt = INTERNAL_IMAGE_OPTION.equals(this.imageOption) ? this.fetchAssetAlt() : "";
        } catch (final RuntimeException e) {
            log.error("Error in post construct", e);
        }
    }

    @Override
    public boolean isEmpty() {
        return this.product == null;
    }

    @Override
    public boolean isAIGeneratedImage() {
        return !INTERNAL_IMAGE_OPTION.equals(this.imageOption);
    }

    @Override
    public Boolean getFound() {
        return this.product.getFound();
    }

    @Override
    public String getName() {
        return this.product.getName();
    }

    @Override
    public String getDescription() {
        return this.product.getDescription();
    }

    @Override
    public String getSku() {
        return this.product.getSku();
    }

    @Override
    public Price getPriceRange() {
        return this.product.getPriceRange();
    }

    @Override
    public Boolean getInStock() {
        return this.product.getInStock();
    }

    @Override
    public Boolean isConfigurable() {
        return this.product.isConfigurable();
    }

    @Override
    public Boolean isGroupedProduct() {
        return this.product.isGroupedProduct();
    }

    @Override
    public Boolean isVirtualProduct() {
        return this.product.isVirtualProduct();
    }

    @Override
    public Boolean isBundleProduct() {
        return this.product.isBundleProduct();
    }

    @Override
    public String getVariantsJson() {
        return this.product.getVariantsJson();
    }

    @Override
    public List<Variant> getVariants() {
        return this.product.getVariants();
    }

    @Override
    public List<GroupItem> getGroupedProductItems() {
        return this.product.getGroupedProductItems();
    }

    @Override
    public List<Asset> getAssets() {
        return this.product.getAssets();
    }

    @Override
    public String getAssetsJson() {
        return this.product.getAssetsJson();
    }

    @Override
    public List<VariantAttribute> getVariantAttributes() {
        return this.product.getVariantAttributes();
    }

    @Override
    public Boolean loadClientPrice() {
        return null;
    }

    @Override
    public AbstractProductRetriever getProductRetriever() {
        return this.product.getProductRetriever();
    }

    @Override
    public ProductStorefrontContext getStorefrontContext() {
        return this.product.getStorefrontContext();
    }

    @Override
    public String getMetaDescription() {
        return this.product.getMetaDescription();
    }

    @Override
    public String getMetaKeywords() {
        return this.product.getMetaKeywords();
    }

    @Override
    public String getMetaTitle() {
        return this.product.getMetaTitle();
    }

    @Override
    public String getCanonicalUrl() {
        return this.product.getCanonicalUrl();
    }

    private Optional<UnsplashImage> getOptionalUnsplashImage() {
        return this.unsplashService.promptUnsplash(this.promptText, 1, this.product.getName())
                .stream()
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .findFirst();
    }

    private String fetchAssetUrl() {
        return Optional.ofNullable(this.resourceResolver)
                .map(resolver -> resolver.getResource(this.fileReference))
                .filter(DamUtil::isAsset)
                .map(DamUtil::resolveToAsset)
                .map(com.day.cq.dam.api.Asset::getPath)
                .orElse(StringUtils.EMPTY);
    }

    private String fetchAssetAlt() {
        return Optional.ofNullable(this.resourceResolver)
                .map(resolver -> resolver.getResource(this.fileReference))
                .filter(DamUtil::isAsset)
                .map(DamUtil::resolveToAsset)
                .map(com.day.cq.dam.api.Asset::getName)
                .orElse(StringUtils.EMPTY);
    }

}
