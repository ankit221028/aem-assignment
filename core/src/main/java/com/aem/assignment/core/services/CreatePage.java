package com.aem.assignment.core.services;

import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.foundation.model.Page;
import org.apache.sling.api.resource.LoginException;
/**
 * An Interface for Page creation service which creates page using PageManager
 */
public interface CreatePage {
    void create(String path) throws LoginException, WCMException;
}
