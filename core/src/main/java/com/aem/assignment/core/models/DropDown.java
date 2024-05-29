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

/**
 * This class is used to create the drop_down dynamically from the 'content/we-retail' page
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DropDown {
    private final String PATH = "/content/we-retail";

    @SlingObject
    ResourceResolver resolver;

    List<String> list = new ArrayList<>();

    /**
     * This function is used to populate the list with the fields to be entered in the drop_down as soon as the getter
     * list is called
     */
    @PostConstruct
    protected void inti(){
        Page parentPage = resolver.adaptTo(PageManager.class).getPage(PATH);
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
