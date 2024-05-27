package com.aem.assignment.core.servlets;

import com.aem.assignment.core.services.CreatePage;
import com.day.cq.wcm.api.WCMException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component(service = Servlet.class,property = {
        ServletResolverConstants.SLING_SERVLET_METHODS+"="+HttpConstants.METHOD_POST,
        ServletResolverConstants.SLING_SERVLET_PATHS+"=/bin/DoSomething"
})
public class DoSomething extends SlingAllMethodsServlet {

    private String Coral_dropDown;
    private String demo_dropDown;

    @Reference
    CreatePage createPage;
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try (InputStream inputStream = request.getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            JSONObject jsonObject=new JSONObject(stringBuilder.toString());
            Coral_dropDown = jsonObject.getString("coralSelectValue");
            demo_dropDown = jsonObject.getString("htmlSelectValue");
            String finalURL = "/content/dam/"+Coral_dropDown+"/"+demo_dropDown;
            createPage.create(finalURL);

    } catch (JSONException | LoginException | WCMException e) {
            throw new RuntimeException(e);
        }
    }
}
