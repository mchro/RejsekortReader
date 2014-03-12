package info.rejsekort.reader.rkf.blocks;

import info.rejsekort.reader.rkf.datatypes.DataType;
import info.rejsekort.reader.rkf.datatypes.RKFInteger;
import info.rejsekort.reader.rkf.datatypes.DateCompact;
import info.rejsekort.reader.rkf.datatypes.ByteString;
import info.rejsekort.reader.rkf.datatypes.MoneyAmount24;

public class TCPUStaticBlock extends InterpretedBlock {

	public ByteString mIdentifier;
	public RKFInteger mVersionNumber;
	public ByteString mAID;
	public ByteString mPurseSerialNumber;
	public DateCompact mStartDate;
	public RKFInteger mDataPointer;
	public MoneyAmount24 mMinimumValue;
	public MoneyAmount24 mAutoLoadValue;
	public ByteString mUnused;

	public TCPUStaticBlock(byte[] bits) {
		super("TCPUStatic", bits);

		mIdentifier = new ByteString(8, false);
		mVersionNumber = new RKFInteger(6, true);
		mAID = new ByteString(12, true);
		mPurseSerialNumber = new ByteString(32, true);
		mStartDate = new DateCompact(14, true);
		mDataPointer = new RKFInteger(4, true);
		mMinimumValue = new MoneyAmount24(24, true);
		mAutoLoadValue = new MoneyAmount24(24, true);
		mUnused = new ByteString(4, true);

		mFields = new DataType[] {
			mIdentifier,
			mVersionNumber,
			mAID,
			mPurseSerialNumber,
			mStartDate,
			mDataPointer,
			mMinimumValue,
			mAutoLoadValue,
			mUnused
		};
        mFieldNames = new String[] {
			"Identifier",
			"VersionNumber",
			"AID",
			"PurseSerialNumber",
			"StartDate",
			"DataPointer",
			"MinimumValue",
			"AutoLoadValue",
			"Unused"
        };

		interpretBlock();
	}
}
