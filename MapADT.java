// --== CS400 File Header Information ==--
// Name: Ishika Mukherjee
// Email: imukherjee@wisc.edu
// Team: Red
// Group: FF
// TA: Daniel
// Lecturer: Gary
// Notes to Grader: I am aware that this is unfinished and not fully functional- I completely underestimated how long it would take to write and I will be starting earlier next time.

import java.util.NoSuchElementException;

public interface MapADT<KeyType, ValueType> {

	public boolean put(KeyType key, ValueType value);
	public ValueType get(KeyType key) throws NoSuchElementException;
	public int size();
	public boolean containsKey(KeyType key);
	public ValueType remove(KeyType key);
	public void clear();
	
}