package info.rejsekort.reader.rkf.blocks;

import info.rejsekort.reader.rkf.datatypes.DataType;
import info.rejsekort.reader.rkf.datatypes.RKFInteger;
import info.rejsekort.reader.rkf.datatypes.DateTime;
import info.rejsekort.reader.rkf.datatypes.ByteString;
import info.rejsekort.reader.rkf.datatypes.MoneyAmount24;

public class TCSTv5Block extends info.rejsekort.reader.rkf.TCSTBlock {

	public ByteString mIdentifier;
	public RKFInteger mVersionNumber;
	public ByteString mAID;
	public ByteString mPIX;
	public ByteString mStatus;
	public ByteString mXXX;
	public MoneyAmount24 mValue;
	public ByteString mXXX2;
	public RKFInteger mLocation;
	public ByteString mXXX3;

	public TCSTv5Block(byte[] bits) {
		super("TCSTv5", bits);

		mIdentifier = new ByteString(8, false);
		mVersionNumber = new RKFInteger(6, true);
		mAID = new ByteString(12, true);
		mPIX = new ByteString(12, true);
		mStatus = new ByteString(8, true);
		mXXX = new ByteString(47, true);
		mValue = new MoneyAmount24(24, true);
		mXXX2 = new ByteString(64, true);
		mLocation = new RKFInteger(14, true);
		mJourneyOriginDateTime = new DateTime(24, true);
		mXXX3 = new ByteString(76, true);

		mFields = new DataType[] {
			mIdentifier,
			mVersionNumber,
			mAID,
			mPIX,
			mStatus,
			mXXX,
			mValue,
			mXXX2,
			mLocation,
			mJourneyOriginDateTime,
			mXXX3
		};
        mFieldNames = new String[] {
			"Identifier",
			"VersionNumber",
			"AID",
			"PIX",
			"Status",
			"XXX",
			"Value",
			"XXX2",
			"Location",
			"JourneyOriginDateTime",
			"XXX3"
        };

		interpretBlock();
	}
}
