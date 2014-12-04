/*
 * Copyright (C) 2013 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.reversi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tv.matchstick.fling.Fling;
import tv.matchstick.fling.FlingDevice;
import tv.matchstick.fling.FlingManager;
import tv.matchstick.fling.ResultCallback;
import tv.matchstick.fling.Status;

import android.util.Log;

/**
 * An abstract class which encapsulates control and game logic for sending and receiving messages
 * during a TicTacToe game.
 */

//!!!!!!  Need to be rewriten  !!!!!!
public abstract class GameChannel implements Fling.MessageReceivedCallback {
    private static final String TAG = GameChannel.class.getSimpleName();

    private static final String GAME_NAMESPACE = "urn:com.example.Reversi";//??????what is this used for??????

    public static final String END_STATE_WHITE_WON = "white-won";
    public static final String END_STATE_BLACK_WON = "black-won";
    public static final String END_STATE_DRAW = "draw";
    public static final String END_STATE_ABANDONED = "abandoned";

    public static final String PLAYER_WHITE = "white";
    public static final String PLAYER_BLACK = "black";

    // Receivable event types
    private static final String KEY_BOARD_LAYOUT_RESPONSE = "board_layout_response";
    private static final String KEY_EVENT = "event";
    private static final String KEY_JOINED = "joined";
    private static final String KEY_MOVED = "moved";
    private static final String KEY_ENDGAME = "endgame";
    private static final String KEY_ERROR = "error";

    // Commands
    private static final String KEY_BOARD_LAYOUT_REQUEST = "board_layout_request";
    private static final String KEY_COMMAND = "command";
    private static final String KEY_JOIN = "join";
    private static final String KEY_MOVE = "move";
    private static final String KEY_LEAVE = "leave";

    private static final String KEY_BOARD = "board";
    private static final String KEY_COLUMN = "column";
    private static final String KEY_END_STATE = "end_state";
    private static final String KEY_GAME_OVER = "game_over";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_NAME = "name";
    private static final String KEY_OPPONENT = "opponent";
    private static final String KEY_PLAYER = "player";
    private static final String KEY_ROW = "row";
    private static final String KEY_WINNING_LOCATION = "winning_location";

    /**
     * An enum representing board rows, columns, and diagonals as numerical values.
     */
    

    /**
     * Constructs a new GameChannel m with GAME_NAMESPACE as the namespace used by
     * the superclass.
     */
    protected GameChannel() {
    }

    /**
     * Performs some action upon a player joining the game.
     *
     * @param playerSymbol either X or O
     * @param opponentName the name of the player who just joined an existing game, or the opponent
     */
    protected abstract void onGameJoined(String playerSymbol, String opponentName);

    /**
     * Performs some action, or updates the game display upon a move.
     *
     * @param playerSymbol either X or O
     * @param row the row index of the move
     * @param column the column index of the move
     * @param isGameOver whether or not the game ended as a result of the move
     */
    protected abstract void onGameMove(
            String playerSymbol, int row, int column, boolean isGameOver);

    /**
     * Performs some action upon game end, depending on game's end state and the position of the
     * winning pieces.
     *
     * @param location an int value corresponding to the enum WinningLocation's values
     */
    protected abstract void onGameEnd();


    /**
     * Performs some action upon a game error.
     *
     * @param errorMessage the string description of the error
     */
    protected abstract void onGameError(String errorMessage);

    /**
     * Returns the namespace for this cast channel.
     */
    public String getNamespace() {
        return GAME_NAMESPACE;
    }

    /**
     * Attempts to connect to an existing session of the game by sending a join command.
     *
     * @param name the name of the player that is joining
     */
    public final void join(FlingManager apiClient, String name) {
        try {
            Log.d(TAG, "join: " + name);
            JSONObject payload = new JSONObject();
            payload.put(KEY_COMMAND, KEY_JOIN);
            payload.put(KEY_NAME, name);
            sendMessage(apiClient, payload.toString());
        } catch (JSONException e) {
            Log.e(TAG, "Cannot create object to join a game", e);
        }
    }

    /**
     * Attempts to make a move by sending a command to place a piece in the given row and column.
     */
    public final void move(FlingManager apiClient, final int row, final int column) {
        Log.d(TAG, "move: row:" + row + " column:" + column);
        try {
            JSONObject payload = new JSONObject();
            payload.put(KEY_COMMAND, KEY_MOVE);
            payload.put(KEY_ROW, row);
            payload.put(KEY_COLUMN, column);
            Log.d("wrh","GameChannel.move:apiClient="+apiClient+"");
            sendMessage(apiClient, payload.toString());
        } catch (JSONException e) {
            Log.e(TAG, "Cannot create object to send a move", e);
        }
    }

    /**
     * Sends a command to leave the current game.
     */
    public final void leave(FlingManager apiClient) {
        try {
            Log.d(TAG, "leave");
            JSONObject payload = new JSONObject();
            payload.put(KEY_COMMAND, KEY_LEAVE);
            sendMessage(apiClient, payload.toString());
        } catch (JSONException e) {
            Log.e(TAG, "Cannot create object to leave a game", e);
        }
    }

    public final void end(FlingManager apiClient) {
        try {
            Log.d(TAG, "end");
            JSONObject payload = new JSONObject();
            payload.put(KEY_COMMAND, KEY_ENDGAME);
            sendMessage(apiClient, payload.toString());
        } catch (JSONException e) {
            Log.e(TAG, "Cannot create object to leave a game", e);
        }
    }
    /**
     * Sends a command requesting the current layout of the board.
     */
    public final void requestBoardLayout(FlingManager apiClient) {
        try {
            Log.d(TAG, "requestBoardLayout");
            JSONObject payload = new JSONObject();
            payload.put(KEY_COMMAND, KEY_BOARD_LAYOUT_REQUEST);
            sendMessage(apiClient, payload.toString());
        } catch (JSONException e) {
            Log.e(TAG, "Cannot create object to request board layout", e);
        }
    }

    /**
     * Processes all Text messages received from the receiver device and performs the appropriate
     * action for the message. Recognizable messages are of the form:
     *
     * <ul>
     * <li> KEY_JOINED: a player joined the current game
     * <li> KEY_MOVED: a player made a move
     * <li> KEY_ENDGAME: the game has ended in one of the END_STATE_* states
     * <li> KEY_ERROR: a game error has occurred
     * <li> KEY_BOARD_LAYOUT_RESPONSE: the board has been laid out in some new configuration
     * </ul>
     *
     * <p>No other messages are recognized.
     */
    @Override
    public void onMessageReceived(FlingDevice flingDevice, String namespace, String message) {
        try {
            Log.d(TAG, "onTextMessageReceived: " + message);
            JSONObject payload = new JSONObject(message);
            Log.d(TAG, "payload: " + payload);
            if (payload.has(KEY_EVENT)) {
                String event = payload.getString(KEY_EVENT);
                if (KEY_JOINED.equals(event)) {
                    Log.d(TAG, "JOINED");
                    try {
                        String player = payload.getString(KEY_PLAYER);
                        String opponentName = payload.getString(KEY_OPPONENT);
                        onGameJoined(player, opponentName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (KEY_MOVED.equals(event)) {
                    Log.d(TAG, "MOVED");
                    try {
                        String player = payload.getString(KEY_PLAYER);
                        int row = payload.getInt(KEY_ROW);
                        int column = payload.getInt(KEY_COLUMN);
                        boolean isGameOver = payload.getBoolean(KEY_GAME_OVER);
                        onGameMove(player, row, column, isGameOver);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (KEY_ENDGAME.equals(event)) {
                    Log.d(TAG, "ENDGAME");
                    onGameEnd();
                } else if (KEY_ERROR.equals(event)) {
                    Log.d(TAG, "ERROR");
                    try {
                        String errorMessage = payload.getString(KEY_MESSAGE);
                        onGameError(errorMessage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } 
            } else {
                Log.w(TAG, "Unknown payload: " + payload);
            }
        } catch (JSONException e) {
            Log.w(TAG, "Message doesn't contain an expected key.", e);
        }
    }

    private final void sendMessage(FlingManager apiClient, String message) {
        Log.d(TAG, "Sending message: (ns=" + GAME_NAMESPACE + ") " + message);
        Fling.FlingApi.sendMessage(apiClient, GAME_NAMESPACE, message).setResultCallback(
                new SendMessageResultCallback(message));
    }

    private final class SendMessageResultCallback implements ResultCallback<Status> {
        String mMessage;

        SendMessageResultCallback(String message) {
            mMessage = message;
        }

        @Override
        public void onResult(Status result) {
            if (!result.isSuccess()) {
                Log.d(TAG, "Failed to send message. statusCode: " + result.getStatusCode()
                        + " message: " + mMessage);
            }
        }
    }

}
