package info.rejsekort.reader.rkf.blocks;

import info.rejsekort.reader.rkf.datatypes.DataType;
import info.rejsekort.reader.rkf.datatypes.ByteString;

public class TCDI3Block extends InterpretedBlock {

	public ByteString mAIDPIX11;
	public ByteString mAIDPIX12;
	public ByteString mAIDPIX13;
	public ByteString mAIDPIX14;
	public ByteString mAIDPIX15;
	public ByteString mChecksum;

	public TCDI3Block(byte[] bits) {
		super("TCDI3", bits);

		mAIDPIX11 = new ByteString(24, true);
		mAIDPIX12 = new ByteString(24, true);
		mAIDPIX13 = new ByteString(24, true);
		mAIDPIX14 = new ByteString(24, true);
		mAIDPIX15 = new ByteString(24, true);
		mChecksum = new ByteString(8, true);

		mFields = new DataType[] {
			mAIDPIX11,
			mAIDPIX12,
			mAIDPIX13,
			mAIDPIX14,
			mAIDPIX15,
			mChecksum
		};
        mFieldNames = new String[] {
			"AIDPIX11",
			"AIDPIX12",
			"AIDPIX13",
			"AIDPIX14",
			"AIDPIX15",
			"Checksum"
        };

		interpretBlock();
	}
}
