package com.venia.core.models.commerce;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import com.adobe.cq.commerce.core.components.models.common.ProductListItem;
import com.adobe.cq.commerce.core.components.models.productcarousel.ProductCarousel;
import com.adobe.cq.commerce.core.components.models.retriever.AbstractProductsRetriever;

@Model(adaptables = SlingHttpServletRequest.class, adapters = CustomProductCarousel.class, resourceType = CustomProductCarouselImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CustomProductCarouselImpl implements CustomProductCarousel {

    protected static final String RESOURCE_TYPE = "venia/components/commerce/customproductcarousel";

    private static final String ECO_FRIENDLY = "eco_friendly";

    @ValueMapValue
    private Boolean isShowPrice;

    @ValueMapValue
    private Boolean isShowOnlyEcoFriendly;

    private AbstractProductsRetriever productRetriever;

    @PostConstruct
    public void initModel() {
        productRetriever = productCarousel.getProductsRetriever();
        if (productRetriever != null) {
        }
    }

    @Self
    @Via(type = ResourceSuperType.class)
    private ProductCarousel productCarousel;

    @Override
    public Boolean isShowPrice() {
        return this.isShowPrice;
    }

    @Override
    public List<ProductListItem> getProducts() {
        List<ProductListItem> availableProducts = this.productCarousel.getProducts();
        return this.isShowOnlyEcoFriendly ? this.getEcoFriendlyProducts(availableProducts) : availableProducts;
    }

    @Override
    public List<ProductListItem> getProductIdentifiers() {
        return this.productCarousel.getProductIdentifiers();
    }

    @Override
    public AbstractProductsRetriever getProductsRetriever() {
        return this.productCarousel.getProductsRetriever();
    }

    @Override
    public boolean isConfigured() {
        return this.productCarousel.isConfigured();
    }

    @Override
    public String getTitleType() {
        return this.productCarousel.getTitleType();
    }

    private List<ProductListItem> getEcoFriendlyProducts(List<ProductListItem> products) {
        return products
                 .stream()
                 .filter(productListItem -> String.valueOf(productListItem.getProduct().get(ECO_FRIENDLY)).equals("1"))
                 .collect(Collectors.toList());
    }

}
