package com.aem.assignment.core.servlets;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class,
property = {
        "sling.servlet.paths="+"/bin/weather",
        "sling.servlet.methods="+ HttpConstants.METHOD_GET
})
public class Weather extends SlingSafeMethodsServlet {
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String Api_Key = "8751a3ccb0c0a371b6b3b89c94d52d0c";
        String latitude = request.getParameter("latitude");
        String longitude = request.getParameter("longitude");
        String apiPath = "https://api.openweathermap.org/data/2.5/weather?lat="+ latitude +"&lon="+longitude+"&appid="+Api_Key;
        try(CloseableHttpClient closeableHttpClient = HttpClients.createDefault()){
            try{
                HttpGet httpGet = new HttpGet(apiPath);
                try(CloseableHttpResponse respons =  closeableHttpClient.execute(httpGet)){
                    int statusCode = respons.getStatusLine().getStatusCode();
                    if(statusCode >= 200 && statusCode <= 300){
                        String responseBody = EntityUtils.toString(respons.getEntity());
                        JSONObject jsonObject = new JSONObject(responseBody);
                        //response.getWriter().write(apiPath+ "\n");
                        JSONObject temp = jsonObject.getJSONObject("main");
                        JSONArray weatherDescription = jsonObject.getJSONArray("weather");
                        JSONObject obj = (JSONObject)weatherDescription.get(0);


                        JSONObject jsonResponse = new JSONObject();
                        jsonResponse.put("temperature", temp.getDouble("temp")); // Assuming temperature is in "temp" field
                        jsonResponse.put("description", obj.getString("description")); // Assuming description is in "description" field

                        response.setContentType("application/json");

                        // Write JSON response to the output stream
                        response.getWriter().write(jsonResponse.toString());

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
