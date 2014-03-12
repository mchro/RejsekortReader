package info.rejsekort.reader.rkf.datatypes;

import java.math.BigInteger;

public abstract class DataType {
	/** The bits as a string of "0" and "1": "000110001..." */
	protected String mBits;
	protected boolean mReverse = true;
	public int mBitlength;
	
	public DataType(int bitlength, boolean reverse) {
		mReverse = reverse;
		mBitlength = bitlength;
	}
	
	public void fromBits(String bits) {
		mBits = bits;
		if (mReverse) {
			//reverse the bits
			mBits = new StringBuilder(bits).reverse().toString();
		}
		interpret();
	}
	
	abstract void interpret();
	
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	public static String getHexString(byte[] buf) {
		StringBuffer sb = new StringBuffer();

		for (byte b : buf) {
			sb.append(String.format("%02x", b));
		}

		return sb.toString();
	}
	
	public static String getBinaryString(byte[] input){
	    StringBuilder sb = new StringBuilder();

	    for (byte c : input) {
	        for (int n =  128; n > 0; n >>= 1){
	            if ((c & n) == 0)
	                sb.append('0');
	            else sb.append('1');
	        }
	    }

	    return sb.toString();
	}
	
	public static byte[] binaryStringToByteArray(String s) {
		byte[] ret = new byte[(s.length()+8-1) / 8];

		BigInteger bigint = new BigInteger(s, 2);
		byte[] bigintbytes = bigint.toByteArray();
		
		if (bigintbytes.length > ret.length) {
			//get rid of preceding 0
			for (int i = 0; i < ret.length; i++) {
				ret[i] = bigintbytes[i+1];
			}
		}
		else {
			ret = bigintbytes;
		}
		return ret;
	}
}
