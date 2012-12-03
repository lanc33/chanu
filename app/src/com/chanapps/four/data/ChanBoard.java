package com.chanapps.four.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.MatrixCursor;
import android.graphics.Point;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.chanapps.four.activity.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class ChanBoard {
	public static final String TAG = ChanBoard.class.getSimpleName();

	private ChanBoard(Type type, String name, String link, int iconId,
			boolean workSafe, boolean classic, boolean textOnly) {
		this.type = type;
		this.name = name;
		this.link = link;
		this.iconId = iconId;
		this.workSafe = workSafe;
		this.classic = classic;
		this.textOnly = textOnly;
	}
	
	public enum Type {JAPANESE_CULTURE, INTERESTS, CREATIVE, ADULT, OTHER, MISC, FAVORITES};

    public static final String WATCH_BOARD_CODE = "watch";

	public MatrixCursor cursor = null;
	
	public String board;
	public String name;
    public String link;
    public int iconId;
	public int no;

	public Type type;
	public boolean workSafe;
	public boolean classic;
	public boolean textOnly;

	public String toString() {
        return "Board " + board + " page " + no;
    }

	private static List<ChanBoard> boards;
    private static Map<Type, List<ChanBoard>> boardsByType;
    private static Map<String, ChanBoard> boardByCode;

    public static String getBoardTypeName(Context ctx, Type boardType) {
        switch (boardType) {
            case JAPANESE_CULTURE: return ctx.getString(R.string.board_type_japanese_culture);
            case INTERESTS: return ctx.getString(R.string.board_type_interests);
            case CREATIVE: return ctx.getString(R.string.board_type_creative);
            case ADULT: return ctx.getString(R.string.board_type_adult);
            case MISC: return ctx.getString(R.string.board_type_misc);
            case OTHER: return ctx.getString(R.string.board_type_other);
            case FAVORITES: return ctx.getString(R.string.board_type_favorites);
            default:
                return ctx.getString(R.string.board_type_japanese_culture);
        }
    }

	public static List<ChanBoard> getBoards(Context context) {
		if (boards == null) {
			initBoards(context);
		}
		return boards;
	}
	
	public static boolean isValidBoardCode(Context context, String boardCode) {
        if (boards == null) {
   			initBoards(context);
   		}
        return boardByCode.containsKey(boardCode);
	}
	
	public static List<ChanBoard> getBoardsByType(Context context, Type type) {
		if (boards == null) {
			initBoards(context);
		}
        else if (type == Type.FAVORITES) {
            loadFavoritesFromPrefs(context);
        }
		return boardsByType.get(type);
	}

    public static void addBoardToFavorites(Context ctx, String boardCode) {
        Set<String> savedFavorites = getFavoritesFromPrefs(ctx);
        if (savedFavorites.contains(boardCode)) {
            Log.i(TAG, "Board " + boardCode + " already in favorites");
            Toast.makeText(ctx, R.string.board_already_in_favorites, Toast.LENGTH_SHORT);
        }
        else {
            savedFavorites.add(boardCode);
            SharedPreferences.Editor editor = ctx.getSharedPreferences(ChanHelper.PREF_NAME, 0).edit();
            editor.putStringSet(ChanHelper.FAVORITE_BOARDS, savedFavorites);
            editor.commit();
            Log.i(TAG, "Board " + boardCode + " added to favorites");
            Log.i(TAG, "Put favorites list to prefs: " + Arrays.toString(savedFavorites.toArray()));
            Toast.makeText(ctx, R.string.board_added_to_favorites, Toast.LENGTH_SHORT);
        }
    }

    public static void clearFavorites(Context ctx) {
        Log.i(TAG, "Clearing favorites...");
        SharedPreferences.Editor editor = ctx.getSharedPreferences(ChanHelper.PREF_NAME, 0).edit();
        editor.remove(ChanHelper.FAVORITE_BOARDS);
        editor.commit();
        Log.i(TAG, "Favorites cleared");
    }

    private static Set<String> getFavoritesFromPrefs(Context ctx) {
        Log.i(TAG, "Getting favorites from prefs...");
        SharedPreferences prefs = ctx.getSharedPreferences(ChanHelper.PREF_NAME, 0);
        Set<String> savedFavorites = prefs.getStringSet(ChanHelper.FAVORITE_BOARDS, new HashSet<String>());
        Log.i(TAG, "Loaded favorites from prefs:" + Arrays.toString(savedFavorites.toArray()));
        savedFavorites.add(WATCH_BOARD_CODE); // always force watch list to be present
        Log.i(TAG, "Returning favorites from prefs:" + Arrays.toString(savedFavorites.toArray()));
        return savedFavorites;
    }

    private static void loadFavoritesFromPrefs(Context ctx) {
        Set<String> savedFavorites = getFavoritesFromPrefs(ctx);
        List<String> favoriteCodes = new ArrayList<String>(savedFavorites);
        Collections.sort(favoriteCodes);

        List<ChanBoard> favorites = new ArrayList<ChanBoard>();
        for (String boardCode : favoriteCodes) {
            ChanBoard existingBoard = boardByCode.get(boardCode);
            Log.i(TAG, "found board: " + boardCode + " with val: " + (existingBoard != null ? existingBoard.name : "none"));
            if (existingBoard != null) {
                favorites.add(existingBoard);
            }
        }

        boardsByType.put(Type.FAVORITES, favorites);
    }

	private static void initBoards(Context ctx) {
		boards = new ArrayList<ChanBoard>();
		boardsByType = new HashMap<Type, List<ChanBoard>>();
        boardByCode = new HashMap<String, ChanBoard>();

        String[][] boardCodesByType = initBoardCodes(ctx);

        for (String[] boardCodesForType : boardCodesByType) {
            Type boardType = Type.valueOf(boardCodesForType[0]);
            List<ChanBoard> boardsForType = new ArrayList<ChanBoard>();
            for (int i = 1; i < boardCodesForType.length; i+=2) {
                String boardCode = boardCodesForType[i];
                String boardName = boardCodesForType[i+1];
                boolean workSafe = !(boardType == Type.ADULT || boardType == Type.MISC);
                ChanBoard b = new ChanBoard(boardType, boardName, boardCode, 0, workSafe, true, false);
                boardsForType.add(b);
                boards.add(b);
                boardByCode.put(boardCode, b);
            }
            boardsByType.put(boardType, boardsForType);
        }

        loadFavoritesFromPrefs(ctx);
   	}

    private static String[][] initBoardCodes(Context ctx) {
        String[][] boardCodesByType = {
                {   Type.JAPANESE_CULTURE.toString(),
                        "a", ctx.getString(R.string.board_a),
                        "c", ctx.getString(R.string.board_c),
                        "w", ctx.getString(R.string.board_w),
                        "m", ctx.getString(R.string.board_m),
                        "cgl", ctx.getString(R.string.board_cgl),
                        "cm", ctx.getString(R.string.board_cm),
                        "n", ctx.getString(R.string.board_n),
                        "jp", ctx.getString(R.string.board_jp),
                        "vp", ctx.getString(R.string.board_vp)
                },
                {   Type.INTERESTS.toString(),
                        "v", ctx.getString(R.string.board_v),
                        "vg", ctx.getString(R.string.board_vg),
                        "co", ctx.getString(R.string.board_co),
                        "g", ctx.getString(R.string.board_g),
                        "tv", ctx.getString(R.string.board_tv),
                        "k", ctx.getString(R.string.board_k),
                        "o", ctx.getString(R.string.board_o),
                        "an", ctx.getString(R.string.board_an),
                        "tg", ctx.getString(R.string.board_tg),
                        "sp", ctx.getString(R.string.board_sp),
                        "sci", ctx.getString(R.string.board_sci),
                        "int", ctx.getString(R.string.board_int)
                },
                {   Type.CREATIVE.toString(),
                        "i", ctx.getString(R.string.board_i),
                        "po", ctx.getString(R.string.board_po),
                        "p", ctx.getString(R.string.board_p),
                        "ck", ctx.getString(R.string.board_ck),
                        "ic", ctx.getString(R.string.board_ic),
                        "wg", ctx.getString(R.string.board_wg),
                        "mu", ctx.getString(R.string.board_mu),
                        "fa", ctx.getString(R.string.board_fa),
                        "toy", ctx.getString(R.string.board_toy),
                        "3", ctx.getString(R.string.board_3),
                        "diy", ctx.getString(R.string.board_diy),
                        "wsg", ctx.getString(R.string.board_wsg)
                },
                {   Type.ADULT.toString(),
                        "s", ctx.getString(R.string.board_s),
                        "hc", ctx.getString(R.string.board_hc),
                        "hm", ctx.getString(R.string.board_hm),
                        "h", ctx.getString(R.string.board_h),
                        "e", ctx.getString(R.string.board_e),
                        "u", ctx.getString(R.string.board_u),
                        "d", ctx.getString(R.string.board_d),
                        "y", ctx.getString(R.string.board_y),
                        "t", ctx.getString(R.string.board_t),
                        "hr", ctx.getString(R.string.board_hr),
                        "gif", ctx.getString(R.string.board_gif)
                },
                {   Type.OTHER.toString(),
                        "q", ctx.getString(R.string.board_q),
                        "trv", ctx.getString(R.string.board_trv),
                        "fit", ctx.getString(R.string.board_fit),
                        "x", ctx.getString(R.string.board_x),
                        "lit", ctx.getString(R.string.board_lit),
                        "adv", ctx.getString(R.string.board_adv),
                        "mlp", ctx.getString(R.string.board_mlp)
                },
                {   Type.MISC.toString(),
                        "b", ctx.getString(R.string.board_b),
                        "r", ctx.getString(R.string.board_r),
                        "r9k", ctx.getString(R.string.board_r9k),
                        "pol", ctx.getString(R.string.board_pol),
                        "soc", ctx.getString(R.string.board_soc)
                },
                {   Type.FAVORITES.toString(),
                        WATCH_BOARD_CODE, ctx.getString(R.string.board_watch)
                }

        };
        return boardCodesByType;
    }

}
