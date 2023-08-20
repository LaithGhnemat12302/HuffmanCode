package application;

public class Heap {

	Node[] elt;
	int size;
	int capacity;

	public Heap() {
		this(1);
	}

	public Heap(int capacity) {
		this.capacity = capacity;
		elt = new Node[capacity];
		size = 0;
	}

	public Node[] getElt() {
		Node[] ret = new Node[size + 1];
		for (int i = 0; i < size + 1; i++)
			ret[i] = elt[i];
		return ret;
	}

	public int getSize() {
		return size;
	}

	public int getCapacity() {
		return capacity;
	}

	public boolean isEmpty() {
		if(size ==0)
			return true;
		else
			return false;
	}

	public void addElt(Node element) {
		int i = ++size;
		while ((i > 1) && elt[i / 2].getFreq() > element.getFreq()) {
			elt[i] = elt[i / 2];
			i /= 2;
		}
		elt[i] = element;
	}

	public Node deleteElt() {
		int child, i;
		Node last, min = null;
		if (size != 0) {
			min = elt[1];
			last = elt[size--];
			for (i = 1; i * 2 <= size; i = child) {
				child = i * 2;
				if (child < size
						&& elt[child].getFreq() > elt[child + 1].getFreq())
					child++;
				if (last.getFreq() > elt[child].getFreq())
					elt[i] = elt[child];
				else
					break;
			}
			elt[i] = last;
		}
		return min;
	}

}
