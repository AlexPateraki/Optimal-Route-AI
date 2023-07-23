package trialAIProject1;

import java.io.File;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * the class includes the main method 
 * @author group LAB31146778
 */
public class Console {
	/**
	 * main method using an instance of the mainProgram class and calling the function of the instance to procced the algorithms and find
	 * the best paths
	 * @param args
	 * @throws IOException 
	 */

	public static void main(String[] args) throws FileNotFoundException {
		mainProgram mp = new mainProgram();
		mainProgram mp2 = new mainProgram();
		int run=0;
		mp.readFileMakeGraph(run);
		mp.MakePredictions_FindBestPath();
		mp.print_MeanValuesofCosts();
		//mp.FindBestPath_Online();
		//Creating a File object that represents the disk file.
        PrintStream o = new PrintStream(new File("output.txt"));
        // Assign o to output stream
        System.setOut(o);
        run=1;
		mp2.readFileMakeGraph(run);
		mp2.MakePredictions_FindBestPath();
		mp.print_MeanValuesofCosts();
		//mp.FindBestPath_Online();
	}


}
