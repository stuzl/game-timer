package com.giraffeandson.myapp;


import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.util.*;

public class PlayingActivity extends Activity
{
	private static final String EXTRA_TIMES = "com.giraffeandson.myapp.TIMES";
	private static final String EXTRA_ITERATOR_POSITION = "com.giraffeandson.myapp.ITERATOR_POS";
	private ListIterator<String> namesIterator;
	private TextView timerText;
	private TextView nameView;
	private MyCounter counter;
	private Map<String,Long> times;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playing);
		Intent intent = getIntent();
		final List<String> names = // Arrays.asList("Bob","Ben");
			intent.getStringArrayListExtra(MainActivity.EXTRA_PLAYERS);
		final long totalTime = intent.getLongExtra(MainActivity.EXTRA_TIME, 0);
		if(savedInstanceState!=null){
		namesIterator = names.listIterator(savedInstanceState.getInt(EXTRA_ITERATOR_POSITION,0));
		} else {
			namesIterator = names.listIterator();	
		}
		if (savedInstanceState != null && savedInstanceState.getSerializable(EXTRA_TIMES) != null)
		{
			times = (Map<String,Long>)savedInstanceState.getSerializable(EXTRA_TIMES);
		}
		else
		{
			times = new HashMap<String,Long>();
			for (String name:names)
			{
				times.put(name, (long) Math.round(totalTime / names.size()));
			}
		}
		nameView = (TextView) findViewById(R.id.nameText);
		final String firstName = namesIterator.next();
		nameView.setText(firstName);
		final Button button = (Button) findViewById(R.id.button);
//		final Chronometer chrono = (Chronometer) findViewById(R.id.chronometer);
//		chrono.setBase(SystemClock.elapsedRealtime());
//		chrono.start();
//		button.setOnClickListener(new OnClickListener(){
//				public void onClick(View view)
//				{
//					if (!namesIterator.hasNext())
//					{namesIterator = names.iterator();}
//					final String name = namesIterator.next();
//					times.put(textView.getText().toString(), SystemClock.elapsedRealtime() - chrono.getBase());
//					textView.setText(name);
//					chrono.setBase(SystemClock.elapsedRealtime() - times.get(name));
//					chrono.start();
//				}
//			})
		counter = new MyCounter(times.get(firstName));
		counter.start();
		timerText = (TextView) findViewById(R.id.timerText);
		button.setOnClickListener(new OnClickListener(){
				public void onClick(View view)
				{
					if (!namesIterator.hasNext())
					{namesIterator = names.listIterator();}
					final String name = namesIterator.next();
					counter.cancel();
					times.put(nameView.getText().toString(), counter.getRemainingTime());
					nameView.setText(name);
					PopUp.show(PlayingActivity.this,times.get(name).toString());
					if (times.get(name).equals(0l))
					{
						
						timerText.setText(getString(R.string.out));
					}
					else
					{
						counter = new MyCounter(times.get(name));
						counter.start();
					}
				}
			});
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		counter.cancel();
		times.put(nameView.getText().toString(), counter.getRemainingTime());
		outState.putSerializable(EXTRA_TIMES, new HashMap<String,Long>(times));
		outState.putInt(EXTRA_ITERATOR_POSITION,namesIterator.previousIndex());
	}

	private class MyCounter extends CountDownTimer
	{
		private long remainingTime;

		public MyCounter(long timeInSeconds)
		{
			super(1000 * timeInSeconds, 1000);
			remainingTime = timeInSeconds;
		}
		public void onTick(long millisUntilFinish)
		{
			final long hours = millisUntilFinish / 3600000;
			final long minutes = millisUntilFinish / 60000 - hours * 60;
			final long seconds = millisUntilFinish / 1000 - minutes * 60 - hours * 3600;

			timerText.setText(hours + ":" 
							  + (minutes < 10 ?"0" + minutes: minutes) + ":"
							  + (seconds < 10 ?"0" + seconds: seconds));
			remainingTime = millisUntilFinish / 1000;
		}

		public long getRemainingTime()
		{
			return remainingTime;
		}


		public void onFinish()
		{
			timerText.setText(getString(R.string.out));
		}


	}

}
