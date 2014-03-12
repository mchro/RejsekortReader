package info.rejsekort.reader.rkf.blocks;

import info.rejsekort.reader.rkf.datatypes.DataType;
import info.rejsekort.reader.rkf.datatypes.RKFInteger;
import info.rejsekort.reader.rkf.datatypes.ByteString;
import info.rejsekort.reader.rkf.datatypes.DateMonth11;

public class TCCPStaticBlock extends InterpretedBlock {

	public ByteString mIdentifier;
	public RKFInteger mVersionNumber;
	public ByteString mAID;
	public ByteString mStatus;
	public RKFInteger mCustomerNumber;
	public ByteString mXXX1;
	public RKFInteger mSequenceNumber;
	public ByteString mXXX2;
	public DateMonth11 mBirthday;
	public ByteString mXXX;

	public TCCPStaticBlock(byte[] bits) {
		super("TCCPStatic", bits);

		mIdentifier = new ByteString(8, false);
		mVersionNumber = new RKFInteger(6, true);
		mAID = new ByteString(12, true);
		mStatus = new ByteString(8, true);
		mCustomerNumber = new RKFInteger(34, true);
		mXXX1 = new ByteString(61, true);
		mSequenceNumber = new RKFInteger(8, true);
		mXXX2 = new ByteString(23, true);
		mBirthday = new DateMonth11(11, true);
		mXXX = new ByteString(256, true);

		mFields = new DataType[] {
			mIdentifier,
			mVersionNumber,
			mAID,
			mStatus,
			mCustomerNumber,
			mXXX1,
			mSequenceNumber,
			mXXX2,
			mBirthday,
			mXXX
		};
        mFieldNames = new String[] {
			"Identifier",
			"VersionNumber",
			"AID",
			"Status",
			"CustomerNumber",
			"XXX1",
			"SequenceNumber",
			"XXX2",
			"Birthday",
			"XXX"
        };

		interpretBlock();
	}
}
