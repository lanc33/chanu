package com.chanapps.four.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

public class ChanHelper {
	//public static final String PREF_NAME = "4Channer";

	public static final String BOARD_TYPE = "boardType";
    public static final String BOARD_CODE = "boardCode";
    public static final String PAGE = "pageNo";
    public static final String THREAD_NO = "threadNo";
    public static final String RETRIES = "noRetries";
    public static final String POST_NO = "postNo";
    public static final String TIM = "tim";
    public static final String TEXT = "text";
    public static final String IMAGE_URL = "imageUrl";
    public static final String IMAGE_WIDTH = "imageWidth";
    public static final String IMAGE_HEIGHT = "imageHeight";
    public static final String FAVORITE_BOARDS = "favoriteBoards";
    public static final String THREAD_WATCHLIST = "threadWatchlist";
    public static final String POST_ID = "_id";
    public static final String POST_BOARD_NAME = "board_name";
    public static final String POST_NOW = "now";
    public static final String POST_TIME = "time";
    public static final String POST_NAME = "name";
    public static final String POST_SUB = "sub";
    public static final String POST_COM = "com";
    public static final String POST_COUNTRY = "country";
    public static final String POST_TIM = "tim";
    public static final String POST_FILENAME = "filename";
    public static final String POST_EXT = "ext";
    public static final String POST_W = "w";
    public static final String POST_H = "h";
    public static final String POST_TN_W = "tn_w";
    public static final String POST_TN_H = "tn_h";
    public static final String POST_FSIZE = "fsize";
    public static final String POST_RESTO = "resto";
    public static final String POST_LAST_UPDATE = "last_update";
    public static final String POST_STICKY = "sticky";
    public static final String POST_CLOSED = "closed";
    public static final String POST_OMITTED_POSTS = "omitted_posts";
    public static final String POST_OMITTED_IMAGES = "omitted_images";
    public static final String POST_HEADER_TEXT = "headerText"; // we construct and filter this
    public static final String POST_SHORT_TEXT = "shortText"; // we construct and filter this
    public static final String POST_TEXT = "text"; // we construct and filter this
    public static final String POST_IMAGE_URL = "image_url"; // we construct this from board and tim
    public static final String POST_COUNTRY_URL = "country_url"; // we construct this from the country code
    public static final String POST_IS_DEAD = "isDead";
    
    public static final String LOAD_PAGE = "load_page";
    public static final String LAST_PAGE = "last_page";
    public static final String LAST_BOARD_POSITION = "lastBoardPosition";
    public static final String LAST_THREAD_POSITION = "lastThreadPosition";
    public static final String LAST_ACTIVITY = "lastActivity";
    public static final String LAST_NO_BOARD_CACHE_TIME = "lastNoBoardCacheTime";
    public static final String LAST_FAVORITES_CACHE_TIME = "lastFavoritesCacheTime";
    public static final String LAST_WATCHLIST_CACHE_TIME = "lastWatchlistCacheTime";
    public static final String IGNORE_DISPATCH = "ignoreDispatch";

    public static final String[] POST_COLUMNS = {
            POST_ID,
            POST_BOARD_NAME,
            POST_RESTO,
            POST_IMAGE_URL,
            POST_COUNTRY_URL,
            POST_SHORT_TEXT,
            POST_HEADER_TEXT,
            POST_TEXT,
            POST_TN_W,
            POST_TN_H,
            POST_W,
            POST_H,
            POST_TIM,
            POST_IS_DEAD,
            LOAD_PAGE,
            LAST_PAGE
    };

    public static final String BOARD_ID = "_id";
    public static final String BOARD_IMAGE_RESOURCE_ID = "boardImageResourceId";
    public static final String BOARD_NAME = "boardName";
    public static final String[] SELECTOR_COLUMNS = {
            BOARD_ID,
            BOARD_CODE,
            BOARD_IMAGE_RESOURCE_ID,
            BOARD_NAME
    };

    public enum Orientation {
        PORTRAIT,
        LANDSCAPE
    }

    public enum LastActivity {
        BOARD_SELECTOR_ACTIVITY,
        BOARD_ACTIVITY,
        THREAD_ACTIVITY,
        FULL_SCREEN_IMAGE_ACTIVITY,
        POST_REPLY_ACTIVITY,
        SETTINGS_ACTIVITY
    }

    public static final Orientation getOrientation(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        if (metrics.widthPixels > metrics.heightPixels) {
            return Orientation.LANDSCAPE;
        }
        else {
            return Orientation.PORTRAIT;
        }
    }

}