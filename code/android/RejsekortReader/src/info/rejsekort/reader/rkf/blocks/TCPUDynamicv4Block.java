package info.rejsekort.reader.rkf.blocks;

import info.rejsekort.reader.rkf.datatypes.DataType;
import info.rejsekort.reader.rkf.datatypes.RKFInteger;
import info.rejsekort.reader.rkf.datatypes.ByteString;
import info.rejsekort.reader.rkf.datatypes.MoneyAmount24;

public class TCPUDynamicv4Block extends InterpretedBlock {

	public RKFInteger mPurseTransactionNumber;
	public MoneyAmount24 mValue;
	public ByteString mXXX;
	public ByteString mMACAlgorithmIdentifier;
	public ByteString mMACKeyIdentifier;
	public ByteString mMACAuthenticator;

	public TCPUDynamicv4Block(byte[] bits) {
		super("TCPUDynamicv4", bits);

		mPurseTransactionNumber = new RKFInteger(16, true);
		mValue = new MoneyAmount24(24, true);
		mXXX = new ByteString(64, true);
		mMACAlgorithmIdentifier = new ByteString(2, true);
		mMACKeyIdentifier = new ByteString(6, true);
		mMACAuthenticator = new ByteString(16, true);

		mFields = new DataType[] {
			mPurseTransactionNumber,
			mValue,
			mXXX,
			mMACAlgorithmIdentifier,
			mMACKeyIdentifier,
			mMACAuthenticator
		};
        mFieldNames = new String[] {
			"PurseTransactionNumber",
			"Value",
			"XXX",
			"MACAlgorithmIdentifier",
			"MACKeyIdentifier",
			"MACAuthenticator"
        };

		interpretBlock();
	}
}
