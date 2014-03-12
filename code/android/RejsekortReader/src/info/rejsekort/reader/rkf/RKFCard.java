package info.rejsekort.reader.rkf;

import info.rejsekort.reader.rkf.blocks.CMIBlock;
import info.rejsekort.reader.rkf.blocks.CommonTCSTBlock;
import info.rejsekort.reader.rkf.blocks.InterpretedBlock;
import info.rejsekort.reader.rkf.blocks.TCAS1Block;
import info.rejsekort.reader.rkf.blocks.TCCIBlock;
import info.rejsekort.reader.rkf.blocks.TCCPStaticBlock;
import info.rejsekort.reader.rkf.blocks.TCDBDynamicBlock;
import info.rejsekort.reader.rkf.blocks.TCDBStaticBlock;
import info.rejsekort.reader.rkf.blocks.TCDI1Block;
import info.rejsekort.reader.rkf.blocks.TCDI2Block;
import info.rejsekort.reader.rkf.blocks.TCDI3Block;
import info.rejsekort.reader.rkf.blocks.TCPUDynamicv4Block;
import info.rejsekort.reader.rkf.blocks.TCPUDynamicv6Block;
import info.rejsekort.reader.rkf.blocks.TCPUStaticBlock;
import info.rejsekort.reader.rkf.blocks.TCSTv4Block;
import info.rejsekort.reader.rkf.blocks.TCSTv5Block;
import info.rejsekort.reader.rkf.datatypes.DataType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.nfc.tech.MifareClassic;
import android.util.Log;
import android.widget.Toast;

public class RKFCard {
	public static final String TAG = "RKFCard";

	MifareClassic mMFC;
	public CMIBlock mCMI;
	public TCCIBlock mTCCI;
	public TCDI1Block mTCDI1;
	public TCDI2Block mTCDI2;
	public TCDI3Block mTCDI3;
	public TCAS1Block mTCAS1;
	public TCPUStaticBlock mTCPUStatic;
	public InterpretedBlock mTCPUDynamic1;
	public InterpretedBlock mTCPUDynamic2;
	public ArrayList<TCELBlock_> mTCEL = new ArrayList<TCELBlock_>();
	public ArrayList<TCSTBlock> mTCST = new ArrayList<TCSTBlock>();
	public ArrayList<TCCPStaticBlock> mTCCP = new ArrayList<TCCPStaticBlock>();
	public TCDBStaticBlock mTCDBStatic;
	public ArrayList<TCDBDynamicBlock> mTCDBDynamic = new ArrayList<TCDBDynamicBlock>();

	
	public LinkedHashMap<String, InterpretedBlock> allBlocks = new LinkedHashMap<String, InterpretedBlock>();
	
	public static final byte[][] SECTORKEYS_A = {
		// sector 0
		DataType.hexStringToByteArray("fc00018778f7"),
		DataType.hexStringToByteArray("fc00018778f7"),
		DataType.hexStringToByteArray("fc00018778f7"),
		DataType.hexStringToByteArray("fc00018778f7"),
		DataType.hexStringToByteArray("fc00018778f7"),
		DataType.hexStringToByteArray("fc00018778f7"),
		DataType.hexStringToByteArray("fc00018778f7"),
		DataType.hexStringToByteArray("fc00018778f7"),
		// sector 8
		DataType.hexStringToByteArray("0297927c0f77"),
		DataType.hexStringToByteArray("0297927c0f77"),
		DataType.hexStringToByteArray("0297927c0f77"),
		DataType.hexStringToByteArray("0297927c0f77"),
		DataType.hexStringToByteArray("0297927c0f77"),
		//Sector 13
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		DataType.hexStringToByteArray("722bfcc5375f"),
		// sector 39
		DataType.hexStringToByteArray("fc00018778f7"),
	};
	
	protected RKFCard() {
	}
	
	public RKFCard(MifareClassic mfc) throws IOException {
		mMFC = mfc;
		parse();
	}

	public void parse() throws IOException {
		parseCMI();
		parseTCCI();
		parseTCDI();
		parseTCAS();
		parseTCPU();
		
		parseTCEL(3);
		parseTCEL(4);
		parseTCEL(5);
		parseTCEL(6);
		parseTCEL(7);
		
		parseTCST(36, 0);
		parseTCST(36, 6);
		parseTCST(36, 12);
		parseTCST(37, 3);
		parseTCST(37, 9);
		parseTCST(38, 0);
		parseTCST(38, 6);
		
		parseTCCP(13, 0, 13, 1);
		parseTCCP(13, 2, 14, 0);
		
		parseTCDB(15);
	}
	
	public byte[] getAccesKeyA(int sector) {
		return SECTORKEYS_A[sector];
	}
	
	public byte[] readBlock(int sector, int block) throws IOException {
		boolean auth = mMFC.authenticateSectorWithKeyA(sector, getAccesKeyA(sector));
		if (auth)
			return mMFC.readBlock(mMFC.sectorToBlock(sector) + block);
		else {
			Log.e(TAG, "Auth failed for sector " + sector);
			throw new IOException("Auth failed for sector " + sector);
		}
	}
	
	public byte[] readEntireCard() throws IOException {
		byte toret[] = new byte[mMFC.getSize()];
		int j = 0;
		
		for (int i = 0; i < mMFC.getBlockCount(); i++) {
			int sector = mMFC.blockToSector(i);
			boolean auth = mMFC.authenticateSectorWithKeyA(sector, getAccesKeyA(sector));
			if (auth) {
				 byte block[] = mMFC.readBlock(i);
				 for (byte b : block) {
					 toret[j++] = b;
				 }
			}
			else {
				Toast t = Toast.makeText(null, "Auth failed for block " + i + " (sector " + sector + ")", Toast.LENGTH_LONG);
				t.show();
			}
			
		}
		return toret;
	}
	
	void parseCMI() throws IOException {
		mCMI = new CMIBlock(readBlock(0, 0));
		allBlocks.put("CMI", mCMI);
	}
	void parseTCCI() throws IOException {
		mTCCI = new TCCIBlock(readBlock(0, 1));
		allBlocks.put("TCCI", mTCCI);
	}
	void parseTCAS() throws IOException {
		mTCAS1 = new TCAS1Block(readBlock(0, 2));
		allBlocks.put("TCAS1", mTCAS1);
	}
	
	void parseTCDI() throws IOException {
		mTCDI1 = new TCDI1Block(readBlock(1, 0));
		allBlocks.put("TCDI1", mTCDI1);
		mTCDI2 = new TCDI2Block(readBlock(1, 1));
		allBlocks.put("TCDI2", mTCDI2);
		mTCDI3 = new TCDI3Block(readBlock(1, 2));
		allBlocks.put("TCDI3", mTCDI3);
	}
	
	void parseTCPU() throws IOException {
		int[] sectors = {11, 9};
		
		for (int sector : sectors) {
			TCPUStaticBlock tcpustatic = new TCPUStaticBlock(readBlock(sector, 0));
			if (!tcpustatic.mIdentifier.mString.equals("85")) //not TCPU sector
				continue;
			if (tcpustatic.mVersionNumber.mInt == 6) {
				mTCPUStatic = tcpustatic;
				//Version 6, new
				allBlocks.put("TCPUStatic", mTCPUStatic);
				
				byte[] b1 = readBlock(sector, 2);
				byte[] b2 = readBlock(sector+1, 0);
				byte[] c = new byte[b1.length + b2.length];
				System.arraycopy(b1, 0, c, 0, b1.length);
				System.arraycopy(b2, 0, c, b1.length, b2.length);
				mTCPUDynamic1 = new TCPUDynamicv6Block(c);
				allBlocks.put("TCPUDynamic1", mTCPUDynamic1);
				
				b1 = readBlock(sector+1, 1);
				b2 = readBlock(sector+1, 2);
				c = new byte[b1.length + b2.length];
				System.arraycopy(b1, 0, c, 0, b1.length);
				System.arraycopy(b2, 0, c, b1.length, b2.length);
				mTCPUDynamic2 = new TCPUDynamicv6Block(c);
				allBlocks.put("TCPUDynamic2", mTCPUDynamic2);
			}
			else if (mTCPUStatic == null) { //only do this as fallback
				//Version 4, old
				mTCPUStatic = tcpustatic;
				allBlocks.put("TCPUStatic", mTCPUStatic);
				mTCPUDynamic1 = new TCPUDynamicv4Block(readBlock(sector, 1));
				allBlocks.put("TCPUDynamic1", mTCPUDynamic1);
				mTCPUDynamic2 = new TCPUDynamicv4Block(readBlock(sector, 2));
				allBlocks.put("TCPUDynamic2", mTCPUDynamic2);
			}
		}
	}
	
	void parseTCEL(int sector) throws IOException {
		for (int i = 0; i < 4; i++) {
			TCELBlock_ b = new TCELBlock_(readBlock(sector, i));
			mTCEL.add(b);
			allBlocks.put("TCEL" + mTCEL.size(), b);
		}
	}
	
	void parseTCST(int sector, int block0) throws IOException {
		byte[] b1 = readBlock(sector, block0);
		byte[] b2 = readBlock(sector, block0+1);
		byte[] b3 = readBlock(sector, block0+2);
		byte[] c = new byte[b1.length + b2.length + b3.length];
		System.arraycopy(b1, 0, c, 0, b1.length);
		System.arraycopy(b2, 0, c, b1.length, b2.length);
		System.arraycopy(b3, 0, c, b1.length+b2.length, b3.length);
		
		CommonTCSTBlock common = new CommonTCSTBlock(c);
		
		if (common.mVersionNumber.mInt == 5) {
			TCSTv5Block b = new TCSTv5Block(c);
			mTCST.add(b);
			allBlocks.put("TCST" + mTCST.size(), b);
		}
		else { //Assume version 4
			TCSTv4Block b = new TCSTv4Block(c);
			mTCST.add(b);
			allBlocks.put("TCST" + mTCST.size(), b);
		}
	}
	
	void parseTCCP(int sector0, int block0, int sector1, int block1) throws IOException {
		byte[] b1 = readBlock(sector0, block0);
		byte[] b2 = readBlock(sector1, block1);
		byte[] c = new byte[b1.length + b2.length];
		System.arraycopy(b1, 0, c, 0, b1.length);
		System.arraycopy(b2, 0, c, b1.length, b2.length);
		
		TCCPStaticBlock tccp = new TCCPStaticBlock(c);
		
		mTCCP.add(tccp);
		allBlocks.put("TCCP" + mTCCP.size(), tccp);
	}
	
	void parseTCDB(int sector) throws IOException {
		mTCDBStatic = new TCDBStaticBlock(readBlock(sector, 0));
		allBlocks.put("TCDBStatic", mTCDBStatic);
		
		{
			TCDBDynamicBlock b = new TCDBDynamicBlock(readBlock(sector, 1));
			mTCDBDynamic.add(b);
			allBlocks.put("TCDBDynamic" + mTCDBDynamic.size(), b);
		}
		
		{
			TCDBDynamicBlock b = new TCDBDynamicBlock(readBlock(sector, 2));
			mTCDBDynamic.add(b);
			allBlocks.put("TCDBDynamic" + mTCDBDynamic.size(), b);
		}
	}
	
	public int getCurSaldo() {
		if (mTCPUStatic.mVersionNumber.mInt == 6) {
			int ret = ((TCPUDynamicv6Block)mTCPUDynamic1).mValue.mAmount;
			if (((TCPUDynamicv6Block)mTCPUDynamic2).mPurseTransactionNumber.mInt > 
				((TCPUDynamicv6Block)mTCPUDynamic1).mPurseTransactionNumber.mInt)
				ret = ((TCPUDynamicv6Block)mTCPUDynamic2).mValue.mAmount;
			return ret;
		}
		else { //Assume version 4
			int ret = ((TCPUDynamicv4Block)mTCPUDynamic1).mValue.mAmount;
			if (((TCPUDynamicv4Block)mTCPUDynamic2).mPurseTransactionNumber.mInt > 
				((TCPUDynamicv4Block)mTCPUDynamic1).mPurseTransactionNumber.mInt)
				ret = ((TCPUDynamicv4Block)mTCPUDynamic2).mValue.mAmount;
			return ret;
		}
	}
	

}
