package info.rejsekort.reader.rkf.blocks;

import info.rejsekort.reader.rkf.datatypes.DataType;
import info.rejsekort.reader.rkf.datatypes.DateCompact;
import info.rejsekort.reader.rkf.datatypes.RKFInteger;
import info.rejsekort.reader.rkf.datatypes.ByteString;
import info.rejsekort.reader.rkf.datatypes.TimeCompact;

public class TCELBlock extends InterpretedBlock {

	public ByteString mIdentifier;
	public DateCompact mEventDateStamp;
	public TimeCompact mEventTimeStamp;
	public ByteString mXXX;
	public ByteString mAID;
	public RKFInteger mLocation;
	public ByteString mXXX3;
	public ByteString mStatusBits;
	public ByteString mXXX2;

	public TCELBlock(byte[] bits) {
		super("TCEL", bits);

		mIdentifier = new ByteString(8, false);
		mEventDateStamp = new DateCompact(14, true);
		mEventTimeStamp = new TimeCompact(16, true);
		mXXX = new ByteString(1, true);
		mAID = new ByteString(12, true);
		mLocation = new RKFInteger(14, true);
		mXXX3 = new ByteString(7, true);
		mStatusBits = new ByteString(6, false);
		mXXX2 = new ByteString(128, true);

		mFields = new DataType[] {
			mIdentifier,
			mEventDateStamp,
			mEventTimeStamp,
			mXXX,
			mAID,
			mLocation,
			mXXX3,
			mStatusBits,
			mXXX2
		};
        mFieldNames = new String[] {
			"Identifier",
			"EventDateStamp",
			"EventTimeStamp",
			"XXX",
			"AID",
			"Location",
			"XXX3",
			"StatusBits",
			"XXX2"
        };

		interpretBlock();
	}
}
