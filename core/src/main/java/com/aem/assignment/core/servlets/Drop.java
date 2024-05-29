package com.aem.assignment.core.servlets;

import com.aem.assignment.core.services.CreatePage;
import com.day.cq.wcm.api.WCMException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
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

/**
 * Servlet to handle POST requests for creating pages based on user selection.
 */
@Component(service = Servlet.class, property = {
        ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_POST,
        ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/DoSomething"
})
public class Drop extends SlingAllMethodsServlet {

    @Reference
    private CreatePage createPage;

    /**
     * Handles HTTP POST requests to create a page based on the user selection.
     *
     * @param request  the Sling HTTP request
     * @param response the Sling HTTP response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an input or output error occurs
     */
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            JSONObject jsonObject = parseRequestBody(request);
            String coralDropDown = jsonObject.getString("coralSelectValue");
            String demoDropDown = jsonObject.getString("htmlSelectValue");
            String finalURL = buildFinalURL(coralDropDown, demoDropDown);
            createPage.create(finalURL);
        } catch (JSONException | LoginException | WCMException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses the request body to a JSON object.
     *
     * @param request the Sling HTTP request
     * @return the JSON object parsed from the request body
     * @throws IOException    if an input or output error occurs
     * @throws JSONException if a JSON parsing error occurs
     */
    private JSONObject parseRequestBody(SlingHttpServletRequest request) throws IOException, JSONException {
        try (InputStream inputStream = request.getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return new JSONObject(stringBuilder.toString());
        }
    }

    /**
     * Builds the final URL based on the provided dropdown values.
     *
     * @param coralDropDown the selected value from the Coral dropdown
     * @param demoDropDown  the selected value from the demo dropdown
     * @return the constructed final URL
     */
    private String buildFinalURL(String coralDropDown, String demoDropDown) {
        return "/content/dam/" + coralDropDown + "/" + demoDropDown;
    }
}
