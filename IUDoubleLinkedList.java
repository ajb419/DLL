import java.util.Iterator; 
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 *  
 * 
 * @author adamberridge
 *
 * @param <T> generic type of object stored in list
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {

	private DLLNode<T> head, tail; // first/last node in linked list
	private DLLNode<T> end; // dummy node at end of implementation
	private int count; // number of elements in list
	private int modCount; // number of modificiations to the single linked list

	/**
	 * default constructor
	 */
	public IUDoubleLinkedList() {
		count = 0;
		modCount = 0;
		head = null;
		tail = null;
		end = new DLLNode<T>(null);
	}

	@Override
	public void addToFront(T element) {

		DLLNode<T> newNode = new DLLNode<T>(element);
		newNode.setNext(head);

		if (count == 0) { // if empty
			tail = newNode;
			head = newNode;
			newNode.setNext(end);
			end.setPrevious(newNode);
		} else {
			head.setPrevious(newNode);
			head = newNode;
		}
		count++;
		modCount++;

	}

	@Override
	public void addToRear(T element) {

		add(element);

	}

	@Override
	public void add(T element) {

		DLLNode<T> newNode = new DLLNode<T>(element);
		if (count == 0) // if empty
		{
			head = newNode;
		} else {
			tail.setNext(newNode);
			newNode.setPrevious(tail);
		}
		newNode.setNext(end);
		end.setPrevious(newNode);
		tail = newNode;
		count++;
		modCount++;

	}

	@Override
	public void addAfter(T element, T target) {

		int index = indexOf(target);
		// check if target is in list
		if (index == -1) {
			throw new NoSuchElementException("IUArrayList - addAfter - Element not found");
		}
		if (index == count - 1) // if target == tail
		{
			add(element);
		} else {
			DLLNode<T> current = head;
			// iterate to index
			for (int i = 0; i < index; i++) {
				current = current.getNext();
			}

			DLLNode<T> newNode = new DLLNode<T>(element);
			DLLNode<T> next = current.getNext();
			current.setNext(newNode);
			newNode.setNext(next);
			newNode.setPrevious(current);
			next.setPrevious(newNode);
			count++;
			modCount++;
		}

	}

	@Override
	public void add(int index, T element) {

		// check if index is in bounds
		if (index < 0 || index > count) {
			throw new IndexOutOfBoundsException("IUSingleLinkedlist - add(index, element) - index is out of bounds");
		}
		// if adding to end of list
		if (index == count) {
			add(element);
		} else if (index == 0) // add to the front
		{
			addToFront(element);

		} else {

			DLLNode<T> newNode = new DLLNode<T>(element);
			DLLNode<T> current = head;
			// iterate to the node before the index reference
			for (int i = 0; i < index - 1; i++) {
				current = current.getNext();
			}
			DLLNode<T> next = current.getNext();
			current.setNext(newNode);
			newNode.setNext(next);
			newNode.setPrevious(current);
			next.setPrevious(newNode);

			count++;
			modCount++;
		}
	}

	@Override
	public T removeFirst() {

		if (isEmpty()) {
			throw new NoSuchElementException("IUSingleLinkedlist - removeFirst - list is empty");
		}
		T theRemoved = head.getElement();
		// If removing last item
		if (count == 1) {
			tail = null;
			head = null;
		} else {

			DLLNode<T> next = head.getNext();
			head.setNext(null);
			head = next;
		}
		count--;
		modCount++;
		return theRemoved;

	}

	@Override
	public T removeLast() {

		if (isEmpty()) {
			throw new NoSuchElementException("IUSingleLinkedlist - removeFirst - list is empty");
		}
		T theRemoved = tail.getElement();
		// If removing last item
		if (count == 1) {
			tail = null;
			head = null;
		} else {
			DLLNode<T> previous = tail.getPrevious();
			previous.setNext(end);
			end.setPrevious(previous);
			tail.setNext(null);
			tail = previous;

		}
		count--;
		modCount++;
		return theRemoved;
	}

	@Override
	public T remove(T element) {

		int index = indexOf(element);
		// if element not in list
		if (index == -1) {
			throw new NoSuchElementException("IUSingleLinkedlist - remove(element) - element not in list");
		}
		return remove(index);
	}

	@Override
	public T remove(int index) {

		// if index out of bounds
		if (index < 0 || index >= count) {
			throw new IndexOutOfBoundsException("IUSingleLinkedlist - remove(index) - index is out of bounds");
		}
		// if removing first element
		if (index == 0) {
			return removeFirst();
		}
		// if removing last element
		else if (index == count - 1) {
			return removeLast();
		}

		else {
			DLLNode<T> current = head;
			// iterate to the node before the node to be removed
			for (int i = 0; i < index - 1; i++) {
				current = current.getNext();
			}

			DLLNode<T> next = current.getNext();
			DLLNode<T> after = next.getNext();
			current.setNext(after);
			after.setPrevious(current);
			T theRemoved = next.getElement();
			next.setNext(null);

			count--;
			modCount++;
			return theRemoved;
		}
	}

	@Override
	public void set(int index, T element) {
		// check if index is in bounds
		if (index < 0 || index >= count) {
			throw new IndexOutOfBoundsException("IUSingleLinkedList - set - invalid index");
		}

		DLLNode<T> current = head;
		// iterate to the node to be set
		for (int i = 0; i < index; i++) {
			current = current.getNext();
		}
		current.setElement(element);
		modCount++;

	}

	@Override
	public T get(int index) {
		// check if index is in bounds
		if (index < 0 || index >= count) {
			throw new IndexOutOfBoundsException("IUDoubleLinkedList - get - invalid index");
		}
		DLLNode<T> current = head;
		// iterate to node at index
		for (int i = 0; i < index; i++) {
			current = current.getNext();
		}
		return current.getElement();
	}

	@Override
	public int indexOf(T element) {

		int index = 0;
		boolean found = false;
		DLLNode<T> current = head;

		while (!found && index < count) // continue until found or end of list
		{
			if (current.getElement() == element) {
				found = true;
			} else {
				index++;
				current = current.getNext();
			}
		}
		if (!found) {
			index = -1;
		}

		return index;
	}

	@Override
	public T first() {
		if (isEmpty()) {
			throw new NoSuchElementException("IUSingleLinkedList - first - invalid element");
		}
		return head.getElement();
	}

	@Override
	public T last() {
		if (isEmpty()) {
			throw new NoSuchElementException("IUSingleLinkedList - last - invalid element");
		}
		return tail.getElement();
	}

	@Override
	public boolean contains(T target) {

		int index = 0;
		boolean found = false;
		DLLNode<T> current = head;

		while (!found && index < count) // continue until found or end of list
		{
			if (current.getElement() == target) {
				found = true;
			} else {
				index++;
				current = current.getNext();
			}
		}
		return found;
	}

	@Override
	public boolean isEmpty() {

		return count == 0;
	}

	@Override
	public int size() {

		return count;
	}

	@Override
	public Iterator<T> iterator() {
		return (Iterator<T>) new AListIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		return new AListIterator();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		// check if index is in bounds
		if (startingIndex < 0 || startingIndex > count) {
			throw new IndexOutOfBoundsException("IUDoubleLinkedList - AListIterator(startingIndex) - invalid index");
		}

		else
			return new AListIterator(startingIndex);

	}

	/**
	 * Iterator for the IUDoubleLinkedList Class
	 * 
	 * @author adamberridge
	 */
	private class AListIterator implements ListIterator<T> {

		private DLLNode<T> next, current, previous; // nodes containing reference to next, current, and previous objects
		private int itrModCount; // number of modifications made to list when iterator instantiated
		private int index; // index of node that would be returned by a call to next
		private boolean canRemove, previousCall, nextCall; // precondition checks

		/**
		 * Default constructor
		 */
		public AListIterator() {
			current = null;
			next = head;
			previous = null;
			itrModCount = modCount;
			canRemove = false;
			index = 0;

		}

		/**
		 * constructor that takes startingIndex parameter
		 * 
		 * @param startingIndex - index to start listIterator at
		 */
		public AListIterator(int startingIndex) {
			next = head;
			previous = null;
			for (int i = 0; i < startingIndex; i++) { // iterate to intended starting index
				next = next.getNext();
			}
			if (next != null) { //check if list is empty
				if (next.getPrevious() != null) { // check and set current to node before next
					current = next.getPrevious();
					if (current.getPrevious() != null) { // check and set previous to node before current
						previous = current.getPrevious();
					}
				}
			}
			itrModCount = modCount;
			canRemove = false;
			index = 0;

		}

		@Override
		public boolean hasNext() {
			if (count == 0) { //if list is empty
				return false;
			}
			return (next != end);
		}

		@Override
		public T next() {
			
			if (modCount != itrModCount) {
				throw new NoSuchElementException("ListIterator - next - end of iteration");
			}

			if (!hasNext()) // Check precondition
			{
				throw new NoSuchElementException("AListIterator - next - end of iteration");
			}
			T item = next.getElement(); // element to return
			previous = current;
			current = next;
			next = next.getNext();
			canRemove = true;
			nextCall = true;
			previousCall = false;
			index++;
			return item;
		}

		public void remove() {

			if (modCount != itrModCount) {
				throw new NoSuchElementException("AListIterator - remove - end of iteration");
			}
			if (!canRemove) {
				throw new IllegalStateException("AListIterator - remove - can't remove");
			}
			if (nextCall) { // if removing next

				// if removing first element
				if (current.equals(head)) {

					head = next;
					head.setPrevious(null);
					current.setNext(null);
					current = null;
				}
				// if removing last element
				else if (current.equals(tail)) {

					tail = previous;
					tail.setNext(end);
					end.setPrevious(tail);
					current.setNext(null);
					;
					current = previous;
					if (previous != null) { // check and set previous to node before itself
						previous = previous.getPrevious();
					}
				} else {

					previous.setNext(next);
					next.setPrevious(previous);
					current.setNext(null);
					current = previous;
					if (previous != null) { // check and set previous to node before itself
						previous = previous.getPrevious();
					}
				}
			}

			else { // removing previous

				// if removing first element
				if (next.equals(head)) {
					next = next.getNext();
					head.setNext(null);
					head = next;
					head.setPrevious(null);

				}
				// if removing last element
				else if (next.equals(tail)) {

					tail = current;
					tail.setNext(end);
					end.setPrevious(tail);
					next.setNext(null);
					next = current.getNext();

				} else {
					DLLNode<T> onePastNext = next.getNext();
					current.setNext(onePastNext);
					onePastNext.setPrevious(current);
					next.setNext(null);
					next = onePastNext;

				}

			}
			count--;
			previousCall = false;
			nextCall = false;
			canRemove = false;
		}

		@Override
		public boolean hasPrevious() {

			return (current != null);
		}

		@Override
		public T previous() {
			if (modCount != itrModCount) {
				throw new NoSuchElementException("ListIterator - next - end of iteration");
			}

			if (!hasPrevious()) // Check precondition
			{
				throw new NoSuchElementException("AListIterator - next - end of iteration");
			}
			T item = current.getElement(); // element to return
			next = current;
			current = previous;
			if (previous != null) { // check and set previous to the node before itself
				previous = previous.getPrevious();
			}
			canRemove = true;
			previousCall = true;
			nextCall = false;
			index--;
			return item;
		}

		@Override
		public int nextIndex() {
			return index;
		}

		@Override
		public int previousIndex() {
			if (index == 0) {
				return -1;
			}
			return (index - 1);
		}

		@Override
		public void set(T e) {
			//check next precondition
			if (nextCall) { 
				current.setElement(e);
				//check previous precondition
			} else if (previousCall) {
				next.setElement(e);
			} else {
				throw new IllegalStateException("AListIterator - set - can't set next or previous");
			}
		}

		@Override
		public void add(T e) {
			DLLNode<T> newNode = new DLLNode<T>(e); //node to add
			// if list is empty
			if (count == 0) {
				head = newNode;
				tail = newNode;
				tail.setNext(end);
				end.setPrevious(tail);
				next = head.getNext();
				current = head;
			}
			// if adding to beginning of list
			else if (current == null) {
				next.setPrevious(newNode);
				newNode.setNext(next);
				current = newNode;
			} else {
				current.setNext(newNode);
				newNode.setNext(next);
				next.setPrevious(newNode);
				newNode.setPrevious(current);
				previous = current;
				current = newNode;
			}

			count++;
			index++;
			previousCall = false;
			nextCall = false;
			canRemove = false;
		}

	}
}
