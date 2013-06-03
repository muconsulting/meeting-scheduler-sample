package net.munichconsulting.tdd.samples.meeting.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.munichconsulting.tdd.samples.meeting.Constants;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

public class BookingRequestReader {

	public static final class OfficeHourReader {

		public BookingRequest.OfficeHour parse(final String header)
				throws IllegalArgumentException, ParseFormatException {

			if (StringUtils.isEmpty(header)) {
				throw new IllegalArgumentException(
						"header can't be null or empty.");
			}
			// format:
			if (header.length() != 9) {
				throw new ParseFormatException(
						"Header format: 'hhmm hhmm', with length = 9");
			}

			BookingRequest.OfficeHour officeHour = new BookingRequest.OfficeHour();
			
			officeHour.setOpening(quickFormat(header.substring(0, 4)));
			officeHour.setClosing(quickFormat(header.substring(5)));

			return officeHour;
		}
		
		private long quickFormat(String value) {
			long hourInMinute = Long.parseLong(value.substring(0, 2)) * 60;
			long minutes = Long.parseLong(value.substring(2));
			return hourInMinute + minutes ;
		}
	}

	
	public static final class MeetingRequestReader {

		private static Date toDate(final String pattern, final String value)
				throws ParseException {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
			return sdf.parse(value);
		}

		public BookingRequest.MeetingRequest parse(final String line1, final String line2)
				throws IllegalArgumentException, ParseFormatException {

			if (StringUtils.isEmpty(line1) || line1.length() < 21) {
				throw new ParseFormatException("Invalid record");
			}

			if (StringUtils.isEmpty(line2) || line2.length() < 18) {
				throw new ParseFormatException("Invalid record");
			}

			BookingRequest.MeetingRequest request = new BookingRequest.MeetingRequest();
			try {
				Date requestDt = toDate(Constants.REQUEST_SUBMISSION_FORMAT,
						line1.substring(0, 19));
				request.setRequestedDt(requestDt);
				request.setEmployeeId(line1.substring(20));
				
				Date startDt = toDate(Constants.MEETING_START_TIME_FORMAT,
						line2.substring(0, 16));
				request.setStartDt(startDt);

				int nbHour = Integer.parseInt(line2.substring(17));
				request.setEndDt(DateUtils.addHours(request.getStartDt(), nbHour));

			} catch (ParseException pe) {
				throw new ParseFormatException(pe);
			}

			return request;
		}

	}
	
	public BookingRequest parse(final String message)
			throws IllegalArgumentException, ParseFormatException {
// TODO Code never reached - due to earlier catch up. 
//		if (StringUtils.isEmpty(message)) {
//			throw new IllegalArgumentException(
//					"message can't be null or empty.");
//		}

		BookingRequest booking = new BookingRequest();

		List<String> lines = Arrays.asList(message.split("\n"));

		// read the office hour.
		BookingRequest.OfficeHour officeHour = new OfficeHourReader()
				.parse(lines.get(0));
		booking.setOfficeHour(officeHour);

		// read all the meeting requests
		if (lines.size() > 1) {
			for (int i = 1; i < lines.size(); i += 2) {
				if (i + 1 >= lines.size()) {
					throw new ParseFormatException("invalid message structure");
				}
				BookingRequest.MeetingRequest meetingRequest = new MeetingRequestReader()
						.parse(lines.get(i), lines.get(i + 1));
				
				booking.addMeetingRequest(meetingRequest);
			}
		}

		return booking;
	}

}
