package net.munichconsulting.tdd.samples.meeting.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import net.munichconsulting.tdd.samples.meeting.Constants;
import net.munichconsulting.tdd.samples.meeting.IBookingSystem;
import net.munichconsulting.tdd.samples.meeting.impl.BookingRequest.MeetingRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

public class DefaultBookingSystem implements IBookingSystem {

	public String generateCalendar(final String message)
			throws IllegalArgumentException, ParseFormatException {
		if (StringUtils.isEmpty(message)) {
			throw new IllegalArgumentException(
					"message can't be null or empty.");
		}

		BookingRequest booking = new BookingRequestReader().parse(message);
		return outputAsString(booking);
	}

	private String outputAsString(final BookingRequest booking) {

		Date today = Calendar.getInstance().getTime();

		StringBuffer builder = new StringBuffer();

		Collection<MeetingRequest> result = booking.getMeetingRequests();

		for (MeetingRequest meeting : result) {
			if (!DateUtils.isSameDay(meeting.getStartDt(), today)) {
				today = meeting.getStartDt();
				builder.append(
						formattedDate(Constants.OUTPUT_DAY_FORMAT, today))
						.append("\n");
			}
			builder.append(
					formattedDate(Constants.OUTPUT_HOUR_FORMAT,
							meeting.getStartDt()))
					.append(" ")
					.append(formattedDate(Constants.OUTPUT_HOUR_FORMAT,
							meeting.getEndDt())).append(" ")
					.append(meeting.getEmployeeId()).append("\n");
		}
		return builder.toString();
	}

	private String formattedDate(String pattern, Date value) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(value);
	}

}
