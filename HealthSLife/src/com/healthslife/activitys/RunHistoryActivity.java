package com.healthslife.activitys;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.healthslife.BaseFragmentActivity;
import com.healthslife.R;
import com.healthslife.adapters.RunHistoryAdapter;
import com.healthslife.run.dao.RunRecord;
import com.healthslife.run.dao.RunResult;

public class RunHistoryActivity extends BaseFragmentActivity implements OnClickListener {
	private ActionBar actionBar;
	private Button dateDropBtn;
	private Button patternDropBtn;
	private Button completeDropBtn;

	private ListPopupWindow dateLPW;
	private ListPopupWindow patternLPW;
	private ListPopupWindow completeLPW;
	private MyAdapter dateAdapter;
	private MyAdapter patternAdapter;
	private MyAdapter completeAdapter;

	private String[] dateStrings;
	private String[] patternStrings;
	private String[] completeStrings;
	private int current_date = 0;
	private int current_pattern = 0;
	private int current_complete = 0;

	private ListView historyList;
	private RunHistoryAdapter historyAdapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setActionBar();
		initView();

	}

	private void initView() {
		setContentView(R.layout.activity_run_history);
		historyList = (ListView) findViewById(R.id.run_history_content_list);
		historyAdapter = new RunHistoryAdapter(this);
		historyList.setAdapter(historyAdapter);
		historyAdapter.changeData(current_date, current_pattern, current_complete);
		historyList.setOnItemClickListener(new OnHistoryItemClick());
		registerForContextMenu(historyList);

		dateDropBtn = (Button) findViewById(R.id.run_history_date_drop_btn);
		patternDropBtn = (Button) findViewById(R.id.run_history_pattern_drop_btn);
		completeDropBtn = (Button) findViewById(R.id.run_history_complete_drop_btn);
		dateDropBtn.setOnClickListener(this);
		patternDropBtn.setOnClickListener(this);
		completeDropBtn.setOnClickListener(this);
		dateLPW = createListPopupWindow(dateDropBtn);
		patternLPW = createListPopupWindow(patternDropBtn);
		completeLPW = createListPopupWindow(completeDropBtn);
		dateStrings = getResources().getStringArray(R.array.run_history_date_drop_list);
		patternStrings = getResources().getStringArray(R.array.run_history_pattern_drop_list);
		completeStrings = getResources().getStringArray(R.array.run_history_complete_drop_list);
		dateAdapter = new MyAdapter(dateStrings);
		patternAdapter = new MyAdapter(patternStrings);
		completeAdapter = new MyAdapter(completeStrings);

		dateLPW.setAdapter(dateAdapter);
		patternLPW.setAdapter(patternAdapter);
		completeLPW.setAdapter(completeAdapter);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.history_list, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_history_delete) {
			AdapterContextMenuInfo itemInfo = (AdapterContextMenuInfo) item.getMenuInfo();
			historyAdapter.deleteRecord(itemInfo.position);
			return true;
		}
		return super.onContextItemSelected(item);
	}

	private void setActionBar() {
		actionBar = getActionBar();
		actionBar.setTitle(R.string.run_history_action_bar_title);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		controlDropList(v);
	}

	private void controlDropList(View dropBtn) {
		if (dropBtn == dateDropBtn) {
			dateLPW.show();
		} else {
			dateLPW.dismiss();
		}
		if (dropBtn == patternDropBtn) {
			patternLPW.show();
		} else {
			patternLPW.dismiss();
		}
		if (dropBtn == completeDropBtn) {
			completeLPW.show();
		} else {
			completeLPW.dismiss();
		}
	}

	private ListPopupWindow createListPopupWindow(View anchorView) {
		ListPopupWindow LPW = new ListPopupWindow(this);
		LPW.setAnchorView(anchorView);
		LPW.setHeight(LayoutParams.WRAP_CONTENT);
		LPW.setOnItemClickListener(new MyDropItemClick(anchorView));
		return LPW;
	}

	private class MyAdapter extends BaseAdapter {
		private String[] items;

		public MyAdapter(String[] items) {
			this.items = items;
		}

		@Override
		public int getCount() {
			if (items == null) {
				return 0;
			} else {
				return items.length;
			}
		}

		@Override
		public Object getItem(int position) {
			return items[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView textView;
			if (convertView == null) {
				textView = new TextView(RunHistoryActivity.this);
				textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_small_l));
				textView.setGravity(Gravity.CENTER);
				int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources()
						.getDisplayMetrics());
				textView.setPadding(0, margin, 0, margin);
			} else {
				textView = (TextView) convertView;
			}
			textView.setText(items[position]);
			return textView;
		}
	}

	private class MyDropItemClick implements OnItemClickListener {
		View dropBtn;

		MyDropItemClick(View view) {
			dropBtn = view;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			boolean isChanged = false;
			if (dropBtn == dateDropBtn) {
				dateLPW.dismiss();
				if (current_date != position) {
					dateDropBtn.setText(dateStrings[position]);
					current_date = position;
					isChanged = true;
				}

			} else if (dropBtn == patternDropBtn) {
				patternLPW.dismiss();
				if (current_pattern != position) {
					current_pattern = position;
					patternDropBtn.setText(patternStrings[position]);
					isChanged = true;
				}
			} else if (dropBtn == completeDropBtn) {
				completeLPW.dismiss();
				if (current_complete != position) {
					current_complete = position;
					completeDropBtn.setText(completeStrings[position]);
					isChanged = true;
				}
			}

			if (isChanged) {
				historyAdapter.changeData(current_date, current_pattern, current_complete);
			}
		}

	}
	
	private class OnHistoryItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			RunRecord record=	(RunRecord) ((RunHistoryAdapter)parent.getAdapter()).getItem(position);
			RunResult result = 	RunRecord.createRunResult(record);
			Intent  intent = new Intent(RunHistoryActivity.this,RunResultActivity.class);
			intent.putExtra(RunResultActivity.EXTRA_RUN_RESULT, result);
			intent.putExtra(RunResultActivity.EXTRA_HISTORY_VIEW,true);
			RunHistoryActivity.this.startActivity(intent);
		}
	}

}
