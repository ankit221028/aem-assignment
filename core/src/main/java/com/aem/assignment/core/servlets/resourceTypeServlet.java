package com.aem.assignment.core.servlets;


import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;

import javax.json.Json;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(service = Servlet.class)
@SlingServletResourceTypes(
        resourceTypes = "training/components/democomponent",
        methods= HttpConstants.METHOD_GET,
        extensions = "json"
)
@ServiceDescription("Simple Sample Servlet")
public class resourceTypeServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        Map<String,String> resMap = new HashMap<>();
        resMap.put("abc","xyz");
        resMap.put("asdf","asdfasdf");
        Gson gson = new Gson();
        String json = gson.toJson(resMap);
        response.getWriter().write(new Gson().toJson(resMap));
        
    }
}
