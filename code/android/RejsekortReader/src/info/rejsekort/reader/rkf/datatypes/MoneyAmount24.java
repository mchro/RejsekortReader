package info.rejsekort.reader.rkf.datatypes;

import java.math.BigInteger;

public class MoneyAmount24 extends DataType {

	public String mSign;
	public int mAmount;
	
	public MoneyAmount24(int bitlength, boolean reverse) {
		super(bitlength, reverse);
	}

	@Override
	void interpret() {
		String signbit = mBits.substring(0, 1);
		if (signbit == "1")
			mSign = "-";
		else
			mSign = "";
		String amountbits = mBits.substring(1);
		
		mAmount = new BigInteger(amountbits, 2).intValue();
		if (mSign == "-")
			mAmount = mAmount * -1;
	}
	
	public String toString() {
		return "MoneyAmount24 " + mAmount; 
	}

}
