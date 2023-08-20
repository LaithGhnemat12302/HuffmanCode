package application;

public class Node implements Comparable<Node> {
	
	int freq;
	char ch;
	Node left;
	Node right;
	
	public Node(int freq, char ch) {
		super();
		this.freq = freq;
		this.ch = ch;
		this.left=null;
		this.right=null;
	}
	
	

	public Node() {
		super();
	}



	public int getFreq() {
		return freq;
	}


	public void setFreq(int freq) {
		this.freq = freq;
	}


	public char getCh() {
		return ch;
	}


	public void setCh(char ch) {
		this.ch = ch;
	}


	public Node getLeft() {
		return left;
	}


	public void setLeft(Node left) {
		this.left = left;
	}


	public Node getRight() {
		return right;
	}


	public void setRight(Node right) {
		this.right = right;
	}


	@Override
	public int compareTo(Node o) {
		// TODO Auto-generated method stub
		return this.freq-o.freq;
	}

	@Override
	public String toString() {
		return "Node [freq=" + freq + ", ch=" + ch + "]";
	}
	
	
	
}
