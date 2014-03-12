package info.rejsekort.reader.rkf.blocks;

import info.rejsekort.reader.rkf.datatypes.DataType;
import info.rejsekort.reader.rkf.datatypes.DateMonth8;
import info.rejsekort.reader.rkf.datatypes.RKFInteger;
import info.rejsekort.reader.rkf.datatypes.ByteString;

public class TCDBDynamicBlock extends InterpretedBlock {

	public ByteString mStatus;
	public DateMonth8 mFirsthMonth;
	public ByteString mXXX1;
	public RKFInteger mDiscountStep;
	public ByteString mXXX;

	public TCDBDynamicBlock(byte[] bits) {
		super("TCDBDynamic", bits);

		mStatus = new ByteString(8, true);
		mFirsthMonth = new DateMonth8(8, true);
		mXXX1 = new ByteString(55, true);
		mDiscountStep = new RKFInteger(8, true);
		mXXX = new ByteString(128, true);

		mFields = new DataType[] {
			mStatus,
			mFirsthMonth,
			mXXX1,
			mDiscountStep,
			mXXX
		};
        mFieldNames = new String[] {
			"Status",
			"FirsthMonth",
			"XXX1",
			"DiscountStep",
			"XXX"
        };

		interpretBlock();
	}
}
