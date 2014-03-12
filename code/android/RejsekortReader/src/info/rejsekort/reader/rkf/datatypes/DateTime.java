package info.rejsekort.reader.rkf.datatypes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTime extends RKFInteger {
	public Date mDate;
	@SuppressWarnings("deprecation")
	public static Date epoch = new Date(100, 0, 1);
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
	
	public DateTime(int bitlength, boolean reverse) {
		super(bitlength, reverse);
	}

	@Override
	void interpret() {
		super.interpret();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(epoch);
		cal.add(Calendar.MINUTE, mInt);
		mDate = cal.getTime();
	}
	
	public String getDate() {
		return sdf.format(mDate);
	}
	
	public String toString() {
		return "Date " + getDate();
	}
}
