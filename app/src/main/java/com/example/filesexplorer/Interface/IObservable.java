package com.example.filesexplorer.Interface;

/**
 * 
 * <h1>Interface Observable</h1>
 * <br>
 * @see IObserver
 *
 */
public interface IObservable {
	/**
	 * Adds an observer
	 * @param observer An observer
	 */
	public void addObserver(IObserver observer);
	
	/**
	 * Removes an observer
	 * @param observer An observer
	 */
	public void removeObserver(IObserver observer);
	
	/**
	 * Notifies all observers
	 */
	public void notifyObservers();
}
