package com.aem.assignment.core.servlets;

import com.google.gson.Gson;
import com.aem.assignment.core.entities.ProductEntity;
import com.aem.assignment.core.services.ProductDetailService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet that retrieves and returns a list of products sorted by some criteria.
 * This servlet handles GET requests and returns the sorted list of products as JSON.
 */
@Component(service = {Servlet.class},
        property = {
                "sling.servlet.paths=/bin/sortedProducts",
                "sling.servlet.methods=GET"
        })
public class SortProductServlet extends SlingSafeMethodsServlet {

    @Reference
    private ProductDetailService productDetailService;

    private static final String MAIN_URL = "https://fakestoreapi.com/products";
    private List<ProductEntity> productEntityList = new ArrayList<>();

    /**
     * Handles the HTTP GET method to fetch and return the sorted list of products.
     *
     * @param request  the Sling HTTP request
     * @param response the Sling HTTP response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an input or output error is detected when the servlet handles the GET request
     */
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        // Fetch the list of products from the product detail service
        productEntityList = productDetailService.getProductList(MAIN_URL);

        // Convert the product list to JSON and write it to the response
        String jsonResponse = new Gson().toJson(productEntityList);
        response.getWriter().write(jsonResponse);
    }
}
