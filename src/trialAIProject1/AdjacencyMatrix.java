package trialAIProject1;

import java.util.LinkedList;
/**
 * this class creates the adjacency matrix of the edges of the graph
 * @author group LAB31146778
 * @param <Matrix> subclass coded in the Multigraph class
 */

public class AdjacencyMatrix<Matrix>{
	
    private  int vertices;
    public float[][][] adjacency_matrix;
    private float[][][] ad;

    public AdjacencyMatrix(int v){
    	this.vertices = v;
    	this.adjacency_matrix = new float[vertices][vertices][20];
        this.ad = new float[vertices][vertices][20];
    }
    /**
     * make the edge of the cell (from,to,k1) scanning into for loop 
     * @param from ->source vertex
     * @param to ->goal vertex
     * @param edge -> the weight of the adjacency matrix in the specific cell of the matrix
     */
    public void makeEdge(int from, int to, float edge){
        try{        
        	for (int k1=0; k1<adjacency_matrix[from][to].length; k1++){
        		if(adjacency_matrix[from][to][k1]==0){
        			adjacency_matrix[from][to][k1] = edge;
        			break;
        		}
        	}  
        }catch(ArrayIndexOutOfBoundsException index){
            System.out.println("The vertices does notkkkkkkkkkkkkkk exists");
        }
    }
    
    /**
     * returning the edge of the adjacency matrix
     * @param from -> first dimension of the adjacency matrix
     * @param to -> second dimension of the adjacency matrix
     * @param indx -> third dimension of the adjacency matrix
     * @return the edge, if not found the method returns -1
     */
 
	public float getEdge(int from, int to, int indx){
        try{
        	return adjacency_matrix[from][to][indx];
        	
        }catch(ArrayIndexOutOfBoundsException index){
            System.out.println("The vertices does not exists");
        }
		return -1;
       
    }
	
	 /**
	  * this methood is called in the method make_AdjacencyMatrix of the class Multigraph to return adjacency matrix in order to 
	  * handle the edges more efficient in the algoriths asked.
	  * @param vertex1->the source vertex
	  * @param vertex2-> the goal vertex
	  * @param m-> linked list of <Matrix> , described and constructed in the class Multigraph
	  * @param predicted_cost -> predicted cost using to call the makeEdge method
	  * @param max -> a local index of the method make_AdjacencyMatrix of  class Multigraph
	  * @return the adjacency matrix
	  */
 
    public float[][][] AdjacencyMatrixWeights(int vertex1,int vertex2,LinkedList<Matrix> m, float predicted_cost, int max){
    	if (max==0){
    		makeEdge(vertex1,vertex2, predicted_cost);
    	}
        else{
        	System.out.println("The adjacency matrix for the given graph is: ");
            System.out.print("          ");
            for (int i = 0; i < m.size(); i++){
            	System.out.print(i + "                                        ");
            }
            System.out.println();
            for (int i = 0; i < m.size(); i++){
                System.out.print(i + " ");
                for (int j = 0; j < m.size(); j++){
                	System.out.print(" ");
                	for(int k=0; k<adjacency_matrix[i][j].length; k++  ){
                		ad[i][j][k] = getEdge(i,j,k);
                		System.out.print(ad[i][j][k] + ",");
                	}
                }
                System.out.println();
            }
        	return ad;
         }
		return adjacency_matrix;
        
    }
    }
 
    
