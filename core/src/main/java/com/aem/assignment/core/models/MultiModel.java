package com.aem.assignment.core.models;

import com.aem.assignment.core.services.MultiService;
import com.aem.assignment.core.services.impl.MultiServiceImpl;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Multi field practice class using the "MultiPojo" pojo class
 */
@Model(adaptables = SlingHttpServletRequest.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MultiModel {

    @ValueMapValue
    private String path;
    @ChildResource
    Resource products;

    @SlingObject
    ResourceResolver resolver;
    @OSGiService
    MultiService multiService;

    public List<MultiPojo> getList() {
        path = products.getPath();
        return multiService.getMultiList(path,resolver);
    }
}
