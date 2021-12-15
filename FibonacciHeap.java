/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{
	public HeapNode first;
	public HeapNode min;
	public int size;
	public int trees;
	public int marks;
	
	public FibonacciHeap()
	{
		
	}
	
	
   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    *   
    */
    public boolean isEmpty() // Complexity O(1)
    {
    	return this.size == 0;
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * The added key is assumed not to already belong to the heap.  
    * 
    * Returns the newly created node.
    */
    public HeapNode insert(int key) //Complexity O(1)
    {    
    	HeapNode toAdd = new HeapNode(key);
    	if(this.min == null) {
    		this.min = toAdd;
    		this.first = toAdd;
    		toAdd.setNext(toAdd);
    		toAdd.setPrev(toAdd);
    	}
    	else {
    		if(key<this.min.getKey()) {
        		this.min = toAdd;
        	}
    		HeapNode last = this.first.getPrev();
    		toAdd.setNext(this.first);
    		toAdd.setPrev(last);
        	this.first.setPrev(toAdd);
        	last.setNext(toAdd);
        	this.first = toAdd;
    	}
    	this.size++;
    	this.trees++;
    	return toAdd;
    }

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    */
    public void deleteMin()
    {
     	return; // should be replaced by student code
     	
    }

   /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    *
    */
    public HeapNode findMin() //Complexity O(1)
    {
    	return this.min;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    *
    */
    public void meld (FibonacciHeap heap2) //Complexity O(1)
    {
    	if(heap2.isEmpty() || this.isEmpty() && heap2.isEmpty()) {
    		return;
    	}
    	else if(this.isEmpty()) {
    		this.first = heap2.first;
    		this.min = heap2.min;
    		this.size = heap2.size;
    		this.trees = heap2.trees;
    		this.marks = heap2.marks;
    		return;
    	}
    	if (this.min.getKey() > heap2.min.getKey()) {
    		this.min = heap2.min;
    	}
    	HeapNode thisLast = this.first.getPrev();
    	HeapNode heap2Last = heap2.first.getPrev();
    	thisLast.setNext(heap2.first);
    	heap2.first.setPrev(thisLast);
    	heap2Last.setNext(this.first);
    	this.first.setPrev(heap2Last);
    	this.size += heap2.size;
    	this.trees += heap2.trees;
    	this.marks += heap2.marks;
    	return;    		
    }

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    *   
    */
    public int size() //Complexity O(1)
    {
    	return this.size;
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * Note: The size of of the array depends on the maximum order of a tree, and an empty heap returns an empty array.
    * 
    */
    public int[] countersRep() //Complexity O(n)
    {
    	int maxRank = -1;
    	HeapNode traveler = this.first;
    	while(traveler.getNext().getKey() == this.first.getKey()) {
    		if(maxRank < traveler.getRank()) {
    			maxRank = traveler.getRank();
    		}
    	}
    	int[] rankArr = new int[maxRank+1];
    	HeapNode traveler2 = this.first;
    	while(traveler2.getNext().getKey() == this.first.getKey()) {
    		rankArr[traveler2.getRank()]++;
    	}
        return rankArr; //	 to be replaced by student code
    }
    
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap.
	* It is assumed that x indeed belongs to the heap.
    *
    */
    public void delete(HeapNode x) 
    {    
    	return; // should be replaced by student code
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	return; // should be replaced by student code
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * 
    * In words: The potential equals to the number of trees in the heap
    * plus twice the number of marked nodes in the heap. 
    */
    public int potential() //Complexity O(1)
    {    
    	return this.trees + 2*this.marks; // should be replaced by student code
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the
    * run-time of the program. A link operation is the operation which gets as input two
    * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
    * tree which has larger value in its root under the other tree.
    */
    public static int totalLinks()
    {    
    	return -345; // should be replaced by student code
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the
    * run-time of the program. A cut operation is the operation which disconnects a subtree
    * from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return -456; // should be replaced by student code
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
    * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
    *  
    * ###CRITICAL### : you are NOT allowed to change H. 
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {    
        int[] arr = new int[100];
        return arr; // should be replaced by student code
    }
    
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in another file. 
    *  
    */
    public static class HeapNode{

    	//public String info;
    	public int key;
    	public int rank;
    	public boolean mark;
    	public HeapNode child;
    	public HeapNode next;
    	public HeapNode prev;
    	public HeapNode parent;
    	

    	public HeapNode(int key) {
    		this.key = key;
    	}

    	public int getKey() {
    		return this.key;
    	}
    	
    	public int getRank() {
    		return this.rank;
    	}
    	
    	public void setRank(int rank) {
    		this.rank = rank;
    	}
    	public boolean getMark() {
    		return this.mark;
    	}
    	
    	public void setMark(boolean mark) {
    		this.mark =  mark;
    	}
    	
    	public HeapNode getChild() {
    		return this.child;
    	}
    	
    	public void setChild(HeapNode child) {
    		this.child = child;
    	}
    	public HeapNode getNext() {
    		return this.next;
    	}
    	
    	public void setNext(HeapNode next) {
    		this.next = next;
    	}
    	public HeapNode getPrev() {
    		return this.prev;
    	}
    	
    	public void setPrev(HeapNode prev) {
    		this.prev = prev;
    	}
    	
    	public HeapNode getParent() {
    		return this.parent;
    	}
    	
    	public void setParent(HeapNode parent) {
    		this.parent = parent;
    	}
    }
}
