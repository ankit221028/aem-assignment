package com.aem.assignment.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class OrphandAsset {
    private List<String> list = new ArrayList<>();

    @SlingObject
    ResourceResolver resolver;

    @PostConstruct
    protected void init(){
        String path = "/content";
        Resource resource = resolver.getResource(path);
        if(resource != null && resource.hasChildren()){
            for(Resource res : resource.getChildren()){
                ValueMap props = res.getValueMap();
                String primaryType = props.get("jcr:primaryType",String.class);
                assert primaryType != null;
                if(primaryType.equals("cq:Page")) list.add(res.getName());
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
