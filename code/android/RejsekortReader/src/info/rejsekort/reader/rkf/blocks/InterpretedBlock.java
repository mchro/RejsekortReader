package info.rejsekort.reader.rkf.blocks;

import info.rejsekort.reader.rkf.datatypes.DataType;

public class InterpretedBlock {

	public DataType[] mFields;
	public String[] mFieldNames;
	public byte[] mBits;
	public String id;
	
	public InterpretedBlock(String pid, byte[] bits) {
		id = pid;
		mBits = bits;
		assert bits.length == 16;
	}
	
	protected void interpretBlock() {
		int i = 0;
		String bitstring = DataType.getBinaryString(mBits);
		
		//Fix the endianness - reverse each byte
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < bitstring.length(); j += 8) {
			sb.append(new StringBuilder(bitstring.substring(j, j+8)).reverse().toString());
		}
		bitstring = sb.toString();
		assert(bitstring.length() == 128);
		//bitstring = new StringBuilder(bitstring).reverse().toString();
		
		for (DataType f : mFields) {
			int len = f.mBitlength;
			//Extract bits i:len
			try {
				String substr = bitstring.substring(i, i+len);
				f.fromBits(substr);
				i += len;
			}
			catch (StringIndexOutOfBoundsException e) {
				break;
			}
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < mFields.length; i++) {
			DataType f = mFields[i];
			String n = mFieldNames[i];
			sb.append(n);
			sb.append(": ");
			sb.append(f.toString());
			sb.append("\n");
		}
		
		return sb.toString();
	}

}
