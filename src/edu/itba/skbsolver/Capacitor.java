package edu.itba.skbsolver;

public class Capacitor {
	private int capacity;
	private int amount;

	public Capacitor(int amount) {
		this.capacity = amount;
		this.amount = 0;
	}

	public void reset() {
		this.amount = 0;
	}

	public void countPlus() {
		this.amount++;
	}

	public void countMinus() {
		this.amount--;
	}

	public boolean isFull() {
		return this.amount == this.capacity;
	}

	public boolean canIstepInto() {
		return this.amount < this.capacity;
	}

	public boolean isEmpty() {
		return this.amount == 0;
	}
}
