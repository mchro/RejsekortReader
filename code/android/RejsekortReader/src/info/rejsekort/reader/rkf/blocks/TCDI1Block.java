package info.rejsekort.reader.rkf.blocks;

import info.rejsekort.reader.rkf.datatypes.DataType;
import info.rejsekort.reader.rkf.datatypes.ByteString;

public class TCDI1Block extends InterpretedBlock {

	public ByteString mAIDPIX1;
	public ByteString mAIDPIX2;
	public ByteString mAIDPIX3;
	public ByteString mAIDPIX4;
	public ByteString mAIDPIX5;
	public ByteString mChecksum;

	public TCDI1Block(byte[] bits) {
		super("TCDI1", bits);

		mAIDPIX1 = new ByteString(24, true);
		mAIDPIX2 = new ByteString(24, true);
		mAIDPIX3 = new ByteString(24, true);
		mAIDPIX4 = new ByteString(24, true);
		mAIDPIX5 = new ByteString(24, true);
		mChecksum = new ByteString(8, true);

		mFields = new DataType[] {
			mAIDPIX1,
			mAIDPIX2,
			mAIDPIX3,
			mAIDPIX4,
			mAIDPIX5,
			mChecksum
		};
        mFieldNames = new String[] {
			"AIDPIX1",
			"AIDPIX2",
			"AIDPIX3",
			"AIDPIX4",
			"AIDPIX5",
			"Checksum"
        };

		interpretBlock();
	}
}
