package com.example.reversi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
//import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
//import android.support.v7.app.MediaRouteActionProvider;
//import android.support.v7.media.MediaRouteSelector;
//import android.support.v7.media.MediaRouter;
//import android.support.v7.media.MediaRouter.RouteInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
//import android.view.MenuItem;
//import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.String;
import java.io.IOException;

//import tv.matchstick.fling.ApplicationMetadata;
//import tv.matchstick.fling.ConnectionResult;
import tv.matchstick.fling.Fling;
//import tv.matchstick.fling.Fling.ApplicationConnectionResult;
//import tv.matchstick.fling.Fling.FlingApi;
//import tv.matchstick.fling.FlingManager.ApiOptions;
//import tv.matchstick.fling.internal.Api;
//import tv.matchstick.fling.FlingDevice;
//import tv.matchstick.fling.FlingManager;
//import tv.matchstick.fling.FlingMediaControlIntent;
//import tv.matchstick.fling.ResultCallback;
//import tv.matchstick.fling.Status;

/**
 * This activity is used to play Reversi on MatchStick Dongle
 * 
 * @author Wang Runhui
 *
 */

public class ReversiActivity extends ActionBarActivity {
	private static final String TAG=ReversiActivity.class.getSimpleName();
	private static final int REQUEST_GMS_ERROR = 0;		//What is this used for ?
	private static final int EMPTY = 0;		//the color on the board
	private static final int WHITE = 1;		//the color on the board
	private static final int BLACK = 2; 	//the color on the board
	
		private static final String APP_URL = "http://192.168.16.131/index.html";
	private FlingReversiManager mFlingGameManager;
	/*	//private static final String APP_ID = "" ???
	public String APP_ID;
	 */	
/*	private static final String APP_ID = Fling.FlingApi.makeApplicationId(
	        "http://fling.infthink.com/receiver/tictactoe/tictactoe.html");
*/	
	private Button mJoinGameButton;
	private Button mStartToPlayButton;
	private Button[][] mChessBoard;
	private int[][][] mIntChessBoard;		//3-dimension array is used for withdraw, not done yet
	private LinearLayout mCBlayout;
	private TextView mInfoView;
	private TextView mPlayerNameView;
	private State mAssignedPlayer = State.UNKNOWN;
	
//	private FlingDevice mSelectedDevice;	//Contain all info about a Fling device.
//	private FlingManager mApiClient;		//Fling Manager interface. Before any operation is executed, the FlingManager must be connected using the connect() method. The device is not considered connected until the onConnected(Bundle) callback has been called. When your app is done using this connection, call disconnect(), even if the async result from connect() has not yet been delivered. You should instantiate a client object in your Activity's onCreate(Bundle) method and then call connect() in onStart() and disconnect() in onStop(), regardless of the state.
//    private Fling.Listener  mFlingListener;	//Interface which will be notified when fling application's status is changed.
	private playController mPlayController;
//    private MediaRouter mMediaRouter;
//	private MediaRouteSelector mMediaRouteSelector;
//    private MediaRouter.Callback mMediaRouterCallback;
	private ReversiChannel mGameChannel;
	private int mRow;
	private int mColumn;

    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		Log.d("wrhOn","onCreate begin!!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reversi);
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setIcon(R.drawable.ic_launcher);
        actionBar.setDisplayShowTitleEnabled(true);

        mFlingGameManager = new FlingReversiManager(this,APP_URL);
        
        DisplayMetrics display = new DisplayMetrics();
        mJoinGameButton = (Button) findViewById (R.id.JoinGame);
        mStartToPlayButton = (Button) findViewById (R.id.StartToPlay);
        mInfoView = (TextView) findViewById (R.id.info);
        mPlayerNameView = (TextView) findViewById (R.id.username);
        mPlayController = new playController();
        
//        mGameChannel = new ReversiChannel();
        
//        mMediaRouter = MediaRouter.getInstance(getApplicationContext());
//        mMediaRouteSelector = new MediaRouteSelector.Builder()
//        	.addControlCategory(FlingMediaControlIntent.categoryForFling(APP_ID))
//        	.build();
        
//        mMediaRouterCallback = new MediaRouterCallback();
//        mFlingListener = new FlingListener();
//        mConnectionCallbacks = new ConnectionCallbacks();
//        mConnectionFailedListener = new ConnectionFailedListener();
        
        mJoinGameButton.setEnabled(false);
        mJoinGameButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
/*                if (mApiClient != null) {
                	Log.d("wrhdebug",""+mGameChannel);
                    mGameChannel.join(mApiClient, "MyName");//need to be rewriten
                    mInfoView.setText("Please wait for the other player...");
                    mJoinGameButton.setEnabled(false);
                }*/
            }
        });
        
        getWindowManager().getDefaultDisplay().getMetrics(display);
        mCBlayout = (LinearLayout)findViewById(R.id.chessBoard);
        int width = display.widthPixels;
        int height = display.heightPixels;
        if (width > height) width = height; 
        int checkerWidth = (width-27)/8;
        int chessBoardWidth = checkerWidth*8+27;
        mCBlayout.setLayoutParams(new LinearLayout.LayoutParams(chessBoardWidth, chessBoardWidth));
        int id=0;
        id= R.id.pos1;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(checkerWidth, checkerWidth);
        mIntChessBoard = new int[61][8][8];
        mChessBoard = new Button[8][];
        for (int i = 0; i < 8; i ++){
        	mChessBoard[i] = new Button[8];
        	for (int j = 0; j < 8; j ++){
        		mChessBoard[i][j] = (Button)findViewById(id);
        		id ++;
        		//add picture
        		mChessBoard[i][j].setLayoutParams(params);
        		mIntChessBoard[0][i][j] = EMPTY;
        		final int r = i;
        		final int c = j;
        		mChessBoard[i][j].setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//wrh important here
						//mGameChannel.move(mApiClient, r, c);
					}
        			
        		});
        		mChessBoard[i][j].setEnabled(false);
        	}
        }
        mIntChessBoard[0][3][3] = WHITE;
        mIntChessBoard[0][5][5] = WHITE;
        mIntChessBoard[0][3][4] = BLACK;
        mIntChessBoard[0][4][3] = BLACK;
        mPlayController.findw();
        refreshChessTable();
        Log.d("wrhOn","onCreate OK!!");
    }
	

	/**
     * Called when the options menu is first created.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	Log.d("wrhOn","onCreateOptionsMenu begin!!");
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu1, menu);
        mFlingGameManager
        	.addMediaRouterButton(menu, R.id.media_route_menu_item);
        Log.d("wrh",mFlingGameManager.mApiClient+"");
      
        Log.d("wrhOn","onCreateOptionsMenu OK!!");
        return true;
    }

    /**
     * Called on application start. Using the previously selected Cast device, attempts to begin a
     * session using the application name TicTacToe.
     */
    @Override
    protected void onStart() {
    	Log.d("wrhOn","onStart begin!!");
        super.onStart();
//        mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback,
//                MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN);
        Log.d("wrhOn","onStart OK!!");
    }


    @Override
    protected void onResume() {
    	Log.d("wrhOn","onResume begin!!");
        super.onResume();
        Log.d("wrhOn","onResume OK!!");
    }

    /**
     * Removes the activity from memory when the activity is paused.
     */
    @Override
    protected void onPause() {
    	Log.d("wrhOn","onPause begin!!");
        finish();
        super.onPause();
        Log.d("wrhOn","onPuase OK!!");
    }

    /**
     * Attempts to end the current game session when the activity stops.
     */
    @Override
    protected void onStop() {
    	Log.d("wrhOn","onStop Begin!!");
    	super.onStop();
        Log.d("wrhOn","onStop OK!!");
    }
    
    @Override
    protected void onDestroy() {
    	Log.d("wrhOn","onDestroy Begin!!");
        super.onDestroy();
        mFlingGameManager.destroy();
        Log.d("wrhOn","onDestroy OK!!");
    }
    
    /**
     * Returns the screen configuration to portrait mode whenever changed.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Log.d("wrhOn","onConfigurationChanged OK!!");
    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
*/    
    /**
     * Returns the string representation of a State object representing a player, or null if the
     * passed player does not correspond to an X or O player.
     */
    public enum State {
        UNKNOWN(-1),
        EMPTY(0),
        PLAYER_WHITE(1),
        PLAYER_BLACK(2);

        private int mValue;

        private State(int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }

        /**
         * Creates and returns a State object based on a passed int value.
         */
        public static State fromInt(int i) {
            for (State s : values()) {
                if (s.getValue() == i) {
                    return s;
                }
            }
            return EMPTY;
        }
    }
    private String convertGameStateToPlayer(State player) {
        if (player == State.PLAYER_WHITE) {
            return GameChannel.PLAYER_WHITE;
        }
        if (player == State.PLAYER_BLACK) {
            return GameChannel.PLAYER_BLACK;
        }
        return null;
    }

    private void setFinished(int winner){
    	String text = winner+"";
    	new AlertDialog.Builder(ReversiActivity.this)
	        .setTitle("Game Over")
	        .setMessage(text)
	        .setCancelable(false)
	        .setPositiveButton("Play again", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int id) {
	                mPlayerNameView.setText(null);
	            //wrh important here
	            //    mGameChannel.join(mApiClient, "MyName");
	                mInfoView.setText("waiting_for_player_assignment");
	            }
	        })
	        .setNegativeButton("Leave", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int id) {
	                finish();
	            }
	        })
	        .create()
	        .show();
    	Log.d("wrhOn","setFinished OK!!");
    }
    
    private class ReversiChannel extends GameChannel {
    	/**
         * Sets displays accordingly when a new player joins the game.
         *
         * @param playerSymbol either WHITE or BLACK
         * @param opponentName the name of the player who just joined an existing game
         */
		@Override
		protected void onGameJoined(String playerSymbol, String opponentName) {
			State newPlayer = State.EMPTY;
			if (GameChannel.PLAYER_WHITE.equals(playerSymbol)) {
				newPlayer = State.PLAYER_WHITE;
			} else if (GameChannel.PLAYER_BLACK.equals(playerSymbol)){
				newPlayer = State.PLAYER_BLACK;
			}
			mAssignedPlayer = newPlayer;
			mPlayerNameView.setText(String.format("You are player <b>%s", playerSymbol));
			mInfoView.setText(String.format("It is player <b>%s</b>\'s turn.", GameChannel.PLAYER_WHITE));
			Log.d("wrhOn","onGameJoined OK!!");
		}
		
		/**
         * Updates the game display upon a move.
         */
		@Override
		protected void onGameMove(String playerSymbol, int row, int column,
				boolean isGameOver) {
			// TODO Auto-generated method stub
			if (playerSymbol.equals(GameChannel.PLAYER_WHITE)) {
				mPlayController.gob(row, column);
			}else {
				mPlayController.gow(row, column);
			}
			if (mPlayController.judge()) {
				//wrh important here
				//mGameChannel.end(mApiClient);
			}
			refreshChessTable();
			Log.d("wrhOn","onGameMove OK!!");
		}

		@Override
		protected void onGameEnd() {
			// TODO Auto-generated method stub
			mPlayController.judge();
			int w = mPlayController.winner;
			if (mPlayController.winner == 3){
				Toast.makeText(getApplicationContext(), "End up a tie!", Toast.LENGTH_LONG).show();
			} else if (mPlayController.winner == mAssignedPlayer.mValue) {
				Toast.makeText(getApplicationContext(), "You win!", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "You lose!", Toast.LENGTH_LONG).show();
			}
			setFinished(w);
			Log.d("wrhOn","OnGameEnd OK!!");
		}


		@Override
		protected void onGameError(String errorMessage) {
			// TODO Auto-generated method stub
			
		}
        /**
         * Sets displays accordingly when a new player joins the game.
         *
         * @param playerSymbol either X or O
         * @param opponentName the name of the player who just joined an existing game
         */
    	
    	//!!!!!!Need to be rewrite!!!!!!
    }
    //??????
/*    private void onRouteSelected(RouteInfo route) {
        Log.d(TAG, "onRouteSelected: " + route.getName());

        FlingDevice device = FlingDevice.getFromBundle(route.getExtras());
        setSelectedDevice(device);
        Log.d("wrhOn","onRouteSelected OK!!");
    }
*/
    public void refreshChessTable() {
		// TODO Auto-generated method stub
    	for (int i = 0; i < 8; i ++){
        	for (int j = 0; j < 8; j ++){
        		int flag = mPlayController.mTable[mPlayController.mStepNum][i][j];
        		if (flag == 3)
        			mChessBoard[i][j].setEnabled(true);
        		else
        			mChessBoard[i][j].setEnabled(false);
        		if (flag == 1) {
        			mChessBoard[i][j].setBackgroundResource(R.drawable.chess_white);
        		}
        		if (flag == 2) {
        			mChessBoard[i][j].setBackgroundResource(R.drawable.chess_black);
        		}
        	}
        }
	}

/*
	//??????
    private void onRouteUnselected(RouteInfo route) {
        Log.d(TAG, "onRouteUnselected: " + route.getName());
        setSelectedDevice(null);
        Log.d("wrhOn","onRouteUnselected OK!!");
    }
 */   
/*    private class MediaRouterCallback extends MediaRouter.Callback {
        @Override
        public void onRouteSelected(MediaRouter router, RouteInfo route) {
        	//?????when will this be called?
            Log.d(TAG, "onRouteSelected: " + route);
            ReversiActivity.this.onRouteSelected(route);
        }

        @Override
        public void onRouteUnselected(MediaRouter router, RouteInfo route) {
            Log.d(TAG, "onRouteUnselected: " + route);
            ReversiActivity.this.onRouteUnselected(route);
        }
    }
*/
/*    private class FlingListener extends Fling.Listener {
        @Override
        public void onApplicationDisconnected(int statusCode) {
            Log.d(TAG, "Cast.Listener.onApplicationDisconnected: " + statusCode);
            try {
                Fling.FlingApi.removeMessageReceivedCallbacks(mApiClient,
                        mGameChannel.getNamespace());
            } catch (IOException e) {
                Log.w(TAG, "Exception while launching application", e);
            }
            Log.d("wrhOn","OnApplicationDisconnected OK!!");
        }
    }
*/    
    
/*    private void connectApiClient() {
    	Log.d("wrhOn","ConnectApiClient begin!!");
        Fling.FlingOptions apiOptions = Fling.FlingOptions.builder(mSelectedDevice, mFlingListener)
                .build();
        //Fling options Contained all fling options, for example, application status listener, log flags.
        //The Fling.FlingOptions.Builder is used to create an instance of Fling.FlingOptions.
        
        //public static final class FlingManager.Builder, extends java.lang.Object, Helper class for FlingApi class
        //public static interface Fling.FlingApi, The entry point for interacting with a Fling device.
        mApiClient = new FlingManager.Builder(this)
                .addApi(Fling.API, apiOptions)//Map<Api, ApiOptions>  ??????What is fling.internal used for ??????
                .addConnectionCallbacks(mConnectionCallbacks)
                .addOnConnectionFailedListener(mConnectionFailedListener)
                .build();
        mApiClient.connect();
        //??????still confused
        Log.d("wrhOn","ConnectApiClient OK!!");
    }

    private void disconnectApiClient() {
        if (mApiClient != null) {
            mApiClient.disconnect();
            mApiClient = null;
        }
        Log.d("wrhOn","DisconnectApiClient OK!!");
    }
*/
/*    private void setSelectedDevice(FlingDevice device) {
        Log.d(TAG, "setSelectedDevice: " + device);
        mSelectedDevice = device;

        if (mSelectedDevice != null) {
            try {
                disconnectApiClient();
                connectApiClient();
            } catch (IllegalStateException e) {
                Log.w(TAG, "Exception while connecting API client", e);
                disconnectApiClient();
            }
        } else {
            if (mApiClient != null) {
                if (mApiClient.isConnected()) {
                    mGameChannel.leave(mApiClient);
                }
                disconnectApiClient();
            }
            mJoinGameButton.setEnabled(false);

            mPlayerNameView.setText(null);
            mInfoView.setText("Select device to start the game.");
            mMediaRouter.selectRoute(mMediaRouter.getDefaultRoute());//???What is mMediaRouter used for ? 
        }
        Log.d("wrhOn","setSelectedDevice OK!!");
    }
*/
/*    private class ConnectionCallbacks implements FlingManager.ConnectionCallbacks {
    	//Connection callback. Provides callbacks that are called when the client is connected or disconnected from the service.
    	//Most applications implement onConnected(Bundle) to start making requests.
        @Override
        public void onConnectionSuspended(int cause) {
            Log.d(TAG, "ConnectionCallbacks.onConnectionSuspended");
        }

        @Override
        public void onConnected(Bundle connectionHint) {
            Log.d(TAG, "ConnectionCallbacks.onConnected");
            Fling.FlingApi.launchApplication(mApiClient, APP_ID).setResultCallback(
                    new ConnectionResultCallback());
        }
    }
*/    
/*    private class ConnectionFailedListener implements FlingManager.OnConnectionFailedListener {
        @Override
        public void onConnectionFailed(ConnectionResult result) {
            Log.d(TAG, "ConnectionFailedListener.onConnectionFailed");
            setSelectedDevice(null);
        }
    }
*/
/*    private final class ConnectionResultCallback implements
            ResultCallback<ApplicationConnectionResult> {
        @Override
        public void onResult(ApplicationConnectionResult result) {
            Status status = result.getStatus();
            ApplicationMetadata appMetaData = result.getApplicationMetadata();

            if (status.isSuccess()) {
                Log.d(TAG, "ConnectionResultCallback: " + appMetaData.getName());
                mJoinGameButton.setEnabled(true);
                try {//?????? Why Fling.FlingApi ??????
                	//in Fling.class: 
                	//public static final FlingApi FlingApi = new FlingApi.FlingApiImpl();
                    Fling.FlingApi.setMessageReceivedCallbacks(mApiClient,
                            mGameChannel.getNamespace(), mGameChannel);
                } catch (IOException e) {
                    Log.w(TAG, "Exception while launching application", e);
                }
            } else {
                Log.d(TAG, "ConnectionResultCallback. Unable to launch the game. statusCode: "
                        + status.getStatusCode());
                mJoinGameButton.setEnabled(false);
            }
        }
    }
*/
}




