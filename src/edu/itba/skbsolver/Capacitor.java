package edu.itba.skbsolver;

public class Capacitor {
	private int capacity;
	private int amount;
	
	public Capacitor(int amount){
		this.capacity = amount;
		this.amount = 0;
	}
	
	public void reset(){
		this.amount = 0;
	}
	
	public void countPlus(){
		this.amount++;
	}
	
	public boolean isFull(){
		return this.amount == this.capacity;
	}
}
