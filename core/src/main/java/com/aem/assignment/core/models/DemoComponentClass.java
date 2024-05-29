package com.aem.assignment.core.models;

import com.aem.assignment.core.services.ProductService;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.foundation.model.Page;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import org.apache.sling.models.annotations.injectorspecific.*;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

/**
 * This class is only for practice purpose, and currently doesn't work because of addition of multiple functions.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DemoComponentClass {

    @ScriptVariable
    SlingHttpServletRequest request;


    @PostConstruct
    protected  void init(){
        Resource resource = request.getResource();


    }

    List<String> list = new ArrayList<>();

    public List<String> getList() {
       // Page page = resource.adaptTo(Page.class);
        list.add("abc");
        return list;
    }
//    @ValueMapValue
//    private String title;
//
//    @ValueMapValue
//    private String path;
//
//    @OSGiService
//    private ProductService productService;
//
//    @ChildResource
//    private Resource products;
//
//    List<ProductObj> productLists = new ArrayList<>();
//
//    @SlingObject
//    private ResourceResolver resolver;
//
//    private  String string;
//
//    public String getString() {
//        return string;
//    }
//
//    public String getTitle() {
//        return title.toUpperCase();
//    }
//
//    @PostConstruct
//    protected void init() throws Exception{
//        String apiUrl = "https://v2.jokeapi.dev/joke/Any?idRange="+title;
//
//        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
//        connection.setRequestMethod("GET");
//
//        int statusCode = connection.getResponseCode();
//        if (statusCode == HttpURLConnection.HTTP_OK) {
//            String jsonResponse = IOUtils.toString(connection.getInputStream());
//            JSONObject data = null;
//            try {
//                data = new JSONObject(jsonResponse);
//               string= data.getString("joke");
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }
//        } else {
//            System.out.println("Nothing, Just to remove Warning");
//        }
//    }
//
//    public List<ProductObj> getProductLists() {
//        return productService.getProductLists();
//    }
}
