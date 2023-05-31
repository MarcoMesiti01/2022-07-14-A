package it.polito.tdp.nyc.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.nyc.db.NYCDao;

public class Model {
	private List<String> boroughs;
	private NYCDao dao = new NYCDao();
	private List<NTA> NTAs;
	private Graph<NTA, DefaultWeightedEdge> grafo;
	
	public List<String> getBoroughs(){
		if(boroughs==null) {
			this.boroughs=dao.getAllBoroughs();
		}
		return boroughs;
	}
	
	public void creaGrafo(String bor) {
		this.NTAs=dao.getNTAbyBorough(bor);
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, this.NTAs);
		
		for(NTA n: this.NTAs) {
			for(NTA n2: this.NTAs) {
				if(n.getNTACode().compareTo(n2.getNTACode())<0) {//!n.equals(n2)
					Set<String> unione = new HashSet<>(n.getSSIDs());
					unione.addAll(n2.getSSIDs());
					Graphs.addEdge(this.grafo, n, n2, unione.size());
				}
			}
		}
		
		System.out.println("Grafo creato con: "+ this.getArchi()+" archi e " +this.getVertici()+" vertici");
	}
	
	public int getArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public int getVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public List<Arco> analisiArchi(){
		double media = 0.0;
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			media+=this.grafo.getEdgeWeight(e);
		}
		media=media/this.grafo.edgeSet().size();
		
		List<Arco> result = new ArrayList<>();
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e)>media) {
				result.add(new Arco(this.grafo.getEdgeSource(e).getNTACode(), 
						this.grafo.getEdgeTarget(e).getNTACode(), 
						this.grafo.getEdgeWeight(e)));
			}
		}
		Collections.sort(result);
		return result;
	}
	
}
