package trialAIProject1;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import trialAIProject1.IDA_Star.HelperNode;
import trialAIProject1.IDA_Star.Node;

public class LRTAStar {
///////for the heuristic Function
	private HelperNode last_Node;
	private LinkedList<HelperNode> nodes_With_estimofCost_NodetoDestination;
	
///////for  the Iterative_Deepening_AStar_search
	private LinkedList<Node> visited;
	private PriorityQueue<Node> priorityQueue;
	private Node last_Node_of_final_path;
	// counter for every better cost of the final path  that we achieve
	//(we have final path when we find the goal)
  private int count = 0;
	public static final float MAX_VALUE = 9999; 
	private Multigraph multiGraph;
	public LinkedList<Node> Final_predicted_path;
	public int executionTime=0;
	
	
	public LRTAStar(Multigraph mGraph){
      this.visited = new LinkedList<Node>();
      this.priorityQueue = new PriorityQueue<>(mGraph.listofvertex.size(), new Node());
      this.multiGraph = mGraph;
      this.Final_predicted_path = new LinkedList<Node>();
      this.nodes_With_estimofCost_NodetoDestination = new LinkedList<HelperNode>();
  }
	
	public void helperHeuristic(float[][][] tree, int destination) {	
		
		for (int node_ind=0; node_ind<multiGraph.listofvertex.size(); node_ind++){
			HelperNode source = new HelperNode(node_ind, 0);
			HelperNode last = Heuristic(tree, node_ind, destination);
			source.estimofCost_NodetoDestination = last.cost;
			nodes_With_estimofCost_NodetoDestination.add(source);
			
		}
	}		
			
	public HelperNode Heuristic(float[][][] tree, int source, int destination){		
		LinkedList<HelperNode> helper_visited = new LinkedList<HelperNode>();
		PriorityQueue<HelperNode> helper_priorityQueue = new PriorityQueue<>(multiGraph.listofvertex.size(), new HelperNode());
		HelperNode existingNode = new HelperNode(0,0);
		HelperNode startoftree = new HelperNode(source, 0);
		helper_priorityQueue.add(startoftree);
		last_Node = new HelperNode(0,MAX_VALUE) ;
	    new HelperTree(startoftree);
	    while (helper_priorityQueue.size() > 0) {
	    	HelperNode p = helper_priorityQueue.poll();	        
	        
	        if (p.node ==destination && (last_Node.cost> p.cost) ){	
          	p.estimofCost_NodetoDestination = 0;
          	//count++;
	            last_Node = p;
  		}
          else if ((p.node != destination) && (last_Node.cost > p.cost)) {
          	if(!(helper_visited.contains(p))){
          		boolean exists2=false;
          		for(int i=0; i<tree[p.node].length; i++){ 
          				int j=0;
          				while(tree[p.node][i][j]!=0){            					
          					boolean exists=false;
                  			for(HelperNode ns : helper_visited){
                  				if(ns.node== i){
                  					existingNode=ns;
                  					exists=true;
                  					//break;
                  				}
                  			}
                  			if(exists!=true){                    				
                  				HelperNode nd = new HelperNode(i, tree[p.node][i][j] + p.cost);
                  				p.childrens.add(nd);
                  				nd.setParent(p);
                  				helper_priorityQueue.add(nd);                    				
          					}
                  			else{
                  				helper_priorityQueue.remove(existingNode);
                  			}
                  			j++;
                  			exists2=true;
          				}            				
          		}
          		if (exists2!=true){
          			helper_priorityQueue.remove(p);          			
  				}               
          		helper_visited.add(p);
	        	}
          	else{
          		helper_priorityQueue.remove(p);
          	}
          }
          else if (p.node != destination  && last_Node.cost < p.cost){//dead end
          	helper_priorityQueue.remove(p);           	
          }
	    }
	    return last_Node;
	}

/*   references   page 8/39   */	
/*   https://arxiv.org/pdf/1110.4076.pdf   */	
/*	LRTA* OnlineSearch
	1 initialize the heuristic: h <- h0
	2 reset the current state: s <- s start
	3 while s not exists in Sg do
	4 generate children one move away from state s
	5 find the state s' with the lowest f = g + h
	6 update h(s) to f(s') if f(s') is greater
	7 execute the action to get to s'
	8 end while
*/		
	public void LRTAStar_OnlineSearch(float [][][] tree, int source, int destination){
		//Node to remove if exists in visitedlist;
		Node existingNode = new Node(0,0);
		// make a new node for source==start
		Node startoftree = new Node(source, 0);
	    // insert the starting node to priority queue
	    priorityQueue.add(startoftree);
	    // set the path's first element to "max value node" : by default 999
	    last_Node_of_final_path = new Node(0,MAX_VALUE) ;
   	
   	new Tree(startoftree);
	    // while the queue is not empty
	    while (priorityQueue.size() > 0) {	    	
	        // get the top element of the 
	        // priority queue and pop it(remove it)
	    	
	        Node p = priorityQueue.poll();
	        if(p.parent!=null){
	        	for(HelperNode nds_s : nodes_With_estimofCost_NodetoDestination){
  					if(nds_s.node == i){
  						p.cost = tree[p.parent.node][p.node][j] + p.parent.cost;
  					}
	        	}
	        }
	        executionTime++;
	        // if a new goal is reached and if the cost is less than the cost of the last found goalNode
          if ((p.node ==destination) && (last_Node_of_final_path.cost> p.cost)){	
          	count++;
          	last_Node_of_final_path = p;
          }
	        // check if the element is not 
          //the goal list and if the cost is less than the cost of the last found goalNode
          else if ((p.node != destination) && (last_Node_of_final_path.cost > p.cost)) {
          	if(!(visited.contains(p))){            	
          		//we expand the p node and add to priority queue
          		boolean exists2=false;
          		for(int i=0; i<tree[p.node].length; i++){ 
          				int j=0;
          				while(tree[p.node][i][j]!=0){            					
          					boolean exists=false;
                  			for(Node ns : visited){
                  				if(ns.node== i){
                  					existingNode=ns;
                  					exists=true;
                  					//break;
                  				}
                  			}
                  			if(exists!=true){ 
                  				for(HelperNode nds : nodes_With_estimofCost_NodetoDestination){
                  					if(nds.node == i){
                  						//Node nd = new Node(i, tree[p.node][i][j] + p.cost);
                  						Node nd = new Node(i, 0);
                  						//h
                  						nd.estimofCost_NodetoDestination = nds.estimofCost_NodetoDestination;
                  						// f = h + g
                  						nd.estimofCost_to_Destination =  tree[p.node][i][j] + p.estimofCost_NodetoDestination + nd.estimofCost_NodetoDestination;
                  						//nds.estimofCost_NodetoDestination;
                  						p.childrens.add(nd);
                  						nd.parent = p;
                  						priorityQueue.add(nd);  
                  					}
                  				}
          					}
                  			else{
                  				priorityQueue.remove(existingNode);
                  			}
                  			j++;
                  			exists2=true;
          				}            				
          		}
          		if (exists2!=true){
          			priorityQueue.remove(p);          			
  				}
              // add to visited list so as to not meet it again
	        	visited.add(p);
	        	}
          	else{
          		priorityQueue.remove(p);
          	}
          }
          else if (p.node != destination  && last_Node_of_final_path.cost < p.cost){//dead end
          	priorityQueue.remove(p);           	
          }
	    }
	    find_final_Path(last_Node_of_final_path);
	}

  public void find_final_Path(Node fpath){
  	System.out.println("<<<IDA*>: finalnode"+fpath.node +" counter"+count);
  	Node current_node = fpath;
  	LinkedList<Node> finalpath = new LinkedList<Node>();
  	finalpath.add(fpath);
  	//System.out.println(current_node.parent);
  	while(current_node.parent!=null){
  		finalpath.add(current_node.parent);
  		current_node = current_node.parent;
  	}
  	
  	int size_of_final_path = finalpath.size();
  	System.out.println("Visited Nodes number: "+ size_of_final_path);
  	System.out.println("executionTime: "+executionTime);
  	for (int i=0; i<size_of_final_path; i++){
  		
  		Node last = finalpath.removeLast();
  		String name = multiGraph.m.get(last.node).getName();
  		if(name.equals( multiGraph.destination)){
  			System.out.println(last.node+", ("+last.cost+") :"+name);
  			System.out.println(" Predicted Cost: "+last.cost+"   FINISHED  ");
  			System.out.println("\n");
  		}
  		else{
  			System.out.println(last.node+", ("+last.cost+") :" +name+ "=>");
  		}
  		Final_predicted_path.add(last);
  	}
  	
  }
	
  public class Tree{
      public Node root;

      public Tree(Node nd) {
          this.root = new Node(nd.node, nd.cost);
          //root.childrens = new ArrayList<Node>();
      }
  }
	
	class Node implements Comparator<Node>{
	    public int node;
	    public float cost;
	    public float estimofCost_NodetoDestination;
	    private Node parent;
	    private List<Node> childrens;
	    public Node(){ }
	    public Node(int source, float d){
	    	this.estimofCost_to_Destination = 0;
	        this.node = source;
	        this.cost = d;
	        this.childrens = new LinkedList<Node>();
	    }
	    
	    @Override
	    public int compare(Node node1, Node node2){
	        if (node1.estimofCost_NodetoDestination < node2.estimofCost_NodetoDestination)
	            return -1;
	        if (node1.estimofCost_NodetoDestination > node2.estimofCost_NodetoDestination)
	            return 1;
	        return 0;
	    }
	 
	    public int compare(){
	    	return 0;
	    }
	    
	    @Override
	    public boolean equals(Object obj){
	        if (obj instanceof Node){
	            Node node = (Node) obj;
	            if (this.node == node.node){
	                return true;
	            }
	        }
	        return false;
	    }
	}
	public class HelperTree{
      public HelperNode root;

      public HelperTree(HelperNode nd) {
          this.root = new HelperNode(nd.node, nd.cost);
          //root.childrens = new ArrayList<Node>();
      }
  }
	class HelperNode implements Comparator<HelperNode>{//extends Node{
		public int node;
	    public float cost;
	    public float estimofCost_NodetoDestination;
	    private HelperNode parent;
	    private List<HelperNode> childrens;
	    public HelperNode(){ }
	    public HelperNode(int source, float d){
	    	this.estimofCost_NodetoDestination = 0;
	        this.node = source;
	        this.cost = d;
	        this.childrens = new LinkedList<HelperNode>();
	    }
	    

		public int compare(HelperNode node1, HelperNode node2){
	        if (node1.estimofCost_NodetoDestination < node2.estimofCost_NodetoDestination)
	            return -1;
	        if (node1.estimofCost_NodetoDestination > node2.estimofCost_NodetoDestination)
	            return 1;
	        return 0;
	    }
		public HelperNode getParent() {
			return parent;
		}
		public void setParent(HelperNode parent) {
			this.parent = parent;
		}
	}

}
