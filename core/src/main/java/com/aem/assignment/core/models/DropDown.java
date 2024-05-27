package com.aem.assignment.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DropDown {
    private String path = "/content/we-retail";

    @SlingObject
    ResourceResolver resolver;

    List<String> list = new ArrayList<>();

    @PostConstruct
    protected void inti(){
        Resource resource = resolver.getResource(path);
        Page parentPage = resolver.adaptTo(PageManager.class).getPage(path);
        if(parentPage != null ){
            Iterator<Page> childPages = parentPage.listChildren();
            while (childPages.hasNext()) {
                Page childPage = childPages.next();
                list.add(childPage.getTitle());
            }
        }

    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

}
