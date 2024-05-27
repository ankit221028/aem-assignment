package com.aem.assignment.core.services;

import com.aem.assignment.core.models.ProductObj;
import org.osgi.service.component.annotations.Component;

import java.util.List;


public interface ProductService {
    public List<ProductObj> getProductLists();
}
