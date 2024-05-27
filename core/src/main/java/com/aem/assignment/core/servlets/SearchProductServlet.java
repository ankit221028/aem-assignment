package com.aem.assignment.core.servlets;

import com.day.cq.search.Predicate;
import com.day.cq.search.QueryBuilder;
import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.*;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(service = Servlet.class, property = {
        Constants.SERVICE_DESCRIPTION + "=Product Search Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths=" + "/bin/productsearch"
})
public class SearchProductServlet extends SlingSafeMethodsServlet {

    public static final String PREDICATE_FULLTEXT = "fulltext";
    public static final String PATH = "path";

    public static final String PATH_PREFIX = "/var/commerce/products/we-retail";
    public static final String TYPE = "nt:unstructured";

    @Reference
    QueryBuilder queryBuilder;

    @Override
    protected void doGet( SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        List<String> results = getResults(request);

        response.setContentType("application/json");

        // Convert results to JSON
        Gson gson = new Gson();
        String json = gson.toJson(results);
        response.getWriter().write(json);
        // Write JSON to response
        response.getWriter().write(json);
    }

    private List<String> getResults(SlingHttpServletRequest request){
        String queryParam = request.getParameter("query");

        List<String> results = new ArrayList<>();


        try {
            Session session = request.getResourceResolver().adaptTo(Session.class);
            QueryManager queryManager = session.getWorkspace().getQueryManager();

            String sqlQuery = "SELECT * FROM [" + TYPE + "] AS product WHERE" +
                    " ISDESCENDANTNODE(product, '" + PATH_PREFIX + "') AND" +
                    " CONTAINS(product.*, '" + queryParam + "')";

            Query query = queryManager.createQuery(sqlQuery, "JCR-SQL2");
            QueryResult result = query.execute();

            NodeIterator nodeIterator = result.getNodes();
            while (nodeIterator.hasNext()) {
                Node node = nodeIterator.nextNode();
                String title = node.getProperty("jcr:title").getString();
                results.add(title);

            }

        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }

        return results;

    }
}