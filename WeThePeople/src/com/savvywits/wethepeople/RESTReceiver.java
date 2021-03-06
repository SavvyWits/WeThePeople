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

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class RESTReceiver extends ResultReceiver {
	
	private Receiver mReceiver;
	
	public RESTReceiver(Handler handler) {
		super(handler);
	}
	
	public void setReceiver(Receiver receiver) {
		mReceiver = receiver;
	}
	
	public interface Receiver {
		public void onReceiveResult(int resultCode, Bundle resultData);
	}
	
	@Override
	protected void onReceiveResult(int resultCode, Bundle resultData) {
		if (mReceiver != null) {
			mReceiver.onReceiveResult(resultCode, resultData);
		}
	}

}