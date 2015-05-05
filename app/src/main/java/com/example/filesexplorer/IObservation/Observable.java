package com.example.filesexplorer.IObservation;

/**
 * 
 * <h1>Interface Observable</h1>
 * <br>
 * @see Observer
 *
 */
public interface Observable {
	/**
	 * Adds an observer
	 * @param observer An observer
	 */
	public void addObserver(Observer observer);
	
	/**
	 * Removes an observer
	 * @param observer An observer
	 */
	public void removeObserver(Observer observer);
	
	/**
	 * Notifies all observers
	 */
	public void notifyObservers();
}
