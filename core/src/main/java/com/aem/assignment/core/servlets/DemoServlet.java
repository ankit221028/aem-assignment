package com.aem.assignment.core.servlets;

import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import java.io.IOException;

/**
 * Practice servlet, to add property to the nodes using JCR api.
 */
@Component(service = Servlet.class,
        property = {
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/bin/custom"
        })
public class DemoServlet extends SlingAllMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
//        // Fetch the value of the "path" parameter from the request
        String path = request.getParameter("path");
        response.getWriter().write(request.getParameter("abc"));

       ResourceResolver resolver = request.getResourceResolver();
        Session session = resolver.adaptTo(Session.class);
        try { 
            Node node = session.getNode(path);
            node.setProperty("adjhf", "dfhdihf");
            session.save();
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
        String s="[{name=ankit}]";
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(s);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            response.getWriter().write(jsonObject.getString("name"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }
}
