package application;

public class BinaryTree {
	char ch;
	String huffCode;
	BinaryTree left;
	BinaryTree right;

	public BinaryTree() {
		ch = '\0';
		huffCode = "";
		left = right = null;

	}

	public BinaryTree(char ch, String huffCode) {
		this.ch = ch;
		this.huffCode = huffCode;
		left = right = null;
	}

	public static BinaryTree addElt(BinaryTree tree, String st,int ind, char ch) {
		if (ind < st.length()) {

			if (st.charAt(ind) == '0') {
				if (tree.left == null)
					tree.left = new BinaryTree();
				tree.left = addElt(tree.left, st, ind + 1, ch);
			} else {
				if (tree.right == null)
					tree.right = new BinaryTree();
				tree.right = addElt(tree.right, st, ind + 1, ch);
			}
			return tree;
		} else {
			tree.ch = ch;
			return tree;
		}
	}
}
