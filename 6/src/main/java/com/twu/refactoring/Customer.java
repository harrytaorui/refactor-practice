package com.twu.refactoring;

import java.util.ArrayList;
import java.util.Iterator;

public class Customer {

	private String name;
	private ArrayList<Rental> rentalList = new ArrayList<Rental>();
	private static final int REGULAR_OFFSET = 2;
	private static final double REGULAR_PLUS = 2;
	private static final double CHILDREN_PLUS = 1.5;
	private static final int CHILDREN_OFFSET = 3;
	private static final double NEW_RELEASE_FACTOR = 3;
	private static final double REGULAR_CHILDREN_FACTOR = 1.5;
	
	private static final String FIGURE = "\t%s\t%.1f\n";
	private static final String FOOTER = "Amount owed is %.1f\nYou earned %d frequent renter points";

	public Customer(String name) {
		this.name = name;
	}

	public void addRental(Rental arg) {
		rentalList.add(arg);
	}

	public String getName() {
		return name;
	}

	public String statement() {
		double totalAmount = 0;
		int frequentRenterPoints = 0;
		Iterator<Rental> rentals = rentalList.iterator();
		String result = "Rental Record for " + getName() + "\n";
		while (rentals.hasNext()) {
			double thisAmount = 0;
			Rental each = rentals.next();

			thisAmount = countAmount(each);

			// add frequent renter points
			frequentRenterPoints++;
			// add bonus for a two day new release rental
			if ((each.getMovie().getPriceCode() == Movie.NEW_RELEASE)
					&& each.getDaysRented() > 1)
				frequentRenterPoints++;

			// show figures for this rental
			result += String.format(FIGURE, each.getMovie().getTitle(), thisAmount);
			totalAmount += thisAmount;

		}
		// add footer lines
		result += String.format(FOOTER, totalAmount, frequentRenterPoints);
		return result;
	}

	private double countAmount(Rental rental) {
		double thisAmount = 0;
		switch (rental.getMovie().getPriceCode()) {
			case Movie.REGULAR:
				thisAmount += REGULAR_PLUS;
				if (rental.getDaysRented() > REGULAR_OFFSET)
					thisAmount += (rental.getDaysRented() - REGULAR_OFFSET) * REGULAR_CHILDREN_FACTOR;
				break;
			case Movie.NEW_RELEASE:
				thisAmount += rental.getDaysRented() * NEW_RELEASE_FACTOR;
				break;
			case Movie.CHILDRENS:
				thisAmount += CHILDREN_PLUS;
				if (rental.getDaysRented() > CHILDREN_OFFSET)
					thisAmount += (rental.getDaysRented() - CHILDREN_OFFSET) * REGULAR_CHILDREN_FACTOR;
				break;

		}
		return thisAmount;
	}
}
