// --== CS400 File Header Information ==--
// Name: Ishika Mukherjee
// Email: imukherjee@wisc.edu
// Team: red
// Group: FF
// TA: Daniel
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>


import java.util.LinkedList;
import java.util.NoSuchElementException;

public class HashTableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {
	
	/**
	 * inner class to allow for the array instance field in the outer class (list) to have multiple types in it 
	 */
	private class Node {
		private KeyType key;
		private ValueType value;
		
		public Node(KeyType key, ValueType value) {
			this.key = key;
			this.value = value;
		}
		
		public KeyType getKey() { return key; }
		
		public ValueType getValue() { return value; }
	}

	private LinkedList<Node>[] list;
	private int size;
	
	
	/**
	 * default constructor that sets the length of this HashTableMap to 10
	 */
	public HashTableMap() {
		this.list = (LinkedList<Node>[]) new LinkedList<?>[10];
		this.size = 0;
	}
	
	
	/**
	 * constructor with parameter of capacity for this HashTableMap
	 * 
	 * @param capacity the capacity of this HashTableMap
	 */
	public HashTableMap(int capacity) {
		this.list = (LinkedList<Node>[]) new LinkedList<?>[capacity];
		this.size = 0;
	}
	
	
	////////// INTERFACE METHODS //////////

	
	/**
	 * stores new values in this HashTableMap at the index corresponding the the absolute value of this key's hashCode()
	 * 
	 * @param key
	 * @param value
	 * @return true if successfully stored a new key-value pair, false otherwise
	 */
	@Override
	public boolean put(KeyType key, ValueType value) {
		// null check / if key is already stored
		if(key == null || containsKey(key))
			return false;

		int index = getBucketNumber(key);
		if(list[index] == null) { // if there is no LinkedList at hash's index of the array
			LinkedList<Node> chain = new LinkedList<Node>();
			chain.add(new Node(key, value));
			list[index] = chain;
		} else { // there is a collision -- add key-value pair to end of LinkedList
			list[index].add(new Node(key, value));
		}
		size++;
		
		// resize & rehash if needed
		if(getLoadCapacity() >= 0.85) {
			this.list = resize();
		}
		
		return true;
	}

	
	/**
	 * finds and returns a given ValueType given its paired KeyType
	 */
	@Override
	public ValueType get(KeyType key) throws NoSuchElementException {
		int index = getBucketNumber(key);
		LinkedList<Node> chain = list[index];
		
		if(chain != null) {
			for(int i = 0; i<chain.size(); i++) {
				if(chain.get(i).getKey().equals(key))
					return chain.get(i).getValue();
			}
		}
		
		throw new NoSuchElementException();
	}

	
	/**
	 * @return the number of key-value pairs stored in this HashTableMap
	 */
	@Override
	public int size() {
		return size;
	}

	
	/**
	 * determines whether or not a key is in this HashTableMap
	 * 
	 * @return true if found, false otherwise
	 */
	@Override
	public boolean containsKey(KeyType key) {
		
		for(int i = 0; i<list.length; i++) {
			if(list[i] != null) {
				for(int j = 0; j<list[i].size(); j++) {
					if(key.equals(list[i].get(j).getKey()))
						return true;
				}
			}
		}
		
		return false;
	}

	
	/**
	 * removes the given key from this HashTableMap
	 * 
	 * @param key the key to remove
	 * @return a reference to the value associated with the key being removed
	 */
	@Override
	public ValueType remove(KeyType key) {
		
		int index = getBucketNumber(key);
		ValueType result;
		
		if(list[index] == null)
			return null;
		else {
			for(int i = 0; i<list[index].size(); i++) {
				if(key.equals(list[index].get(i).getKey())) {
					result = list[index].get(i).getValue();
					list[index].remove(i);
					size--;
					return result;
				}
			}
		}
		return null;
	}

	
	/**
	 * removes all key-value pairs from this HashTableMap
	 */
	@Override
	public void clear() {
		int capacity = list.length;
		this.list = (LinkedList<Node>[]) new LinkedList<?>[capacity];
		this.size = 0;
	}
	
	
	////////// (PRIVATE) HELPER METHODS //////////
	
	
	/**
	 * calucates the load capacity of this HashTableMap
	 * 
	 * @return the percentage (in decimal representation) of the load capacity
	 */
	private double getLoadCapacity() {
		return (double) size() / list.length;
	}
	
	
	/**
	 * resizes the array
	 * @return the updated HashTableMap
	 */
	private LinkedList<Node>[] resize() {
		
		// create new hash table with double size
		HashTableMap htm = new HashTableMap(2*list.length);
		
		for(int i=0; i<list.length; i++) {
			if(list[i] != null) {
				for(int j=0; j<list[i].size(); j++) {
					htm.put(list[i].get(j).getKey(), list[i].get(j).getValue());
				}
			}
		}
		return htm.list;
	}
	
	
	/**
	 * computes the hash for each key
	 * 
	 * @param key the key to find the hash for
	 * @return the index number of the key
	 */
	private int getBucketNumber(KeyType key) {
		return Math.abs(key.hashCode()) % list.length;
	}
}
