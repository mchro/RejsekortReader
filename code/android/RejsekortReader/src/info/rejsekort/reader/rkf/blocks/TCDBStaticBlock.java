package info.rejsekort.reader.rkf.blocks;

import info.rejsekort.reader.rkf.datatypes.DataType;
import info.rejsekort.reader.rkf.datatypes.RKFInteger;
import info.rejsekort.reader.rkf.datatypes.ByteString;

public class TCDBStaticBlock extends InterpretedBlock {

	public ByteString mIdentifier;
	public RKFInteger mVersionNumber;
	public ByteString mAID;
	public RKFInteger mDiscountType1;
	public RKFInteger mDiscountType2;
	public RKFInteger mDiscountType3;
	public ByteString mFree;

	public TCDBStaticBlock(byte[] bits) {
		super("TCDBStatic", bits);

		mIdentifier = new ByteString(8, false);
		mVersionNumber = new RKFInteger(6, true);
		mAID = new ByteString(12, true);
		mDiscountType1 = new RKFInteger(8, true);
		mDiscountType2 = new RKFInteger(8, true);
		mDiscountType3 = new RKFInteger(8, true);
		mFree = new ByteString(78, true);

		mFields = new DataType[] {
			mIdentifier,
			mVersionNumber,
			mAID,
			mDiscountType1,
			mDiscountType2,
			mDiscountType3,
			mFree
		};
        mFieldNames = new String[] {
			"Identifier",
			"VersionNumber",
			"AID",
			"DiscountType1",
			"DiscountType2",
			"DiscountType3",
			"Free"
        };

		interpretBlock();
	}
}
