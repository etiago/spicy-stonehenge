package nl.tudelft.ewi.st.atlantis.v1.services.tools.filters;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.impl.handlers.BaseHandler;
import org.ebayopensource.turmeric.runtime.common.pipeline.MessageContext;


public class ClientRequestFilter extends BaseHandler {

	@Override
	public void invoke(MessageContext ctx) throws ServiceException {
		System.out.println("ClientRequest: "+ctx.getRequestId());
	}

}
