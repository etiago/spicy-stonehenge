package nl.tudelft.ewi.st.atlantis.tudelft.logdump;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.tudelft.ewi.st.atlantis.tudelft.logdump.json.JSONArray;
import nl.tudelft.ewi.st.atlantis.tudelft.logdump.json.JSONException;
import nl.tudelft.ewi.st.atlantis.tudelft.logdump.json.JSONObject;

public class LogDumper extends HttpServlet {
	
//	public static void main(String[] args) throws Exception {
//		LogDumper l = new LogDumper();
//		
//		Class.forName("org.apache.derby.jdbc.ClientDriver");
//		Connection c = DriverManager.getConnection("jdbc:derby://localhost:1527/logdb");
//		
//		Calendar farFuture = Calendar.getInstance();
//		farFuture.add(Calendar.YEAR, 100);
//		
//		String timeEnd = null;
//		
//		Timestamp tStart = new Timestamp(Long.valueOf(0));
//		Timestamp tEnd = timeEnd == null ? new Timestamp(farFuture.getTimeInMillis())
//										 : new Timestamp(Long.valueOf(timeEnd));
//		
//		String s = l.getDynamicInfo(c, tStart, tEnd);
//		System.out.println(s);
//	}
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//super.doGet(req, resp);
		resp.setContentType("application/json");
		
		String staticInfo = req.getParameter("static");
		String timeStart = req.getParameter("timestart");
		String timeEnd = req.getParameter("timeend");
		String id = req.getParameter("id");
		
		try {
			if (timeStart == null && (id == null && staticInfo == null)) return;

			Class.forName("org.apache.derby.jdbc.ClientDriver");
			Connection c = DriverManager.getConnection("jdbc:derby://localhost:1527/logdb");
			String output = null;

			if (id != null && staticInfo != null) {
				output = getStaticInfo(c, Integer.parseInt(id));
			} else {
				Calendar farFuture = Calendar.getInstance();
				farFuture.add(Calendar.YEAR, 100);
				
				Timestamp tStart = new Timestamp(Long.valueOf(timeStart));
				Timestamp tEnd = timeEnd == null ? new Timestamp(farFuture.getTimeInMillis())
												 : new Timestamp(Long.valueOf(timeEnd));
				
				if (staticInfo == null) {
					output = getDynamicInfo(c, tStart, tEnd);
				} else if (staticInfo.equals("true")) {
					output = getStaticInfo(c, tStart, tEnd);
				} else {
					return;
				} 
			}
			
			resp.getWriter().print(output);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e.getCause());
		}
		
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
	private String getStaticInfo(Connection c, Timestamp tStart, Timestamp tEnd) throws SQLException, JSONException {
		String sql = "SELECT si.id, json_data, tstamp " +
					 "FROM staticinfo as si INNER JOIN staticinfo_time as sit ON si.id = sit.staticinfo_id "+
					 "WHERE tstamp >= ? AND tstamp <= ?";
		
		PreparedStatement st = c.prepareStatement(sql);
		
		st.setTimestamp(1, tStart);
		st.setTimestamp(2, tEnd);
		
		ResultSet rs = st.executeQuery();
		
		JSONObject obj = new JSONObject();
		
		while(rs.next()) {
			obj.append(String.valueOf(rs.getTimestamp("tstamp").getTime()), new JSONArray(rs.getString("json_data")).put(rs.getInt("id")));
		}
		
		return obj.toString();
	}
	
	private String getStaticInfo(Connection c, int id) throws SQLException, JSONException {
		String sql = "SELECT json_data " +
		 "FROM staticinfo "+
		 "WHERE id = ?";

		PreparedStatement st = c.prepareStatement(sql);
		
		st.setInt(1, id);
		
		ResultSet rs = st.executeQuery();
		
		if (!rs.next()) return "";
		
		JSONObject obj = new JSONObject();
		
		//while(rs.next()) {
		obj.append(id+"", new JSONArray(rs.getString("json_data")));
		//}
		
		return obj.toString();
	}
	
	private String getDynamicInfo(Connection c, Timestamp tStart, Timestamp tEnd) throws SQLException, JSONException {
		//System.out.println("Start:"+tStart.getTime()+" End:"+tEnd.getTime());
		// Old sql
		//String sql = "SELECT requestid, consumer, service, consumer_ip, service_ip FROM pair WHERE timestamp >= ?";
		String sql = "select DISTINCT CONSUMER,consumer_method, SERVICE,service_method, count(*) as CNT " +
					 "from APP.PAIR WHERE timestamp >= ? AND timestamp <= ? "+
					 "group by consumer, service, consumer_method, service_method";
		
		String sqlStats = "select sum(cnt) from (select DISTINCT CONSUMER,SERVICE, count(*) as CNT "+
						  "from APP.PAIR WHERE timestamp >= ? AND timestamp <= ? group by consumer,service) as t";
		
		PreparedStatement st = c.prepareStatement(sql);
		
		st.setTimestamp(1, tStart);
		st.setTimestamp(2, tEnd);
		
		ResultSet rs = st.executeQuery();
		JSONArray data = convertResultSetToJSON(rs);
		
		st = c.prepareStatement(sqlStats);
		st.setTimestamp(1, tStart);
		st.setTimestamp(2, tEnd);
		
		ResultSet stats = st.executeQuery();
		stats.next();
		
		JSONObject statsObj = new JSONObject();
		statsObj.put("totalCalls", stats.getInt(1));
		
		JSONObject obj = new JSONObject();
		obj.put("data", data);
		obj.put("stats", statsObj);
		
		return obj.toString();
	}
	
	public JSONArray convertResultSetToJSON(java.sql.ResultSet rs){

        JSONArray json = new JSONArray();

        try {

             java.sql.ResultSetMetaData rsmd = rs.getMetaData();

             while(rs.next()){
                 int numColumns = rsmd.getColumnCount();
                 JSONObject obj = new JSONObject();

                 for (int i=1; i<numColumns+1; i++) {

                     String column_name = rsmd.getColumnName(i);

                     if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
                         obj.put(column_name, rs.getArray(column_name));
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
                         obj.put(column_name, rs.getInt(column_name));
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
                         obj.put(column_name, rs.getBoolean(column_name));
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
                         obj.put(column_name, rs.getBlob(column_name));
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
                         obj.put(column_name, rs.getDouble(column_name)); 
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
                         obj.put(column_name, rs.getFloat(column_name));
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
                         obj.put(column_name, rs.getInt(column_name));
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.NVARCHAR){
                         obj.put(column_name, rs.getNString(column_name));
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
                         obj.put(column_name, rs.getString(column_name));
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
                         obj.put(column_name, rs.getInt(column_name));
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.SMALLINT){
                         obj.put(column_name, rs.getInt(column_name));
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.DATE){
                         obj.put(column_name, rs.getDate(column_name));
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.TIMESTAMP){
                        obj.put(column_name, rs.getTimestamp(column_name));   
                     }
                     else{
                         obj.put(column_name, rs.getObject(column_name));
                     } 

                    }//end foreach
                 json.put(obj);

             }//end while




        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return json;
    }
}
