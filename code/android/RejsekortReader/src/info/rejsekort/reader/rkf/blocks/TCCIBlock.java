package info.rejsekort.reader.rkf.blocks;

import info.rejsekort.reader.rkf.datatypes.DataType;
import info.rejsekort.reader.rkf.datatypes.RKFInteger;
import info.rejsekort.reader.rkf.datatypes.DateCompact;
import info.rejsekort.reader.rkf.datatypes.ByteString;

public class TCCIBlock extends InterpretedBlock {

	public ByteString mMADInfoByte;
	public RKFInteger mCardVersion;
	public ByteString mCardProvider;
	public DateCompact mCardValidityEndDate;
	public ByteString mCardStatus;
	public RKFInteger mCardCurrencyUnit;
	public RKFInteger mEventLogVersionNumber;
	public ByteString mMACAlgorithmIdentifier;
	public ByteString mMACKeyIdentifier;
	public ByteString mMACAuthenticator;
	public ByteString mUnused;

	public TCCIBlock(byte[] bits) {
		super("TCCI", bits);

		mMADInfoByte = new ByteString(16, true);
		mCardVersion = new RKFInteger(6, true);
		mCardProvider = new ByteString(12, true);
		mCardValidityEndDate = new DateCompact(14, true);
		mCardStatus = new ByteString(8, true);
		mCardCurrencyUnit = new RKFInteger(16, true);
		mEventLogVersionNumber = new RKFInteger(6, true);
		mMACAlgorithmIdentifier = new ByteString(2, true);
		mMACKeyIdentifier = new ByteString(6, true);
		mMACAuthenticator = new ByteString(16, true);
		mUnused = new ByteString(26, true);

		mFields = new DataType[] {
			mMADInfoByte,
			mCardVersion,
			mCardProvider,
			mCardValidityEndDate,
			mCardStatus,
			mCardCurrencyUnit,
			mEventLogVersionNumber,
			mMACAlgorithmIdentifier,
			mMACKeyIdentifier,
			mMACAuthenticator,
			mUnused
		};
        mFieldNames = new String[] {
			"MADInfoByte",
			"CardVersion",
			"CardProvider",
			"CardValidityEndDate",
			"CardStatus",
			"CardCurrencyUnit",
			"EventLogVersionNumber",
			"MACAlgorithmIdentifier",
			"MACKeyIdentifier",
			"MACAuthenticator",
			"Unused"
        };

		interpretBlock();
	}
}
