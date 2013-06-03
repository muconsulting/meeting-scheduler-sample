package net.munichconsulting.tdd.samples.meeting;

import net.munichconsulting.tdd.samples.meeting.impl.ParseFormatException;

public interface IBookingSystem {

	String generateCalendar(final String message)
			throws IllegalArgumentException, ParseFormatException;

}
