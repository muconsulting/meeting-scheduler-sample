package net.munichconsulting.tdd.samples.meeting.impl;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

public class BookingRequest {

	private OfficeHour officeHour;

	private Map<Date, MeetingRequest> hmMeetingRequests = new TreeMap<Date, MeetingRequest>();

	public static final class OfficeHour {

		private long opening; // in minutes

		private long closing; // in minutes

		/**
		 * @return the opening
		 */
		public long getOpening() {
			return opening;
		}

		/**
		 * @param opening
		 *            the opening to set
		 */
		public void setOpening(long opening) {
			this.opening = opening;
		}

		/**
		 * @return the closing
		 */
		public long getClosing() {
			return closing;
		}

		/**
		 * @param closing
		 *            the closing to set
		 */
		public void setClosing(long closing) {
			this.closing = closing;
		}

		
		public boolean isOpenAllDay() {
			return opening == 0
					&& closing == 24*3600;
		}

	}

	public static final class MeetingRequest {

		private String employeeId;

		private Date requestedDt;

		private Date startDt;

		private Date endDt;

		/**
		 * @return the employeeId
		 */
		public String getEmployeeId() {
			return employeeId;
		}

		/**
		 * @param employeeId
		 *            the employeeId to set
		 */
		public void setEmployeeId(String employeeId) {
			this.employeeId = employeeId;
		}

		/**
		 * @return the requestedDt
		 */
		public Date getRequestedDt() {
			return requestedDt;
		}

		/**
		 * @param requestedDt
		 *            the requestedDt to set
		 */
		public void setRequestedDt(Date requestedDt) {
			this.requestedDt = requestedDt;
		}

		/**
		 * @return the startDt
		 */
		public Date getStartDt() {
			return startDt;
		}

		/**
		 * @param startDt
		 *            the startDt to set
		 */
		public void setStartDt(Date startDt) {
			this.startDt = startDt;
		}

		/**
		 * @return the endDt
		 */
		public Date getEndDt() {
			return endDt;
		}

		/**
		 * @param endDt
		 *            the endDt to set
		 */
		public void setEndDt(Date endDt) {
			this.endDt = endDt;
		}

		public boolean equals(MeetingRequest other) {
			if (other == null)
				return false;
			return DateUtils.isSameDay(this.requestedDt, other.requestedDt)
					&& DateUtils.isSameDay(this.startDt, other.startDt)
					&& DateUtils.isSameDay(this.endDt, other.endDt)
					&& StringUtils.equals(this.employeeId, other.employeeId);
		}

		protected boolean contains(Date value) {
			return value.getTime() > startDt.getTime()
					&& value.getTime() <= endDt.getTime();
		}

		protected boolean overlapsWith(MeetingRequest other) {
			return other.contains(this.startDt) || other.contains(this.endDt);
		}

	}

	/**
	 * @return the officeHour
	 */
	public OfficeHour getOfficeHour() {
		return officeHour;
	}

	/**
	 * @param officeHour
	 *            the officeHour to set
	 */
	public void setOfficeHour(OfficeHour officeHour) {
		this.officeHour = officeHour;
	}

	/**
	 * @return a sorted list of {@code MeetingRequest}.
	 */
	public Collection<MeetingRequest> getMeetingRequests() {

		// result must be sorted by meeting start
		Map<Date, MeetingRequest> result = new TreeMap<Date, MeetingRequest>();

		// Values are sorted by request date.
		// Older requests prevent newer requests from applying if overlapping
		for (MeetingRequest request : hmMeetingRequests.values()) {
			// are there any older requests overlapping?
			boolean overlaps = overlaps(request, result.values());
			if (!overlaps)
				result.put(request.getStartDt(), request);
		}

		return result.values();
	}

	private boolean overlaps(MeetingRequest request,
			Collection<MeetingRequest> values) {
		for (MeetingRequest entry : values) {
			if (entry.overlapsWith(request))
				return true;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public void addMeetingRequest(final MeetingRequest meetingRequest) {

		// Meeting across days: they are OK if we are open 24/7
		// TODO: check for weekends :-)
		if (this.getOfficeHour().isOpenAllDay()) {
			hmMeetingRequests.put(meetingRequest.getRequestedDt(),
					meetingRequest);
			return;
		}

		if ((meetingRequest.getEndDt().getHours() - meetingRequest.getStartDt()
				.getHours()) > 24) {
			return;
		}
		
		
		
		// Not across days, and not open 24/7: meetings only inside office hours
		if ((meetingRequest.getStartDt().getHours() * 60 + meetingRequest.getStartDt().getMinutes() < this.getOfficeHour().getOpening()) 
				|| (meetingRequest.getEndDt().getHours() * 60 + meetingRequest.getEndDt().getMinutes() > this.getOfficeHour().getClosing()))
			return;
		
		hmMeetingRequests.put(meetingRequest.getRequestedDt(), meetingRequest);

	}

}
