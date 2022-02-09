package com.i3market.sdk.ri.common_services.data.discovery;

import com.i3m.api.ApiClient;
import com.i3m.api.ApiException;
import com.i3m.api.ApiResponse;
import com.i3m.api.Configuration;
import com.i3m.api.backplane.RegistrationOfferingApi;
import com.i3m.model.backplane.Offerings;
import com.i3m.model.backplane.OfferingsList;
import com.i3market.sdk.ri.execution_patterns.SdkRiConstants;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RetrieveTotalOfferingAndOfferingList {

    private static final Logger _log = LoggerFactory.getLogger(RetrieveTotalOfferingAndOfferingList.class);

    public  ApiResponse<List<Offerings>> getTotalOfferingAndOfferingList(
            String providerId, String category,
            Integer page, Integer size, String sortBy, String orderBy) throws ApiException {

        String backPlanePath = SdkRiConstants.BACKPLANE_ENDPOINT;

        ApiClient apiClient = Configuration.getDefaultApiClient();

        apiClient.setBasePath(backPlanePath);

        apiClient.setServerIndex(null);

        RegistrationOfferingApi registrationOfferingApi = new RegistrationOfferingApi();
        
        return registrationOfferingApi.getTotalOfferingAndOfferingListUsingGETWithHttpInfo(providerId, category, page, size, orderBy, sortBy);
    }

}