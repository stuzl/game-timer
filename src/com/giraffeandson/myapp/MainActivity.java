package com.giraffeandson.myapp;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import java.io.*;
import java.text.*;
import java.util.*;

import android.view.View.OnClickListener;


public class MainActivity extends Activity
{
	public static final String EXTRA_PLAYERS = "com.giraffeandson.myapp.PLAYERS";
	public static final String EXTRA_TIME = "com.giraffeandson.myapp.TIME";
	private static final String JPEG_FILE_PREFIX = "PlayerPhoto";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	private String mCurrentPhotoPath;
	private final ArrayList<String> list = new ArrayList<String>();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		if(savedInstanceState!=null){
		List<String> savedList = savedInstanceState.getStringArrayList(EXTRA_PLAYERS);
			if(savedList != null){list.addAll(savedList);}
		}
		final Button button = (Button) findViewById(R.id.button);
		final Button addPlayerButton = (Button) findViewById(R.id.button2);
		final EditText editBox = (EditText) findViewById(R.id.editText);
		final ListView listView = (ListView) findViewById(R.id.listView);
		final NumberPicker hourPicker = (NumberPicker) findViewById(R.id.hourPicker);
		final NumberPicker minutePicker = (NumberPicker) findViewById(R.id.minutePicker);
		hourPicker.setMinValue(0);
		hourPicker.setMaxValue(9);
		minutePicker.setMinValue(0);
		minutePicker.setMaxValue(59);
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
		listView.setAdapter(adapter);

		button.setOnClickListener(new OnClickListener(){
				public void onClick(View view)
				{
					if (list.size() == 0)
					{PopUp.show(MainActivity.this, "At least one player needed");}
					else 
					if (hourPicker.getValue() == 0 && minutePicker.getValue() == 0)
					{
						PopUp.show(MainActivity.this, "total time should be greater than 0");	
					}
					else
					{
						Intent intent = new Intent(MainActivity.this, PlayingActivity.class);
						intent.putExtra(EXTRA_PLAYERS, list);
						intent.putExtra(EXTRA_TIME, (long) 60 * (60 * hourPicker.getValue() + minutePicker.getValue()));
						startActivity(intent);
					}
				}
			});
		addPlayerButton.setOnClickListener(new OnClickListener(){
				public void onClick(View view)
				{
					list.add(editBox.getText().toString());
					adapter.notifyDataSetChanged();
					editBox.setText("");
				}
			});
		listView.setOnItemClickListener(new OnItemClickListener(){
				public void onItemClick(AdapterView<?> adapterView, View view, int item, long size)
				{
					if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
					{
						Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						takePicture.putExtra("android.intent.extras.CAMERA_FACING", 1);
						try
						{
							final File imageFile = createImageFile();
							PopUp.show(MainActivity.this, imageFile.getAbsolutePath());
							//	takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
						}
						catch (IOException e)
						{
							editBox.setText("file not found");
						}

						startActivityForResult(takePicture, 0);//zero can be replced with any action code
					}
				}

			});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		final ImageView imageView = (ImageView) findViewById(R.id.imageView);
		final Bitmap bitmap = (Bitmap) data.getExtras().get("data");
		imageView.setImageBitmap(bitmap);
	}

	private File createImageFile() throws IOException
	{
		// Create an image file name
		String timeStamp = 
			new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp;
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		AlertDialog dialog = builder
			.setMessage(getAlbumDir().getAbsolutePath() + getAlbumDir().isDirectory())
			.setPositiveButton("ok", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int id)
				{}
			})
			.create();

		dialog.show();
		File image = File.createTempFile(
			imageFileName, 
			JPEG_FILE_SUFFIX, 
			MainActivity.this.getExternalFilesDir("Photo")
		);
		mCurrentPhotoPath = image.getAbsolutePath();
		return image;
	}

	private File getAlbumDir()
	{
		File storageDir = new File(
			Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES
			), getString(R.string.app_name) + '/'

		);   
		return storageDir;
	}
	
	@Override
	public void onSaveInstanceState(Bundle saveInstanceState){
		super.onSaveInstanceState(saveInstanceState);
		saveInstanceState.putStringArrayList(EXTRA_PLAYERS,list);
	}
	
}
