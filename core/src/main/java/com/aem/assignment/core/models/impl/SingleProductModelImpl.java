package com.aem.assignment.core.models.impl;

import com.aem.assignment.core.entities.ProductEntity;
import com.aem.assignment.core.models.SingleProductModel;
import com.aem.assignment.core.services.ProductDetailService;
import com.aem.assignment.core.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Implementation class for SingleProductModel.
 * Retrieves product details based on the product ID from the request URL.
 */
@Model(adaptables = {SlingHttpServletRequest.class},
        adapters = {SingleProductModel.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SingleProductModelImpl implements SingleProductModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingleProductModel.class);

    // Base URL for fetching product details
    private static final String BASE_URL = "https://fakestoreapi.com/products/";

    @ScriptVariable
    private SlingHttpServletRequest request;

    @OSGiService
    private ProductDetailService productDetailService;


    private ProductEntity productEntity = new ProductEntity();


    @Default(values = StringUtils.EMPTY)
    private String productId;

    /**
     * Retrieves product entity based on the product ID from the request URL.
     *
     * @return ProductEntity containing product details.
     */
    @Override
    public ProductEntity getProductEntity() {
        List<String> suffix = CommonUtils.getParamsFromURL(request);
        LOGGER.debug("Suffix fetched from current URL: {}", suffix);


        if (!suffix.isEmpty()) {
            productId = suffix.get(0);
            String productUrl = BASE_URL + productId;
            productEntity = productDetailService.getProductsData(productUrl);
            LOGGER.debug("Product entity based on the URL suffix: {}", productEntity);
        }
        return productEntity;
    }
}
