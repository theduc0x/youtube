package com.example.youtubeapp.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.Util;
import com.example.youtubeapp.adapter.CommentYoutubeAdapter;
import com.example.youtubeapp.model.itemrecycleview.CommentItem;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class BottomSheetDialogCommentFragment extends BottomSheetDialogFragment {
    private String idVideoM;
    private TextView tvTotalCmtCount;
    private RecyclerView rvListComment;
    private ArrayList<CommentItem> listComment;
    CommentYoutubeAdapter adapter;
    Toolbar tbCommentVideo;

    // Khởi tạo fragment dialog với dữ liệu truyền vào là 1 CommentItem
    public static BottomSheetDialogCommentFragment newInstance(String idVideo) {
        BottomSheetDialogCommentFragment bsdCommentFragment =
                new BottomSheetDialogCommentFragment();

        // Khởi tạo dialog fragment và gửi dữ liệu đi tới bước sau bằng bundle
        Bundle bundle = new Bundle();
        bundle.putSerializable(Util.BUNDLE_EXTRA_OBJECT_ITEM_COMMENT, idVideo);

        bsdCommentFragment.setArguments(bundle);
        return bsdCommentFragment;
    }
    // Khi khởi tạo thì bắt đầu nhận
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleReceive = getArguments();
        if (bundleReceive != null) {
            idVideoM = bundleReceive.getString(Util.BUNDLE_EXTRA_OBJECT_ITEM_COMMENT);
        }
    }

    // Chuyển fagment thành dialog
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog =
                (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View viewDialog = LayoutInflater.from(getContext()).inflate(
                R.layout.fragment_bottom_sheet_comment, null);
        bottomSheetDialog.setContentView(viewDialog);
        bottomSheetDialog.setCanceledOnTouchOutside(false);

        // chiều cao của màn hình activiy chứa fragment
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        int maxHeight = (int) (height*0.65);

        BottomSheetBehavior bottomSheetBehavior =
                BottomSheetBehavior.from(((View) viewDialog.getParent()));
        bottomSheetBehavior.setMaxHeight(maxHeight);
        bottomSheetBehavior.setPeekHeight(maxHeight);
        intMain(viewDialog);
        return bottomSheetDialog;
    }

    private void intMain(View view) {
        tvTotalCmtCount = view.findViewById(R.id.tv_comment_count_video_main);
        rvListComment = view.findViewById(R.id.rv_list_comment);
        listComment = new ArrayList<>();
        tbCommentVideo = view.findViewById(R.id.tb_comment_video);
    }

    private void setDataComment() {
        if (idVideoM == null) {
            return;
        }

    }

    private void callApiComment(String id) {

    }
}
