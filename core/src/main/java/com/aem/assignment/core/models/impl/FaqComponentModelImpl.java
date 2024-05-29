package com.aem.assignment.core.models.impl;


import com.aem.assignment.core.entities.FaqEntity;
import com.aem.assignment.core.models.FaqComponentModel;
import lombok.Data;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * The implementation class of the FaqComponentModel.
 */
@Model(adaptables = {SlingHttpServletRequest.class},
        adapters = {FaqComponentModel.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FaqComponentModelImpl implements FaqComponentModel {

    private List<FaqEntity> faqList = new ArrayList<>();

    /**
     * The @ChildResource is used to fetch the child resource under the present resource.
     */
    @ChildResource
    private Resource faqFields;

    /**
     * This function works as a constructor and fills the faq entries in the faqList as soon as the getter for faqList
     * is called.
     */
    @PostConstruct
    public void init(){
        if(faqFields!=null && faqFields.hasChildren()){
            for(Resource res: faqFields.getChildren()){
                ValueMap valueMap = res.getValueMap();
                String faqQuestion = valueMap.get("faqQuestion", String.class);
                String faqAnswer = valueMap.get("faqAnswer", String.class);
                FaqEntity faqEntity = new FaqEntity(faqQuestion,faqAnswer);
                faqList.add(faqEntity);
            }
        }
    }

    public List<FaqEntity> getFaqList() {
        return faqList;
    }

}

