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

@Data
@Model(adaptables = {SlingHttpServletRequest.class},
        adapters = {FaqComponentModel.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FaqComponentModelImpl implements FaqComponentModel {

    List<FaqEntity> faqList = new ArrayList<>();


    @ChildResource
    Resource faqFields;

    @PostConstruct
    public void init(){
        if(faqFields!=null && faqFields.hasChildren()){
            for(Resource res: faqFields.getChildren()){
                FaqEntity faqEntity = new FaqEntity();
                ValueMap valueMap = res.getValueMap();
                if(valueMap.containsKey("faqQuestion")){
                    faqEntity.setQuestion(valueMap.get("faqQuestion", String.class));
                }

                if(valueMap.containsKey("faqAnswer")){
                    faqEntity.setAnswer(valueMap.get("faqAnswer", String.class));
                }

                faqList.add(faqEntity);
            }
        }
    }

    public List<FaqEntity> getFaqList() {
        return faqList;
    }

    public void setFaqList(List<FaqEntity> faqList) {
        this.faqList = faqList;
    }
}

