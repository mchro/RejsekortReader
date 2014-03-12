package info.rejsekort.reader.rkf;

import info.rejsekort.reader.rkf.blocks.InterpretedBlock;
import info.rejsekort.reader.rkf.datatypes.DateTime;

public abstract class TCSTBlock extends InterpretedBlock implements Comparable<TCSTBlock> {

	public DateTime mJourneyOriginDateTime;

	
	public TCSTBlock(String pid, byte[] bits) {
		super(pid, bits);
	}

	@Override
	public int compareTo(TCSTBlock another) {
		int i = mJourneyOriginDateTime.mDate.compareTo(another.mJourneyOriginDateTime.mDate);
		if (i == 0)
			return mJourneyOriginDateTime.mDate.compareTo(another.mJourneyOriginDateTime.mDate);
		return i;
	}
}
