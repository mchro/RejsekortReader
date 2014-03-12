package info.rejsekort.reader.rkf.datatypes;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeCompact extends DataType {
	public Date mTime;
	public int hour;
	public int minute;
	public int seconds;
	public static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);

	
	public TimeCompact(int bitlength, boolean reverse) {
		super(bitlength, reverse);
	}

	@SuppressWarnings("deprecation")
	@Override
	void interpret() {
		String hourbs = mBits.substring(0, 5);
		String minutebs = mBits.substring(5, 11);
		String secondsbs = mBits.substring(11, 16);
		
		hour = new BigInteger(hourbs, 2).intValue();
		minute = new BigInteger(minutebs, 2).intValue();
		seconds = new BigInteger(secondsbs, 2).intValue() * 2;
		
		mTime = new Date(0, 0, 0, hour, minute, seconds);
	}
	
	public String getTime() {
		return sdf.format(mTime);
	}
	
	public String toString() {
		return "Time " + getTime();
	}

}
