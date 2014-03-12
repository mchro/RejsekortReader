package info.rejsekort.reader;

import java.io.IOException;

import info.rejsekort.reader.rkf.RKFCard;
import info.rejsekort.reader.rkf.datatypes.DataType;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class DumpRaw extends Activity {
	
	private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    public RKFCard mRKFCard;
    private String TAG = "RejsekortMain";
    public byte[] mCardContents;
    private TextView txtRaw;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dump_raw);
		// Show the Up button in the action bar.
		setupActionBar();
		
		mAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // Setup an intent filter for all MIME based dispatches
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
	    try {
	        ndef.addDataType("*/*");
	    } catch (MalformedMimeTypeException e) {
	        throw new RuntimeException("fail", e);
	    }
		IntentFilter td = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
		mFilters = new IntentFilter[] { ndef, td };

		// Setup a tech list for all NfcF tags
		mTechLists = new String[][] { 
				new String[] { 
						/*NfcV.class.getName(),
						NfcF.class.getName(),*/ 
						NfcA.class.getName(),
						//NfcB.class.getName() 
						} 
				};
		
		txtRaw = (TextView) findViewById(R.id.txtRaw);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dump_raw, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.dumprawsendmail:
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(Intent.EXTRA_EMAIL  , new String[]{""});
			i.putExtra(Intent.EXTRA_SUBJECT, "Rejsekortdump " + mRKFCard.mCMI.mCardSerialNumber.mString);
			i.putExtra(Intent.EXTRA_TEXT   , DataType.getHexString(mCardContents));
			try {
			    startActivity(Intent.createChooser(i, "Send rejsekortdump..."));
			} catch (android.content.ActivityNotFoundException ex) {
			    Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
			}
			return true;
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
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
				mCardContents = mRKFCard.readEntireCard();
				txtRaw.setText(DataType.getHexString(mCardContents));
			} catch (IOException e1) {
				Log.e(TAG, e1.getLocalizedMessage(), e1);
			}
			Toast t = Toast.makeText(this, "Dump done!", Toast.LENGTH_SHORT);
			t.show();
		}
	}// End of method
        
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
