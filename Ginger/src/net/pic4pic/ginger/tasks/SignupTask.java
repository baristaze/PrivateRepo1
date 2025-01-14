package net.pic4pic.ginger.tasks;

import android.content.Context;
import android.widget.Button;

import net.pic4pic.ginger.entities.GingerException;
import net.pic4pic.ginger.entities.SignupRequest;
import net.pic4pic.ginger.entities.UserResponse;
import net.pic4pic.ginger.service.Service;
import net.pic4pic.ginger.utils.MyLog;

public class SignupTask extends BlockedTask<String, Void, UserResponse> {
	
	private SignupListener listener;
	private SignupRequest request;
	
	public SignupTask(SignupListener listener, Context context, Button button, SignupRequest request){			
		super(context, button);		
		this.listener = listener;
		this.request = request;
	}
		
    @Override
    protected UserResponse doInBackground(String... executeArgs) {    	
    	// make an HTTP post in a RESTfull way. Use JSON. 
    	// Once you get the data, convert it to UserResponse
    	try {
			return Service.getInstance().signup(this.context, this.request);
		} 
    	catch (GingerException e) {
    		
    		MyLog.bag().add(e).e();
    		
    		MyLog.bag()
			.add("funnel", "signup")
			.add("step", "7")
			.add("page", "signup")
			.add("action", "post signup request")
			.add("success", "0")
			.add("error", "ginger")
			.m();
    		
			UserResponse response = new UserResponse();
			response.setErrorCode(1);
			response.setErrorMessage(e.getMessage());
			return response;
		}
    	catch(Throwable e){
    		
    		MyLog.bag().add(e).e();
    		
    		MyLog.bag()
			.add("funnel", "signup")
			.add("step", "7")
			.add("page", "signup")
			.add("action", "post signup request")
			.add("success", "0")
			.add("error", "throwable")
			.m();
    		
    		UserResponse response = new UserResponse();
			response.setErrorCode(1);
			response.setErrorMessage("Connection to server failed when signing up");
			return response; 	
    	}
    }

    protected void onPostExecute(UserResponse response) {    	
    	super.onPostExecute(response);    	
    	this.listener.onSignup(response, this.request);
    }
    
    public interface SignupListener{
    	public void onSignup(UserResponse response, SignupRequest request);
    }
}
