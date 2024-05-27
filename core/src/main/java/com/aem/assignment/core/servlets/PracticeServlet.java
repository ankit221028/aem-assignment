package com.aem.assignment.core.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.paths=" + "/bin/servlet"
        }
)
public class PracticeServlet extends SlingAllMethodsServlet {

//    @Override
//    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
//        response.getWriter().write("hello");
//    }

    // Uncomment this method to handle POST requests

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
//        String string1 = null;
//        try {
//            InputStream stream = request.getInputStream();
//            byte[] bytes = new byte[1024];
//            int bytesRead;
//            StringBuilder stringBuilder = new StringBuilder(); // Instantiate the StringBuilder object
//            while ((bytesRead = stream.read(bytes)) != -1) {
//                stringBuilder.append(new String(bytes, 0, bytesRead));
//            }
//            stream.close();
//            String string = stringBuilder.toString();
//            JSONObject jsonObject = new JSONObject(string);
//            ObjectMapper objectMapper=new ObjectMapper();
//           Obj user= objectMapper.readValue(string, Obj.class);
//           response.getWriter().write("asdf\n");
//           response.getWriter().write(user.getName()+"\n");
//            response.getWriter().write(user.getPassword());
//        } catch (Exception e) {
//            response.getWriter().write("successfully");
//        }
        try {

            String name = request.getParameter("name");
            String password = request.getParameter("password");


            if (name != null && password != null) {
                // Create a JSON object to hold the parameters
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", name);
                jsonObject.put("password", password);

                // Write JSON response
                response.setContentType("application/json");
                response.getWriter().write(jsonObject.toString());
            } else {
                // Parameters not found, send an error response
                response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Error: Name and/or password parameter missing in the request.");
            }
        } catch (JSONException e) {
            // JSON error occurred, send an error response
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error: Failed to create JSON response.");
        }
    }

}
