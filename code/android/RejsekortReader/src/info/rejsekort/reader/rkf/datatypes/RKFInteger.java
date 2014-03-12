package info.rejsekort.reader.rkf.datatypes;

import java.math.BigInteger;

public class RKFInteger extends DataType {
	public int mInt;

	public RKFInteger(int bitlength, boolean reverse) {
		super(bitlength, reverse);
	}

	@Override
	void interpret() {
		BigInteger a = new BigInteger(mBits, 2);
		mInt = a.intValue();
	}
	
	public String toString() {
		return "Int " + Integer.toString(mInt);
	}

}
