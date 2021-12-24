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
    public static int links;
    public static int cuts;
    public static final double phi = (1+Math.sqrt(5))/2;
    
    public FibonacciHeap()
    {
        
    }
    
    public HeapNode getFirst()
    {
        return this.first;
    }
    
    public int getMarked()
    {
        return this.marks;
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
    public void deleteMin() //Complexity O(n)
    {
        if(this.isEmpty()) {
            return;
        }
        else if(this.min.getNext().getKey() == this.min.getKey() && this.min.getPrev().getKey() == this.min.getKey() && this.min.getChild() == null) {
            this.size = 0;
            this.marks = 0;
            this.trees = 0;
            return;
        }
        boolean minKid = false;
        if(this.min.getChild() != null) { //disconnect min's children and add them to the main branch
            minKid = true; //min has a child
            this.trees += this.min.getRank()-1; // add min's children to size -1 because of the min we delete
            HeapNode child = this.min.getChild();
            HeapNode traveler = child;
            
            int i = 0;
            while(i < this.findMin().getRank()) {  
                traveler.setParent(null);
                if(traveler.getMark() == true) {
                    this.marks--;
                }
                traveler.setMark(false);
                traveler = traveler.getNext();
                i++;
            }
            
            
//          while(traveler.getNext().getKey() != child.getKey()) {
//              traveler.setParent(null);
//              traveler.setMark(false);
//              traveler = traveler.getNext();
//          }
            
            if(this.min.getKey()==this.min.getNext().getKey()) { //min has no brothers
                this.first=child;
            }
            else {
                HeapNode prevChild = child.getPrev();

                HeapNode nextMin = this.min.getNext();
                HeapNode prevMin = this.min.getPrev();
                
                child.setPrev(prevMin);
                prevMin.setNext(child);
                prevChild.setNext(nextMin);
                nextMin.setPrev(prevChild);
            }

            

            if(this.min.getKey() == this.first.getKey()) {
                this.first = child;
            }
        }
        HeapNode toDelete = this.min;
        if(toDelete.getKey() == first.getKey()) {
            this.first = this.first.getNext();
        }
        if(!minKid) {
            toDelete.getNext().setPrev(toDelete.getPrev());
            toDelete.getPrev().setNext(toDelete.getNext());
        }
        toDelete.setChild(null);
        toDelete.setParent(null);
        toDelete.setNext(null);
        toDelete.setPrev(null);
        toDelete.setRank(0);
        
        this.consolidate();
        this.size--; //decrease size after deleting
        return;
        
    }
    
    public void consolidate() //Complexity O(n)
    {
        HeapNode[] B = toBuckets();
        fromBuckets(B);
    }
    
    public HeapNode[] toBuckets() //Complexity O(n)
    {               
        int length = (int)Math.ceil(Math.log(this.size)/Math.log(phi));
        HeapNode[] B = new HeapNode[length];
        this.first.getPrev().setNext(null);
        HeapNode x = this.first;
        while(x != null) {
            HeapNode y = x;
            x = x.getNext();
            y.setNext(y); //controversial not sure if to disconnect the current node before putting it in B
            y.setPrev(y); //controversial not sure if to disconnect the current node before putting it in B
            while (B[y.getRank()] != null) {
                y = link(y, B[y.getRank()]);
                B[y.getRank()-1] = null;
            }
            B[y.getRank()] = y;
        }
        return B;
    }
    
    public HeapNode link(HeapNode y, HeapNode tenant) //Complexity O(1)
    {
        links++;
        if(y.getKey()> tenant.getKey()) {
            HeapNode child = tenant.getChild(); //connect y to tenant's children
            if(child != null) {
                if(tenant.getRank() == 1) { //check if working
                    child.setNext(child);
                    child.setPrev(child);
                }
                y.setNext(child);
                y.setPrev(child.getPrev());
                child.getPrev().setNext(y);
                child.setPrev(y);
            }
            y.setParent(tenant);
            
            tenant.setChild(y); //connect y to tenant
            tenant.setRank(tenant.getRank()+1);
            return tenant;
        }
        else {
            HeapNode child = y.getChild(); //connect tenant to y's children
            if(child != null) {
                if(y.getRank() == 1) { //check if working
                    child.setNext(child);
                    child.setPrev(child);
                }
                tenant.setNext(child);
                tenant.setPrev(child.getPrev());
                child.getPrev().setNext(tenant);
                child.setPrev(tenant);
            }
            tenant.setParent(y);
            
            y.setChild(tenant); //connect tenant to y
            y.setRank(y.getRank()+1);
            return y;
        }
    }
    
    public void fromBuckets(HeapNode[] B) //Complexity O(log(n))
    {
        this.trees = 0;
        HeapNode x = null;
        for(int i = 0; i < B.length; i++) {
            if(B[i] != null) {
                this.trees++; //number of trees will be the same as the filled places in B
                if(x == null) {
                    x = B[i];
                    x.setNext(x);
                    x.setPrev(x);
                    this.first = x;
                    this.min = x;
                    x.setRank(i);
                }
                else {
                    if(this.min.getKey() > B[i].getKey()) {
                        this.min = B[i];
                    }
                    x.setNext(B[i]);
                    B[i].setPrev(x);
                    B[i].setNext(this.first);
                    this.first.setPrev(B[i]);
                    x = B[i];
                    x.setRank(i);
                }
            }
        }
        this.first.setPrev(x);
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
        return rankArr; //   to be replaced by student code
    }
    
    
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap.
    * It is assumed that x indeed belongs to the heap.
    *
    */
    public void delete(HeapNode x) //Complexity O(n)
    {
        this.decreaseKey(x, x.getKey() - (this.min.getKey() - 1));
        this.deleteMin();
        return;
    }
    

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta) //Complexity O(n)
    {    
        x.setKey(x.getKey()-delta);
        if(x.getKey() < this.min.getKey()) {
            this.min = x;
        }
        if(x.getParent() != null && x.getParent().getKey() > x.getKey()) { //heap rule is broken
            HeapNode y = x.getParent();
            cascadingCut(x,y);
        }
        return;
    }
    
    public void cascadingCut(HeapNode x, HeapNode y) //Complexity O(n)
    {
        Cut(x,y);
        if(y.getParent() != null) {
            if(y.getMark() == false) {
                y.setMark(true);
                this.marks++;
            }
            else {
                cascadingCut(y, y.getParent());
            }
        }
    }
    
    public void Cut(HeapNode x, HeapNode y) //Complexity O(1)
    {
        if(x.getMark()) {
            x.setMark(false);
            this.marks--;
        }
        x.setParent(null);
        y.setRank(y.getRank()-1);
        if(x.getNext().getKey() == x.getKey()) {
            y.setChild(null);
        }
        else {
            HeapNode tempNext = x.getNext();
            HeapNode tempPrev = x.getPrev();
            if(y.getChild().getKey() == x.getKey()) {
                y.setChild(tempNext);
            }           
            tempNext.setPrev(tempPrev);
            tempPrev.setNext(tempNext);
        }
        HeapNode thisLast = this.first.getPrev(); //adding x to the main branch of the heap
        thisLast.setNext(x);
        x.setPrev(thisLast);
        x.setNext(this.first);
        this.first.setPrev(x);
        cuts++;
        this.trees++;
        this.first = x;
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
        return this.trees + 2*this.marks; 
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the
    * run-time of the program. A link operation is the operation which gets as input two
    * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
    * tree which has larger value in its root under the other tree.
    */
    public static int totalLinks() //Complexity O(1)
    {    
        return links; 
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the
    * run-time of the program. A cut operation is the operation which disconnects a subtree
    * from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts() //Complexity O(1)
    {    
        return cuts; 
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
    * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
    *  
    * ###CRITICAL### : you are NOT allowed to change H. 
    */
    public static int[] kMin(FibonacciHeap H, int k) //Complexity O(k*Deg(H))
    {    
        if(k <= 0 || H.isEmpty()) {
            int[] arr = new int[0];
            return arr;
        }
        int[] arr = new int[k];
        if(k > H.size) {
            k = H.size;
        }
        if(k == 1) {
            arr[0] = H.findMin().getKey();
            return arr;
        }
        FibonacciHeap minChildHeap = new FibonacciHeap(); //temp Heap to find the next min
        arr[0] = H.findMin().getKey();
        HeapNode traveler = H.findMin().getChild();
        HeapNode hTraveler = H.findMin().getChild(); //pointer in the original heap to help keep track
        int j = 0;
        while(j < H.getFirst().getRank()) {  
            //traveler.setPosition(traveler.getNext());
            minChildHeap.insert(traveler.getKey());
            minChildHeap.getFirst().setPosition(traveler.getNext());
            traveler = traveler.getNext();
            j++;
        }
        for(int i = 1; i < k; i++) {
            //HeapNode nextMin = minChildHeap.findMin();
            int nextMinKey = minChildHeap.findMin().getKey();
            while(hTraveler.getKey() != nextMinKey) {
                hTraveler = hTraveler.getNext();
            }
            arr[i] = nextMinKey;
            minChildHeap.deleteMin();
            traveler = minChildHeap.first;
            if(hTraveler.getChild() != null) {
                FibonacciHeap tempMinHeap = new FibonacciHeap();
                hTraveler = hTraveler.getChild();
                //int anotherStopKey = hTraveler.getKey();
                int l = 0;
                while(l < hTraveler.getParent().getRank()) { //hTraveler.getNext().getKey() != anotherStopKey) {
                    //hTraveler.setPosition(hTraveler.getNext());
                    tempMinHeap.insert(hTraveler.getKey());
                    tempMinHeap.getFirst().setPosition(hTraveler.getNext());
                    hTraveler = hTraveler.getNext();
                    l++;
                }
                minChildHeap.meld(tempMinHeap);
            }
            hTraveler = minChildHeap.findMin().getPosition().getPrev();
        }
        
        
        return arr;
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
        public HeapNode position;
        

        public HeapNode(int key) {
            this.key = key;
        }

        public int getKey() {
            return this.key;
        }
        
        public void setKey(int key) {
            this.key = key;
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
        
        public HeapNode getPosition() {
            return this.position;
        }
        
        public void setPosition(HeapNode position) {
            this.position = position;
        }
    }
}