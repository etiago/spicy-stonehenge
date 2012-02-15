package nl.tudelft.ewi.st.atlantis.v1.services.tools.filters;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.impl.handlers.BaseHandler;
import org.ebayopensource.turmeric.runtime.common.pipeline.MessageContext;


public class ClientResponseFilter extends Filter  {
	private static Log logger = LogFactory.getLog(ClientResponseFilter.class);

	@Override
	public void invoke(MessageContext ctx) throws ServiceException {
		//logger.debug(contextToString(ctx));

	}

}
