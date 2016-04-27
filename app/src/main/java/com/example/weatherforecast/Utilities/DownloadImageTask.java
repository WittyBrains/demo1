package com.example.weatherforecast.Utilities;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	  private static final String TAG = "AppWeather";
	  ImageView imageView;

	  public DownloadImageTask(ImageView imageView)
	  {
	      this.imageView = imageView;
	  }

	  protected Bitmap doInBackground(String... urls)
	  {
	      Bitmap bitmap = null;
	      try
	      {
	    	  InputStream in = new java.net.URL(urls[0]).openStream();
	    	  bitmap = BitmapFactory.decodeStream(in);
	      }
	      catch (Exception e)
	      {
	          Log.e(TAG, e.getMessage());
	      }
	      return bitmap;
	  }

	  protected void onPostExecute(Bitmap bitmap)
	  {
		  imageView.setImageBitmap(bitmap);
	  }
}