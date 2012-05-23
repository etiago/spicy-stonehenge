package nl.tudelft.ewi.st.atlantis.v1.services.tools.filters;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.tudelft.ewi.st.atlantis.tudelft.logunits.LogPair;
import nl.tudelft.ewi.st.atlantis.tudelft.logunits.LogTrace;
import nl.tudelft.ewi.st.atlantis.tudelft.logunits.LogUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.impl.handlers.BaseHandler;
import org.ebayopensource.turmeric.runtime.common.pipeline.MessageContext;


public class ServerRequestFilter extends BaseHandler {
	private static Log logger = LogFactory.getLog(ServerRequestFilter.class);

	@Override
	public void invoke(MessageContext ctx) throws ServiceException {
		
//		//logger.debug(contextToString(ctx));
//		if(ctx.getProperty("tud") != null){
//			logger.debug("[REQ] Service: "+ ctx.getAdminName()+" | TUD: "+ctx.getProperty("tud"));
//			
//		} else {
//			ctx.setProperty("tud", ctx.getAdminName());
//			logger.debug("[REQ] Service: "+ ctx.getAdminName()+" | SET TUD!");
//		}
		
		//String who = ctx.getRequestMessage().getTransportHeader("who") == null ? "Unavailable" : ctx.getRequestMessage().getTransportHeader("who");
		
		//logger.debug("[SerReq] "+ctx.getAdminName()+" being called... From: "+who+" Request ID ("+ctx.getProcessingStage()+"): "+ctx.getRequestId());
		System.out.println("------------- Service: "+ctx.getServiceId()+" with request id: "+ ctx.getRequestId()+" operation name: "+ ctx.getOperationName());
		ctx.setRequestId(ctx.getRequestId()+ctx.getOperationName()+"#", "moo");

		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver");
			Connection c = DriverManager.getConnection("jdbc:derby://localhost:1527/logdb");

			// Get ctx.getOperationName() and add it to db 
			
			String fullID = ctx.getRequestId();
			
			String uuid = fullID.substring(0, fullID.indexOf("!"));
			LogTrace trace = getRequestPairs(fullID);
			
			String sql = "INSERT INTO pair (requestid, timestamp, consumer, service, consumer_ip, service_ip, consumer_method, service_method) VALUES (?,CURRENT_TIMESTAMP,?,?,?,?,?,?)";
			String cleanup = "DELETE FROM pair WHERE requestid = ?";
			
			PreparedStatement st = c.prepareStatement(sql);
			PreparedStatement stClean = c.prepareStatement(cleanup);
			
			stClean.setString(1, uuid);
			stClean.execute();
			stClean.clearParameters();
			
			System.out.println("preparing "+trace.getRequestID()+" with a trace of "+trace.getTrace().size());
			for(LogPair pair : trace.getTrace()) {
				st.setString(1, uuid);
				st.setString(2, pair.getConsumer().getServiceName());
				st.setString(3, pair.getService().getServiceName());
				st.setString(4, pair.getConsumer().getIP());
				st.setString(5, pair.getService().getIP());
				st.setString(6, pair.getConsumer().getMethod());
				st.setString(7, pair.getService().getMethod());
				
				st.execute();
				st.clearParameters();
			}
			
			
		} catch (Exception e) {
			//throw new ServiceException(e.getMessage());
			e.printStackTrace();
		}
		
		System.out.println("CTX: "+ctx.getRequestId());
	}
	
	private LogTrace getRequestPairs(String in) {
		
		int boundary = in.indexOf("!");
		String id = in.substring(0, boundary);
		
		in = in.substring(boundary+1);
		
		
		LogTrace t = new LogTrace(id);
		
		String[] parts = in.split("#!");
		
		for(int i=0;i<parts.length;i++) {
			String[] invokerData = parts[i].split("!");
			
			LogUnit invoker = new LogUnit(invokerData[0], invokerData[1], invokerData[2].substring(1));
			
			if (i+1 < parts.length) {
				String[] multipleInvoked = parts[i+1].split("#\\]!");				
				
				for(String s : multipleInvoked) {
					s = s.endsWith("#") ? s.substring(0, s.length()-1) : s;
					
					String[] invokedData = s.split("!");

					
					LogUnit invoked = new LogUnit(invokedData[0], invokedData[1], invokedData[2].substring(1));
					
					LogPair pair = new LogPair(invoker, invoked);
					
					t.addPair(pair);
				}
			}
			 
		}
		
//		System.out.println(t.getRequestID());
//		for(LogPair p : t.getTrace()) {
//			LogUnit consumer = p.getConsumer();
//			LogUnit service = p.getService();
//			
//			System.out.println(consumer.getServiceName()+"("+consumer.getMethod()+") => "
//								+service.getServiceName()+"("+service.getMethod()+")");		
//		}
		
		return t;
	}

}
