package com.example.reversi;

public class ReversiClient {
	private boolean mIsConnected;
	private int[][] mChessBoard;
	private boolean isReady;
	private int mColor;
	private int mState;
	//there maybe other variables
	public ReversiClient(){
		mChessBoard = new int[8][8];
		mIsConnected = false;
		isReady = false;
		mColor = 0;
		mState = -1;
	}
	
	public boolean initConnect(){
		//a lot of things need to be done here. Parameters not set yet
		
		return mIsConnected;
	}
	
	public void sendMessage(){
		//not done yet
		
		
	}
	
	public String receiveMessage(){
		//not done yet
		String message = new String();
		
		
		
		return message;
	}
	
	public void parseMessage(String message){
		//
	}
	
	public void play(){
		while(true){
			try{
				String message = receiveMessage();
				Thread.sleep(100L);
				if (message != null){
					parseMessage(message);
				}
			}catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				
			}
		}
	}
}
