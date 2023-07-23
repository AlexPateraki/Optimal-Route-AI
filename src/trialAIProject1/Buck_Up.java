package trialAIProject1;

import java.util.LinkedList;
import trialAIProject1.UCS.Node;

public class Buck_Up {
	
	public float actual_Cost_ofPredictedRoad_UCS;
	public float actual_Cost_ofPredictedRoad_IDA;
	public LinkedList<Node> best_actual_path_IDA;
	public LinkedList<Node> best_actual_path_UCS;
	public float[][][] adgMatrix_with_actuallCosts;
	
	public Buck_Up(float act_cost_UCS, float act_cost_IDA, float[][][] adg2, LinkedList<Node> bestActuallPath_UCS, LinkedList<Node> bestActuallPath_IDAstar) {
		this.actual_Cost_ofPredictedRoad_UCS = act_cost_UCS;
		this.actual_Cost_ofPredictedRoad_IDA = act_cost_IDA;
		this.best_actual_path_UCS = bestActuallPath_UCS;
		this.best_actual_path_IDA = bestActuallPath_IDAstar;
		this.adgMatrix_with_actuallCosts = adg2;
	}

}
