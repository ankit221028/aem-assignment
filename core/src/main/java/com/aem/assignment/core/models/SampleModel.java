package com.aem.assignment.core.models;

import com.day.cq.wcm.api.*;
import com.day.cq.wcm.api.msm.Blueprint;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.*;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.*;

/**
 * Only a practice model for learning purpose.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SampleModel {
    @SlingObject
    private ResourceResolver resourceResolver;

    private final static Logger logger = LoggerFactory.getLogger(SampleModel.class);


//    @SlingObject
//    private SlingHttpServletRequest slingHttpServletRequest;
////    @SlingObject
//    Resource curr;
//
//    @SlingObject
//    RequestPathInfo requestPathInfo;



    @ValueMapValue
    private String directPath;
//    List<String> list = new ArrayList<>();
//    public List<String> getList() {
//        RequestPathInfo  requestPathInfo=slingHttpServletRequest.getRequestPathInfo();
//        list.add(requestPathInfo.getResourcePath());
//        list.add(curr.getPath());
//        return list;
//    }

    @OSGiService
    ResourceResolverFactory resourceResolverFactory;

    @PostConstruct
    protected void init() throws LoginException, PersistenceException, RepositoryException {
//        Map<String,Object> authMap = new HashMap<>();
//        authMap.put(ResourceResolverFactory.SUBSERVICE,"demosystemuser");
       // ResourceResolver resourceResolver = resourceResolverFactory.getResourceResolver(authMap);
//        Resource resource = resourceResolver.getResource(directPath);
//        ValueMap map = resource.adaptTo(ModifiableValueMap.class);
//        map.put("abcfc","dfihfuidh");
//        resourceResolver.commit();
//        Session session = resourceResolver.adaptTo(Session.class);
//        Node node = session.getNode(directPath);
//        node.setProperty("sdfdf","sdfsdfsdfsd");
//        session.save();
//        logger.info("Succefulllllllll");
//        logger.error("asdfasdfasdfasdfasdfasdfasdf");
        PageManager pageManager = new PageManager() {
            @Override
            public Page getPage(String s) {
                return null;
            }

            @Override
            public Page getContainingPage(Resource resource) {
                return null;
            }

            @Override
            public Page getContainingPage(String s) {
                return null;
            }

            @Override
            public Page create(String s, String s1, String s2, String s3) throws WCMException {
                return null;
            }

            @Override
            public Page create(String s, String s1, String s2, String s3, boolean b) throws WCMException {
                return null;
            }

            @Override
            public Page move(Page page, String s, String s1, boolean b, boolean b1, String[] strings) throws WCMException {
                return null;
            }

            @Override
            public Page move(Page page, String s, String s1, boolean b, boolean b1, String[] strings, String[] strings1) throws WCMException {
                return null;
            }

            @Override
            public Resource move(Resource resource, String s, String s1, boolean b, boolean b1, String[] strings) throws WCMException {
                return null;
            }

            @Override
            public Resource move(Resource resource, String s, String s1, boolean b, boolean b1, String[] strings, String[] strings1) throws WCMException {
                return null;
            }

            @Override
            public Resource copy(CopyOptions copyOptions) throws WCMException {
                return null;
            }

            @Override
            public Page copy(Page page, String s, String s1, boolean b, boolean b1) throws WCMException {
                return null;
            }

            @Override
            public Page copy(Page page, String s, String s1, boolean b, boolean b1, boolean b2) throws WCMException {
                return null;
            }

            @Override
            public Resource copy(Resource resource, String s, String s1, boolean b, boolean b1) throws WCMException {
                return null;
            }

            @Override
            public Resource copy(Resource resource, String s, String s1, boolean b, boolean b1, boolean b2) throws WCMException {
                return null;
            }

            @Override
            public void delete(Page page, boolean b) throws WCMException {

            }

            @Override
            public void delete(Page page, boolean b, boolean b1) throws WCMException {

            }

            @Override
            public void delete(Resource resource, boolean b) throws WCMException {

            }

            @Override
            public void delete(Resource resource, boolean b, boolean b1) throws WCMException {

            }

            @Override
            public void order(Page page, String s) throws WCMException {

            }

            @Override
            public void order(Page page, String s, boolean b) throws WCMException {

            }

            @Override
            public void order(Resource resource, String s) throws WCMException {

            }

            @Override
            public void order(Resource resource, String s, boolean b) throws WCMException {

            }

            @Override
            public Template getTemplate(String s) {
                return null;
            }

            @Override
            public Collection<Template> getTemplates(String s) {
                return null;
            }

            @Override
            public Collection<Blueprint> getBlueprints(String s) {
                return null;
            }

            @Override
            public Revision createRevision(Page page) throws WCMException {
                return null;
            }

            @Override
            public Revision createRevision(Page page, String s, String s1) throws WCMException {
                return null;
            }

            @Override
            public Collection<Revision> getRevisions(String s, Calendar calendar) throws WCMException {
                return null;
            }

            @Override
            public Collection<Revision> getRevisions(String s, Calendar calendar, boolean b) throws WCMException {
                return null;
            }

            @Override
            public Collection<Revision> getChildRevisions(String s, Calendar calendar) throws WCMException {
                return null;
            }

            @Override
            public Collection<Revision> getChildRevisions(String s, Calendar calendar, boolean b) throws WCMException {
                return null;
            }

            @Override
            public Collection<Revision> getChildRevisions(String s, String s1, Calendar calendar) throws WCMException {
                return null;
            }

            @Override
            public Page restore(String s, String s1) throws WCMException {
                return null;
            }

            @Override
            public Page restoreTree(String s, Calendar calendar) throws WCMException {
                return null;
            }

            @Override
            public Page restoreTree(String s, Calendar calendar, boolean b) throws WCMException {
                return null;
            }

            @Override
            public void touch(Node node, boolean b, Calendar calendar, boolean b1) throws WCMException {

            }
        };

    }
}
