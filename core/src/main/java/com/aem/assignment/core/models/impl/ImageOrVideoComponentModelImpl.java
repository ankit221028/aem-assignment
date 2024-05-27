package com.aem.assignment.core.models.impl;

import com.aem.assignment.core.models.ImageOrVideoComponentModel;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.ArrayList;

@Data
@Model(adaptables = {SlingHttpServletRequest.class},
        adapters = {ImageOrVideoComponentModel.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ImageOrVideoComponentModelImpl implements ImageOrVideoComponentModel {

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    String contentType;

    @ValueMapValue
    @Default(values = "")
    private String image;

    @ValueMapValue
    @Default(values = "")
    private String video;





}
