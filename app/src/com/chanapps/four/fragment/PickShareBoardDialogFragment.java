package com.chanapps.four.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import com.chanapps.four.activity.PostReplyShareActivity;
import com.chanapps.four.activity.R;
import com.chanapps.four.data.ChanBoard;
import com.chanapps.four.data.ChanHelper;

import java.util.List;

/**
* Created with IntelliJ IDEA.
* User: arley
* Date: 12/14/12
* Time: 12:44 PM
* To change this template use File | Settings | File Templates.
*/
public class PickShareBoardDialogFragment extends DialogFragment {

    public static final String TAG = PickShareBoardDialogFragment.class.getSimpleName();

    private static final boolean DEBUG = false;

    private String[] boards;
    private Handler activityHandler;

    private void initBoards(Context context) {
        List<ChanBoard> chanBoards = ChanBoard.getBoardsRespectingNSFW(context);
        boards = new String[chanBoards.size()];
        int i = 0;
        for (ChanBoard chanBoard : chanBoards) {
            String boardCode = chanBoard.link;
            String boardName = chanBoard.name;
            String boardLine = "/" + boardCode + " " + boardName;
            boards[i] = boardLine;
            i++;
        }
    }

    public PickShareBoardDialogFragment(Handler handler) {
        activityHandler = handler;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initBoards(getActivity());
        return new AlertDialog.Builder(getActivity())
        .setTitle(R.string.post_reply_share_pick_board)
        .setItems(boards, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String boardLine = boards[which];
                String boardCode = boardLine.substring(1, boardLine.indexOf(' '));
                if (DEBUG) Log.i(TAG, "Picked board=" + boardCode);
                Bundle b = new Bundle();
                b.putString(ChanHelper.BOARD_CODE, boardCode);
                Message msg = Message.obtain(activityHandler, PostReplyShareActivity.PICK_BOARD);
                msg.setData(b);
                msg.sendToTarget();
            }
        })
        .setNegativeButton(R.string.dialog_cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (DEBUG) Log.i(TAG, "Picking board cancelled");
                        Message.obtain(activityHandler, PostReplyShareActivity.POST_CANCELLED).sendToTarget();
                    }
                })
        .create();
    }

}
