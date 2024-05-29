package com.aem.assignment.core.services.impl;

import com.aem.assignment.core.CommonConstants;
import com.aem.assignment.core.utils.CommonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.aem.assignment.core.bean.ClientResponse;
import com.aem.assignment.core.entities.ProductEntity;
import com.aem.assignment.core.services.ProductDetailService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the ProductDetailService interface.
 * This service fetches product details from a specified API endpoint.
 */
@Component(service = {ProductDetailService.class})
public class ProductDetailServiceImpl implements ProductDetailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDetailServiceImpl.class);

    /**
     * Fetches the client response from the given URL.
     *
     * @param mainUrl the URL to fetch the client response from.
     * @return the client response.
     * @throws Exception if an error occurs while fetching the response.
     */
    public ClientResponse getProductDetails(String mainUrl) throws Exception {
        return CommonUtils.getClientResponse(CommonConstants.GET, mainUrl, null, null);
    }

    /**
     * Fetches product data from the given URL and converts it to a ProductEntity.
     *
     * @param mainURL the URL to fetch the product data from.
     * @return the product data as a ProductEntity.
     */
    @Override
    public ProductEntity getProductsData(String mainURL) {
        ProductEntity productEntity = new ProductEntity();

        try {
            ClientResponse clientResponse = getProductDetails(mainURL);
            LOGGER.debug("Client response from the REST service from the mainURL: {}", clientResponse);
            if (isSuccessfulResponse(clientResponse)) {
                JSONObject responseObj = new JSONObject(clientResponse.getData());
                ObjectMapper objectMapper = new ObjectMapper();
                productEntity = objectMapper.readValue(responseObj.toString(), ProductEntity.class);
                LOGGER.debug("Product Entity object: {}", productEntity);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to get the product: ", e);
        }
        return productEntity;
    }

    /**
     * Fetches a list of products from the given URL and converts it to a list of ProductEntity.
     *
     * @param mainUrl the URL to fetch the product list from.
     * @return the list of ProductEntity.
     */
    @Override
    public List<ProductEntity> getProductList(String mainUrl) {
        List<ProductEntity> productEntityList = new ArrayList<>();
        try {
            ClientResponse clientResponse = getProductDetails(mainUrl);
            LOGGER.debug("Client response from the REST service from the mainURL: {}", clientResponse);
            if (isSuccessfulResponse(clientResponse)) {
                String entityJson = clientResponse.getData();
                JSONArray jsonArray = new JSONArray(entityJson);
                ObjectMapper objectMapper = new ObjectMapper();
                productEntityList = objectMapper.readValue(
                        jsonArray.toString(),
                        new TypeReference<List<ProductEntity>>() {}
                );
                LOGGER.debug("Product Entity list fetched from API: {}", productEntityList);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to get the product list from API", e);
        }

        return productEntityList;
    }

    /**
     * Checks if the client response is successful.
     *
     * @param clientResponse the client response to check.
     * @return true if the response is successful, false otherwise.
     */
    private boolean isSuccessfulResponse(ClientResponse clientResponse) {
        return clientResponse != null && clientResponse.getStatusCode() == HttpServletResponse.SC_OK;
    }
}
