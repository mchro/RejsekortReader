package info.rejsekort.reader.rkf.blocks;

import info.rejsekort.reader.rkf.datatypes.DataType;
import info.rejsekort.reader.rkf.datatypes.RKFInteger;
import info.rejsekort.reader.rkf.datatypes.ByteString;
import info.rejsekort.reader.rkf.datatypes.DateTime;

public class TCSTv4Block extends info.rejsekort.reader.rkf.TCSTBlock {

	public ByteString mIdentifier;
	public RKFInteger mVersionNumber;
	public ByteString mAID;
	public ByteString mPIX;
	public ByteString mStatus;
	public ByteString mXXX;

	public TCSTv4Block(byte[] bits) {
		super("TCSTv4", bits);

		mIdentifier = new ByteString(8, false);
		mVersionNumber = new RKFInteger(6, true);
		mAID = new ByteString(12, true);
		mPIX = new ByteString(12, true);
		mStatus = new ByteString(8, true);
		mXXX = new ByteString(138, true);
		mJourneyOriginDateTime = new DateTime(24, true);

		mFields = new DataType[] {
			mIdentifier,
			mVersionNumber,
			mAID,
			mPIX,
			mStatus,
			mXXX,
			mJourneyOriginDateTime
		};
        mFieldNames = new String[] {
			"Identifier",
			"VersionNumber",
			"AID",
			"PIX",
			"Status",
			"XXX",
			"JourneyOriginDateTime"
        };

		interpretBlock();
	}
}
