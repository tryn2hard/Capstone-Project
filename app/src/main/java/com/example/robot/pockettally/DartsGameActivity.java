package com.example.robot.pockettally;

import android.app.Dialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.SharedPreferences;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.robot.pockettally.database.GameMark;
import com.example.robot.pockettally.database.GameMarkDatabase;
import com.example.robot.pockettally.database.Player;
import com.example.robot.pockettally.database.PlayerDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DartsGameActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        PlayerFragment.PlayerProgressListener {


    @BindView(R.id.end_game_button)
    Button reset_game_button;
    @BindView(R.id.undo_mark_button)
    Button undo_mark_button;

    @BindViews({R.id.closed_out_20, R.id.closed_out_19, R.id.closed_out_18,
            R.id.closed_out_17, R.id.closed_out_16, R.id.closed_out_15, R.id.closed_out_bulls})
    List<View> CrossedOutLine;

    private List<Player> Players = new ArrayList<>();
    private ArrayList<GameMark> GameHistory = new ArrayList<>();
    private int num_of_players;
    private String game_mode;
    private Boolean pref_vibrate;
    private FragmentManager mFragmentManager;
    private PlayerDatabase mPlayersDb;
    private GameMarkDatabase mGameMarkDb;
    private SharedPreferences sharedPreferences;

    private final List<Integer> fragment_containers = new ArrayList<Integer>() {{
        add(R.id.player_1_container);
        add(R.id.player_2_container);
        add(R.id.player_3_container);
        add(R.id.player_4_container);
    }};

    private static final String LOG_TAG = DartsGameActivity.class.getSimpleName();

    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onCreate called");
        super.onCreate(savedInstanceState);

        // Get instance of the Players database
        mPlayersDb = PlayerDatabase.getsInstance(getApplicationContext());

        // Get instance of the GameMarks database
        mGameMarkDb = GameMarkDatabase.getsInstance(getApplicationContext());

        // Get instance of the fragment manager
        mFragmentManager = getSupportFragmentManager();

        // Get instance of default shared preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Instantiating the Interstitial ad
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id));

        final LiveData<List<Player>> players = mPlayersDb.playerDao().loadAllPlayersLiveData();
        players.observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(@Nullable List<Player> players) {
                Log.i(LOG_TAG, "onChanged called for Players database");
                Players = players;
            }
        });

        final LiveData<List<GameMark>> marks = mGameMarkDb.gameMarkDao().loadAllGameMarks();
        marks.observe(this, new Observer<List<GameMark>>() {
            @Override
            public void onChanged(@Nullable List<GameMark> gameMarks) {
                GameHistory = (ArrayList) gameMarks;
            }
        });

    }

        /*
    ***********************************************************************************************
    Additional LifeCycle methods
    ***********************************************************************************************
     */

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume called");

        // Get sharedPreferences
        setupSharedPreferences();

        // Load ad
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        // Choose which layout the user will see based on the number of players in the game
        if (num_of_players == 2) {
            setContentView(R.layout.activity_darts_game);
        } else {
            setContentView(R.layout.activity_darts_game_multi_player);
        }

        ButterKnife.bind(this);


        AppExecutors.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                Players = mPlayersDb.playerDao().loadAlPlayers();
                loadGame();
            }
        });


        // Reset current game
        reset_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
            }
        });

        // Remove the previous throw from the user
        undo_mark_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoThrow();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    /*
    ***********************************************************************************************
    Methods to start a new game, reset a game, reinitialize a previous game, or end the game
    ***********************************************************************************************
     */

    /**
     * Method sets up the number of Player fragments needed for the game base on the number of players.
     * A for loop is used to create the number of PlayerFragments needed by iterating through the
     * number of players in the game. PlayerFragment will carry two arguments: Game mode and Vibrate.
     * Each PlayerFragment is tagged for later retrieval.
     *
     * @param playerCount an integer value showing number of players. Can range from 2-4
     */
    private void initNewGame(int playerCount) {
        Log.i(LOG_TAG, "initNewGame called");

        deleteAllPlayersDb();
        Players.clear();

        for (int i = 0; i < playerCount; i++) {
            String tag = tagGenerator(i);
            setupPlayer(tag);
            sharedPreferences.edit().putBoolean(getResources().getString(R.string.pref_game_init_key),
                    true).apply();
        }

    }

    /**
     * Method will be called if a game has already been started and needs to be reloaded from the
     * database
     */
    private void loadGame() {
        Log.i(LOG_TAG, "loadGame called");
        Log.i(LOG_TAG, "players size is " + Players.size());

        if (Players.size() != num_of_players) {
            initNewGame(num_of_players);
        }

        for (int i = 0; i < Players.size(); i++) {
            // Retrieve player's current game status
            Player currentPlayer = Players.get(i);
            String name = currentPlayer.getName();
            int avatar = currentPlayer.getAvatar();
            String fragmentTag = currentPlayer.getFragmentTag();
            boolean[] closedOut = currentPlayer.getScoreboardCondition();
            int[] count = currentPlayer.getScoreboardCounts();
            int totalScore = currentPlayer.getTotalScore();
            boolean[] scoreboardDone = currentPlayer.getScoreboardDone();

            // Create new fragments and bundle player's data
            PlayerFragment playerFragment = new PlayerFragment();
            Bundle args = new Bundle();

            // Shared preferences for the game
            args.putBoolean(PlayerFragment.FRAGMENT_ARGS_VIBE_KEY, pref_vibrate);
            args.putString(PlayerFragment.FRAGMENT_ARGS_GAME_MODE_KEY, game_mode);

            // Bundle all the Player data
            args.putString(PlayerFragment.FRAGMENT_ARGS_PLAYER_NAME_KEY, name);
            args.putInt(PlayerFragment.FRAGMENT_ARGS_PLAYER_AVATAR_KEY, avatar);
            args.putBooleanArray(PlayerFragment.FRAGMENT_ARGS_CLOSED_OUT_KEY, closedOut);
            args.putBooleanArray(PlayerFragment.FRAGMENT_ARGS_SCOREBOARD_DONE_KEY, scoreboardDone);
            args.putIntArray(PlayerFragment.FRAGMENT_ARGS_SCOREBOARD_COUNTS_KEY, count);
            args.putInt(PlayerFragment.FRAGMENT_ARGS_TOTAL_SCORE_KEY, totalScore);
            args.putBoolean(PlayerFragment.FRAGMENT_ARGS_GAME_INIT_KEY,
                    sharedPreferences.getBoolean(getResources().getString(R.string.pref_game_init_key),
                            true));

            // Attach the bundle to the fragment and away we go
            playerFragment.setArguments(args);
            if(sharedPreferences.getBoolean(getResources().getString(R.string.pref_game_init_key),
                    true))
            mFragmentManager.beginTransaction()
                    .replace(fragment_containers.get(i), playerFragment, fragmentTag)
                    .commit();
            else{
                mFragmentManager.beginTransaction()
                        .add(fragment_containers.get(i), playerFragment, fragmentTag)
                        .commit();
            }
        }

    }

    /**
     * Method will resets the game to the initial settings. The names and avatars are not reset, so
     * that the user may continue to play with the previous players.
     */
    private void resetGame() {
        Log.i(LOG_TAG, "resetGame called");

        for (int i = 0; i < Players.size(); i++) {
            resetPlayer(Players.get(i));
            PlayerFragment frag =
                    (PlayerFragment) mFragmentManager.findFragmentByTag(Players.get(i).getFragmentTag());
            frag.resetScoreboards();
        }

        for (int j = 0; j < CrossedOutLine.size(); j++) {
            CrossedOutLine.get(j).setVisibility(View.INVISIBLE);
        }

        GameHistory.clear();
        deleteAllGameMarksDb();



    }

    /**
     * Method will create a dialog to display that the end of game has been reached. The dialog will
     * ask the user will create new game upon pressing the play_again_button.
     *
     * @param tag string will be the generic user name to be displayed in the end of game message
     */

    private void endGame(final String tag) {
        Log.i(LOG_TAG, "endGame called");
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.end_of_game_dialog);
        TextView winner_tv = dialog.findViewById(R.id.winning_player_tv);
        String winner;
        if (Players.get(getIdFromTag(tag)).getName() != null) {
            winner = Players.get(getIdFromTag(tag)).getName() + " has won!";
        } else {
            winner = tag + " has won!";
        }
        winner_tv.setText(winner);

        Button play_again_button = dialog.findViewById(R.id.play_again_button);
        play_again_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
                dialog.dismiss();
                mInterstitialAd.show();

            }
        });

        dialog.show();
    }

    /*
    ***********************************************************************************************
    Methods to create or reset players
    ***********************************************************************************************
     */

    /**
     * Helper method to add players into the Players array list and add that player to the database.
     * Each player is created with a tag.
     *
     * @param tag A string containing the fragment tag that the player is tied to.
     */
    private void setupPlayer(String tag) {
        Log.i(LOG_TAG, "setupPlayer called");
        final Player newPlayer = new Player(tag);
        Players.add(newPlayer);

        AppExecutors.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mPlayersDb.playerDao().insertPlayer(newPlayer);
                Log.i(LOG_TAG, "Player inserted");
            }
        });
    }

    /**
     * Method will reset this player to a start of game state. Usually done when the game has ended
     * or when the user presses the reset button
     */
    public void resetPlayer(Player player) {
        Log.i(LOG_TAG, "resetPlayer called");
        Log.i("resetPlayer", player.getFragmentTag() + " has called reset.");
        for (int i = 0; i < player.getScoreboardCondition().length; i++) {
            player.getScoreboardCondition()[i] = Scoreboard.SCOREBOARD_OPEN;
            player.getScoreboardCounts()[i] = 0;
            player.getScoreboardDone()[i] = Scoreboard.SCOREBOARD_OPEN;
        }

        player.setTotalScore(0);
        updatePlayersDb(player);
    }

    /**
     * A simple helper method to generate a tag for the fragment. It also helps create a simple name
     * for each of the players. Rather than having player0, this programmer chose to offset the
     * naming by 1
     *
     * @param value integer value that will be concatenated with the string "Player" to help
     *              identify the player and fragment
     * @return string of the specific player
     */
    private String tagGenerator(int value) {
        return "Player " + (value + 1);
    }

    /**
     * A simple helper method used to pull the player id out of the fragment tag
     *
     * @param tag string fragmentTag holding the user id .
     * @return integer value of the id attached to the fragment tag
     */
    public int getIdFromTag(String tag) {
        int id = Integer.parseInt(tag.substring(tag.length() - 1));
        id -= 1;
        return id;
    }

    /*
    ***********************************************************************************************
    Methods to take care of sharedPreferences
    ***********************************************************************************************
     */

    /**
     * A helper method class to set up the sharedPreferences. This method will retrieve the number
     * of players in a game, the game mode, and if the user would like the vibrate feedback.
     */
    private void setupSharedPreferences() {
        Log.i(LOG_TAG, "setupSharedPreferences called");

        num_of_players = Integer.parseInt(sharedPreferences.getString(getResources()
                .getString(R.string.pref_num_of_players_key), getResources().getString(R.string.pref_num_of_players_default)));

        pref_vibrate = sharedPreferences.getBoolean(getResources()
                .getString(R.string.pref_vibrate_key), getResources().getBoolean(R.bool.pref_vibrate_default));

        game_mode = sharedPreferences.getString(getResources()
                .getString(R.string.pref_game_mode_key), getResources().getString(R.string.pref_game_mode_default));

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.i(LOG_TAG, "onSharedPreferenceChanged called");

        if (key.equals(getString(R.string.pref_num_of_players_key))) {
            num_of_players = Integer.parseInt(sharedPreferences.getString(key,
                    getString(R.string.pref_num_of_players_default)));
        } else if (key.equals(getString(R.string.pref_game_mode_key))) {
            game_mode = sharedPreferences.getString(key,
                    getString(R.string.pref_game_mode_default));
        } else if (key.equals(getString(R.string.pref_vibrate_key))) {
            pref_vibrate = sharedPreferences.getBoolean(key,
                    getResources().getBoolean(R.bool.pref_vibrate_default));
        }
    }

    /*
    ***********************************************************************************************
    Methods to take care of the options menu
    ***********************************************************************************************
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_how_to) {
            startActivity(new Intent(this, HowToPlayActivity.class));
        }
        if (id == R.id.action_game_settings) {
            startActivity(new Intent(this, GameSettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    ***********************************************************************************************
    Methods for Game History and removing an erroneous throw
    ***********************************************************************************************
     */

    /**
     * Method is used to store the game history data to be used for when the user presses the 'undo'
     * button
     *
     * @param tag        string used to identify which player and fragment made the mark
     * @param scoreValue integer used to identify which tally mark was struck
     * @param increment  integer used to identify the increment value of the mark
     */
    private void storeGameHistory(final String tag, final int scoreValue, final int increment) {
        Log.i(LOG_TAG, "storeGameHistory called");
        GameHistory.add(new GameMark(tag, scoreValue, increment));

        AppExecutors.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mGameMarkDb.gameMarkDao().insertGameMark(new GameMark(tag, scoreValue, increment));
                Log.i(LOG_TAG, "GameMark inserted");
            }
        });

    }

    /**
     * Method will return a GameMark object holding the necessary data to remove a mark erroneously
     * made
     *
     * @return GameMark object holding the fragment tag, scoreValue, and increment
     */
    private GameMark getGameHistory() {
        Log.i(LOG_TAG, "getGameHistory called");
        GameMark undoMark = null;

        if (!GameHistory.isEmpty()) {
            undoMark = GameHistory.get(GameHistory.size() - 1);
            GameHistory.remove(GameHistory.size() - 1);
            GameHistory.trimToSize();
            deleteGameMarkDb(undoMark);
        }
        return undoMark;
    }

    /**
     * A method called upon when the user click the undo button in the UI. This method will access
     * the game history and undo the last mark in the array. However, if the game is history is null
     * the method will display a simple toast.
     */
    public void undoThrow() {
        Log.i(LOG_TAG, "undoThrow called");
        GameMark undoMark = getGameHistory();
        if (undoMark == null) {
            Toast.makeText(this, "Nothing to Undo", Toast.LENGTH_SHORT).show();
        } else {
            PlayerFragment currentFrag = (PlayerFragment) mFragmentManager.findFragmentByTag(undoMark.getTag());
            currentFrag.undoThrow(undoMark.getValue(), undoMark.getIncrement());
            Players.get(getIdFromTag(undoMark.getTag()))
                    .getScoreboardCounts()[ScoreboardUtils.matchScoreValue(undoMark.getValue())]
                    -= undoMark.getIncrement();
            updatePlayersDb(Players.get(getIdFromTag(undoMark.getTag())));
        }

    }

    /*
    ***********************************************************************************************
    Methods to retrieve information about the Player's marks and total score
    ***********************************************************************************************
     */

    /**
     * Method will return the status of a specific scoreboard
     *
     * @param scoreValue the integer value of the scoreboard in question
     * @return boolean value of the current condition of the scoreboard
     */
    public boolean isScoreboardClosed(int scoreValue, Player currentPlayer) {
        Log.i(LOG_TAG, "isScoreboardClosed called");
        return currentPlayer.getScoreboardCondition()[ScoreboardUtils.matchScoreValue(scoreValue)];
    }

    /**
     * Method will check the current condition of all the scoreboards for this player.
     *
     * @return boolean value of true will be returned if all the scoreboards are closed.
     */
    public boolean areAllScoreboardsClosedForPlayer(Player currentPlayer) {
        Log.i(LOG_TAG, "areAllScoreboardsClosedForPlayer called");
        for (boolean b : currentPlayer.getScoreboardCondition()) if (!b) return false;
        return true;
    }

    /**
     * Method will check all the players to see if a scoreboard has been closed out.
     *
     * @param scoreValue the integer score value that will be checked
     * @return a boolean value will be returned. True if the scoreboard in questions is closed out
     * by all, otherwise false.
     */
    private boolean isScoreboardClosedOutByAll(int scoreValue) {
        Log.i(LOG_TAG, "isScoreboardClosedOutByAll called");
        for (int i = 0; i < Players.size(); i++) {
            boolean tallyCondition = isScoreboardClosed(scoreValue, Players.get(i));
            if (!tallyCondition) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method will notify this player that a tally mark has had it's state changed.
     *
     * @param scoreValue the integer value of the tally mark that has been closed out
     */
    public void setScoreboardCondition(int scoreValue, Player player, boolean condition) {
        Log.i(LOG_TAG, "setScoreboardCondition called");
        Log.i(LOG_TAG, player.getFragmentTag() + " has set " + scoreValue + " to " + condition);
        player.getScoreboardCondition()[ScoreboardUtils.matchScoreValue(scoreValue)] = condition;
        updatePlayersDb(player);
    }

    /**
     * Method is used during a standard game of crickets to ensure that the player is also leading
     * in points before being declared the winner
     *
     * @param tag string is used to find the current player in question
     * @return will return true if the player is leading in points
     */
    private boolean isPlayerLeadingInPoints(String tag) {
        Log.i(LOG_TAG, "isPlayerLeadingInPoints called");
        int currentPlayerTotalScore = 0;
        int scoreToCompare = Players.get(getIdFromTag(tag)).getTotalScore();
        boolean playerIsLeading;
        for (int i = 0; i < Players.size(); i++) {
            if (Players.get(i).getTotalScore() >= currentPlayerTotalScore) {
                currentPlayerTotalScore = Players.get(i).getTotalScore();
            }
        }
        playerIsLeading = scoreToCompare >= currentPlayerTotalScore;

        return playerIsLeading;
    }

    /*
    ***********************************************************************************************
    Interface methods for the playerFragment to communicate back to the host activity, and update the
    data for players
    ***********************************************************************************************
     */

    /**
     * An implemented method from the PlayerFragment. Method updated the player in question with the
     * current total score in the game.
     *
     * @param tag        string fragmentTag used to identify the player in
     * @param totalScore integer value of the total score that has been changed
     */
    @Override
    public void TotalScoreHasChanged(String tag, int totalScore) {
        Log.i(LOG_TAG, "TotalScoreHasChanged called");
        Player currentPlayer = Players.get(getIdFromTag(tag));
        currentPlayer.setTotalScore(totalScore);
        updatePlayersDb(currentPlayer);
        if (areAllScoreboardsClosedForPlayer(Players.get(getIdFromTag(tag))) && isPlayerLeadingInPoints(tag)) {
            endGame(tag);
        }
    }

    /**
     * An implemented method from the PlayerFragment. Used to store a mark made by the user into the
     * game history array.
     *
     * @param tag        string fragmentTag to help identify the player
     * @param scoreValue integer of the position marked on the scoreboard
     * @param increment   integer of the number of marks made
     */
    @Override
    public void ScoreboardMarked(String tag, int scoreValue, int increment) {
        Log.i(LOG_TAG, "ScoreboardMarked called by " + tag + " for " + scoreValue);
        Player currentPlayer = Players.get(getIdFromTag(tag));
        // if the game is in standard mode store the game history
        if (game_mode.equals(getResources().getString(R.string.pref_standard_game_mode_value))) {
            storeGameHistory(tag, scoreValue, increment);
            currentPlayer.getScoreboardCounts()[ScoreboardUtils.matchScoreValue(scoreValue)] += increment;
            updatePlayersDb(currentPlayer);
            if (areAllScoreboardsClosedForPlayer(Players.get(getIdFromTag(tag))) && isPlayerLeadingInPoints(tag)) {
                endGame(tag);
            }
            // only save the game history for no points mode if the current scoreboard is not closed out
        } else if (game_mode.equals(getResources().getString(R.string.pref_no_points_game_mode_value))) {
            if (!(currentPlayer.getScoreboardCondition()[ScoreboardUtils.matchScoreValue(scoreValue)])) {
                storeGameHistory(tag, scoreValue, increment);
                currentPlayer.getScoreboardCounts()[ScoreboardUtils.matchScoreValue(scoreValue)] += increment;
                updatePlayersDb(currentPlayer);
            }
        }
    }

    /**
     * An implemented method from the PlayerFragment. Used to store the name of a player into a
     * player object.
     *
     * @param tag  string to find the player that was renamed
     * @param name string of the name that the user has chosen
     */
    @Override
    public void PlayerNamed(String tag, String name) {
        Log.i(LOG_TAG, "PlayerNamed called");
        Players.get(getIdFromTag(tag)).setName(name);
        updatePlayersDb(Players.get(getIdFromTag(tag)));

    }

    /**
     * An implemented method from the PlayerFragment. Used to store the avatar id into a player
     * object.
     *
     * @param tag    string to find the player that was renamed
     * @param avatar integer value of the drawable resource
     */
    @Override
    public void AvatarSelected(String tag, int avatar) {
        Log.i(LOG_TAG, "AvatarSelected called");
        Player currentPlayer = Players.get(getIdFromTag(tag));
        currentPlayer.setAvatar(avatar);
        updatePlayersDb(currentPlayer);
    }

    /**
     * An interface method from PlayerFragment alerting the activity that a player has closed out a
     * tally.
     * When an alert is made, the associated player is found and the notified that a specific tally
     * has been closed. After which, a check in conducted to check if the tally mark has been closed
     * out by all the other players. If the tally mark has been closed out by all, then the
     * setClosedOutLine method will be called.
     *
     * @param tag        string holding the fragment tag
     * @param scoreValue an integer value of the score which has been closed out
     * @param condition  the state of the scoreboard in the fragment
     */
    @Override
    public void ChangeToScoreboardConditionNotification(String tag, int scoreValue, boolean condition) {
        if (condition) {
            setScoreboardCondition(scoreValue, Players.get(getIdFromTag(tag)), Scoreboard.SCOREBOARD_CLOSED);
            if (isScoreboardClosedOutByAll(scoreValue)) {
                setClosedOutLine(scoreValue);
                setAllClosedOutForFrags(scoreValue, condition);
            }
            if (game_mode.equals(getResources().getString(R.string.pref_no_points_game_mode_value))){
                if (areAllScoreboardsClosedForPlayer(Players.get(getIdFromTag(tag)))) {
                    endGame(tag);
                }
            }
        } else {
            setScoreboardCondition(scoreValue, Players.get(getIdFromTag(tag)), Scoreboard.SCOREBOARD_OPEN);
            if (!isScoreboardClosedOutByAll(scoreValue)) {
                // remove the cross out divider
                CrossedOutLine.get(ScoreboardUtils.matchScoreValue(scoreValue)).setVisibility(View.INVISIBLE);
                setAllClosedOutForFrags(scoreValue, condition);

            }
        }
    }

    /*
    ***********************************************************************************************
    Methods to communicate information and data to PlayerFragment
    ***********************************************************************************************
    */

    /**
     * Method will notify the PlayerFragment that the tally mark has been closed out by all players,
     * thus no longer allowing for additional marks to be made.
     *
     * @param scoreValue the integer score value indicating which mark has been closed out by all
     *                   players
     */
    private void setAllClosedOutForFrags(int scoreValue, boolean condition) {
        Log.i(LOG_TAG, "setAllClosedOutForFrags called");

            for (int i = 0; i < Players.size(); i++) {
                Players.get(i).getScoreboardDone()[ScoreboardUtils.matchScoreValue(scoreValue)] = condition;
                updatePlayersDb(Players.get(i));
                PlayerFragment frag =
                        (PlayerFragment) mFragmentManager.findFragmentByTag(Players.get(i).getFragmentTag());
                if(frag != null){
                frag.setAllClosedOut(scoreValue, condition);
            }
        }
    }

    /*
    ***********************************************************************************************
    UI display methods
    ***********************************************************************************************
     */

    /**
     * This method is called when a specific tally mark has been closed out by all the players. When
     * it is called, the method will identify which mark has been closed out by using the score
     * value, and the appropriate crossed out divider will become visible to show the user that the
     * tally mark has been closed out.
     *
     * @param scoreValue the integer score value indicating which tally mark has been closed.
     */
    private void setClosedOutLine(int scoreValue) {
        Log.i(LOG_TAG, "setClosedOutLine called");
        CrossedOutLine.get(ScoreboardUtils.matchScoreValue(scoreValue)).setVisibility(View.VISIBLE);
    }

    /*
    ***********************************************************************************************
    Database methods
    ***********************************************************************************************
    */

    /**
     * Method creates a Executor to run an update to the database
     *
     * @param player the current player to update to the database
     */
    private void updatePlayersDb(final Player player) {
        Log.i(LOG_TAG, "updatePlayersDb called");
        AppExecutors.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mPlayersDb.playerDao().updatePlayer(player);
            }
        });
    }

    private void deleteGameMarkDb(final GameMark gameMark) {

        AppExecutors.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mGameMarkDb.gameMarkDao().deleteGameMark(gameMark);

            }
        });
    }

    private void deleteAllGameMarksDb() {

        AppExecutors.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mGameMarkDb.gameMarkDao().delete();

            }
        });
    }

    private void deleteAllPlayersDb() {
        AppExecutors.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mPlayersDb.playerDao().delete();
            }
        });
    }

}



