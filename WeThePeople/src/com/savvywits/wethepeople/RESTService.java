package com.savvywits.wethepeople;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

public class RESTService extends IntentService {
	
	private static final String ZIP_CODE_BASE =
			"http://whoismyrepresentative.com/getall_mems.php?output=json&zip=";
	
	private static final int STATUS_RUNNING = 1;
	private static final int STATUS_FINISHED = 2;
	private static final int STATUS_ERROR = 3;

	public RESTService() {
		super(RESTService.class.getName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		ResultReceiver receiver = extras.getParcelable("receiver");
		String data = extras.getString("zipcode");
		String url = ZIP_CODE_BASE + data;
		
		Bundle bundle = new Bundle();
		receiver.send(STATUS_RUNNING, Bundle.EMPTY);
		try {
        	String json = EntityUtils.toString(new DefaultHttpClient()
    		.execute(new HttpGet(url)).getEntity());
			bundle.putString("rest_result", json);
			receiver.send(STATUS_FINISHED, bundle);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
		} catch(Exception e) {
			bundle.putString(Intent.EXTRA_TEXT, e.toString());
			receiver.send(STATUS_ERROR, bundle);
		}
	}
}