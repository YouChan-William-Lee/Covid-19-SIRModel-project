import java.io.PrintWriter;
import java.util.Random;

/**
 * SIR model.
 *
 * @author Jeffrey Chan, 2021.
 */
public class SIRModel
{

    /**
     * Default constructor, modify as needed.
     */
    public SIRModel() {

    } // end of SIRModel()


    /**
     * Run the SIR epidemic model to completion, i.e., until no more changes to the states of the vertices for a whole iteration.
     *
     * @param graph Input contracts graph.
     * @param seedVertices Set of seed, infected vertices.
     * @param infectionProb Probability of infection.
     * @param recoverProb Probability that a vertex can become recovered.
     * @param sirModelOutWriter PrintWriter to output the necessary information per iteration (see specs for details).
     */
    public void runSimulation(ContactsGraph graph, String[] seedVertices,
        float infectionProb, float recoverProb, PrintWriter sirModelOutWriter)
    {
        // IMPLEMENT ME!
        int size = graph.getVerticesSize();
    	Random random = new Random();
    	String infect[];
        infect = null;
    	String recover[] = new String[size];
        sirModelOutWriter.flush();
    	
    	for (int i = 0 ; i < size ; i ++) {
    		recover[i] = "";
    	}
    	
    	int num_of_infect = seedVertices.length;
    	int num_of_recover = 0;

        infect = new String[num_of_infect];

    	for (int i = 0; i < num_of_infect; i++) {
    		String name = seedVertices[i];
    		infect[i] = name;
    		graph.toggleVertexState(name);
    	}
            	
    	int t = 1;
        int noChange = 0;

    	while (noChange < 10 && num_of_infect != 0) {
    		String new_infect[] = new String[size];
    		String new_recover[] = new String[size];
    		
    		int new_num_of_infect = 0, new_num_of_recover = 0;
    		
        	for (int i = 0; i < size; i++) {
        		new_infect[i] = "";
        		new_recover[i] = "";
        	}

    		for (int i = 0; i < num_of_infect; i++) {
                // Infection
				String[] neighbor = new String[size];
				neighbor = graph.kHopNeighbours(1, infect[i]);

                if (neighbor[0] != "") {
                    for (int j = 0; j < neighbor.length; j++) {
                        if (neighbor[j] != null && graph.getState(neighbor[j]).equals(SIRState.S)) {
                            float infe = (float)random.nextGaussian();					
                            if (infe <= infectionProb) {
                                new_infect[new_num_of_infect] = neighbor[j];
                                graph.toggleVertexState(neighbor[j]);
                                new_num_of_infect += 1;
                            }
                        }
                    }
                }

                // Recovery
                float recv = (float)random.nextGaussian();
				if(recv <= recoverProb) {
					new_recover[new_num_of_recover] = infect[i];
					graph.toggleVertexState(infect[i]);
                    new_num_of_recover += 1;
				}
    		}

            if (new_num_of_infect == 0 && new_num_of_recover == 0) {
                noChange += 1;
            }
            else {
                noChange = 0;
            }

            // Print new infected and new recovered vertices
            sirModelOutWriter.print(t + ": [");
            sirModelOutWriter.flush();
            for (int i = 0; i < new_num_of_infect; i++) {
                if (i == new_num_of_infect - 1) {
                    sirModelOutWriter.print(new_infect[i]);
                    sirModelOutWriter.flush();
                }
                else {
                    sirModelOutWriter.print(new_infect[i] + " ");
                    sirModelOutWriter.flush();
                }
            }
    		sirModelOutWriter.print("] : [");
            sirModelOutWriter.flush();

            for (int i = 0; i < new_num_of_recover; i++) {
                if (i == new_num_of_recover - 1) {
                    sirModelOutWriter.print(new_recover[i]);
                    sirModelOutWriter.flush();
                }
                else {
                    sirModelOutWriter.print(new_recover[i] + " ");
                    sirModelOutWriter.flush();
                }
            }
    		sirModelOutWriter.println("]");
            sirModelOutWriter.flush();

            // Update recovered vertices
            for (int i = 0; i < num_of_infect; i++) {
                boolean recovery = false;
                for (int j = 0; j < new_num_of_recover; j++) {
                    if(infect[i].equals(new_recover[j])) {
                        recovery = true;
                        break;
                    }
                }  
                if (recovery == false) {
                    new_infect[new_num_of_infect] = infect[i];
                    new_num_of_infect += 1;
                }
            }
            infect = new_infect;
            num_of_infect = new_num_of_infect;
            t = t + 1;

            // Rearrange infect array (remove "" elements)
            String finalInfect[];
		    finalInfect = null;
		    int index = 0;

            for(int i = 0; i < infect.length; i++) {
                if(infect[i] != "") {
                    if (finalInfect == null) {
                        finalInfect = new String[1];
                        finalInfect[0] = infect[i];
                    }
                    else {
                        String newFinalInfect[] = new String[index + 1];
                        for (int j = 0; j < index + 1; j++) {
                            if (j == index) {
                                newFinalInfect[index] = infect[i];
                            }
                            newFinalInfect[j] = infect[j];
                        }
                        finalInfect = newFinalInfect;
                    }
                    index += 1;
                }
            } 
            infect = finalInfect;

            // When there are no more infected vertices, print [] : [] and finish
            if (num_of_infect == 0) {
                sirModelOutWriter.println(t + ": [] : []");
                sirModelOutWriter.flush();
            }
    	}
    } // end of runSimulation()
} // end of class SIRModel
