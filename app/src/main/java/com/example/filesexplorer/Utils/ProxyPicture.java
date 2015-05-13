package com.example.filesexplorer.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.filesexplorer.Model.FileAndroid;

public class ProxyPicture {
	
	private static class ProxyPictureHolder {
		private static ProxyPicture proxyPictureInstance;
	}
	
	private ProxyPicture() { }
	
	public static ProxyPicture getInstance() {
		if (ProxyPictureHolder.proxyPictureInstance == null) {
			ProxyPictureHolder.proxyPictureInstance = new ProxyPicture();
		}
		return ProxyPictureHolder.proxyPictureInstance;
	}
	
	public void setPictureByAsyncTask(FileAndroid fileAndroid) {
		ProxyPictureAsyncTask proxyPictureAsyncTask = new ProxyPictureAsyncTask(fileAndroid);
		proxyPictureAsyncTask.execute();
	}
	
	class ProxyPictureAsyncTask extends AsyncTask<Void, Integer, Bitmap> {

		private FileAndroid fileAndroid;
		
		public ProxyPictureAsyncTask(FileAndroid fileAndroid) {
			this.fileAndroid = fileAndroid;
		}
		
		@Override
		protected Bitmap doInBackground(Void... arg0) {
			try {
				Bitmap bitmap = BitmapFactory.decodeFile(fileAndroid.getFile().getAbsolutePath());
				return getResizedBitmap(bitmap, 100);
			} catch (Exception e) {
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (bitmap != null) {
				fileAndroid.setBitmap(bitmap);
			}
		}
		
	}
	
	private Bitmap getResizedBitmap(Bitmap src, int height) {
		int width = src.getWidth()*height/src.getHeight();
		return Bitmap.createScaledBitmap(src, width, height, false);
	}
}
