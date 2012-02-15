package nl.tudelft.ewi.st.atlantis.v1.services.tools.filters;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.impl.handlers.BaseHandler;
import org.ebayopensource.turmeric.runtime.common.pipeline.MessageContext;


public class ServerResponseFilter extends Filter {
	private static Log logger = LogFactory.getLog(ServerResponseFilter.class);

	@Override
	public void invoke(MessageContext ctx) throws ServiceException {
//		//logger.debug(contextToString(ctx));
//		if(ctx.getProperty("tud") != null){
//			logger.debug("[RES] Service: "+ ctx.getAdminName()+" | TUD: "+ctx.getProperty("tud"));
//			
//		} else {
//			ctx.setProperty("tud", ctx.getAdminName());
//			logger.debug("[RES] Service: "+ ctx.getAdminName()+" | SET TUD!");
//		}
		//System.out.println("CTX: "+ctx.getRequestId());
	}

}
