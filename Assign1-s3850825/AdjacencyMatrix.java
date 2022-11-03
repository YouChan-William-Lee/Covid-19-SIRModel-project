import java.io.PrintWriter;


/**
 * Adjacency matrix implementation for the GraphInterface interface.
 *
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2021.
 */
public class AdjacencyMatrix extends AbstractGraph
{   
    // 2D array for adjacency matrix
    private int adjMatrix[][];
    // A array for vertex
    private String vertList[];
    // A array for vertex' SIRstate
    private SIRState vertSIRstate[];
    private int size;
	/**
	 * Contructs empty graph.
	 */
    public AdjacencyMatrix() {
        adjMatrix = null;
        vertList = null;
        vertSIRstate = null;
        size = 0;
    } // end of AdjacencyMatrix()


    public void addVertex(String vertLabel) {
        // if there is no vertex
        if (vertList == null) {
            adjMatrix = new int[1][1];
            adjMatrix[0][0] = 0;
            vertList = new String[1];
            vertSIRstate = new SIRState[1];
            vertList[0] = vertLabel;
            // default state is S
            vertSIRstate[0] = SIRState.S;
        }
        // if there are some vertices
        else {
            // make new array which is one size bigger
            int newAdjMatrix[][] = new int[size + 1][size + 1];
            String newVertList[] = new String[size + 1];
            SIRState newVertSIRstate[] = new SIRState[size + 1];

            for (int i = 0; i < size + 1; i++) {
                for (int j = 0; j < size + 1; j++) {
                    if (i < size && j < size) {
                        // pass all the edges from original to new array
                        newAdjMatrix[i][j] = adjMatrix[i][j];
                    }
                    else {
                        // new vertex has no edge so 0
                        newAdjMatrix[i][j] = 0;
                    }
                }
            }

            for (int i = 0; i < size + 1; i++) {
                if (i == size) {
                    // pass all the vertices and SIRstates from original to new array
                    newVertList[size] = vertLabel;
                    newVertSIRstate[size] = SIRState.S;
                }
                else {
                    newVertList[i] = vertList[i];
                    newVertSIRstate[i] = vertSIRstate[i];
                }
            }            

            adjMatrix = newAdjMatrix;
            vertList = newVertList;
            vertSIRstate = newVertSIRstate;
        }

    	size += 1;
    } // end of addVertex()


    public void addEdge(String srcLabel, String tarLabel) {
        // src for index of srcLabel in vertList
        // tar for index of tarLabel in vertList
        int src = -1, tar = -1;
    	for (int i = 0; i < size; i++) {
    		if(vertList[i].equals(srcLabel)) {
    			src = i;
    			break;
    		}
    	}

    	for (int i = 0; i < size; i++) {
    		if(vertList[i].equals(tarLabel)) {
    			tar = i;
    			break;
    		}
    	}
        // if two vertices exist in vertList, indices are differnt, and they don't have a edge, then implement edge addition
        if (src >= 0 && tar >= 0 && src != tar && adjMatrix[src][tar] != 1 && adjMatrix[tar][src] != 1) {
    	    adjMatrix[src][tar] = 1;
    	    adjMatrix[tar][src] = 1;
        }
    } // end of addEdge()


    public void toggleVertexState(String vertLabel) {
        // index for index of vertLabel in vertList
    	int index = -1;
    	for(int i = 0; i < size; i ++) {
    		if(vertList[i].equals(vertLabel)) {
    			index = i;
    			break;
    		}
    	}
        // if vertLabel exists in vertList, toggle SIRstate
    	if (index >= 0) {
            // if vertLabel's SIRstate is S
            if(vertSIRstate[index].equals(SIRState.S)) {
                vertSIRstate[index] = SIRState.I;
            }
            // if vertLabel's SIRstate is not S
            else {
                vertSIRstate[index] = SIRState.R;
            }
        }
    } // end of toggleVertexState()


    public void deleteEdge(String srcLabel, String tarLabel) {
        // src for index of srcLabel in vertList
        // tar for index of tarLabel in vertList
        int src = -1, tar = -1;
    	for(int i = 0; i < size; i ++) {
    		if(vertList[i].equals(srcLabel)) {
    			src = i;
    			break;
    		}
    	}
    	for(int i = 0; i < size; i ++) {
    		if(vertList[i].equals(tarLabel)) {
    			tar = i;
    			break;
    		}
    	}
        // if two vertices exist in vertList and they have a edge, then implement edge deletion
    	if (src >= 0 && tar >= 0 && src != tar && adjMatrix[src][tar] != 0 && adjMatrix[tar][src] != 0) {
    	    adjMatrix[src][tar] = 0;
    	    adjMatrix[tar][src] = 0;
        }
    } // end of deleteEdge()


    public void deleteVertex(String vertLabel) {
        // index for index of vertLabel in vertList
        int index = -1;
    	for(int i = 0 ; i < size; i ++) {
    		if(vertList[i].equals(vertLabel)) {
    			index = i;
    			break;
    		}
    	}
        // if vertLabel exists in vertList
        if (index >= 0) {
            // make new array which is one size smaller
            int newAdjMatrix[][] = new int[size - 1][size - 1];
            String newVertList[] = new String[size - 1];
            SIRState newVertSIRstate[] = new SIRState[size - 1];

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (i < index && j < index) {
                        // pass all the edges from original to new array
                        newAdjMatrix[i][j] = adjMatrix[i][j];
                    }
                    else if (i < index && j > index) {
                        // one column smaller
                        newAdjMatrix[i][j - 1] = adjMatrix[i][j];
                    }
                    else if (i > index && j < index) {
                        // one row smaller
                        newAdjMatrix[i - 1][j] = adjMatrix[i][j];
                    }
                    else if (i > index && j > index) {
                        // one row and column smaller
                        newAdjMatrix[i - 1][j - 1] = adjMatrix[i][j];
                    }
                }
            }

            for (int i = 0; i < size; i++) {
                if (i < index) {
                    // pass all the vertices and SIRstates from original to new array
                    newVertList[i] = vertList[i];
                    newVertSIRstate[i] = vertSIRstate[i];
                }
                else if (i > index) {
                    // one size smaller
                    newVertList[i - 1] = vertList[i];
                    newVertSIRstate[i - 1] = vertSIRstate[i];
                }
            }

            adjMatrix = newAdjMatrix;
            vertList = newVertList;
            vertSIRstate = newVertSIRstate;
        }

    	size -= 1;
    } // end of deleteVertex()


    public String[] kHopNeighbours(int k, String vertLabel) {
        // visit array for how many times away from vertLabel 
        int visit[] = new int[size];
        // answer array for neighbours
    	String answer[] = new String[size];
    	// Initialisation
    	for (int i = 0; i < size; i++) {
    		visit[i] = 0;
    		answer[i] = "";
    	}
        // index for index of vertLabel in vertList
    	int index = -1;
    	for (int i = 0; i < size; i++) {
    		if(vertList[i].equals(vertLabel)) {
    			index = i ;
    			break;
    		}
    	}
        // if vertLabel exists in adjList
        if (index >= 0) {
            // vertLabel itself is k + 1, vertLabel's next node will be k, vertLabel's next next node will be k - 1, and so on
            visit[index] = k + 1;

            index = 0;
            while (k > 0) {
                for (int i = 0; i < size; i++) {
                    // if the vertex is one away from the previous node
                    if (visit[i] == k + 1) {
                        for (int j = 0; j < size; j++) {
                            // check all the edges
                            // if the node is already visited, we don't put the node again into answer array 
                            if (adjMatrix[i][j] == 1 && visit[j] == 0) {
                                visit[j] = k;
                                // put this node into answer array
                                answer[index] = vertList[j];
                                index += 1;
                            }
                        }
                    }
                }
                k -= 1;
            }
        }

        // rearrange answer array (remove "" elements)
        String finalAnswer[] = new String[1];
        finalAnswer[0] = "";
		index = 0;
        // pass all the values from answer array to finalAnswer array except "" 
		for(int i = 0; i < answer.length; i++) {
            if(answer[i] != "") {
				if (finalAnswer[0] == "") {
					finalAnswer[0] = answer[i];
				}
				else {
					String newFinalAnswer[] = new String[index + 1];
					for (int j = 0; j < index + 1; j++) {
						if (j == index) {
							newFinalAnswer[index] = answer[i];
						}
						newFinalAnswer[j] = answer[j];
					}
					finalAnswer = newFinalAnswer;
				}
				index += 1;
			}
        }

        return finalAnswer;
    } // end of kHopNeighbours()


    public void printVertices(PrintWriter os) {
        os.flush();
        // print all the vertices with their SIRstate
    	for (int i = 0; i < size ; i ++) {
    		os.print("(" + vertList[i] + ","+ vertSIRstate[i]+") ");
            os.flush();
    	}
    	os.println("");
        os.flush();
    } // end of printVertices()


    public void printEdges(PrintWriter os) {
        os.flush();
        // print all the edges 
    	for (int i = 0; i < size; i ++) {
    		for (int j = 0; j < size; j ++) {
    			if (adjMatrix[i][j] == 1) {
    				os.println(vertList[i] + " " + vertList[j]);
                    os.flush();
    			}
    		}
    	}
    } // end of printEdges()

    public SIRState getState(String vertLabel) {
    	for(int i = 0; i < size; i++) {
    		if(vertList[i].equals(vertLabel)) {
    			return vertSIRstate[i];
    		}
    	}
		return null;
	} // end of getState()

    public int getVerticesSize() {
        return size;
    } // end of getVerticesSize()
    
} // end of class AdjacencyMatrix
