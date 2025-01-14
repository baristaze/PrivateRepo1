package net.pic4pic.ginger.utils;

import com.google.gson.annotations.SerializedName;

public class LogBagItem {
	
	@SerializedName("Name")
	protected String name;
	
	@SerializedName("Value")
	protected String value;
	
	public LogBagItem(String name, String value)
	{
		this.name = name;
		this.value = value;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getValue(){
		return this.value;
	}
	
	@Override
	public String toString(){
		
		if(this.name == null || this.name.length() == 0 ){
			return "";
		}
		
		if(this.value == null || this.value.length() == 0){
			return name;
		}
		
		return this.name + "=" + this.value; 
	}
}
