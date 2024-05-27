package com.aem.assignment.core.services.impl;

import com.aem.assignment.core.models.ProductObj;
import com.aem.assignment.core.services.ProductConfig;
import com.aem.assignment.core.services.ProductService;
import com.drew.lang.StringUtil;
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

@Component(service = ProductService.class)
@Designate(ocd = ProductConfig.class)
public class ProductServiceImpl implements ProductService{

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    String path;

    @Activate
    @Modified
    protected void activate(ProductConfig config){
        path = config.getPath();
    }

    static List<ProductObj> productList = new ArrayList<>();


    @Override
    public List<ProductObj> getProductLists() {
        ResourceResolver resolver = getResourceResolver();
        if(StringUtils.isNoneEmpty(path)){
            if(resolver != null){
                Resource resource = resolver.getResource(path);
                if(resource != null && resource.hasChildren()){
                    for(Resource item : resource.getChildren()){
                        ProductObj obj = new ProductObj();
                        ValueMap props = item.getValueMap();
                        obj.setProductImage(props.get("productImage", String.class));
                        obj.setProductTitle(props.get("productTitle", String.class));
                        obj.setProductPrice(props.get("productPrice", String.class));
                        productList.add(obj);
                    }
                }
            }
        }

        return productList;
    }

    private Map<String, String> getQuery(String path){
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("path",path);
        queryMap.put("property", "cq:tags");
        queryMap.put("property.value","we-retail:equipment");
        return queryMap;
    }
    private ResourceResolver getResourceResolver() {
        Map<String, Object> authMap = new HashMap<>();
        authMap.put(ResourceResolverFactory.SUBSERVICE,"demosystemuser");
        try{
            return resourceResolverFactory.getResourceResolver(authMap);
        }
        catch (LoginException e){
            e.printStackTrace();
        }
        return null;
    }

}
