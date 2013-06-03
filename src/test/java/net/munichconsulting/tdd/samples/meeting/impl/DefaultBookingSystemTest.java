package net.munichconsulting.tdd.samples.meeting.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DefaultBookingSystemTest {

	public static final String OFFICE_HOUR_INFO = "0900 1730\n";

	public static final String FIRST_BOOKING_REQUEST = "2011-03-17 10:17:06 EMP001\n"
			+ "2011-03-21 09:00 2\n";

	public static final String SECOND_BOOKING_REQUEST = "2011-03-16 12:34:56 EMP002\n"
			+ "2011-03-21 09:00 2\n";

	public static final String THIRD_BOOKING_REQUEST = "2011-03-16 09:28:23 EMP003\n"
			+ "2011-03-22 14:00 2\n";

	public static final String FOURTH_BOOKING_REQUEST = "2011-03-17 11:23:45 EMP004\n2011-03-22 16:00 1\n";

	public static final String FIFTH_BOOKING_REQUEST = "2011-03-15 17:29:12 EMP005\n2011-03-21 16:00 3\n";

	public static final String INPUT_BOOKING_LINES = FIRST_BOOKING_REQUEST
			+ SECOND_BOOKING_REQUEST + THIRD_BOOKING_REQUEST
			+ FOURTH_BOOKING_REQUEST + FIFTH_BOOKING_REQUEST;

	static final String CORRECT_INPUT = OFFICE_HOUR_INFO + INPUT_BOOKING_LINES;

	static final String CORRECT_OUTPUT = "2011-03-21\n"
			+ "09:00 11:00 EMP002\n" + "2011-03-22\n" + "14:00 16:00 EMP003\n"
			+ "16:00 17:00 EMP004\n" + "";

	private DefaultBookingSystem service;

	@Test(expected = IllegalArgumentException.class)
	public void shouldRaisedException() throws IllegalArgumentException,
			ParseFormatException {
		service = new DefaultBookingSystem();
		service.generateCalendar(null);
	}
	
	@Test(expected = ParseFormatException.class)
	public void shouldRaisedParseFormatException() throws IllegalArgumentException,
			ParseFormatException {
		service = new DefaultBookingSystem();
		service.generateCalendar(OFFICE_HOUR_INFO + "09:00 11:00 EMP002\n");
	}

	@Test(expected = ParseFormatException.class)
	public void shouldRaisedParseFormatDateException() throws IllegalArgumentException,
			ParseFormatException {
		service = new DefaultBookingSystem();
		service.generateCalendar(OFFICE_HOUR_INFO + "2011-03-17 11:23:45 EMP004\n2011-FF-22 16:00 1\n");
	}
	

	@Test
	public void shouldGenerateBookingOK() throws IllegalArgumentException,
			ParseFormatException {
		service = new DefaultBookingSystem();
		String result = service.generateCalendar(CORRECT_INPUT);
		assertEquals(CORRECT_OUTPUT, result);
	}


	@Test
	public void shouldGenerateEmptyBooking() throws IllegalArgumentException,
			ParseFormatException {
		service = new DefaultBookingSystem();
		String result = service.generateCalendar(OFFICE_HOUR_INFO);
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void shouldMeetingNotOverlap() throws IllegalArgumentException,
			ParseFormatException {

		String firstMeeting = "2011-03-10 12:00:00 EMP001\n"
				+ "2011-03-21 16:00 1\n";

		String secondMeeting = "2011-03-15 12:00:00 EMP001\n"
				+ "2011-03-21 15:00 2\n";

		service = new DefaultBookingSystem();
		String result = service.generateCalendar(OFFICE_HOUR_INFO
				+ firstMeeting + secondMeeting);

		// then
		assertEquals("2011-03-21\n" + "16:00 17:00 EMP001\n", result);
	}

	
	@Test
	public void shouldRequestProcessedInSubmitOrder() throws IllegalArgumentException, ParseFormatException {
	    String firstMeeting = "2011-03-17 12:00:00 EMP001\n" +
	                          "2011-03-21 16:00 1\n";

	    String secondMeeting = "2011-03-16 12:00:00 EMP001\n" +
	                           "2011-03-21 15:00 2\n";

		service = new DefaultBookingSystem();
		String result = service.generateCalendar(OFFICE_HOUR_INFO
				+ firstMeeting + secondMeeting);

	    assertEquals("2011-03-21\n15:00 17:00 EMP001\n", result);
	}
	
	@Test
	public void noPartOfMeetingMayFallOutsideOfficeHours() throws IllegalArgumentException, ParseFormatException {
	    //given
	    String tooEarlyBooking = "2011-03-16 12:00:00 EMP001\n" +
	                             "2011-03-21 06:00 2\n";

	    String tooLateBooking = "2011-03-16 12:00:00 EMP001\n" +
	                            "2011-03-21 20:00 2\n";

	    service = new DefaultBookingSystem();
		String result = service.generateCalendar(OFFICE_HOUR_INFO
				+ tooEarlyBooking + tooLateBooking);

	    assertTrue(result.isEmpty());
	}
	
	@Test
	public void testOrorderingOfMeetingRequestShouldNotAffectOutput() throws IllegalArgumentException, ParseFormatException {

		service = new DefaultBookingSystem();
		String result = service.generateCalendar(OFFICE_HOUR_INFO + FIFTH_BOOKING_REQUEST
				+ FOURTH_BOOKING_REQUEST + THIRD_BOOKING_REQUEST
				+ SECOND_BOOKING_REQUEST + FIRST_BOOKING_REQUEST);
	    
		assertEquals(CORRECT_OUTPUT, result);
	}   
}
