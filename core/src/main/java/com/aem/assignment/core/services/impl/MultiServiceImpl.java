package com.aem.assignment.core.services.impl;

import com.aem.assignment.core.models.MultiPojo;
import com.aem.assignment.core.services.MultiService;
import org.apache.sling.api.resource.*;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(service = MultiService.class)
public class MultiServiceImpl implements MultiService {


    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Override
    public List<MultiPojo> getMultiList(String path,ResourceResolver resourceResolver) {
        List<MultiPojo> multiList = new ArrayList<>();
        ResourceResolver resolver = getResourceResolver();
        assert resolver != null;
        Resource resource = resolver.getResource(path);
        if(resource != null && resource.hasChildren()){
            for(Resource res : resource.getChildren()){
                MultiPojo multiPojo = new MultiPojo();
                ValueMap props = res.getValueMap();

                multiPojo.setQuestion(props.get("faqQuestion", String.class));
                multiPojo.setAnswer(props.get("faqAnswer", String.class));
                multiList.add(multiPojo);
            }
        }
        return multiList;
    }
    private ResourceResolver getResourceResolver() {
        Map<String, Object> authMap = new HashMap<>();
        authMap.put(ResourceResolverFactory.SUBSERVICE,"TestUser");
        try{
            return resourceResolverFactory.getServiceResourceResolver(authMap);
        }
        catch (LoginException e){
            e.printStackTrace();
        }
        return null;
    }

}
