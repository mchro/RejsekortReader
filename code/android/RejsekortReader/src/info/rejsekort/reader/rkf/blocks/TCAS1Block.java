package info.rejsekort.reader.rkf.blocks;

import info.rejsekort.reader.rkf.datatypes.DataType;
import info.rejsekort.reader.rkf.datatypes.RKFInteger;
import info.rejsekort.reader.rkf.datatypes.ByteString;

public class TCAS1Block extends InterpretedBlock {

	public ByteString mIdentifier;
	public ByteString mVersionNumber;
	public RKFInteger mSectorStatus0;
	public RKFInteger mSectorStatus1;
	public RKFInteger mSectorStatus2;
	public RKFInteger mSectorStatus3;
	public RKFInteger mSectorStatus4;
	public RKFInteger mSectorStatus5;
	public RKFInteger mSectorStatus6;
	public RKFInteger mSectorStatus7;
	public RKFInteger mSectorStatus8;
	public RKFInteger mSectorStatus9;
	public RKFInteger mSectorStatus10;
	public RKFInteger mSectorStatus11;
	public RKFInteger mSectorStatus12;
	public RKFInteger mSectorStatus13;
	public RKFInteger mSectorStatus14;
	public RKFInteger mSectorStatus15;
	public RKFInteger mTransactionNumber;
	public RKFInteger mEventLogRecordNumber;
	public RKFInteger mTicketLogAreaSectorPointer;
	public RKFInteger mTicketLogSectorPointer1;
	public RKFInteger mTicketLogSectorPointer2;
	public RKFInteger mTicketLogSectorPointer3;
	public RKFInteger mTicketLogSectorPointer4;
	public RKFInteger mTicketLogSectorPointer5;
	public RKFInteger mTicketLogSectorPointer6;
	public RKFInteger mTicketLogSectorPointer7;
	public RKFInteger mTicketLogSectorPointer8;
	public ByteString mMACAlgorithmIdentifier;
	public ByteString mMACKeyIdentifier;
	public ByteString mMACAuthenticator;
	public ByteString mUnused;

	public TCAS1Block(byte[] bits) {
		super("TCAS1", bits);

		mIdentifier = new ByteString(8, true);
		mVersionNumber = new ByteString(6, true);
		mSectorStatus0 = new RKFInteger(2, true);
		mSectorStatus1 = new RKFInteger(2, true);
		mSectorStatus2 = new RKFInteger(2, true);
		mSectorStatus3 = new RKFInteger(2, true);
		mSectorStatus4 = new RKFInteger(2, true);
		mSectorStatus5 = new RKFInteger(2, true);
		mSectorStatus6 = new RKFInteger(2, true);
		mSectorStatus7 = new RKFInteger(2, true);
		mSectorStatus8 = new RKFInteger(2, true);
		mSectorStatus9 = new RKFInteger(2, true);
		mSectorStatus10 = new RKFInteger(2, true);
		mSectorStatus11 = new RKFInteger(2, true);
		mSectorStatus12 = new RKFInteger(2, true);
		mSectorStatus13 = new RKFInteger(2, true);
		mSectorStatus14 = new RKFInteger(2, true);
		mSectorStatus15 = new RKFInteger(2, true);
		mTransactionNumber = new RKFInteger(8, true);
		mEventLogRecordNumber = new RKFInteger(4, true);
		mTicketLogAreaSectorPointer = new RKFInteger(4, true);
		mTicketLogSectorPointer1 = new RKFInteger(4, true);
		mTicketLogSectorPointer2 = new RKFInteger(4, true);
		mTicketLogSectorPointer3 = new RKFInteger(4, true);
		mTicketLogSectorPointer4 = new RKFInteger(4, true);
		mTicketLogSectorPointer5 = new RKFInteger(4, true);
		mTicketLogSectorPointer6 = new RKFInteger(4, true);
		mTicketLogSectorPointer7 = new RKFInteger(4, true);
		mTicketLogSectorPointer8 = new RKFInteger(4, true);
		mMACAlgorithmIdentifier = new ByteString(2, true);
		mMACKeyIdentifier = new ByteString(6, true);
		mMACAuthenticator = new ByteString(16, true);
		mUnused = new ByteString(10, true);

		mFields = new DataType[] {
			mIdentifier,
			mVersionNumber,
			mSectorStatus0,
			mSectorStatus1,
			mSectorStatus2,
			mSectorStatus3,
			mSectorStatus4,
			mSectorStatus5,
			mSectorStatus6,
			mSectorStatus7,
			mSectorStatus8,
			mSectorStatus9,
			mSectorStatus10,
			mSectorStatus11,
			mSectorStatus12,
			mSectorStatus13,
			mSectorStatus14,
			mSectorStatus15,
			mTransactionNumber,
			mEventLogRecordNumber,
			mTicketLogAreaSectorPointer,
			mTicketLogSectorPointer1,
			mTicketLogSectorPointer2,
			mTicketLogSectorPointer3,
			mTicketLogSectorPointer4,
			mTicketLogSectorPointer5,
			mTicketLogSectorPointer6,
			mTicketLogSectorPointer7,
			mTicketLogSectorPointer8,
			mMACAlgorithmIdentifier,
			mMACKeyIdentifier,
			mMACAuthenticator,
			mUnused
		};
        mFieldNames = new String[] {
			"Identifier",
			"VersionNumber",
			"SectorStatus0",
			"SectorStatus1",
			"SectorStatus2",
			"SectorStatus3",
			"SectorStatus4",
			"SectorStatus5",
			"SectorStatus6",
			"SectorStatus7",
			"SectorStatus8",
			"SectorStatus9",
			"SectorStatus10",
			"SectorStatus11",
			"SectorStatus12",
			"SectorStatus13",
			"SectorStatus14",
			"SectorStatus15",
			"TransactionNumber",
			"EventLogRecordNumber",
			"TicketLogAreaSectorPointer",
			"TicketLogSectorPointer1",
			"TicketLogSectorPointer2",
			"TicketLogSectorPointer3",
			"TicketLogSectorPointer4",
			"TicketLogSectorPointer5",
			"TicketLogSectorPointer6",
			"TicketLogSectorPointer7",
			"TicketLogSectorPointer8",
			"MACAlgorithmIdentifier",
			"MACKeyIdentifier",
			"MACAuthenticator",
			"Unused"
        };

		interpretBlock();
	}
}
