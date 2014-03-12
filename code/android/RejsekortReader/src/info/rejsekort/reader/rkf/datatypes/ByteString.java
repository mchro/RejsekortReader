package info.rejsekort.reader.rkf.datatypes;

public class ByteString extends DataType {
	public String mString; 
	
	public ByteString(int bitlength, boolean reverse) {
		super(bitlength, reverse);
	}
	
	@Override
	void interpret() {
		String revbits = new StringBuffer(mBits).reverse().toString();
		byte[] revbitsarr = binaryStringToByteArray(revbits);
		
		byte[] bitsarr = new byte[revbitsarr.length];
		for (int i = 0; i < revbitsarr.length; i++) {
			bitsarr[revbitsarr.length - i - 1] = revbitsarr[i];
		}
		
		mString = getHexString(bitsarr);
	}

	public String toString() {
		return mString;
	}
}
