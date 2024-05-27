package com.aem.assignment.core.services;


import com.aem.assignment.core.models.MultiPojo;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.List;

public interface MultiService {
    public List<MultiPojo> getMultiList(String path, ResourceResolver resolver);
}
