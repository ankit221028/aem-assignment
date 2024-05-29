package com.aem.assignment.core.servlets;

import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet that handles GET requests for searching products.
 * Searches for products within a specified path using a full-text search query parameter.
 */
@Component(service = Servlet.class, property = {
        Constants.SERVICE_DESCRIPTION + "=Product Search Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths=" + "/bin/productsearch"
})
public class SearchProductServlet extends SlingSafeMethodsServlet {

    private static final String PATH_PREFIX = "/var/commerce/products/we-retail";
    private static final String TYPE = "nt:unstructured";

    /**
     * Handles the HTTP GET method.
     *
     * @param request  the Sling HTTP request
     * @param response the Sling HTTP response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an input or output error occurs
     */
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        List<String> results = getResults(request);
        response.setContentType("application/json");
        String json = new Gson().toJson(results);
        response.getWriter().write(json);
    }

    /**
     * Retrieves search results based on the query parameter from the request.
     *
     * @param request the Sling HTTP request
     * @return a list of product titles matching the search query
     */
    private List<String> getResults(SlingHttpServletRequest request) {
        String queryParam = request.getParameter("query");
        List<String> results = new ArrayList<>();

        try {
            Session session = request.getResourceResolver().adaptTo(Session.class);
            if (session != null) {
                QueryManager queryManager = session.getWorkspace().getQueryManager();
                String sqlQuery = createSQLQuery(queryParam);
                Query query = queryManager.createQuery(sqlQuery, Query.JCR_SQL2);
                results = executeQuery(query);
            }
        } catch (RepositoryException e) {
            throw new RuntimeException("Error executing JCR query", e);
        }

        return results;
    }

    /**
     * Creates the JCR-SQL2 query string for searching products.
     *
     * @param queryParam the search query parameter
     * @return the JCR-SQL2 query string
     */
    private String createSQLQuery(String queryParam) {
        return "SELECT * FROM [" + TYPE + "] AS product WHERE" +
                " ISDESCENDANTNODE(product, '" + PATH_PREFIX + "') AND" +
                " CONTAINS(product.*, '" + queryParam + "')";
    }

    /**
     * Executes the given JCR query and retrieves the product titles.
     *
     * @param query the JCR query
     * @return a list of product titles
     * @throws RepositoryException if an error occurs while executing the query
     */
    private List<String> executeQuery(Query query) throws RepositoryException {
        List<String> results = new ArrayList<>();
        QueryResult result = query.execute();
        NodeIterator nodeIterator = result.getNodes();

        while (nodeIterator.hasNext()) {
            Node node = nodeIterator.nextNode();
            if (node.hasProperty("jcr:title")) {
                String title = node.getProperty("jcr:title").getString();
                results.add(title);
            }
        }
        return results;
    }
}
