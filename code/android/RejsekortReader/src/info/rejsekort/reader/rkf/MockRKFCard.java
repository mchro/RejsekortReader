package info.rejsekort.reader.rkf;

import java.io.IOException;

public class MockRKFCard extends RKFCard {

	private byte[] mCardData = new byte[4096];

	public MockRKFCard(String cardData) throws IOException {
		super();
		assert(cardData.length() == 8192);
		
		for (int i = 0; i < 4096; i++) {
			mCardData[i] = (byte) Integer.parseInt(cardData.substring(i*2, i*2+1+1), 16);
		}
		parse();
	}
	
	public byte[] readBlock(int sector, int block) throws IOException {
		int width;
		int index;
		if (sector < 32) {
            width = 4;
            index = sector * width;
		}
        else {
            width = 16;
            index = 32 * 4 + width * (sector-32);
        }

		byte[] b = new byte[16];
		System.arraycopy(mCardData, index*16+block*16, b, 0, 16);
		
		return b;
	}

}
