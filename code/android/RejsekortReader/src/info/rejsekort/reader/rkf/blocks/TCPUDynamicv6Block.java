package info.rejsekort.reader.rkf.blocks;

import info.rejsekort.reader.rkf.datatypes.DataType;
import info.rejsekort.reader.rkf.datatypes.RKFInteger;
import info.rejsekort.reader.rkf.datatypes.ByteString;
import info.rejsekort.reader.rkf.datatypes.MoneyAmount24;

public class TCPUDynamicv6Block extends InterpretedBlock {

	public RKFInteger mPurseTransactionNumber;
	public MoneyAmount24 mValue;
	public ByteString mXXX;

	public TCPUDynamicv6Block(byte[] bits) {
		super("TCPUDynamicv6", bits);

		mPurseTransactionNumber = new RKFInteger(16, true);
		mValue = new MoneyAmount24(24, true);
		mXXX = new ByteString(216, true);

		mFields = new DataType[] {
			mPurseTransactionNumber,
			mValue,
			mXXX
		};
        mFieldNames = new String[] {
			"PurseTransactionNumber",
			"Value",
			"XXX"
        };

		interpretBlock();
	}
}
