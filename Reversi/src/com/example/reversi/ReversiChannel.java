package com.example.reversi;

import org.json.JSONException;
import org.json.JSONObject;

import tv.matchstick.fling.Fling;
import tv.matchstick.fling.FlingDevice;
import tv.matchstick.fling.FlingManager;
import tv.matchstick.fling.ResultCallback;
import tv.matchstick.fling.Status;

import android.util.Log;
public class ReversiChannel implements Fling.MessageReceivedCallback{
	private static final String TAG = ReversiChannel.class.getSimpleName();
	
	private static final String GAME_NAMESPACE = "";//UNDONE!!!!!

	public ReversiChannel() {
	}

	public String getNamespace() {
		return GAME_NAMESPACE;
	}
	
	  /**
     * The sender can use that to send String messages to the receiver over that channel
     * @param apiClient
     * @param message
     */
    private final void sendMessage(FlingManager apiClient, String message) {
        Log.d("wrhOn", "Sending message: (ns=" + GAME_NAMESPACE + ") " + message);
        Fling.FlingApi.sendMessage(apiClient, GAME_NAMESPACE, message)
                .setResultCallback(new SendMessageResultCallback(message));
        Log.d("wrhOn","Message sended successfully!");
    }

    private final class SendMessageResultCallback implements
    		ResultCallback<Status> {
    	String mMessage;

    	SendMessageResultCallback(String message) {
    		mMessage = message;
    	}

		@Override
		public void onResult(Status result) {
		    if (!result.isSuccess()) {
		        Log.d(TAG,
		                "Failed to send message. statusCode: "
		                        + result.getStatusCode() + " message: "
		                        + mMessage);
		    }
		}
    }
    
    public final void start(FlingManager apiClient) {
        try {
            Log.d("wrhOn", "start");
            JSONObject payload = new JSONObject();
            payload.put("command", "start");
            sendMessage(apiClient, payload.toString());
            Log.d("wrhOn","start ok!");
        } catch (JSONException e) {
            Log.e(TAG, "Cannot create object to join a game", e);
        }
    }

    public final void stop(FlingManager apiClient) {
        Log.d(TAG, "stop");
        try {
            JSONObject payload = new JSONObject();
            payload.put("command", "stop");
            sendMessage(apiClient, payload.toString());
        } catch (JSONException e) {
            Log.e(TAG, "Cannot create object to send a move", e);
        }
    }
    
    @Override
    public void onMessageReceived(FlingDevice flingDevice, String namespace,
            String message) {
        Log.d(TAG, "onTextMessageReceived: " + message);
        JSONObject payload;
        try {
            payload = new JSONObject(message);
            Log.d(TAG, "payload: " + payload);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    
}
