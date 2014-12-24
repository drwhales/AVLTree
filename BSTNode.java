/**
 * Represents a node in a binary search tree.
 * @author Sahil Pal
 *
 * @param <T>	The generic type of the data that will be stored in the node.
 */
public class BSTNode<T extends Comparable<T>> implements Comparable<BSTNode<T>>{
	private T data;
	private BSTNode<T> left;
	private BSTNode<T> right;
	private int height=0;
	/**
	 * Creates a node with the given data argument.
	 * @param data	The value of the data of the new node.
	 */
	public BSTNode(T data){
		this.setData(data);
	}
	/**
	 * Creates a node with the given data and left and right children.
	 * @param data	The value of the data of the new node.
	 * @param left	The left child of the new node.
	 * @param right	The right child of the new node.
	 */
	public BSTNode(T data, BSTNode<T> left, BSTNode<T> right){
		this.setData(data);
		this.setLeft(left);
		this.setRight(right);
	}
	
	@Override
	/**
	 * Compares a node to this node on the basis of the data field "data".
	 * @param	The node that is being compared to this one.
	 * @return	Returns 1 if this node has a greater data value, 0 if it has an equal data value, -1 if it has a lower data value.
	 */
	public int compareTo(BSTNode<T> o) {
		if(this.data.compareTo(o.data)>0){
			return 1;
		}
		else if(this.data.compareTo(o.data)==0){
			return 0;
		}
		else{
			return -1;
		}
	}
	/**
	 * Returns the "data" data field.
	 * @return	The "data" data field.
	 */
	public T getData() {
		return data;
	}
	/**
	 * Changes the value of the "data" data field.
	 * @param data	The new value of the "data" data field.
	 */
	public void setData(T data) {
		this.data = data;
	}
	/**
	 * Returns the "left" data field.
	 * @return	The "left" data field.
	 */
	public BSTNode<T> getLeft() {
		return left;
	}
	/**
	 * Changes the "left" data field.
	 * @param left	The new "left" data field.
	 */
	public void setLeft(BSTNode<T> left) {
		this.left = left;
	}
	/**
	 * Returns the "right" data field.
	 * @return	The "right" data field.
	 */
	public BSTNode<T> getRight() {
		return right;
	}
	/**
	 * Changes the "right" data field.
	 * @param right	The new "right" data field.
	 */
	public void setRight(BSTNode<T> right) {
		this.right = right;
	}
	/**
	 * Returns the "height" data field.
	 * @return	The "height" data field.
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * Changes the "height" data field.
	 * @param height	The new "height" data field.
	 */
	public void setHeight(int height) {
		this.height = height;
	}
}