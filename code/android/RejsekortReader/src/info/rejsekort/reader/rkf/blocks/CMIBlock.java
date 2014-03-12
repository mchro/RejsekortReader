package info.rejsekort.reader.rkf.blocks;

import info.rejsekort.reader.rkf.datatypes.DataType;
import info.rejsekort.reader.rkf.datatypes.ByteString;

public class CMIBlock extends InterpretedBlock {

	public ByteString mCardSerialNumber;
	public ByteString mCardSerialNumberCheckByte;
	public ByteString mManufacturerData;

	public CMIBlock(byte[] bits) {
		super("CMI", bits);

		mCardSerialNumber = new ByteString(32, true);
		mCardSerialNumberCheckByte = new ByteString(8, true);
		mManufacturerData = new ByteString(88, true);

		mFields = new DataType[] {
			mCardSerialNumber,
			mCardSerialNumberCheckByte,
			mManufacturerData
		};
        mFieldNames = new String[] {
			"CardSerialNumber",
			"CardSerialNumberCheckByte",
			"ManufacturerData"
        };

		interpretBlock();
	}
}
