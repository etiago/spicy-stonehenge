
package nl.tudelft.ewi.st.atlantis.tudelft.v1.services.configurationservice.impl;

import nl.tudelft.ewi.st.atlantis.tudelft.util.TypeFactory;
import nl.tudelft.ewi.st.atlantis.tudelft.v1.services.GetBSConfigRequest;
import nl.tudelft.ewi.st.atlantis.tudelft.v1.services.GetBSConfigResponse;
import nl.tudelft.ewi.st.atlantis.tudelft.v1.services.GetBSLocationsRequest;
import nl.tudelft.ewi.st.atlantis.tudelft.v1.services.GetBSLocationsResponse;
import nl.tudelft.ewi.st.atlantis.tudelft.v1.services.GetClientConfigRequest;
import nl.tudelft.ewi.st.atlantis.tudelft.v1.services.GetClientConfigResponse;
import nl.tudelft.ewi.st.atlantis.tudelft.v1.services.GetESLocationRequest;
import nl.tudelft.ewi.st.atlantis.tudelft.v1.services.GetESLocationsResponse;
import nl.tudelft.ewi.st.atlantis.tudelft.v1.services.GetOPSConfigRequest;
import nl.tudelft.ewi.st.atlantis.tudelft.v1.services.GetOPSConfigResponse;
import nl.tudelft.ewi.st.atlantis.tudelft.v1.services.GetOPSLocationsRequest;
import nl.tudelft.ewi.st.atlantis.tudelft.v1.services.GetOPSLocationsResponse;
import nl.tudelft.ewi.st.atlantis.tudelft.v1.services.GetQSLocationsRequest;
import nl.tudelft.ewi.st.atlantis.tudelft.v1.services.GetQSLocationsResponse;
import nl.tudelft.ewi.st.atlantis.tudelft.v1.services.SetBSToOPSRequest;
import nl.tudelft.ewi.st.atlantis.tudelft.v1.services.SetBSToOPSResponse;
import nl.tudelft.ewi.st.atlantis.tudelft.v1.services.SetClientToBSRequest;
import nl.tudelft.ewi.st.atlantis.tudelft.v1.services.SetClientToBSResponse;
import nl.tudelft.ewi.st.atlantis.tudelft.v1.services.SetServiceLocationRequest;
import nl.tudelft.ewi.st.atlantis.tudelft.v1.services.SetServiceLocationResponse;
import nl.tudelft.ewi.st.atlantis.tudelft.v1.services.configurationservice.ConfigurationServiceV1;
import nl.tudelft.stocktrader.dal.ConfigServiceDAO;
import nl.tudelft.stocktrader.dal.DAOFactory;
import nl.tudelft.stocktrader.dal.configservice.BSConfig;
import nl.tudelft.stocktrader.dal.configservice.ClientConfig;
import nl.tudelft.stocktrader.dal.configservice.OPSConfig;

public class ConfigurationServiceV1Impl
    implements ConfigurationServiceV1
    {

    	private final DAOFactory factory;

    	public ConfigurationServiceV1Impl() {
    		factory = DAOFactory.getFactory();
    	}
    	
        public ConfigServiceDAO getConfigServiceDAO() {
            return factory.getConfigServiceDAO();
        }
        
        public GetClientConfigResponse getClientConfig(GetClientConfigRequest request) {
            String clientName = request.getClientName();
            ClientConfig config = getConfigServiceDAO().getClientConfig(clientName);
            
            return TypeFactory.toGetClientConfigResponse(config);
        }

        public SetServiceLocationResponse setServiceLocation(SetServiceLocationRequest request) {
        	String serviceName = request.getLocation().getServiceName();
            String serviceUrl = request.getLocation().getServiceURL();
            Boolean isSec = request.getLocation().isSec();
            
            if (getConfigServiceDAO().setServiceLocation(serviceName, serviceUrl, isSec)) {
                return new SetServiceLocationResponse();
            }
            return null;
        }

        public GetBSConfigResponse getBSConfig(GetBSConfigRequest request) {
            BSConfig configResponse = getConfigServiceDAO().getBSConfig(request.getBSName());
            
            return TypeFactory.toGetBSConfigResponse(configResponse);
        }

        public GetOPSConfigResponse getOPSConfig(GetOPSConfigRequest request) {
        	OPSConfig response = getConfigServiceDAO().getOPSConfig(request.getOPSName());
            
            return TypeFactory.toGetOPSConfigResponse(response);
        }

        public SetBSToOPSResponse setBSToOPS(SetBSToOPSRequest request) {
            if (getConfigServiceDAO().setBSToOPS(request.getBs(), request.getOps())) {
                return new SetBSToOPSResponse();
            }
            return null;
        }

        public SetClientToBSResponse setClientToBS(SetClientToBSRequest request) {
            if (getConfigServiceDAO().setClientToBS(request.getClient(), request.getBs())) {
                return new SetClientToBSResponse();
            }
            return null;
        }

        public GetBSLocationsResponse getBSLocations(GetBSLocationsRequest request) {
        	GetBSLocationsResponse getBSLocationsResponse = new GetBSLocationsResponse();
        	getBSLocationsResponse.getLocations()
        				.addAll(TypeFactory.toRemoteServiceLocationList(
        						getConfigServiceDAO().getBSLocations()));
        	return getBSLocationsResponse;
        }

		public GetOPSLocationsResponse getOPSLocations(GetOPSLocationsRequest getOPSLocationsRequest) {
			GetOPSLocationsResponse getOPSLocationsResponse = new GetOPSLocationsResponse();
			
			getOPSLocationsResponse.getLocations()
						.addAll(TypeFactory.toRemoteServiceLocationList(
								getConfigServiceDAO().getOPSLocations()));
			
			return getOPSLocationsResponse;	
		}

		@Override
		public GetESLocationsResponse getESLocations(
				GetESLocationRequest getESLocationsRequest) {
			GetESLocationsResponse getESLocationsResponse = new GetESLocationsResponse();
			getESLocationsResponse.getLocations()
        				.addAll(TypeFactory.toRemoteServiceLocationList(
        						getConfigServiceDAO().getESLocations()));
        	return getESLocationsResponse;
		}

		public GetQSLocationsResponse getQSLocations(GetQSLocationsRequest getQSLocationsRequest) {
			GetQSLocationsResponse response = new GetQSLocationsResponse();
			
			response.getLocations().addAll(TypeFactory.toRemoteServiceLocationList(
								getConfigServiceDAO().getQSLocations()));
			
			return response;
		}
}