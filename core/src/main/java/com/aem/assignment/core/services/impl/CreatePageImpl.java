package com.aem.assignment.core.services.impl;

import com.aem.assignment.core.services.CreatePage;
import com.day.cq.search.QueryBuilder;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.foundation.model.Page;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Session;
import javax.management.Query;
import java.util.HashMap;
import java.util.Map;

@Component(service = CreatePage.class)
public class CreatePageImpl implements CreatePage {


    String TEMPLATE_PATH = "/conf/aem_assignment/settings/wcm/templates/page-content";

    String PAGE_PATH = "/content/aem_assignment/us/en";

    @Reference
    ResourceResolverFactory resourceResolverFactory;


    @Reference
    private QueryBuilder queryBuilder;

    private Map<String , String> getQuery(String path){
        Map<String, String> query = new HashMap<>();
        query.put("path", "content/we-retail/");
        query.put("property", "jcr:PrimaryType");
        return query;
    }
    @Override
    public void create(String path) throws LoginException, WCMException {
        ResourceResolver resolver = getResourceResolver(resourceResolverFactory);
       // Resource resource = resolver.getResource("/content/aem_assignment/us/en");
        Session session = resolver.adaptTo(Session.class);

        PageManager pageManager = resolver.adaptTo(PageManager.class);
        pageManager.create(PAGE_PATH, "Scheduler_Page", TEMPLATE_PATH, "schedulerPage");

        return;
    }

    private ResourceResolver getResourceResolver(ResourceResolverFactory resourceResolverFactory) throws LoginException {
        Map<String,Object> authMap = new HashMap<>();
        authMap.put(ResourceResolverFactory.SUBSERVICE,"demosystemuser");
        ResourceResolver resolver = resourceResolverFactory.getResourceResolver(authMap);
        return resolver;
    }
}
