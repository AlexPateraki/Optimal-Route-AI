package trialAIProject1;

//import com.sun.corba.se.impl.orbutil.graph.Graph;

//import sun.security.provider.certpath.Vertex;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import trialAIProject1.UCS.Node;


public class Multigraph{
	
	class Vertex {
	    private String Vertexname;
	    public Vertex(String Vertexname) {
	        this.Vertexname = Vertexname;
	    }
	}
	
	class Edge {
	    private String Edgename;
	    private float weigth;
	    
	    public String estimationTraffic;
		public float predicted_cost;
		public String actualTraffic;
		public float actual_cost;
		private int heavy=0;
		private int low=0;
		private int normal=0;
	    public Edge(String Edgename, float weigth) {
	        this.Edgename = Edgename;
	        this.weigth = weigth;
	    }
	}
	
	class Matrix{
		private String Name;
		private int count;
		public Matrix(String Name, int c) {
	        this.Name = Name;
	        this.count = c;
	    }
		public int getCount() {
			return this.count;
		}
		public String getName() {
			return this.Name;
		}
	}

	 public boolean exists;
	 private String name;
	 public LinkedList<Vertex> listofvertex;
	 private HashMap<Vertex, LinkedList<Vertex>> adjacentVertices;
	 private HashMap<Edge, LinkedList<Vertex>> listofedge;
	 public LinkedList<Matrix> m;
	 public int count=0;
	 public String source;
	 public String destination;
	 
	public Multigraph(String name) {
		this.name = name;
		this.listofvertex = new LinkedList<>();
		this.listofedge = new HashMap<>();
		this.adjacentVertices = new HashMap<>();
	}	
	
	public String getGraphName(){
		return name;
	}
	
	public int getSource(String src){
		for(Matrix mtrx1 : m){
			if(mtrx1.Name.equals(src)){
				return mtrx1.count;
			}
		}
		return -1;
	}
	
	public void setSource(String src){
		this.source= src;
	}
	
	public int getDestination(String destin){
		for(Matrix mtrx2 : m){
			if(mtrx2.Name.equals(destin)){
				return mtrx2.count;
			}
		}
		return -1;
	}
	
	public void setDestination(String destin){
		this.destination= destin;
	}
	
	public int addVertex(String vname){
		exists = false;
		if (listofvertex!=null)	{
			for (Vertex vrtx : listofvertex) {
				
				if (vrtx.Vertexname.equals(vname) ) {	
					exists = true;
					break;	
				}		
			}
		}
		else{
			Vertex v = new Vertex(vname);
			listofvertex.add(v);
			adjacentVertices.putIfAbsent(v, new LinkedList<>());
			return 0;
		}
		if (exists!=true){
			Vertex v = new Vertex(vname);
			listofvertex.add(v);
			adjacentVertices.putIfAbsent(v, new LinkedList<>());
		}
		return 0;
	}
	
	public  void addEdge(String vertex1, String vertex2, String ename, float weigth){
		Edge e = new Edge(ename, weigth);
		if (vertex1!=null){
			listofedge.putIfAbsent(e, new LinkedList<>());
			Vertex v1 = getVertex(vertex1);
			Vertex v2 = getVertex(vertex2);
			
			adjacentVertices.get(v1).add(v2);
			listofedge.get(e).add(v1);
			listofedge.get(e).add(v2);
		}
	}
	
	public void printListofMatrix(){
		for(Matrix mtrx : m){
			System.out.println("Matrix:"+mtrx.Name+"pointer:"+mtrx.count);
		}
	}

	
	public Vertex getVertex(String vname){
		Vertex vrtx1 = null;
		for(Vertex vrtx : listofvertex){
			if  (vrtx.Vertexname.equals(vname)  ){
				vrtx1=vrtx;
				break;
			}	
		}
		return vrtx1;
	}
	
	public Edge getEdge(String ename){
		for(Edge edge : listofedge.keySet()){
			if  (edge.Edgename.equals(ename) ){
				return edge;
			}	
		}
		System.out.println("Oh noo");
		return null;
	}
	
	public void printHashVertexes(){
		System.out.println("\n");
		System.out.println("Map of adjacent vertexes");
		
		for(Vertex vrtx : adjacentVertices.keySet()){
			System.out.println(vrtx.Vertexname+"=>");
			for(Vertex vrtx2 :  adjacentVertices.get(vrtx)){
				System.out.println(vrtx2.Vertexname);
			}
			System.out.println("\n");
		}
		
	}

	public void printHashEdges(){
		System.out.println("\n");
		System.out.println("Map of edges with info: linked vertexes, weight");
		for(Edge edges : listofedge.keySet()){
			System.out.println(edges.Edgename+"=>"+edges.weigth);
			for(Vertex vrtx3 :  listofedge.get(edges)){
				System.out.println(vrtx3.Vertexname);
			}
			System.out.println("\n");
		}
	}
	
	public void printListVertexes(){
		System.out.println("\n");
		System.out.println("List of Vertexes");
		for(Vertex vrtx : listofvertex){
			System.out.println(vrtx.Vertexname);
			}	
		}
	
	public LinkedList<Vertex> getAdjVertices(String label) {
	    return adjacentVertices.get(new Vertex(label));
	}
	
	
	public void addPredictionsPerDayinEdges(String ename, String estim){
		for(Edge edge : listofedge.keySet()){
			if(edge.Edgename.equals(ename)){
				edge.estimationTraffic = estim;
				break;
			}
		}
	}
// this is a function with our own thoughts of how to make the best predictions
	//for predicted_cost
	// taking into consideration measurements of previous days and the current 
	//predictions for every road in a day
	public void calc_CostsPerDayAfterPredictions(int day){
		if(day<10){
		
			for(Edge edge : listofedge.keySet()){
				
				if(edge.estimationTraffic.equals("heavy")){
					edge.predicted_cost =  (float) (edge.weigth + (0.25*edge.weigth));
				}
				else if(edge.estimationTraffic.equals("low")){
					edge.predicted_cost =  (float) (edge.weigth - (0.1*edge.weigth));
				}
				else{
					edge.predicted_cost = edge.weigth;
				}		
			}
		}
		else{
			for(Edge edge : listofedge.keySet()){
				if(edge.heavy>edge.low && edge.heavy>edge.normal && edge.estimationTraffic!="low"){
					edge.predicted_cost =  (float) (edge.weigth + (0.25*edge.weigth));
				}
				else if(edge.low>edge.heavy && edge.low>edge.normal && edge.estimationTraffic!="heavy"){
					edge.predicted_cost =  (float) (edge.weigth - (0.1*edge.weigth));
				}
				else if (edge.normal>edge.heavy && edge.normal>edge.normal && edge.estimationTraffic.equals("heavy")){
					edge.predicted_cost = (float) (edge.weigth + (0.25*edge.weigth));
				}
				else if (edge.normal>edge.heavy && edge.normal>edge.normal && edge.estimationTraffic.equals("low")){
					edge.predicted_cost =  (float) (edge.weigth - (0.1*edge.weigth));
				}else{
					edge.predicted_cost = edge.weigth;
				}
				
				
			
			}
		}
		
	}
		
	//make the adjacency matrix according to predicted_cost
	/**
	 * method to construct the adjacency matrix adding the predicted_cost
	 * @return adjacency matrix
	 */
	public void make_AdjacencyMatrix(AdjacencyMatrix<Matrix> g){
		for(Edge edge : listofedge.keySet()){
			
			String vertex1 = listofedge.get(edge).get(0).Vertexname;
			String vertex2 = listofedge.get(edge).get(1).Vertexname;
			int found_v1=0;
			int found_v2=0;
			int count1=0;
			int count2=0;
			if (vertex1!=null){
			
				if (m ==null){
					Matrix matr=new Matrix(vertex1, count);
					this.m = new LinkedList<>();
					m.add(count, matr); 
					count1=count;
					count++;
	
					Matrix mat=new Matrix(vertex2, count);
					m.add(count, mat); 
					count2 = count;
					count++;
				
				}	
				else {
					for (int i=0; i<m.size(); i++){
						if (m.get(i).Name.equals(vertex1.toString())){
							found_v1=1;
							count1=i;
							break;
						}

					}
					if (found_v1==0){
						Matrix man1=new Matrix(vertex1, count);
						m.add(count, man1); 
						count1 = count;
						count++;
					}
					for (int i=0;i<m.size();i++){
						if (m.get(i).Name.equals(vertex2.toString())){
							found_v2=1;
							count2=i;
							break;
						}

					}
					if (found_v2==0){
						Matrix man2=new Matrix(vertex2, count);
						m.add(count, man2); 
						count2 = count;
						count++;
					}
				}
			
				g.AdjacencyMatrixWeights(count1,count2,m,edge.predicted_cost,0);
			}

		}			
	}

	public void addActualTrafficPerDayinEdges(String ename, String estim) {
		for(Edge edge : listofedge.keySet()){
			if(edge.Edgename.equals(ename)){
				if(estim=="heavy"){
					(edge.heavy)++;
				}
				else if(estim=="low"){
					(edge.low)++;
				}
				else{
					(edge.normal)++;
				}
				edge.actualTraffic = estim;
				break;
			}
		}
	}
	public void calc_ActuallCostsPerDay(){	
		for(Edge edge : listofedge.keySet()){
		
			if(edge.actualTraffic.equals("heavy")){
				edge.actual_cost =  (float) (edge.weigth + 0.25*(edge.weigth));
			}
			else if(edge.actualTraffic.equals("low")){
				edge.actual_cost =  (float) (edge.weigth - 0.1*(edge.weigth));
			}
			else{
				edge.actual_cost = edge.weigth;
			}
		
		}
		
	}

	public float ActualCostofthePredictedRoad_UCS(LinkedList<Node> final_predicted_path) {
		float final_actual_cost=0;
		for(int i=0; i<final_predicted_path.size()-1;){
			Vertex currentEdge1 = null;
			Vertex currentEdge2 = null;
			for(Matrix mtrx : m){
				if(mtrx.count == final_predicted_path.get(i).node){
					
					currentEdge1 = getVertex(mtrx.Name);
				}
				if(mtrx.count == final_predicted_path.get(i+1).node){
					
					currentEdge2 = getVertex(mtrx.Name);
				}
			}
			Edge currentedge = null;			
			for(Edge ed : listofedge.keySet()){
				if((listofedge.get(ed).get(0)).equals(currentEdge1) && (listofedge.get(ed).get(1)).equals(currentEdge2)){
					
					currentedge = ed;
					break;
				}
				
			}			
			
			final_actual_cost = final_actual_cost + currentedge.actual_cost;
			i=i+1;
		}
		System.out.println("FINAL ACTUAL COST IS : "+final_actual_cost);
		return final_actual_cost;
		
	}
	
	//make the adjacency matrix according to weights
	/**
	 * method to construct the adjacency matrix adding the weights
	 * @return adjacency matrix
	 */
	public float[][][] make_AdjacencyMatrix_with_Weights(){
		AdjacencyMatrix<Matrix> g3 = new AdjacencyMatrix<>(listofvertex.size());
		for(Edge edge : listofedge.keySet()){
			
			String vertex1 = listofedge.get(edge).get(0).Vertexname;
			String vertex2 = listofedge.get(edge).get(1).Vertexname;
			int found_v1=0;
			int found_v2=0;
			int count1=0;
			int count2=0;
			if (vertex1!=null){
			
				if (m ==null){
					Matrix matr=new Matrix(vertex1, count);
					this.m = new LinkedList<>();
					m.add(count, matr); 
					count1=count;
					count++;
					
					Matrix mat=new Matrix(vertex2, count);
					m.add(count, mat); 
					count2 = count;
					count++;
					
				}	
				else {
					for (int i=0; i<m.size(); i++){
						if (m.get(i).Name.equals(vertex1.toString())){
							found_v1=1;
							count1=i;
							break;
						}

					}
					if (found_v1==0){
						Matrix man1=new Matrix(vertex1, count);
						m.add(count, man1); 
						count1 = count;
						count++;
					}
					for (int i=0;i<m.size();i++){
						if (m.get(i).Name.equals(vertex2.toString())){
							found_v2=1;
							count2=i;
							break;
						}

					}
					if (found_v2==0){
						Matrix man2=new Matrix(vertex2, count);
						m.add(count, man2); 
						count2 = count;
						count++;
					}
				}
			
				g3.AdjacencyMatrixWeights(count1,count2,m,edge.weigth,0);
			}

		}
		return g3.adjacency_matrix;
	}
	
	
	//make the adjacency matrix according to actual_cost
	/**
	 * method to construct the adjacency matrix adding the actual_costs
	 * @return adjacency matrix
	 */
	public float[][][] make_AdjacencyMatrix_with_actual_cost(AdjacencyMatrix<Matrix> g){
		
		for(Edge edge : listofedge.keySet()){
			
			String vertex1 = listofedge.get(edge).get(0).Vertexname;
			String vertex2 = listofedge.get(edge).get(1).Vertexname;
			int found_v1=0;
			int found_v2=0;
			int count1=0;
			int count2=0;
			if (vertex1!=null){
			
				if (m ==null){
					Matrix matr=new Matrix(vertex1, count);
					this.m = new LinkedList<>();
					m.add(count, matr); 
					count1=count;
					count++;
					
					Matrix mat=new Matrix(vertex2, count);
					m.add(count, mat); 
					count2 = count;
					count++;
					
				}	
				else {
					for (int i=0; i<m.size(); i++){
						if (m.get(i).Name.equals(vertex1.toString())){
							found_v1=1;
							count1=i;
							break;
						}

					}
					if (found_v1==0){
						Matrix man1=new Matrix(vertex1, count);
						m.add(count, man1); 
						count1 = count;
						count++;
					}
					for (int i=0;i<m.size();i++){
						if (m.get(i).Name.equals(vertex2.toString())){
							found_v2=1;
							count2=i;
							break;
						}

					}
					if (found_v2==0){
						Matrix man2=new Matrix(vertex2, count);
						m.add(count, man2); 
						count2 = count;
						count++;
					}
				}
			
				g.AdjacencyMatrixWeights(count1,count2,m,edge.actual_cost,0);
			}

		}
		return g.adjacency_matrix;
	}

	public float ActualCostofthePredictedRoad_IDA(List<trialAIProject1.IDA_Star.Node> final_predicted_path) {
		float final_actual_cost=0;
		for(int i=0; i<final_predicted_path.size()-1;){
			Vertex currentEdge1 = null;
			Vertex currentEdge2 = null;
			
			for(Matrix mtrx : m){
				if(mtrx.count == final_predicted_path.get(i).node){
					
					currentEdge1 = getVertex(mtrx.Name);
				}
				if(mtrx.count == final_predicted_path.get(i+1).node){
					
					currentEdge2 = getVertex(mtrx.Name);
				}
			}
			Edge currentedge = null;			
			for(Edge ed : listofedge.keySet()){
				if((listofedge.get(ed).get(0)).equals(currentEdge1) && (listofedge.get(ed).get(1)).equals(currentEdge2)){
					
					currentedge = ed;
					break;
				}
				
			}			
			
			final_actual_cost = final_actual_cost + currentedge.actual_cost;
			i=i+1;
		}
		System.out.println("FINAL ACTUAL COST IS : "+final_actual_cost);
		System.out.println("\n\n");
		return final_actual_cost;
	}

	public float ActualCostofthePredictedRoad_LRTA(LinkedList<trialAIProject1.LRTAStar.Node> final_predicted_path) {
		float final_actual_cost=0;
		for(int i=0; i<final_predicted_path.size()-1;){
			Vertex currentEdge1 = null;
			Vertex currentEdge2 = null;
			
			for(Matrix mtrx : m){
				if(mtrx.count == final_predicted_path.get(i).node){
					
					currentEdge1 = getVertex(mtrx.Name);
				}
				if(mtrx.count == final_predicted_path.get(i+1).node){
					
					currentEdge2 = getVertex(mtrx.Name);
				}
			}
			Edge currentedge = null;			
			for(Edge ed : listofedge.keySet()){
				if((listofedge.get(ed).get(0)).equals(currentEdge1) && (listofedge.get(ed).get(1)).equals(currentEdge2)){
					
					currentedge = ed;
					break;
				}
				
			}	
			final_actual_cost = final_actual_cost + currentedge.actual_cost;
			i=i+1;
		}
		System.out.println("FINAL ACTUAL COST IS : "+final_actual_cost);
		System.out.println("\n\n");
		return final_actual_cost;
	}
	
	
}
























