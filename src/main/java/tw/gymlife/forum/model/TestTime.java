package tw.gymlife.forum.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.hibernate.type.descriptor.java.TimeZoneJavaType;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TestTime {

	public static void main(String[] args) {
		//用周次篩選
		Date today = new Date();
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Taipei"));
		cal.setTime(today);
		System.out.println(cal.get(Calendar.WEEK_OF_YEAR));
	}

}
