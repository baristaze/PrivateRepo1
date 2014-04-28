package net.pic4pic.ginger.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import net.pic4pic.ginger.entities.BaseRequest;
import net.pic4pic.ginger.entities.GingerException;
import net.pic4pic.ginger.entities.MatchedCandidateListResponse;
import net.pic4pic.ginger.service.Service;

public class MatchedCandidatesTask extends AsyncTask<String, Void, MatchedCandidateListResponse> {

	private Context activity;
	private BaseRequest request;
	private MatchedCandidatesListener listener;
	
	public MatchedCandidatesTask(Context activity, MatchedCandidatesListener listener, BaseRequest request) {
		this.activity = activity;
		this.request = request;
		this.listener = listener;
	}
	
	@Override
    protected MatchedCandidateListResponse doInBackground(String... executeArgs) {
		
		// make an HTTP post in a RESTfull way. Use JSON. 
    	// Once you get the data, convert it to Person
    	try {
    		return Service.getInstance().getTodaysMatches(this.activity, this.request);
		} 
    	catch (GingerException e) {
    		
    		Log.e("TodaysMatches", e.toString());
    		
    		MatchedCandidateListResponse response = new MatchedCandidateListResponse();
			response.setErrorCode(1);
			response.setErrorMessage(e.getMessage());
			return response; 
		}
    	catch(Exception e){
    		
    		Log.e("Signin", e.toString());
    		
    		MatchedCandidateListResponse response = new MatchedCandidateListResponse();
			response.setErrorCode(1);
			response.setErrorMessage("Unexpected error when retrieving matches");
			return response; 	
    	}
    }

    protected void onPostExecute(MatchedCandidateListResponse result) {
    	this.listener.onMatchComplete(result, this.request);
    }
    
    public interface MatchedCandidatesListener{    	
    	public void onMatchComplete(MatchedCandidateListResponse response, BaseRequest request);
    }
}