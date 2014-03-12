package info.rejsekort.reader;


import info.rejsekort.reader.rkf.MockRKFCard;
import info.rejsekort.reader.rkf.RKFCard;
import info.rejsekort.reader.rkf.TCELBlock_;
import info.rejsekort.reader.rkf.TCSTBlock;
import info.rejsekort.reader.rkf.blocks.TCSTv4Block;
import info.rejsekort.reader.rkf.blocks.TCSTv5Block;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    public RKFCard mRKFCard;
    private String TAG = "RejsekortMain";
    private ArrayList<String> mItems;
    private ArrayAdapter<String> mItemsAdapter;
    // For intents
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
		
		ListView bjarne = (ListView) findViewById(R.id.mainListView);
		mItems = new ArrayList<String>();
		mItemsAdapter = new ArrayAdapter<String>(this,
	        android.R.layout.simple_list_item_1, mItems);
		bjarne.setAdapter(mItemsAdapter);

		
		final MainActivity curAct = this;
		bjarne.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(curAct,
						DisplayTravelDetailActivity.class);
				int[] posArray = {position};
				intent.putExtra(EXTRA_MESSAGE, posArray);
				startActivity(intent);
			}
		});
		    
		/* NFC stuff */
		// Actually redundant with the uses-permission part in the Manifests file
		mAdapter = NfcAdapter.getDefaultAdapter(this);
		// When a MiFare tag is discovered, the NFC stack would get the details of the tag and deliver it to a new Intent of this same activity. Hence to handle this, we would need an instance of the  PendingIntent from the current activity.
        mPendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // Setup an intent filter for all MIME based dispatches
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
	    try {
	    	// We want everything
	        ndef.addDataType("*/*");
	    } catch (MalformedMimeTypeException e) {
	        throw new RuntimeException("fail", e);
	    }
		IntentFilter td = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
		mFilters = new IntentFilter[] { ndef, td };

		// Setup a tech list for all NfcF tags
		mTechLists = new String[][] { 
				new String[] {  
						NfcA.class.getName(),
						} 
				};
		
		if (getIntent() != null)
			resolveIntent(getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		// Mock data dumps
		SubMenu sm = menu.addSubMenu("Test data");
		MenuItem mi = sm.add("Test data 1");
		mi.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				try {
					mRKFCard = new MockRKFCard("");
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				RejsekortReaderApp app = (RejsekortReaderApp) getApplication();
				app.mRKFCard = mRKFCard;
				parseCard();
				
				return true;
			}
		});
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_dump:
	            Intent intent = new Intent(this, DumpRaw.class);
	            startActivity(intent);
	            return true;
	        case R.id.action_settings:
	            Toast t = Toast.makeText(this, "TODO settings menu", Toast.LENGTH_SHORT);
	            t.show();
	            return true;
	        case R.id.action_block_list:
	        	Intent intent2 = new Intent(this, BlockListActivity.class);
	        	startActivity(intent2);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	void resolveIntent(Intent intent) {
		// 1) Parse the intent and get the action that triggered this intent
		String action = intent.getAction();
		// 2) Check if it was triggered by a tag discovered interruption.
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
			// 3) Get an instance of the TAG from the NfcAdapter
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			// 4) Get an instance of the Mifare classic card from this TAG
			// intent
			MifareClassic mfc = MifareClassic.get(tagFromIntent);
			try {
				mfc.connect();
				mRKFCard = new RKFCard(mfc);
				
				RejsekortReaderApp app = (RejsekortReaderApp) getApplication();
				app.mRKFCard = mRKFCard;
				
				//Indicate we are done
				Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				v.vibrate(100);
				
				Toast t = Toast.makeText(this, "Serial no.: " + mRKFCard.mCMI.mCardSerialNumber.mString, Toast.LENGTH_LONG);
				t.show();
				
				parseCard();
				
			} catch (IOException e1) {
				Log.e(TAG, e1.getLocalizedMessage(), e1);
				Toast t = Toast.makeText(this, "Error reading card.", Toast.LENGTH_LONG);
				t.show();			
			} catch (NullPointerException e1) {
				Log.e(TAG, e1.getLocalizedMessage(), e1);
				Toast t = Toast.makeText(this, "Error reading card.", Toast.LENGTH_LONG);
				t.show();
			}
		}
	}// End of method

	void parseCard() {
		mItemsAdapter.clear();
		mItemsAdapter.add("Saldo: " + ((float)mRKFCard.getCurSaldo())/100 + " kr.");
		
		mItemsAdapter.add("Rejser:");
		ArrayList<TCSTBlock> sorted2 = new ArrayList<TCSTBlock>(mRKFCard.mTCST);
		Collections.sort(sorted2);
		Collections.reverse(sorted2);
		for (TCSTBlock b : sorted2) {
			StringBuilder sb = new StringBuilder();

			if (b instanceof TCSTv5Block) {
				TCSTv5Block tb = (TCSTv5Block)b;
				
				sb.append(tb.mJourneyOriginDateTime.toString());
				sb.append(" @");
				sb.append(tb.mLocation.mInt);
				sb.append(" ");
				sb.append((double)tb.mValue.mAmount/100);
				sb.append(" kr.");
			}
			else if (b instanceof TCSTv4Block) {
				TCSTv4Block tb = (TCSTv4Block)b;
				
				sb.append(tb.mJourneyOriginDateTime.toString());
				//sb.append(" @");
				//sb.append(tb.mLocation.mInt);
				//sb.append(" ");
				//sb.append((double)tb.mValue.mAmount/100);
				//sb.append(" kr.");
			}
			
			mItemsAdapter.add(sb.toString());
		}
		
		
		mItemsAdapter.add("Events:");
		ArrayList<TCELBlock_> sorted = new ArrayList<TCELBlock_>(mRKFCard.mTCEL);
		Collections.sort(sorted);
		Collections.reverse(sorted);
		for (TCELBlock_ b : sorted) {
			StringBuilder sb = new StringBuilder();
			
			sb.append(b.mEventDateStamp.getDate());
			sb.append(" ");
			sb.append(b.mEventTimeStamp.getTime());
			
			if (b.mStatusBits.mString.equals("1b")) {
				sb.append(" Checkin");
			}
			else if (b.mStatusBits.mString.equals("1c")) {
				sb.append(" Checkout");
			}
			else if (b.mStatusBits.mString.equals("08")) {
				sb.append(" Optankning");
			}
			else if (b.mStatusBits.mString.equals("16") || b.mStatusBits.mString.equals("00")) {
				continue;
			}
			
			sb.append(" @");
			sb.append(b.mLocation.mInt);
			mItemsAdapter.add(sb.toString());
		}
	}
	
    @Override
    public void onResume() {
        super.onResume();
        mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.i("Foreground dispatch", "Discovered tag with intent: " + intent);
        resolveIntent(intent);            
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.disableForegroundDispatch(this);
    }
}
