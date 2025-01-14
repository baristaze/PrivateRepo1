package net.pic4pic.ginger.tasks;

import java.io.InputStream;
import java.util.UUID;

import net.pic4pic.ginger.utils.ImageCacher;
import net.pic4pic.ginger.utils.MyLog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {
	
	private UUID imageId;
	private ImageView imageView;
	private boolean clickableAfterDownload;
	private ImageDownloadListener listener;
	
	public ImageDownloadTask(UUID imageId, ImageView imageView){
		this(imageId, imageView, false);
	}
	
	public ImageDownloadTask(UUID imageId, ImageView imageView, boolean clickableAfterDownload) {
		this(imageId, imageView, clickableAfterDownload, null);
	}
	
    public ImageDownloadTask(UUID imageId, ImageView imageView, boolean clickableAfterDownload, ImageDownloadListener listener) {
        this.imageId = imageId;
    	this.imageView = imageView;
        this.clickableAfterDownload = clickableAfterDownload;
        
        if(this.imageId == null){
        	this.imageId = new UUID(0, 0);
        	MyLog.bag().w("Image ID hasn't been defined yet");
        }
        
        this.listener = listener;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {   
    	
    	// check cache first
    	if(ImageCacher.Instance().exists(this.imageId)){
    		return ImageCacher.Instance().get(this.imageId);
    	}
    	
    	// else... download it
    	Bitmap bitmap = null;    	
    	if(urls != null && urls.length > 0){
	        String urldisplay = urls[0];
	        long startTime = System.nanoTime();
	        try {
	        	MyLog.bag().addObject("Image", this.imageId).v("Downloading Image... ");
	        	
	        	System.gc();
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            
	            try{
	            	bitmap = BitmapFactory.decodeStream(in);
	            }
	            catch(OutOfMemoryError e){
	            	
		        	// clear cache
		    		ImageCacher.Instance().clear();
		    		
		    		// force FC as well
		    		System.gc();
		    		
		    		// log
		    		MyLog.bag().i("OutOfMemoryError error is received. Retrying after trimming memory.");
		    		
		    		// re-try
		    		in = new java.net.URL(urldisplay).openStream();
		    		bitmap = BitmapFactory.decodeStream(in);
		        }	            
	        }
	        catch (Throwable e) {
	            MyLog.bag().add(e).e("Image download failed with error");
	            e.printStackTrace();
	        }
		    finally{
		    	long elapsedTime = System.nanoTime() - startTime;
		    	elapsedTime /= 1000000;
		    	MyLog.bag().add("ImageDownload", urldisplay).add("ElapsedTimeMSec", Long.toString(elapsedTime)).add("Success", (bitmap == null ? "0" : "1")).i();
		    }

    	}
    	else{
    		MyLog.bag().e("ImageDownloadTask.execute() method should have a valid Url to download.");
    	}
        return bitmap;
    }
    
    @Override
    protected void onPostExecute(Bitmap result) {
    	
    	if(result != null){    		
    		
    		// set it to the cache
    		ImageCacher.Instance().put(this.imageId, result);
    		
    		// update resources
    		this.imageView.setImageBitmap(result);
    		if(this.clickableAfterDownload){
    			this.imageView.setClickable(true);
    		}
    	}
    	else{
    		MyLog.bag().e("Image couldn't be downloaded? Result bitmap is null in onPostExecute()");
    	}
    	
    	if(this.listener != null){
    		this.listener.onImageDownload(this.imageId, this.imageView, result);
    	}
    }	
    
    public interface ImageDownloadListener {
    	void onImageDownload(UUID imageId, ImageView imageView, Bitmap result);
    }
}
