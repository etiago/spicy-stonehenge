package nl.tudelft.ewi.st.atlantis.tudelft.logdump;

import java.beans.Statement;
import java.io.IOException;
import java.security.MessageDigest;
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
import nl.tudelft.ewi.st.atlantis.tudelft.logdump.json.JSONObject;

public class StaticImporter extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		return;
	}
	
	private static Connection c;
	
	static {
		try {
			c = DriverManager.getConnection("jdbc:derby://localhost:1527/logdb");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {		
		String data = req.getParameter("data");
		
		if (data == null) return;
		
		try {
			JSONArray obj = new JSONArray(data);
			
			String parsed = obj.toString();
			
			Class.forName("org.apache.derby.jdbc.ClientDriver");
			
			byte[] bytesOfMessage = parsed.getBytes("UTF-8");

			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(bytesOfMessage);
			
			
			StringBuffer hexString = new StringBuffer();
			for (int i=0;i<thedigest.length;i++) {
				hexString.append(Integer.toHexString(0xFF & thedigest[i]));
			}

			//PreparedStatement st = c.prepareStatement("SELECT COUNT(*) FROM staticinfo WHERE md5 = ?");
			PreparedStatement st = c.prepareStatement("SELECT md5, staticinfo.id, MAX(tstamp) FROM staticinfo_time INNER JOIN staticinfo ON staticinfo_time.staticinfo_id = staticinfo.id GROUP BY md5, staticinfo.id");
			
			//st.setString(1, hexString.toString());
			
			ResultSet rs = st.executeQuery();
			
			boolean exists = rs.next();
			
			if (exists) {
				String tableMD5 = rs.getString(1);

				// Means we have the same as the latest
				if (tableMD5.equals(hexString.toString())) {
					return;
				}
				
				st = c.prepareStatement("SELECT id FROM staticinfo WHERE md5 = ?");
				
				st.clearParameters();
				
				st.setString(1, hexString.toString());
				
				rs = st.executeQuery();
				
				if (!rs.next()) {
					addNonExistentInfo(parsed, hexString.toString());
					return;
				}
				
				// If we reach here, we already have this static info
				
				st = c.prepareStatement("INSERT INTO staticinfo_time (staticinfo_id, tstamp) VALUES (?,?)");
				
				st.clearParameters();
				
				st.setInt(1, rs.getInt(1));
				st.setTimestamp(2, new Timestamp(Calendar.getInstance().getTimeInMillis()));
				
				st.execute();
				
				return;
			}
				
			addNonExistentInfo(parsed, hexString.toString());
			
			return;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private void addNonExistentInfo(String json_data, String md5) throws SQLException {
		PreparedStatement st = c.prepareStatement("INSERT INTO staticinfo (json_data, md5) VALUES (?,?)",org.apache.derby.client.am.Statement.RETURN_GENERATED_KEYS);
		
		st.clearParameters();
		
		st.setString(1, json_data);
		st.setString(2, md5);
		
		st.execute();
		
		ResultSet keys = st.getGeneratedKeys();
		keys.next();
		
		st = c.prepareStatement("INSERT INTO staticinfo_time (staticinfo_id, tstamp) VALUES (?,?)");
		
		st.clearParameters();
		
		st.setInt(1, keys.getInt(1));
		st.setTimestamp(2, new Timestamp(Calendar.getInstance().getTimeInMillis()));
		
		st.execute();
	}
}
