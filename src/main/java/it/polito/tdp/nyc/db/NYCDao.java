package it.polito.tdp.nyc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nyc.model.Hotspot;
import it.polito.tdp.nyc.model.NTA;

public class NYCDao {
	
	public List<Hotspot> getAllHotspot(){
		String sql = "SELECT * FROM nyc_wifi_hotspot_locations";
		List<Hotspot> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Hotspot(res.getInt("OBJECTID"), res.getString("Borough"),
						res.getString("Type"), res.getString("Provider"), res.getString("Name"),
						res.getString("Location"),res.getDouble("Latitude"),res.getDouble("Longitude"),
						res.getString("Location_T"),res.getString("City"),res.getString("SSID"),
						res.getString("SourceID"),res.getInt("BoroCode"),res.getString("BoroName"),
						res.getString("NTACode"), res.getString("NTAName"), res.getInt("Postcode")));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}

	public List<String> getAllBoroughs() {
		// TODO Auto-generated method stub
		String sql = "SELECT DISTINCT n.Borough "
				+ "FROM nyc_wifi_hotspot_locations n "
				+ "ORDER BY n.Borough ASC";
		List<String> listReturn = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				listReturn.add(rs.getString(1));
			}
			conn.close();
			return listReturn;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Errore nel database");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<NTA> getNTAbyBorough(String bor) {
		// TODO Auto-generated method stub
		String sql = "SELECT DISTINCT NTACode, SSID "
				+ "FROM nyc_wifi_hotspot_locations n "
				+ "WHERE n.Borough=? "
				+ "ORDER BY NTACode ASC ";
		List<NTA> listReturn = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, bor);
			ResultSet rs = st.executeQuery();
			String NTACorrente = "";
			Set<String> setSSID = new HashSet<>();
			while(rs.next()) {
				if(rs.getString(1).compareTo(NTACorrente)!=0) {
					listReturn.add(new NTA(NTACorrente, setSSID));
					setSSID.clear();
					setSSID.add(rs.getString(2));
					NTACorrente=rs.getString(1);
				}else {
					listReturn.get(listReturn.size()-1).getSSIDs().add(rs.getString(2));
				}
			}
			return listReturn;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
}
