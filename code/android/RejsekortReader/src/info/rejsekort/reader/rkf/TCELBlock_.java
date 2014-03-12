package info.rejsekort.reader.rkf;

import info.rejsekort.reader.rkf.blocks.TCELBlock;

public class TCELBlock_ extends TCELBlock implements Comparable<TCELBlock_> {

	public TCELBlock_(byte[] bits) {
		super(bits);
	}

	@Override
	public int compareTo(TCELBlock_ another) {
		int i = mEventDateStamp.mDate.compareTo(another.mEventDateStamp.mDate);
		if (i == 0)
			return mEventTimeStamp.mTime.compareTo(another.mEventTimeStamp.mTime);
		return i;
	}

}
