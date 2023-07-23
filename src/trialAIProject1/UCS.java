package trialAIProject1;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * class that creats the algorithm of uninformed search. This specific algorithm is uniform cost search.
 * This class has two subclasses Tree and Node.
 * @author group LAB31146778 
 */

public class UCS {
	/**
	 * linked list of visited nodes
	 */
	private LinkedList<Node> visited;
	/**
	 * priority queue of nodes
	 */
	private PriorityQueue<Node> priorityQueue;
	/**
	 * pointer of last node
	 * 
	 */
	private Node last_Node_of_final_path;
	/**
	 *  counter for every better cost of the final path  that we achieve(we have final path when we find the goal)
	 */
    private int count = 0;
	public static final float MAX_VALUE = 9999;
	/**
	 * instance of multigraph
	 */
	private Multigraph multiGraph;
	public LinkedList<Node> Final_predicted_path;
	public int executionTime=0;
	
	public UCS(Multigraph mGraph){
        this.visited = new LinkedList<Node>();
        this.priorityQueue = new PriorityQueue<>(mGraph.listofvertex.size(), new Node());
        this.multiGraph = mGraph;
        this.Final_predicted_path = new LinkedList<Node>();
    }

	/**
	 * a method in which occurs the algorithm of ucs and also keep records and execution time
	 * @param tree represents the adjacement matrix
	 * @param source -> unique number that represents the source
	 * @param destination -> unique number that represents the destination vertex
	 */
	public void uniform_cost_search(float [][][] tree, int source, int destination){
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
	        //the current root
	        //Node current_root = p;

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
                    				Node nd = new Node(i, tree[p.node][i][j] + p.cost);
                    				p.childrens.add(nd);
                    				nd.parent = p;
                    				priorityQueue.add(nd);                    				
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
            	else{//dead end
            		priorityQueue.remove(p);
            	}
            }
            else if (p.node != destination  && last_Node_of_final_path.cost < p.cost){//dead end
            	priorityQueue.remove(p);           	
            }
	    }
	    find_final_Path(last_Node_of_final_path);
	}

	/**
	 * when reaching the final node, the destination vertex this method runs in order to print the final path
	 * @param fpath
	 */
    public void find_final_Path(Node fpath){
    	System.out.println("<<<Uninformed Search Algorithm>: finalnode"+fpath.node +" counter"+count);
    	Node current_node = fpath;
    	LinkedList<Node> finalpath = new LinkedList<Node>();
    	finalpath.add(fpath);
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

    /**
     * a subclass consists of the root node
     * @author group LAB31146778 
     *
     */
    public class Tree{
        public Node root;

        public Tree(Node nd) {
            this.root = new Node(nd.node, nd.cost);
        }
    }
    /**
     * subclass node which represents each vertex and saves the number of the current node, the cost, parent and the childrens.
     * @author group LAB31146778 
     */
	class Node implements Comparator<Node>{
	    public int node;
	    public float cost;
	    private Node parent;
	    private List<Node> childrens;
	    public Node(){ }
	    public Node(int source, float d){
	        this.node = source;
	        this.cost = d;
	        this.childrens = new LinkedList<Node>();
	    }
	    /**
	     * comaring method of two nodes by cost
	     */
	    @Override
	    public int compare(Node node1, Node node2){
	        if (node1.cost < node2.cost)
	            return -1;
	        if (node1.cost > node2.cost)
	            return 1;
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

