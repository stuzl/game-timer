package com.giraffeandson.myapp;
import android.widget.*;
import android.os.*;
import android.content.*;
import android.util.*;

@RemoteViews.RemoteView()
public class CountUpTimer extends Chronometer
{
	private long time=0;

	public CountUpTimer(Context context)
	{
		super(context);
	}
	public CountUpTimer(Context context, AttributeSet attr)
	{
		super(context, attr);
	}
	public CountUpTimer(Context context, AttributeSet attr, int defStyle)
	{
		super(context, attr, defStyle);
	}

	public void start()
	{
		super.setBase(
			SystemClock.elapsedRealtime() - time);
		super.start();
	}
	
	public void stop()
	{
		time = SystemClock.elapsedRealtime() - getBase();
		super.stop();
	}

	public void reset()
	{
		time = 0;
	}
}
