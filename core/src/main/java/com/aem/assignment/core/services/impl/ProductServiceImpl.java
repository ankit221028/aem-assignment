package com.aem.assignment.core.services.impl;

import com.aem.assignment.core.models.ProductObj;
import com.aem.assignment.core.services.ProductConfig;
import com.aem.assignment.core.services.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the ProductService interface.
 * This service fetches product details from a specified AEM path.
 */
@Component(service = ProductService.class)
@Designate(ocd = ProductConfig.class)
public class ProductServiceImpl implements ProductService {

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    private String path;

    private static List<ProductObj> productList = new ArrayList<>();

    /**
     * Activates or modifies the service configuration.
     *
     * @param config the ProductConfig containing the configuration details.
     */
    @Activate
    @Modified
    protected void activate(ProductConfig config) {
        this.path = config.getPath();
    }

    /**
     * Fetches the list of products from the configured path in AEM.
     *
     * @return a list of ProductObj containing product details.
     */
    @Override
    public List<ProductObj> getProductLists() {
        productList.clear(); // Clear the list to avoid duplicate entries
        if (StringUtils.isNotEmpty(path)) {
            try (ResourceResolver resolver = getResourceResolver()) {
                if (resolver != null) {
                    Resource resource = resolver.getResource(path);
                    if (resource != null && resource.hasChildren()) {
                        for (Resource item : resource.getChildren()) {
                            productList.add(createProductObjFromResource(item));
                        }
                    }
                }
            } catch (Exception e) {
                // Log the error if necessary
                e.printStackTrace();
            }
        }
        return productList;
    }

    /**
     * Creates a ProductObj from the given resource.
     *
     * @param resource the resource representing a product.
     * @return a ProductObj containing product details.
     */
    private ProductObj createProductObjFromResource(Resource resource) {
        ValueMap props = resource.getValueMap();
        String productImagePath = props.get("productImage", String.class);
        String productTitle = props.get("productTitle", String.class);
        String productPrice = props.get("productPrice", String.class);
        return new ProductObj(productTitle, productPrice, productImagePath);
    }

    /**
     * Obtains a ResourceResolver using the configured service user.
     *
     * @return a ResourceResolver or null if login fails.
     */
    private ResourceResolver getResourceResolver() {
        Map<String, Object> authMap = new HashMap<>();
        authMap.put(ResourceResolverFactory.SUBSERVICE, "demosystemuser");
        try {
            return resourceResolverFactory.getServiceResourceResolver(authMap);
        } catch (LoginException e) {
            e.printStackTrace();
            return null;
        }
    }
}
