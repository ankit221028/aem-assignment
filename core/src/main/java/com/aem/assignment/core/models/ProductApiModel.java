package com.aem.assignment.core.models;

import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.foundation.model.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductApiModel {
    @Self
    private Resource resource;

    List<String> list = new ArrayList<>();

    public List<String> getList() {
        Page page = resource.adaptTo(Page.class);
        list.add("abc");
        return list;
    }
}
