/***
  Copyright (c) 2013 Rich Dudka
  
  Licensed under the Apache License, Version 2.0 (the "License"); you may
  not use this file except in compliance with the License. You may obtain
  a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

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
        	
        	if (!validateJSON(json)) {
        		receiver.send(STATUS_ERROR, Bundle.EMPTY);
        	} else {
        		bundle.putString("rest_result", json);
        		receiver.send(STATUS_FINISHED, bundle);
        	}
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
	
	public static boolean validateJSON(String string) {
		return string != null && ("null".equals(string)
				|| (string.startsWith("[") && string.endsWith("]"))
				|| (string.startsWith("{") && string.endsWith("}")));
	}
}