/**
 * 
 */
package com.gl.couchbase.test;

/**
 * @author Deepti
 *
 */
public class ConnectionAttributes {
	private String connectionUrl="";
	private String bucketName="";
	public String getConnectionUrl() {
		return connectionUrl;
	}
	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
}
