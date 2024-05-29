package com.aem.assignment.core.servlets;

import com.adobe.cq.commerce.common.ValueMapDecorator;
import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.cq.wcm.api.Page;
import com.day.crx.JcrConstants;
import org.apache.commons.collections.iterators.TransformIterator;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Servlet to populate a dropdown with child pages of a specified path.
 */
@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Drop Down Servlet",
                "sling.servlet.paths=" + "/bin/dropDown"
        })
public class DropDownServlet extends SlingSafeMethodsServlet {

    /**
     * Handles GET requests to populate the dropdown with child pages.
     *
     * @param request  the Sling HTTP request
     * @param response the Sling HTTP response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an input or output error occurs
     */
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            String path = "/content/we-retail";
            ResourceResolver resolver = request.getResourceResolver();
            List<KeyValue> dropDownList = getDropDownList(resolver, path);

            if (!dropDownList.isEmpty()) {
                DataSource ds = createDataSource(resolver, dropDownList);
                request.setAttribute(DataSource.class.getName(), ds);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a list of key-value pairs representing child pages of the specified path.
     *
     * @param resolver the resource resolver
     * @param path     the path to retrieve child pages from
     * @return a list of key-value pairs
     */
    private List<KeyValue> getDropDownList(ResourceResolver resolver, String path) {
        List<KeyValue> dropDownList = new ArrayList<>();
        Resource resourcePath = resolver.getResource(path);

        if (resourcePath != null) {
            Page page = resourcePath.adaptTo(Page.class);
            if (page != null) {
                Iterator<Page> iterator = page.listChildren();
                iterator.forEachRemaining(pageChild -> {
                    String name = pageChild.getName();
                    String title = pageChild.getTitle();
                    dropDownList.add(new KeyValue(name, title));
                });
            }
        }

        return dropDownList;
    }

    /**
     * Creates a DataSource from a list of key-value pairs.
     *
     * @param resolver     the resource resolver
     * @param dropDownList the list of key-value pairs
     * @return a DataSource
     */
    private DataSource createDataSource(ResourceResolver resolver, List<KeyValue> dropDownList) {
        return new SimpleDataSource(
                new TransformIterator(

                        dropDownList.iterator(),
                         input -> {
                            KeyValue keyValue = (KeyValue) input;
                            ValueMap vm = new ValueMapDecorator(new HashMap<>());
                            vm.put("value", keyValue.getName());
                            vm.put("text", keyValue.getTitle());
                            return new ValueMapResource(
                                    resolver, new ResourceMetadata(),
                                    JcrConstants.NT_UNSTRUCTURED, vm
                            );
                        }
                )
        );
    }

    /**
     * A simple data structure to hold key-value pairs.
     */
    static class KeyValue {
        private String name;
        private String title;

        public KeyValue(String name, String title) {
            this.name = name;
            this.title = title;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
