package info.rejsekort.reader.rkf.blocks;

import info.rejsekort.reader.rkf.datatypes.DataType;
import info.rejsekort.reader.rkf.datatypes.RKFInteger;
import info.rejsekort.reader.rkf.datatypes.ByteString;

public class CommonTCSTBlock extends InterpretedBlock {

	public ByteString mIdentifier;
	public RKFInteger mVersionNumber;

	public CommonTCSTBlock(byte[] bits) {
		super("CommonTCST", bits);

		mIdentifier = new ByteString(8, false);
		mVersionNumber = new RKFInteger(6, true);

		mFields = new DataType[] {
			mIdentifier,
			mVersionNumber
		};
        mFieldNames = new String[] {
			"Identifier",
			"VersionNumber"
        };

		interpretBlock();
	}
}
