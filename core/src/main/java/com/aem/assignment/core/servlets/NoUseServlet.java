package com.aem.assignment.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.mongodb.connection.Server;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(service = Servlet.class,
property = {
        "sling.servlet.methods="+ HttpConstants.METHOD_GET,
        "sling.servlet.paths="+"/bin/nouse"
})
public class NoUseServlet extends SlingSafeMethodsServlet {

    @Reference
    private QueryBuilder queryBuilder;

    @SlingObject
    private ResourceResolverFactory resourceResolverFactory;

    public NoUseServlet() throws LoginException {
    }

    private ResourceResolver getResourceResolver(ResourceResolverFactory resourceResolverFactory) throws LoginException {
        Map<String, Object> authMap = new HashMap<>();
        authMap.put(ResourceResolverFactory.SUBSERVICE,"demosystemuser");
        return resourceResolverFactory.getResourceResolver(authMap);
    }
    private static Map<String, String> getQuery(String path){
        Map<String, String> query = new HashMap<>();
        query.put("type", "cq:Page");
        query.put("path",path);
        return query;
    }


    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        //Iterate query results
        response.getWriter().write("hello");
        ResourceResolver resolver = request.getResourceResolver();



        Session session = resolver.adaptTo(Session.class);
        Query query = queryBuilder.createQuery(PredicateGroup.create(getQuery("/content/we-retail")),session);

        SearchResult result = query.getResult();
        List<Hit> list = result.getHits();

        for (Hit hit : list) {
            response.getWriter().write(hit.toString()+"\n");
        }

    }
}
