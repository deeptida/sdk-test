/**
 * 
 */
package com.gl.couchbase.test;

/**
 * @author Deepti
 *
 */
public class OperationAttributes {
	private String key="";
	private int expTime=0;
	private String value="";
	private String operationName = "";
	private String valueToAppend = "";
	private long casValue=0;
	public long getCasValue() {
		return casValue;
	}
	public void setCasValue(long casValue) {
		this.casValue = casValue;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getExpTime() {
		return expTime;
	}
	public void setExpTime(int expTime) {
		this.expTime = expTime;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public String getValueToAppend() {
		return valueToAppend;
	}
	public void setValueToAppend(String valueToAppend) {
		this.valueToAppend = valueToAppend;
	}	
}
