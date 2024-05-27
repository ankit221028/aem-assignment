package com.aem.assignment.core.models;

import com.aem.assignment.core.servlets.Obj;
import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = {SlingHttpServletRequest.class},defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TestingModel{

    @ScriptVariable
    SlingHttpServletRequest request;

    @ScriptVariable
    Page currentPage;

    List<ProductPojo> list = new ArrayList<>();
    String strings;
    @PostConstruct
    void  init() throws  Exception{
        String[] string = (String[]) currentPage.getProperties().get("id");
        String str = string[1];
        String str1="https://fakestoreapi.com/products/"+str;
        HttpURLConnection connection = (HttpURLConnection) new URL(str1).openConnection();
        connection.setRequestMethod("GET");
        ProductPojo obj = new ProductPojo();
        int statusCode = connection.getResponseCode();
        if (statusCode == HttpURLConnection.HTTP_OK) {
            String jsonResponse = IOUtils.toString(connection.getInputStream());
            JSONObject data = null;
            try {
                data = new JSONObject(jsonResponse);
                obj.setImagePath(data.getString("image"));
                obj.setDescription(data.getString("description"));
                obj.setId(data.getInt("id"));
                obj.setTitle(data.getString("title"));
                list.add(obj);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Nothing, Just to remove Warning");
        }
    }

    public List<ProductPojo> getList() {
        return list;
    }
}
