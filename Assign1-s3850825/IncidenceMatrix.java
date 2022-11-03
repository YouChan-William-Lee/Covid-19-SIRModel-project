import java.io.PrintWriter;

/**
 * Incidence matrix implementation for the GraphInterface interface.
 *
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2021.
 */
public class IncidenceMatrix extends AbstractGraph
{
    // 2D array for incidence matrix
    private int incMatrix[][];
    // A array for vertex
    private String vertList[];
    // A array for vertex' SIRstate
    private SIRState vertSIRstate[];
    // A array for incidence matrix' column names
    private String colInfo[];
    // column size for incMatrix
    private int colSize;
    // row size for incMatrix
    private int rowSize;
	/**
	 * Contructs empty graph.
	 */
    public IncidenceMatrix() {
        incMatrix = null;
        vertList = null;
        vertSIRstate = null;
        colInfo = null;
        colSize = 0;
        rowSize = 0;
    } // end of IncidenceMatrix()


    public void addVertex(String vertLabel) {
        // if there is no vertex
        if (vertList == null) {
            vertList = new String[1];
            vertSIRstate = new SIRState[1];
            vertList[0] = vertLabel;
            // default state is S
            vertSIRstate[0] = SIRState.S;
        }
        // if there are some vertices
        else {
            // make new array which is one size bigger
            String newVertList[] = new String[rowSize + 1];
            SIRState newVertSIRstate[] = new SIRState[rowSize + 1];

            for (int i = 0; i < rowSize + 1; i++) {
                if (i == rowSize) {
                    // for new vertex
                    newVertList[rowSize] = vertLabel;
                    newVertSIRstate[rowSize] = SIRState.S;
                }
                else {
                    // pass all the edges from original to new array
                    newVertList[i] = vertList[i];
                    newVertSIRstate[i] = vertSIRstate[i];
                }
            }            
            vertList = newVertList;
            vertSIRstate = newVertSIRstate;
        }

        // if there is any edge in incMatrix, then add one more row
        if (incMatrix != null) {
            // make new array which is one row size bigger
            int newIncMatrix[][] = new int[rowSize + 1][colSize];
            for (int i = 0; i < rowSize + 1; i++) {
                for (int j = 0; j < colSize; j++) {
                    if (i == rowSize) {
                        // new vertex has no edge so 0
                        newIncMatrix[rowSize][j] = 0;
                    }
                    else {
                        // pass all the edges from original to new array
                        newIncMatrix[i][j] = incMatrix[i][j];
                    }
                }
            }
            incMatrix = newIncMatrix;
        }
        
    	rowSize += 1;
    } // end of addVertex()


    public void addEdge(String srcLabel, String tarLabel) {
        // src for index of srcLabel in vertList
        // tar for index of tarLabel in vertList
        int src = -1, tar = -1;
    	for (int i = 0; i < rowSize; i++) {
    		if(vertList[i].equals(srcLabel)) {
    			src = i;
    			break;
    		}
    	}

    	for (int i = 0; i < rowSize; i++) {
    		if(vertList[i].equals(tarLabel)) {
    			tar = i;
    			break;
    		}
    	}
        // exist shows whether srcLabel and tarLabel has an edge
        boolean exist = false;
        for (int i = 0; i < colSize; i++) {
            // colInfo array has all the edges information, so check if it has the edge
            if(colInfo[i].equals(srcLabel + "," + tarLabel) || colInfo[i].equals(tarLabel + "," + srcLabel)) {
                exist = true;
            }
        }
        // if two vertices exist in vertList, an edge exists in colInfo, and indices are different, then implement edge addition
        if (src >= 0 && tar >= 0 && !exist && src != tar){
            // if there is no any edge
            if (incMatrix == null) {
                incMatrix = new int[rowSize][1];
                for (int i = 0; i < rowSize; i++) {
                    if (i == src || i == tar) {
                        // if it is srcLabel or tarLabel, then 1
                        incMatrix[i][0] = 1;
                    }
                    else {
                        // if it is not srcLabel and tarLabel, then 0
                        incMatrix[i][0] = 0;
                    }
                }
                colInfo = new String[1];
                // make column information as srcLabel,tarLabel (for example, edge "1,2" for vertex 1 and vertex 2)
                colInfo[0] = srcLabel + "," + tarLabel;
            }
            // if there are some edges
            else {
                // make new array which is one column size bigger
                int newIncMatrix[][] = new int[rowSize][colSize + 1];

                for (int i = 0; i < rowSize; i++) {
                    for (int j = 0; j < colSize + 1; j++) {
                        if (j == colSize) {
                            if (i == src || i == tar) {
                                // if it is srcLabel or tarLabel, then 1
                                newIncMatrix[i][j] = 1;
                            }
                            else {
                                // if it is not srcLabel and tarLabel, then 0
                                newIncMatrix[i][j] = 0;
                            }
                        }
                        else {
                            // pass all the edges from original to new array
                            newIncMatrix[i][j] = incMatrix[i][j]; 
                        }
                    }
                }
                // make new array which is one size bigger
                String newColInfo[] = new String[colSize + 1];
                for (int i = 0; i < colSize + 1; i++) {
                    if (i == colSize) {
                        // new column information
                        newColInfo[i] = srcLabel + "," + tarLabel;
                    }
                    else {
                        // pass all the edges from original to new array
                        newColInfo[i] = colInfo[i]; 
                    }
                }
                colInfo = newColInfo;
                incMatrix = newIncMatrix;
            }

            colSize += 1;
        }
    } // end of addEdge()


    public void toggleVertexState(String vertLabel) {
        // index for index of vertLabel in vertList
        int index = -1;
    	for(int i = 0; i < rowSize; i ++) {
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
        // index for index of edge for srcLabel and tarLabel in colInfo
        int index = -1;
        for (int i = 0; i < colSize; i++) {
            if (colInfo[i].equals(srcLabel + "," + tarLabel) || colInfo[i].equals(tarLabel + "," + srcLabel)) {
                index = i;
                break;
            }
        }
        // if an edge for srcLabel and tarLabel exists in colInfo, then implement edge deletion
    	if (index >= 0) {
            // if this is the last column, then make incMatrix and colInfo null
            if (colSize == 1){
                incMatrix = null;
                colInfo = null;
            }
            // if there are some columns
            else {
                // make new array which is one column size smaller
                int newIncMatrix[][] = new int[rowSize][colSize - 1];
               
                for (int i = 0; i < rowSize; i++) {
                    for (int j = 0; j < colSize; j++) {
                        if (j < index) {
                            // pass all the edges from original to new array 
                            newIncMatrix[i][j] = incMatrix[i][j];
                        }
                        else if (j > index) {
                            // pull one column
                            newIncMatrix[i][j - 1] = incMatrix[i][j];
                        }
                    }
                }
                // make new array which is one size smaller
                String newColInfo[] = new String[colSize - 1];
                for (int i = 0; i < colSize; i++) {
                    if(i < index) {
                        // pass all the column information
                        newColInfo[i] = colInfo[i];
                    }
                    else if (i > index) {
                        // pull one column
                        newColInfo[i - 1] = colInfo[i];
                    }
                }
                colInfo = newColInfo;
                incMatrix = newIncMatrix;
            } 
            colSize -= 1;
        }
    } // end of deleteEdge()


    public void deleteVertex(String vertLabel) {
        // index for index of vertLabel in vertList
        int index = -1;
    	for(int i = 0 ; i < rowSize; i ++) {
    		if(vertList[i].equals(vertLabel)) {
    			index = i;
    			break;
    		}
    	}
        // if vertLabel exists in vertList
        if (index >= 0) {
            // remove all the edges in incMatrix
            int q = 0;
            while(q != colSize){
                // if vertLabel has any edge in colInfo
                if (colInfo[q].contains(vertLabel)) {
                    // delete the edge
                    // for example, for "1,2" edge, then call deleteEdge(1, 2)
                    deleteEdge(colInfo[q].substring(0, colInfo[q].indexOf(",")), colInfo[q].substring(colInfo[q].indexOf(",") + 1));
                }
                else {
                    q += 1;
                }
            }
            // make new array which is one size smaller
            String newVertList[] = new String[rowSize - 1];
            SIRState newVertSIRstate[] = new SIRState[rowSize - 1];

            for (int i = 0; i < rowSize; i++) {
                if (i < index) {
                    // pass all the verties and SIRstates from original to new array 
                    newVertList[i] = vertList[i];
                    newVertSIRstate[i] = vertSIRstate[i];
                }
                else if (i > index) {
                    // pull one column
                    newVertList[i - 1] = vertList[i];
                    newVertSIRstate[i - 1] = vertSIRstate[i];
                }
            }
            // if the vertex is the part of edge in last column, then incMatrix will be null in deleteEdge() method
            // if incMatrix has some edges
            if (incMatrix != null) {
                // make new array which is one row size smaller 
                int newIncMatrix[][] = new int[rowSize - 1][colSize];

                for (int i = 0; i < rowSize; i++) {
                    for (int j = 0; j < colSize; j++) {
                        if (i < index) {
                            // pass all the edges from original to new array
                            newIncMatrix[i][j] = incMatrix[i][j];
                        }
                        else if (i > index) {
                            // pull one row
                            newIncMatrix[i - 1][j] = incMatrix[i][j];
                        }
                    }
                }
                incMatrix = newIncMatrix;
            }
            
            vertList = newVertList;
            vertSIRstate = newVertSIRstate;

            rowSize -= 1;
        }
    } // end of deleteVertex()


    public String[] kHopNeighbours(int k, String vertLabel) {
        // visit array for how many times away from vertLabel
        int visit[] = new int[rowSize];
        // answer array for neighbours
    	String answer[] = new String[rowSize];
    	// initialisation
    	for (int i = 0; i < rowSize; i++) {
    		visit[i] = 0;
    		answer[i] = "";
    	}
        // index for index of vertLabel in adjList
    	int index = -1;
    	for (int i = 0; i < rowSize; i++) {
    		if(vertList[i].equals(vertLabel)) {
    			index = i ;
    			break;
    		}
    	}
        // if vertLabel exists in adjList
        if (index >= 0) {
            // vertLabel itself is k + 1, vertLabel's next node will be k, vertLabel's next next node will be k - 1, and so on
            visit[index] = k+1;

            index = 0;
            while (k > 0) {
                for (int i = 0; i < rowSize; i ++) {
                    // if the vertex is one away from the previous node
                    if (visit[i] == k + 1) {
                        for (int j = 0; j < colSize; j ++) {
                            // check all the edges
                            if (incMatrix[i][j] == 1) {
                                int idx = -1;
                                // find the index of the vertex which has edge with vertList[i]
                                // for example, let's say vertLabel is 1 and tarLabel is 2
                                // for "1,2" edge, we need to find 2's index inf vertList
                                if(colInfo[j].substring(0, colInfo[j].indexOf(",")).equals(vertList[i])) {
                                    for(int l = 0; l < rowSize; l++) {
                                        if(vertList[l].equals(colInfo[j].substring(colInfo[j].indexOf(",") + 1))) {
                                            idx = l;
                                            break;
                                        }
                                    }
                                }
                                else {
                                    for(int l = 0; l < rowSize; l++) {
                                        if(vertList[l].equals(colInfo[j].substring(0, colInfo[j].indexOf(",")))) {
                                            idx = l;
                                            break;
                                        }
                                    }
                                }
                                // if the node is already visited, we don't put the node again into answer array 
                                if(visit[idx] == 0) {
                                    visit[idx] = k;
                                    // put this node into answer array
                                    answer[index] = vertList[idx];
                                    index += 1;
                                }
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

		for(int i = 0; i < answer.length; i++) {
            if(answer[i] != "") {
				if (finalAnswer[0] == "") {
					finalAnswer[0] = answer[i];
				}
				else {
                    // pass all the values from answer array to finalAnswer array except "" 
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
        for (int i = 0; i < rowSize ; i ++) {
    		os.print("(" + vertList[i] + ","+ vertSIRstate[i]+") ");
            os.flush();
    	}
    	os.println("");
        os.flush();
    } // end of printVertices()


    public void printEdges(PrintWriter os) {
        os.flush();
        // count array is for checking whether the column is printed
        // for example, if 1, 2 is printed, "1,2" column in count array will be added one
        // so that next time it will be printed opposite way such as 2, 1
        int count[] = new int[colSize];
        for (int i = 0; i < colSize; i++) {
            count[i] = 0;
        }
        // print all the vertices with their SIRstate
        for (int i = 0; i < rowSize; i ++) {
    		for (int j = 0; j < colSize; j ++) {
    			if (incMatrix[i][j] == 1) {
                    if (count[j] == 0) {
                        // print just column information itself
    				    os.println(colInfo[j].substring(0, colInfo[j].indexOf(",")) + " " + colInfo[j].substring(colInfo[j].indexOf(",") + 1));
                        os.flush();
                    }
                    else {
                        // print oppoiste way
                        os.println(colInfo[j].substring(colInfo[j].indexOf(",") + 1) + " " + colInfo[j].substring(0, colInfo[j].indexOf(",")));
                        os.flush();
                    }
                    count[j] += 1;
    			}
    		}
    	}
    } // end of printEdges()
    
    public SIRState getState(String vertLabel) {
    	for(int i = 0; i < rowSize; i++) {
    		if(vertList[i].equals(vertLabel)) {
    			return vertSIRstate[i];
    		}
    	}
		return null;
	} // end of getState()

    public int getVerticesSize() {
        return rowSize;
    } // end of getVerticesSize()
    
} // end of class IncidenceMatrix
