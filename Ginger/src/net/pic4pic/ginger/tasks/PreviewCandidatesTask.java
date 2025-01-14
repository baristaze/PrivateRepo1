package net.pic4pic.ginger.tasks;

import android.content.Context;
import android.os.AsyncTask;

import net.pic4pic.ginger.entities.GingerException;
import net.pic4pic.ginger.entities.Location;
import net.pic4pic.ginger.entities.MatchedCandidateListResponse;
import net.pic4pic.ginger.entities.SimpleRequest;
import net.pic4pic.ginger.service.Service;
import net.pic4pic.ginger.utils.MyLog;

public class PreviewCandidatesTask extends AsyncTask<String, Void, MatchedCandidateListResponse> {

	private Context activity;
	private Location location;
	private PreviewCandidatesListener listener;
	
	public PreviewCandidatesTask(Context activity, PreviewCandidatesListener listener, Location location) {
		this.activity = activity;
		this.location = location;
		this.listener = listener;
	}
	
	@Override
    protected MatchedCandidateListResponse doInBackground(String... executeArgs) {
		
		// make an HTTP post in a RESTfull way. Use JSON. 
    	// Once you get the data, convert it to Person
    	try {
    		SimpleRequest<Location> request = new SimpleRequest<Location>();
    		request.setData(this.location);
    		return Service.getInstance().getPreviewMatches(this.activity, request);
		} 
    	catch (GingerException e) {
    		
    		MyLog.bag().add(e).e();
    		
    		MatchedCandidateListResponse response = new MatchedCandidateListResponse();
			response.setErrorCode(1);
			response.setErrorMessage(e.getMessage());
			return response; 
		}
    	catch(Throwable e){
    		
    		MyLog.bag().add(e).e();
    		
    		MatchedCandidateListResponse response = new MatchedCandidateListResponse();
			response.setErrorCode(1);
			response.setErrorMessage("Connection to server failed when retrieving matches");
			return response; 	
    	}
    }

	@Override
    protected void onPostExecute(MatchedCandidateListResponse result) {
    	this.listener.onMatchComplete(result, this.location);
    }
    
    public interface PreviewCandidatesListener{    	
    	public void onMatchComplete(MatchedCandidateListResponse response, Location location);
    }
}
