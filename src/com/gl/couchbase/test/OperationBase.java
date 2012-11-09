package com.gl.couchbase.test;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.internal.GetFuture;
import net.spy.memcached.internal.OperationFuture;

import com.couchbase.client.CouchbaseClient;

public class OperationBase {

	public OperationBase(ConnectionAttributes conAttr) {
		this.conAttr=conAttr;
	}

	public OperationBase(OperationAttributes opAttr) {
		this.opAttr=opAttr;	
	}

	private ConnectionAttributes conAttr;
	private OperationAttributes opAttr;
	private String displayableException;
	private String displayableMessage;

	public String getDisplayableMessage() {
		return displayableMessage;
	}

	public void setDisplayableMessage(String displayableMessage) {
		this.displayableMessage = displayableMessage;
	}

	public String getDisplayableException() {
		return displayableException;
	}

	public void setDisplayableException(String displayableException) {
		this.displayableException = displayableException;
	}

	public Object executeOperation(CouchbaseClient client) {		
		Object getObject=null;
		String operationName = opAttr.getOperationName();
		if(operationName.equals("Get")){
			getObject = getKeyValueFromDB(client);
		}else if (operationName.equals("Set")){
			setKeyValueToDB(client);
		}else if (operationName.equals("Remove")){
			deleteKeyValueFromDB(client);
		}else if(operationName.equals("Add")){
			addKeyValueToDB(client);
		}else if(operationName.equals("Append")){
			appendKeyValueToDB(client);
		}else if(operationName.equals("Prepend")){
			prependKeyValueToDB(client);
		}else if(operationName.equals("Get with CAS")){
			getObject =getKeyValueWithCas(client);
		}else if(operationName.equals("Set with CAS")){
			setKeyValueWithCas(client);
		}else if(operationName.equals("DPR") || operationName.equals("Delete and Persist")){
//			getObject =deleteKeyValueAndPersist(client);
		}	
		return getObject;
	}

	private Object setKeyValueWithCas(CouchbaseClient client) {

		CASResponse setOp = client.cas(opAttr.getKey(), opAttr.getCasValue(), opAttr.getValue());	
		

		// Now we want to see what happened with our data
		// Check to see if our set succeeded
		try {
			if (setOp.equals(CASResponse.OK)) {
				setDisplayableMessage("Value was updated");
			}
			else if (setOp.equals(CASResponse.NOT_FOUND)) {
				setDisplayableException("Value is not found");
			}
			else if (setOp.equals(CASResponse.EXISTS)) {
				setDisplayableException("Value exists, but CAS didn't match");
			}

		}
		catch (Exception e) {
			setDisplayableException("Exception while doing set: "+ e.getMessage());
		}
		return setOp;

	}

	public CouchbaseClient connect() {
		// Set the URIs and get a client
		List<URI> uris = new LinkedList<URI>();

		// Connect to localhost or to the appropriate URI
		uris.add(URI.create(conAttr.getConnectionUrl()));

		CouchbaseClient client = null;
		try {
			client = new CouchbaseClient(uris, "default", "");			
		} catch (Exception e) {
			setDisplayableException("Error connecting to Couchbase: "+ e.getMessage());
			System.exit(0);
		}
		return client;
	}

	private Future<Boolean> prependKeyValueToDB(CouchbaseClient client) {
		// Do an asynchronous prepend
		Future<Boolean> prependOp=null;
		String key = opAttr.getKey();
		CASValue<Object> casValue = client.gets(key);
		if (casValue == null) {
			setDisplayableException("Key does not exist");			
		} else {
			prependOp= client.prepend(casValue.getCas(),
					key, opAttr.getValueToAppend());			
		}		

		// Check to see if our prepend succeeded
		try {
			if (prependOp.get().booleanValue()) {
				setDisplayableMessage("Prepend Succeeded");
			} else {
				setDisplayableException("Prepend failed: ");
			}
		} catch (Exception e) {
			setDisplayableException("Exception while doing append: "+ e.getMessage());
		}
		return prependOp;
	}

	/**
	 * This method is used to append the value corresponding to the key into the DB.
	 * @param do_delete
	 * @param client
	 * @return
	 */
	private Future<Boolean> appendKeyValueToDB(CouchbaseClient client) {
		// Do an asynchronous append
		Future<Boolean> appendOp=null;
		String key = opAttr.getKey();
		CASValue<Object> casValue = client.gets(key);
		if (casValue == null) {
			setDisplayableException("Key does not exist");			
		} else {
			appendOp= client.append(casValue.getCas(),
					key, opAttr.getValueToAppend());			
		}		

		// Check to see if our append succeeded
		try {
			if (appendOp.get().booleanValue()) {
				setDisplayableMessage("Append Succeeded");
			} else {
				setDisplayableException("Append failed: ");
			}
		} catch (Exception e) {
			setDisplayableException("Exception while doing append: "+ e.getMessage());
		}
		return appendOp;
	}

	/**
	 * This method is used to delete the value corresponding to the key from the DB.
	 * @param do_delete
	 * @param client
	 * @return
	 */
	private OperationFuture<Boolean> deleteKeyValueFromDB(CouchbaseClient client) {
		// Do an asynchronous delete
		OperationFuture<Boolean> delOp = client.delete(opAttr.getKey());

		// Check to see if our delete succeeded
		try {
			if (delOp.get().booleanValue()) {
				setDisplayableMessage("Delete Succeeded");
			} else {
				setDisplayableException("Delete failed: " +delOp.getStatus().getMessage());
			}
		} catch (Exception e) {
			setDisplayableException("Exception while doing delete: "+ e.getMessage());
		}

		return delOp;
	}

	/**
	 * This method is used to delete the value corresponding to the key from the DB.
	 * @param do_delete
	 * @param client
	 * @return
	 */
	private OperationFuture<Boolean> addKeyValueToDB(CouchbaseClient client) {
		// Do an asynchronous add
		OperationFuture<Boolean> addOp = client.add(opAttr.getKey(), opAttr.getExpTime(), opAttr.getValue());	

		// Check to see if our add succeeded
		try {
			if (addOp.get().booleanValue()) {
				setDisplayableMessage("Add Succeeded");
			} else {
				setDisplayableException("Add failed: " +addOp.getStatus().getMessage());
			}
		} catch (Exception e) {
			setDisplayableException("Exception while doing add: "+ e.getMessage());
		}

		return addOp;
	}

	/**
	 * This method is used to set the value corresponding to the key into the DB.
	 * @param client
	 * @return
	 */
	private OperationFuture<Boolean> setKeyValueToDB(CouchbaseClient client) {
		// Do an asynchronous set
		OperationFuture<Boolean> setOp = client.set(opAttr.getKey(), opAttr.getExpTime(), opAttr.getValue());		

		// Now we want to see what happened with our data
		// Check to see if our set succeeded
		try {
			if (setOp.get().booleanValue()) {
				setDisplayableMessage("Set Succeeded");
			} else {
				setDisplayableException("Set failed: "+ setOp.getStatus().getMessage());
			}
		} catch (Exception e) {
			setDisplayableException("Exception while doing set: "+ e.getMessage());
		}
		return setOp;
	}

	/**
	 * This method is used to get the value stored in DB for the corresponding key.
	 * @param client
	 * @return
	 */
	private Object getKeyValueFromDB(CouchbaseClient client) {
		// Do a synchronous get
		String key = opAttr.getKey();
		Object getObject = client.get(key);

		// Print the value from synchronous get
		if (getObject != null) {
			setDisplayableMessage("Synchronous Get Suceeded: "+ (String) getObject);
		} else {
			setDisplayableException("Synchronous Get failed");
		}

		// Do an asynchronous get
		GetFuture<Object> getOp = client.asyncGet(key);

		// Check to see if ayncGet succeeded
		try {
			if ((getObject = getOp.get()) != null) {
				setDisplayableMessage("Asynchronous Get Succeeded: "+ getObject);
			} else {
				setDisplayableException("Asynchronous Get failed: "+ getOp.getStatus().getMessage());
			}
		} catch (Exception e) {
			setDisplayableException("Exception while doing Aynchronous Get: "+ e.getMessage());			
		}

		return getObject;
	}

	private CASValue<Object> getKeyValueWithCas(CouchbaseClient client) {

		String key = opAttr.getKey();
		CASValue<Object> casValue = client.gets(key);
		if (casValue == null) {
			setDisplayableException("Key does not exist");			
		} 
		return casValue;
	}
}
