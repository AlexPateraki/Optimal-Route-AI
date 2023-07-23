package trialAIProject1;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import trialAIProject1.Multigraph.Matrix;

/**
 * a class, in which the user inserts the name of the file in the line 14 of the code in order to run the algorithms using 
 * the java package of Scanner. So, the code reads the file, line by line and saves the
 * information in the right variables or calls the right function from other classes for organising the Multigraph.
 * 
 * @author group LAB31146778 
 */

public class mainProgram{
	/**
	 * string variable being the name of the file in which the user should type the name of the preferred file.
	 */
	String FileName = "sampleGraph3.txt";
	/**
	 * instance of the multigraph of the edges and vertexes
	 */
	Multigraph MGraph = new Multigraph(FileName);
	private float mean_Actuall_Value_of_total_cost_perDay_UCS=0;
	private float mean_Actuall_Value_of_total_cost_perDay_IDA=0;
	private float mean_Actuall_Value_of_total_cost_perDay_LRTAStar=0;
	int run;
	/**
	 * this method handles opening the file, separating the words using split command 
	 *  and saving the information needed in local variables or calling methods in other classes.
	 */
	public void readFileMakeGraph(int run){
		try{
			RandomAccessFile file1 = new RandomAccessFile(FileName, "r");	  
		    System.out.println("NAME of the GRAPH is:"+MGraph.getGraphName());
		    String str1 = file1.readLine();
			String Source0[]  = str1.split(">");
			String Source1[] = Source0[1].split("</");
			MGraph.addVertex(Source1[0]);
			MGraph.setSource(Source1[0]);
			String str2 = file1.readLine();
			String Destination0[] = str2.split(">");
			String Destination1[] = Destination0[1].split("</");
			MGraph.addVertex(Destination1[0]);
			MGraph.setDestination(Destination1[0]);
			
			String str3;
			str3 = file1.readLine(); //"<Roads>"
			while ( !(str3 = file1.readLine()).equals("</Roads>") ) {
				//System.out.println(str3);
				String[] data = str3.split("; ");
				MGraph.addVertex(data[1]);
				MGraph.addVertex(data[2]);
				//System.out.println(data[1]+ data[2]+ data[0]+data[3]+"\n");
			    MGraph.addEdge(data[1], data[2], data[0], Float.parseFloat(data[3]));  
			}			
			file1.close();
			}catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();	
			}		
	}
	
	/**
	 * handler of the predictions of the algorithms(uniform cost search, IDA*) by counting the 80 days and calling the methods of each 
	 * algorithm respectively
	 */
	public void MakePredictions_FindBestPath(){
		try{
			@SuppressWarnings("resource")
			RandomAccessFile file = new RandomAccessFile(FileName, "r");
			@SuppressWarnings("resource")
			RandomAccessFile file2 = new RandomAccessFile(FileName, "r");
			String str;
			String actTraffic;
			while(!(str = file.readLine()).equals("<Predictions>")){	
			}
			while(!(actTraffic = file2.readLine()).equals("<ActualTrafficPerDay>")){	
			}
			for(int day=1; day<=80; day++){
				System.out.println("        DAY :  "+day);
				promptEnterKey();
				// Predictions
				file.readLine();
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				// FIND PREDICTIONS AND CALCULATE THE FINAL REDICTED COST AND THE PREDICTED ROAD				
				while (!(str = file.readLine()).equals("</Day>")){
					String[] data1 = str.split("; ");
					//System.out.println(data[0]+data[1]);
					MGraph.addPredictionsPerDayinEdges(data1[0], data1[1]);
				}				
				
				AdjacencyMatrix<Matrix> g1=new AdjacencyMatrix<Matrix>(MGraph.listofvertex.size());
				MGraph.calc_CostsPerDayAfterPredictions(day);
				MGraph.make_AdjacencyMatrix(g1);
				
				//MGraph.printHashEdges();
				//MGraph.printHashVertexes();
				//MGraph.printListVertexes();
				//MGraph.printListofMatrix();
				////print AdjacencyMatrix
				//g.AdjacencyMatrixWeights(0, 0, MGraph.m, 0, 1);
				// UCS
				UCS UCS = new UCS(MGraph);
				int source = MGraph.getSource(MGraph.source);
				int destination = MGraph.getDestination(MGraph.destination);
				System.out.println("hiiiiii source:"+source +"  dest:"+destination);
				UCS.uniform_cost_search(g1.adjacency_matrix, source, destination);
				// IDAStar
				IDA_Star IDAstar = new IDA_Star(MGraph);
				float[][][] adg1 = MGraph.make_AdjacencyMatrix_with_Weights();
				IDAstar.helperHeuristicFunc(adg1, destination);
				IDAstar.Iterative_Deepening_AStar_search(g1.adjacency_matrix, source, destination);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				// FIND ACTUAL TRAFFIC AND CALCULATE THE FINAL ACTUAL COST OF THE PREDICTED ROAD				
				file2.readLine();
				while (!(actTraffic = file2.readLine()).equals("</Day>")){
					String[] data2 = actTraffic.split("; ");
					//System.out.println(data2[0]+data2[1]);
					MGraph.addActualTrafficPerDayinEdges(data2[0], data2[1]);
				}
				MGraph.calc_ActuallCostsPerDay();
				float act_cost_UCS = MGraph.ActualCostofthePredictedRoad_UCS(UCS.Final_predicted_path);
				float act_cost_IDA = MGraph.ActualCostofthePredictedRoad_IDA(IDAstar.Final_predicted_path);
				mean_Actuall_Value_of_total_cost_perDay_UCS += act_cost_UCS; 
				mean_Actuall_Value_of_total_cost_perDay_IDA += act_cost_IDA; 
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				//FIND the best actual road when the day has passed
				System.out.println("FIND the best actual road when the day has passed");
				AdjacencyMatrix<Matrix> g2=new AdjacencyMatrix<Matrix>(MGraph.listofvertex.size());
				float[][][] adg2 = MGraph.make_AdjacencyMatrix_with_actual_cost(g2);
				// UCS
				UCS UCS2 = new UCS(MGraph);
				UCS2.uniform_cost_search(adg2, source, destination);
				LinkedList<trialAIProject1.UCS.Node> bestActuallPath_UCS=  UCS2.Final_predicted_path;
				// IDAStar
				IDA_Star IDAstar2 = new IDA_Star(MGraph);
				IDAstar2.helperHeuristicFunc(adg1, destination);
				IDAstar2.Iterative_Deepening_AStar_search(adg2, source, destination);
				LinkedList<trialAIProject1.UCS.Node> bestActuallPath_IDAstar= UCS2.Final_predicted_path;
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				//BUCK UP OF THE ACTUALL MEASUREMENTS FOR THE NEXT DAY
				//if something would be useful for optimization
				Buck_Up buckup = new Buck_Up(act_cost_UCS, act_cost_IDA, adg2, bestActuallPath_UCS, bestActuallPath_IDAstar );
				
			}
			
		}catch(IOException e){
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
	}
	
	public void print_MeanValuesofCosts(){
		System.out.println("mean cost of actuall costs of the predictions:");
		System.out.println("UCS "+mean_Actuall_Value_of_total_cost_perDay_UCS/80);
		System.out.println("IDA :"+mean_Actuall_Value_of_total_cost_perDay_IDA/80);
		//System.out.println("IDA :"+mean_Actuall_Value_of_total_cost_perDay_LRTAStar/80);
		

	}
	
	public void FindBestPath_Online(){
		try{
			@SuppressWarnings("resource")
			RandomAccessFile file_online = new RandomAccessFile(FileName, "r");
			
			String str_online;
			
			
			while(!(str_online = file_online.readLine()).equals("<ActualTrafficPerDay>")){	
			}
			for(int day=1; day<=80; day++){
				System.out.println("        DAY :  "+day);
				promptEnterKey();
				// Predictions
				file_online.readLine();
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				// FIND PREDICTIONS AND CALCULATE THE FINAL REDICTED COST AND THE PREDICTED ROAD				
//				while (!(str_online = file_online.readLine()).equals("</Day>")){
//					String[] data1 = str.split("; ");
//					//System.out.println(data[0]+data[1]);
//					MGraph.addPredictionsPerDayinEdges(data1[0], data1[1]);
//				}				
				
			
//				MGraph.calc_CostsPerDayAfterPredictions(day);
//				MGraph.make_AdjacencyMatrix(g1);
				
				//MGraph.printHashEdges();
				//MGraph.printHashVertexes();
				//MGraph.printListVertexes();
				//MGraph.printListofMatrix();
				////print AdjacencyMatrix
				//g.AdjacencyMatrixWeights(0, 0, MGraph.m, 0, 1);
				// UCS
//				UCS UCS = new UCS(MGraph);
				int source = MGraph.getSource(MGraph.source);
				int destination = MGraph.getDestination(MGraph.destination);
//				System.out.println("hiiiiii source:"+source +"  dest:"+destination);
//				UCS.uniform_cost_search(g1.adjacency_matrix, source, destination);
//				// IDAStar
//				IDA_Star IDAstar = new IDA_Star(MGraph);
//				float[][][] adg1 = MGraph.make_AdjacencyMatrix_with_Weights();
//				IDAstar.helperHeuristicFunc(adg1, destination);
//				IDAstar.Iterative_Deepening_AStar_search(g1.adjacency_matrix, source, destination);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				// FIND ACTUAL TRAFFIC AND CALCULATE THE FINAL ACTUAL COST OF THE PREDICTED ROAD				
				while (!(str_online = file_online.readLine()).equals("</Day>")){
					String[] data2 = str_online.split("; ");
					//System.out.println(data2[0]+data2[1]);
					MGraph.addActualTrafficPerDayinEdges(data2[0], data2[1]);
				}
				MGraph.calc_ActuallCostsPerDay();
				
				// LRTAtar
				LRTAStar lrta = new LRTAStar(MGraph);
				float[][][] adg1 = MGraph.make_AdjacencyMatrix_with_Weights();
				lrta.helperHeuristic(adg1, destination);
				lrta.LRTAStar_OnlineSearch(adg1, source, destination);	
				
				//ActualCostofthePredictedRoad_LRTA is the same with ActualCostofthePredictedRoad_IDA
				float act_cost_UCS = MGraph.ActualCostofthePredictedRoad_LRTA(lrta.Final_predicted_path);
				mean_Actuall_Value_of_total_cost_perDay_LRTAStar += act_cost_UCS; 
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				//FIND the best actual road when the day has passed
//				System.out.println("FIND the best actual road when the day has passed");
//				AdjacencyMatrix<Matrix> g2=new AdjacencyMatrix<Matrix>(MGraph.listofvertex.size());
//				float[][][] adg2 = MGraph.make_AdjacencyMatrix_with_actual_cost(g2);
//				// UCS
//				UCS UCS2 = new UCS(MGraph);
//				UCS2.uniform_cost_search(adg2, source, destination);
//				LinkedList<trialAIProject1.UCS.Node> bestActuallPath_UCS=  UCS2.Final_predicted_path;
//				// IDAStar
//				IDA_Star IDAstar2 = new IDA_Star(MGraph);
//				IDAstar2.helperHeuristicFunc(adg1, destination);
//				IDAstar2.Iterative_Deepening_AStar_search(adg2, source, destination);
//				LinkedList<trialAIProject1.UCS.Node> bestActuallPath_IDAstar= UCS2.Final_predicted_path;
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				//BUCK UP OF THE ACTUALL MEASUREMENTS FOR THE NEXT DAY
					
			}
			
		}catch(IOException e){
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * method to interrupt each day in order to analyze the records of each day
	 */
	private void promptEnterKey() {
//		System.out.println("Press \"ENTER\" to continue...");
//		try {
//		System.in.read();
//		System.out.print("");	
//		}catch (IOException e) {
//			e.printStackTrace();
//		 }
		
		
		System.out.println("Press \"ENTER\" to continue...");
		if (run==0){try {
			
				System.in.read();	
			}catch (IOException e) 
		{
			e.printStackTrace();
		 }
		}
		else
				System.out.print("");	
			 
		}
	}

	



	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	




