import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represents an auto-balancing binary search tree. It provides standard methods for a binary search tree including add, remove, contains, and size.
 * @author Sahil Pal
 *
 * @param <T>	A generic type which represents the type of the "data" data field of the nodes that will be added to the tree.
 */
public class AVLTree<T extends Comparable<T>> implements Iterable<T> {
	private BSTNode<T> root;
	/**
	 * Creates an empty AVLTree object.
	 */
	public AVLTree(){
		root=null;
	}
	/**
	 * Adds a node containing the data argument to the tree.
	 * @param data	The "data" data field of the node that will be added to the tree.
	 */
	public void add(T data){
		if(root==null){		//should only occur for the first node that is being added
			BSTNode<T> addNode=new BSTNode<T>(data);
			root=addNode;
		}
		else{	//occurs every other time
			root=recAdd(root,data);
		}
	}
	/*
	 * Actual recursive method which adds a node with the data to the tree.
	 * @param node	The root of the subtree on which the method is acting.
	 * @param data	The "data" data field of the node that will be added to the tree.
	 * @return A reference to the root of the new tree after the addition and possible rebalancing.
	 */
	private BSTNode<T> recAdd(BSTNode<T> node, T data){
		if(node==null){		//base case
			BSTNode<T> addNode=new BSTNode<T>(data);
			return addNode;		
		}
		if(data.compareTo(node.getData())<0){	
			node.setLeft(recAdd(node.getLeft(),data));	//once it reaches a leaf, sets the left as addNode
		}
		else{
			node.setRight(recAdd(node.getRight(),data));//once it reaches a leaf, sets the right as addNode
		}
		updateHeight(node);
		node=balance(node);		
		return node;	//this causes backtracking up the tree, the new root after balancing is returned and set as the left or right of the node above it
	}
	/**
	 * Removes a node containing the item argument from the tree.
	 * @param item	The "data" data field of the node which is to be removed.
	 * 
	 */
	public void remove(T item){
		root=recRemove(root, item);
	}
	/*
	 * The actual recursive method that performs removal.
	 * @param node The root of the subtree that is being searched.
	 * @param item The "data" data field of the node which is to be removed.
	 * @return The root of the new tree which has performed the removal. 
	 */
	private BSTNode<T> recRemove(BSTNode<T> node, T item){
		if(node==null){	//if node is null, there was nothing to remove
			return null;
		}
		else if(item.compareTo(node.getData())<0){
			node=recRemove(node.getLeft(), item);
		}
		else if(item.compareTo(node.getData())>0){
			node=recRemove(node.getRight(), item);
		}
		else{
			node=remove(node);	//the node has been found, now it can be removed
		}
		updateHeight(node);
		node=balance(node);	
		return node;
	}
	/*
	 * Finds the predecessor of the argument.
	 * @param The node whose predecessor is being found.
	 * @return	The predecessor of the argument.
	 */
	private T getPredecessor(BSTNode<T> n){
		if(n.getLeft()==null){
			return null;
		}
		else{
			BSTNode<T> current=n.getLeft();
			while(current.getRight() != null){
				current=current.getRight();
			}
			return current.getData();
		}
	}
	/*
	 * Performs an actual removal when the node matching the data has been found.
	 * @param The node whose data is equal to the data param of the remove method.
	 * @return Returns the new node that is in the place of the now removed node.
	 */
	private BSTNode<T> remove(BSTNode<T> node){
		//no children
		if(node.getLeft()==null && node.getRight()==null){
			return null;	//there is no replacement needed
		}
		//one child
		if(node.getLeft()==null){
			return node.getRight();	//replacement must be the item that's not null
		}
		if(node.getRight()==null){
			return node.getLeft();
		}
		//two children
		T data=getPredecessor(node);
		node.setData(data);
		node.setLeft(recRemove(node.getLeft(),data));
		return node;
	}
	/**
	 * Determines whether or not a node containing the item is in the tree.
	 * @param item	The item which is being searched for.
	 * @return	Returns true if a node corresponding to the item is found, otherwise returns false.
	 */
	public boolean contains(T item){
		return recContains(item, root);
	}
	/*
	 * The actual method that recursively searches for the node.
	 * @param item	The item which is being searched for.
	 * @param currentnode	The root of the subtree being searched.
	 * @return	Returns either true or false depending on whether or not the item is found.
	 */
	private boolean recContains(T item, BSTNode<T> currentNode){
		if(currentNode==null){	//have gotten to the bottom of the tree but there is still no match
			return false;
		}
		if(currentNode.getData().compareTo(item)==0){	//match found
			return true;
		}
		else if(item.compareTo(currentNode.getData())<0){
			return recContains(item, currentNode.getLeft());
		}
		else if(item.compareTo(currentNode.getData())>0){
			return recContains(item, currentNode.getRight());
		}
		else{
			return false;
		}
	}
	/**
	 * Determines the number of nodes in the AVLTree.
	 * @return	Returns an int representing the number of nodes in the tree.
	 */
	public int size(){
		return recSize(root);
	}
	/*
	 * The recursive method that actually determines the size of the tree.
	 * @param root	The root of the current tree.
	 * @return Returns an int representing the number of nodes in the tree.
	 */
	private int recSize(BSTNode<T> root){
		if(root==null){
			return 0;
		}
		else{
			return recSize(root.getLeft())+recSize(root.getRight())+1;
		}
	}

	@Override
	/**
	 * Creates an iterator for the AVLTree and removes duplicate values.
	 * @return	The iterator object.
	 */
	public Iterator<T> iterator() {
		TreeIterator iteratorObject=new TreeIterator(this.size());
		iteratorObject.removeDuplicates();
		return (iteratorObject);
	}
	/**
	 * An object representing an iterator for the AVLTree.
	 * @author Sahil Pal
	 *
	 */
	private class TreeIterator implements Iterator<T>{
		private int current;
		private ArrayList<BSTNode<T>> referenceList;
		/**
		 * Creates a TreeIterator object which generates an ArrayList of all the nodes in the tree.
		 * @param size	The size of the tree and the size of the ArrayList.
		 */
		public TreeIterator(int size){
			current=0;
			referenceList=new ArrayList<BSTNode<T>>(size);
			recInOrder(root);	//will add the items to the reference list inOrder
		}
		/*
		 * Performs an inOrder transversal of the tree.
		 * @param node	The root of the subtree on which the method is acting.
		 */
		private void recInOrder(BSTNode<T> node){
			//this method adds all nodes to the referenceList inOrder
			if(node!=null){	
				recInOrder(node.getLeft());
				referenceList.add(node);	//adds the node once there is nothing remaining to the left
				recInOrder(node.getRight());
			}
			else{
				return;
			}
		}

		@Override
		/**
		 * Determines if there is another item in the iterator.
		 * @return Returns true if another item is in the ArrayList of the iterator. Otherwise, returns false.
		 */
		public boolean hasNext() {
			if(current+1<referenceList.size()){		
				if(referenceList.get(current+1)!=null){
					return true;
				}
				else{
					return false;
				}
			}
			else{
				return false;
			}
		}

		@Override
		/**
		 * Moves to the next item in the iterator.
		 * @return The data of the node that was just passed.
		 */
		public T next() {	//note that the method should only be called once the hasNext method returns true
			BSTNode<T> returnNode=referenceList.get(current);
			current++;
			return returnNode.getData();
			//this methods returns the value of the data of the node that current pointed to and then moves current forward by one
		}
		/**
		 * Removes duplicate values in the ArrayList.
		 */
		public void removeDuplicates(){
			int i = 1;
			while (i < referenceList.size()) {
				if (referenceList.get(i).compareTo(referenceList.get(i - 1))==0)
					referenceList.remove(i);
				else
					i++;
			}
			return;
		}
	}
	
	
	//below are all methods associated with AVL tree balancing
	/**
	 * Determines if a node is balanced. If not, calls the appropriate rotation to produce a balanced tree.
	 * @param top	The root of the current subtree.
	 * @return	A balanced subtree which may or may not have undergone rotations.
	 */
	private BSTNode<T> balance(BSTNode<T> top){		
		int bFactor=balanceFactor(top);				
		int childFactor;
		if(bFactor>=2){
			childFactor=balanceFactor(top.getRight());
			if(childFactor<0){	//checking if direction needs to be changed, otherwise it just goes further in same direction
				top=RLRotation(top);	//the root of that subtree becomes the root of the rotated subtree
			}
			else{
				top=RRRotation(top);
			}
		}
		else if(bFactor<=(-2)){
			childFactor=balanceFactor(top.getLeft());
			if(childFactor>0){
				top=LRRotation(top);
			}
			else{
				top=LLRotation(top);
			}
		}
		return top;	
	}
	/**
	 * Determines a balanceFactor of a node based on its left and right children.
	 * @param n	The node whose balanceFactor is being determined.
	 * @return	Returns an int representing the difference between the height of the right and left children.
	 */
	private int balanceFactor(BSTNode<T> n){	//a positive balance factor indicates a heavier right side
		if(n.getRight()==null){						//a negative balance factor indicates a heavier left side
			return -n.getHeight();					//to determine rotation type, we use balance factor and then use it once more
		}										//on the appropriate child. A balance factor of 1 or -1 indicates direction.
		if(n.getLeft()==null){					
			return n.getHeight();
		}
		return n.getRight().getHeight()-n.getLeft().getHeight();
	}
	/**
	 * Updates the height of a given node.
	 * @param n	The node whose height is being updated.
	 */
	private void updateHeight(BSTNode<T> n){		
		if(n.getRight()==null && n.getLeft()==null){
			n.setHeight(0);	//node is a leaf
		}
		else if(n.getLeft()==null){
			n.setHeight(n.getRight().getHeight()+1);
		}
		else if(n.getRight()==null){
			n.setHeight(n.getLeft().getHeight()+1);
		}
		else{	//height is based on the child that has the greater height
			if(n.getLeft().getHeight()>n.getRight().getHeight()){
				n.setHeight(n.getLeft().getHeight()+1);
			}
			else{
				n.setHeight(n.getRight().getHeight()+1);
			}
		}
	}
	/**
	 * Performs an LLRotation to balance a subtree.
	 * @param A	The original root of the subtree.
	 * @return	The new root of the subtree.
	 */
	private BSTNode<T> LLRotation(BSTNode<T> A){
		BSTNode<T> B = A.getLeft();
		
		A.setLeft(B.getRight());
		B.setRight(A);
		
		updateHeight(A);
		updateHeight(B);
		
		return B;
	}
	/**
	 * Performs an RRRotation to balance a subtree.
	 * @param A	The original root of the subtree.
	 * @return	The new root of the subtree.
	 */
	private BSTNode<T> RRRotation(BSTNode<T> A){	//this rotation should occur often since items are being added in sorted order
		BSTNode<T> B= A.getRight();
		A.setRight(B.getLeft());
		B.setLeft(A);					//B keeps its own right value
		
		updateHeight(A);	//this order makes sense since A becomes a child of B
		updateHeight(B);
		
		return B;
	}
	/**
	 * Performs an LRRotation to balance a subtree.
	 * @param A	The original root of the subtree.
	 * @return	The new root of the subtree.
	 */
	private BSTNode<T> LRRotation(BSTNode<T> A){
		BSTNode<T> B= A.getLeft();
		BSTNode<T> C= B.getRight();
		
		A.setLeft(C.getRight());
		B.setRight(C.getLeft());
		C.setLeft(B);
		C.setRight(A);
		
		updateHeight(A);
		updateHeight(B);
		updateHeight(C);
		
		return C;
	}
	/**
	 * Performs an RLRotation to balance a subtree.
	 * @param A	The original root of the subtree.
	 * @return	The new root of the subtree.
	 */
	private BSTNode<T> RLRotation(BSTNode<T> A){
		BSTNode<T> B= A.getRight();
		BSTNode<T> C= B.getLeft();
		
		A.setRight(C.getLeft());
		B.setLeft(C.getRight());
		C.setLeft(A);
		C.setRight(B);
		
		updateHeight(A);
		updateHeight(B);
		updateHeight(C);
		
		return A;
	}
}
