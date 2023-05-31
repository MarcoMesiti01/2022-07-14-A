package it.polito.tdp.nyc.db;

import java.util.List;
import java.util.Set;

import it.polito.tdp.nyc.model.NTA;

public class TestDao {

	public static void main(String[] args) {
		NYCDao dao = new NYCDao();
		System.out.println(dao.getAllHotspot().size());
		List<String> bor = dao.getAllBoroughs();
		System.out.println(bor.size());
		System.out.println(dao.getNTAbyBorough("SI"));
		
		
	}

}
