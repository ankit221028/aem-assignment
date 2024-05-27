package com.aem.assignment.core.services.impl;

import com.aem.assignment.core.models.ProductPojo;
import com.aem.assignment.core.services.ApiFetchService;
import com.aem.assignment.core.services.CreatePage;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component(service = ApiFetchService.class)
public class ApiFetchServiceImpl implements ApiFetchService {

    @Reference
    private CreatePage createPage;

    @Override
    public void fetchApi() throws IOException {
        int Product_Id = 1;
        String apiPath = "https://fakestoreapi.com/products/"+Product_Id;
        try(CloseableHttpClient closeableHttpClient = HttpClients.createDefault()){
            try{
                HttpGet httpGet = new HttpGet(apiPath);
                try(CloseableHttpResponse response =  closeableHttpClient.execute(httpGet)){
                    int statusCode = response.getStatusLine().getStatusCode();
                    if(statusCode >= 200 && statusCode <= 300){
                        String responseBody = EntityUtils.toString(response.getEntity());
                        createPage.create("/content/we-retail");

                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
