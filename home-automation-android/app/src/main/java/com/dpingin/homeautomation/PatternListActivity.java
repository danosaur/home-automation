package com.dpingin.homeautomation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dpingin.homeautomation.content.PatternSelectorContent;
import com.dpingin.homeautomation.spice.HomeAutomationSpiceService;
import com.dpingin.homeautomation.spice.request.SelectPatternRequest;
import com.dpingin.homeautomation.spice.request.manager.RequestManager;
import com.octo.android.robospice.SpiceManager;

import java.util.List;

/**
 * An activity representing a list of Patterns. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PatternDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class PatternListActivity extends AppCompatActivity
{
	private SpiceManager spiceManager = new SpiceManager(HomeAutomationSpiceService.class);

	private RequestManager selectPatternRequestManager;
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pattern_list);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitle(getTitle());

//		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//		fab.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View view)
//			{
//				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//						.setAction("Action", null).show();
//			}
//		});

		View recyclerView = findViewById(R.id.pattern_list);
		assert recyclerView != null;
		setupRecyclerView((RecyclerView) recyclerView);

		if (findViewById(R.id.pattern_detail_container) != null)
		{
			// The detail container view will be present only in the
			// large-screen layouts (res/values-w900dp).
			// If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;
		}
	}


	@Override
	protected void onStart()
	{
		spiceManager.start(this);
		selectPatternRequestManager = new RequestManager(spiceManager)
				.start();
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		selectPatternRequestManager.stop();
		spiceManager.shouldStop();
		super.onStop();
	}

	private void setupRecyclerView(@NonNull RecyclerView recyclerView)
	{
		recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(PatternSelectorContent.ITEMS));
	}

	public class SimpleItemRecyclerViewAdapter
			extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>
	{

		private final List<PatternSelectorContent.PatternSelectorItem> mValues;

		public SimpleItemRecyclerViewAdapter(List<PatternSelectorContent.PatternSelectorItem> items)
		{
			mValues = items;
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
		{
			View view = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.pattern_list_content, parent, false);
			return new ViewHolder(view);
		}

		@Override
		public void onBindViewHolder(final ViewHolder holder, int position)
		{
			holder.mItem = mValues.get(position);
			holder.mDetailsView.setText(mValues.get(position).details);
			holder.mContentView.setText(mValues.get(position).content);

			holder.mView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (mTwoPane)
					{
						Bundle arguments = new Bundle();
						arguments.putString(PatternDetailFragment.ARG_ITEM_ID, holder.mItem.id);
						PatternDetailFragment fragment = new PatternDetailFragment();
						fragment.setArguments(arguments);
						getSupportFragmentManager().beginTransaction()
								.replace(R.id.pattern_detail_container, fragment)
								.commit();
					}
					else
					{
						Context context = v.getContext();

						Intent intent = null;
						switch (holder.mItem.id)
						{
							case "static":
								selectPatternRequestManager.submit(new SelectPatternRequest(holder.mItem.id), null);
								intent = new Intent(context, ColorPickerActivity.class);
								break;
							case "minim":
								selectPatternRequestManager.submit(new SelectPatternRequest(holder.mItem.id), null);
								intent = new Intent(context, MusicControlledActivity.class);
								break;
							case "flow":
								selectPatternRequestManager.submit(new SelectPatternRequest(holder.mItem.id), null);
								intent = new Intent(context, FlowActivity.class);
								break;
						}
						if (intent != null)
						{
							intent.putExtra(PatternDetailFragment.ARG_ITEM_ID, holder.mItem.id);

							context.startActivity(intent);
						}
					}
				}
			});
		}

		@Override
		public int getItemCount()
		{
			return mValues.size();
		}

		public class ViewHolder extends RecyclerView.ViewHolder
		{
			public final View mView;
			public final TextView mContentView;
			public final TextView mDetailsView;
			public PatternSelectorContent.PatternSelectorItem mItem;

			public ViewHolder(View view)
			{
				super(view);
				mView = view;
				mContentView = (TextView) view.findViewById(R.id.content);
				mDetailsView = (TextView) view.findViewById(R.id.details);
			}

			@Override
			public String toString()
			{
				return super.toString() + " '" + mContentView.getText() + "'";
			}
		}
	}
}
