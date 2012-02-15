package nl.tudelft.ewi.st.atlantis.v1.services.tools.filters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.impl.handlers.BaseHandler;
import org.ebayopensource.turmeric.runtime.common.pipeline.MessageContext;


public class ClientRequestFilter extends Filter {

	private static Log logger = LogFactory.getLog(ClientRequestFilter.class);

	@Override
	public void invoke(MessageContext ctx) throws ServiceException {
		//logger.debug(contextToString(ctx));
		//String who = ctx.getProperty("who") == null ? "Unavailable" : ctx.getProperty("who").toString();
		
		
		//logger.debug("[CliReq] Calling: "+ctx.getOperationName()+" Request ID ("+ctx.getProcessingStage()+"): "+ctx.getRequestId());

		//ctx.setProperty("who", who);
		//ctx.getRequestMessage().setTransportHeader("who", super.getAdminName());
		
		
	}
	
	

}
