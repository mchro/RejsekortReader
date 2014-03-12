package info.rejsekort.reader;

import info.rejsekort.reader.rkf.RKFCard;
import android.app.Application;

public class RejsekortReaderApp extends Application {

	public RKFCard mRKFCard;
	
	@Override
	public void onCreate() {
		super.onCreate();
	}

}
