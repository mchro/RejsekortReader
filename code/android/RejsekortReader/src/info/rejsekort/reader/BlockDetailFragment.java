package info.rejsekort.reader;

import info.rejsekort.reader.rkf.RKFCard;
import info.rejsekort.reader.rkf.blocks.InterpretedBlock;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A fragment representing a single Block detail screen. This fragment is either
 * contained in a {@link BlockListActivity} in two-pane mode (on tablets) or a
 * {@link BlockDetailActivity} on handsets.
 */
public class BlockDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private InterpretedBlock mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public BlockDetailFragment() {
	}
	
	private RKFCard mRKFCard;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		RejsekortReaderApp app = (RejsekortReaderApp) getActivity().getApplication();
		mRKFCard = app.mRKFCard;
		
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = mRKFCard.allBlocks.get(getArguments().getString(ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_block_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			((TextView) rootView.findViewById(R.id.block_detail))
					.setText(mItem.toString());
		}

		return rootView;
	}
}
