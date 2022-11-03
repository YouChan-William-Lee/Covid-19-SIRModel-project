import java.io.PrintWriter;

/**
 * Adjacency list implementation for the AssociationGraph interface.
 *
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2019.
 */
public class AdjacencyList extends AbstractGraph
{	
	// A array for adjacency list
	private Node adjList[];
    private int size;

    /**
	 * Contructs empty graph.
	 */
    public AdjacencyList() {
		adjList = null;
        size = 0;
    } // end of AdjacencyList()


    public void addVertex(String vertLabel) {
		// when adjList is null which is no vertex
		if (adjList == null) {
			adjList = new Node[1];
            adjList[0] = new Node(vertLabel);
        }
		// when adjList has some vertices
        else {
			// make new array which is one size bigger
            Node newAdjList[] = new Node[size + 1];

            for (int i = 0; i < size + 1; i++) {
				if (i == size) {
					newAdjList[size] = new Node(vertLabel);
				}
				else {
                	newAdjList[i] = adjList[i];
				}
			}
            adjList = newAdjList;
        }

        size += 1;
    } // end of addVertex()


    public void addEdge(String srcLabel, String tarLabel) {
		// src for index of srcLabel in adjList
		// tar for index of tarLabel in adjList
		int src = -1, tar = -1;
		for (int i = 0; i < size; i++) {
    		if(adjList[i].getName().equals(srcLabel)) {
    			src = i;
				break;
    		}
		}

		for (int i = 0; i < size; i++) {
    		if(adjList[i].getName().equals(tarLabel)) {
    			tar = i;
				break;
    		}
		}
		// if both of srcLabel and tarLabel exists in adjList and they are not same, then implement edge addtion
		if (src >= 0 && tar >= 0 && src != tar) {
			Node currNode = adjList[src];
			// when current node doesn't have next node
			if (currNode.getNext() == null) {
				currNode.setNext(new Node(adjList[tar].getName()));
			}
			// when current node has next node
			else {
				// while loop lasts until next node is null or next node's name is same as tarLabel
				while(currNode.getNext() != null && !currNode.getNext().getName().equals(adjList[tar].getName())) {
					currNode = currNode.getNext();
				}
				// if current node already has tarLabel, then don't set it as next node
				if(currNode.getNext() == null) {
					currNode.setNext(new Node(adjList[tar].getName()));
				}
			}

			currNode = adjList[tar];
			// when current node doesn't have next node
			if (currNode.getNext() == null) {
				currNode.setNext(new Node(adjList[src].getName()));
			}
			else {
				// while loop lasts until next node is null or next node's name is same as srcLabel
				while(currNode.getNext() != null && !currNode.getNext().getName().equals(adjList[src].getName())) {
					currNode = currNode.getNext();
				}
				// if current node already has srcLabel, then don't set it as next node
				if(currNode.getNext() == null) {
					currNode.setNext(new Node(adjList[src].getName()));
				}
			}
		}
    } // end of addEdge()


    public void toggleVertexState(String vertLabel) {
		// index for index of vertLabel in adjList
        int index = -1;
    	for(int i = 0; i < size; i ++) {
    		if(adjList[i].getName().equals((vertLabel))) {
    			index = i;
    			break;
    		}
    	}
		// when index is not -1 and this node's SIR state is S
		if(index >= 0 && adjList[index].getSIRstate().equals(SIRState.S)) {
    		adjList[index].setSIRstate(SIRState.I);
    	}
		// when index is not -1 and this node's SIR state is not S
    	else {
    		adjList[index].setSIRstate(SIRState.R);
    	}
    } // end of toggleVertexState()


    public void deleteEdge(String srcLabel, String tarLabel) {
		// src for index of srcLabel in adjList
		// tar for index of tarLabel in adjList
        int src = -1, tar = -1;
		for (int i = 0; i < size; i++) {
    		if(adjList[i].getName().equals(srcLabel)) {
    			src = i;
				break;
    		}
		}

		for (int i = 0; i < size; i++) {
    		if(adjList[i].getName().equals(tarLabel)) {
    			tar = i;
				break;
    		}
		}
		// if both of srcLabel and tarLabel exists in adjList and they are not same, then implement edge deletion
		if (src >= 0 && tar >= 0 && src != tar) {
			Node currNode = adjList[src];
			// while loop lasts until next node is null
			while(currNode.getNext() != null) {
				// if current node's next node's name is same as tarLabel, then set current node's next to next next node
				if(currNode.getNext().getName() == adjList[tar].getName()) {
					currNode.setNext(currNode.getNext().getNext());
					break;
				}
				currNode = currNode.getNext();
			}

			currNode = adjList[tar];
			// while loop lasts until next node is null
			while(currNode.getNext() != null) {
				// if current node's next node's name is same as srcLabel, then set current node's next to next next node
				if(currNode.getNext().getName() == adjList[src].getName()) {
					currNode.setNext(currNode.getNext().getNext());
					break;
				}
				currNode = currNode.getNext();
			}
		}
    } // end of deleteEdge()


    public void deleteVertex(String vertLabel) {
		// index for index of vertLabel in adjList
        int index = -1;
		for (int i = 0; i < size; i++) {
    		if(adjList[i].getName().equals(vertLabel)) {
    			index = i;
				break;
    		}
		}
		// if vertLabel exists in adjList, then implement vertex deletion
		if (index >= 0) {

			// Remove all the edges which connect to with vertLabel
			for (int i = 0; i < size; i++) {
				Node currNode = adjList[i];
				while(currNode.getNext() != null && i != index) {
					if(currNode.getNext().getName().equals(vertLabel)) {
						currNode.setNext(currNode.getNext().getNext());
						break;
					}
					currNode = currNode.getNext();
				}
			}

			// remove vertLabel vertex
			// make new array which is one size smaller
			Node newAdjList[] = new Node[size - 1];

            for (int i = 0; i < size; i++) {
				if (i < index) {
					newAdjList[i] = adjList[i];
				}
				else if (i > index) {
                	newAdjList[i - 1] = adjList[i];
				}
			}
			adjList = newAdjList;
		}

    	size -= 1;
    } // end of deleteVertex()


    public String[] kHopNeighbours(int k, String vertLabel) {
		// visit array for how many times away from vertLabel 
        int visit[] = new int[size];
        // answer array for neighbours
        String answer[] = new String[size];
		// initialisation
        for(int i = 0 ; i < size ; i ++) {
            visit[i] = 0;
            answer[i] = "";
        }
		// index for index of vertLabel in adjList
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (adjList[i].getName().equals(vertLabel)) {
                index = i; 
                break;
            }
        }
		// if vertLabel exists in adjList
		if (index >= 0) {
			// check if vertLabel has any edge
			if (adjList[index].getNext() != null) {
				// vertLabel itself is k + 1, vertLabel's next node will be k, vertLabel's next next node will be k - 1, and so on.  
				visit[index] = k + 1;
				// index for answer array
				index = 0;
				while (k > 0) {
					for (int i = 0; i < size; i++) {
						// if the vertex is one away from the previous node
						if (visit[i] == k + 1) {
							Node currNode = adjList[i];
							// check current node's all next nodes
							while (currNode.getNext() != null) {
								currNode = currNode.getNext();
								// idx for index of current node
								int idx = -1;
								for (int j = 0; j < size; j++) {
									if (adjList[j].getName().equals(currNode.getName())) {
										idx = j;
										break;
									}
								}
								// if the node is already visited, we don't put the node again into answer array 
								if(visit[idx] == 0) {
									visit[idx] = k;
									// put this node into answer array
									answer[index] = currNode.getName();
									index += 1;                                
								}
							}
						}
					}
					k -= 1;
				}
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
      	for (int i = 0; i < size; i++) {
			os.print("(" + adjList[i].getName() + "," + adjList[i].getSIRstate() + ") ");
			os.flush();
		}
		os.println("");
		os.flush();
    } // end of printVertices()


    public void printEdges(PrintWriter os) {
		os.flush();
		// print all the edges 
		for (int i = 0; i < size; i++) {
			Node currNode = adjList[i];
			// while loop last unitl next node is null
			while(currNode.getNext() != null) {
				os.println(adjList[i].getName() + " " + currNode.getNext().getName());
				os.flush();
				currNode = currNode.getNext();
			}
		}
    } // end of printEdges()

	/**
     * Node type, inner private class.
     */
    private class Node
    {
        protected Node next;
		protected String name;
		protected SIRState SIRstate;

        public Node(String name) {
            this.name = name;
			this.next = null;
			// default state is S
			this.SIRstate = SIRState.S;
        }

        public Node getNext() {
            return next;
        }

		public String getName() {
			return name;
		}

		public SIRState getSIRstate() {
			return SIRstate;
		}

        public void setNext(Node next) {
            this.next = next;
        }

		public void setSIRstate(SIRState SIRstate) {
			this.SIRstate = SIRstate;
		}
    } // end of inner class Node

    public SIRState getState(String vertLabel) {
    	for(int i = 0; i < size; i++) {
    		if(adjList[i].getName().equals(vertLabel)) {
    			return adjList[i].getSIRstate();
    		}
    	}
		return null;
	} // end of getState()

    public int getVerticesSize() {
        return size;
    } // end of getVerticesSize()

} // end of class AdjacencyList
