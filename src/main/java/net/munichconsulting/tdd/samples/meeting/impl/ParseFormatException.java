package net.munichconsulting.tdd.samples.meeting.impl;


public class ParseFormatException extends Exception {

	/** */
	private static final long serialVersionUID = 1L;
	
	public ParseFormatException(final String message) {
		super(message);
	}

	public ParseFormatException(Throwable pe) {
		super(pe);
	}

}
