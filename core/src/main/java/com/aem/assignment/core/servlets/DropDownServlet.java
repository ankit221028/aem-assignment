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
import   com.adobe.granite.ui.components.ds.DataSource.*;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Component(service = Servlet.class,
property = {
        Constants.SERVICE_DESCRIPTION+  "=Drop Down Servlet",
        "sling.servlet.paths=" + "/bin/dropDown"
})
public class DropDownServlet extends SlingSafeMethodsServlet {
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

        try {
            String path = "/content/we-retail";
            ResourceResolver resolver = request.getResourceResolver();
            List<KeyValue> dropDownList = new ArrayList<>();

            Resource resourcePath = resolver.getResource(path);
            if (resourcePath != null) {
                Page page = resourcePath.adaptTo(Page.class);
                if (page != null) {
                    Iterator<Page> iterator = page.listChildren();
                    List<Page> list = new ArrayList<>();
                    iterator.forEachRemaining(list::add);
                    list.forEach(pageChild -> {
                        String name = pageChild.getName();
                        String title = pageChild.getTitle();
                        dropDownList.add(new KeyValue(name, title));
                    });
                    @SuppressWarnings("unchecked")
                    DataSource ds =
                            new SimpleDataSource(
                                    new TransformIterator(
                                            dropDownList.iterator(),
                                            input -> {
                                                KeyValue keyValue = (KeyValue) input;
                                                ValueMap vm = new ValueMapDecorator(new HashMap<>());
                                                vm.put("value",keyValue.name);
                                                vm.put("text",keyValue.title);
                                                return new ValueMapResource(
                                                        resolver, new ResourceMetadata(),
                                                        JcrConstants.NT_UNSTRUCTURED,vm
                                                );
                                            }
                                    )
                            );
                    request.setAttribute(DataSource.class.getName(),ds) ;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
}
    static class KeyValue{
        String name;
        String title;

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

        public KeyValue(String name, String title) {
            this.name = name;
            this.title = title;
        }
    }
}
