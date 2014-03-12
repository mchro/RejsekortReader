package info.rejsekort.reader.rkf.datatypes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateMonth11 extends RKFInteger {
	public Date mDate;
	@SuppressWarnings("deprecation")
	public static Date epoch = new Date(0, 0, 1); //1900-01-01
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
	
	public DateMonth11(int bitlength, boolean reverse) {
		super(bitlength, reverse);
	}

	@Override
	void interpret() {
		super.interpret();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(epoch);
		cal.add(Calendar.MONTH, mInt);
		mDate = cal.getTime();
	}
	
	public String getDate() {
		return sdf.format(mDate);
	}
	
	public String toString() {
		return super.toString() + " " + "DateMonth11 " + getDate();
	}

}
