package info.rejsekort.reader.rkf.blocks;

import info.rejsekort.reader.rkf.datatypes.DataType;
import info.rejsekort.reader.rkf.datatypes.ByteString;

public class TCDI2Block extends InterpretedBlock {

	public ByteString mAIDPIX6;
	public ByteString mAIDPIX7;
	public ByteString mAIDPIX8;
	public ByteString mAIDPIX9;
	public ByteString mAIDPIX10;
	public ByteString mChecksum;

	public TCDI2Block(byte[] bits) {
		super("TCDI2", bits);

		mAIDPIX6 = new ByteString(24, true);
		mAIDPIX7 = new ByteString(24, true);
		mAIDPIX8 = new ByteString(24, true);
		mAIDPIX9 = new ByteString(24, true);
		mAIDPIX10 = new ByteString(24, true);
		mChecksum = new ByteString(8, true);

		mFields = new DataType[] {
			mAIDPIX6,
			mAIDPIX7,
			mAIDPIX8,
			mAIDPIX9,
			mAIDPIX10,
			mChecksum
		};
        mFieldNames = new String[] {
			"AIDPIX6",
			"AIDPIX7",
			"AIDPIX8",
			"AIDPIX9",
			"AIDPIX10",
			"Checksum"
        };

		interpretBlock();
	}
}
