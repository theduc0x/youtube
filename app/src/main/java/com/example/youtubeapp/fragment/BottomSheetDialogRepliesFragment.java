package com.example.youtubeapp.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.Util;
import com.example.youtubeapp.activitys.VideoPlayActivity;
import com.example.youtubeapp.adapter.RepliesCommentAdapter;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.itemrecycleview.CommentItem;
import com.example.youtubeapp.model.itemrecycleview.RepliesCommentItem;
import com.example.youtubeapp.model.listcomment.Comments;
import com.example.youtubeapp.model.listcomment.RepliesComment;
import com.example.youtubeapp.model.listreplies.ItemsR;
import com.example.youtubeapp.model.listreplies.Replies;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetDialogRepliesFragment extends BottomSheetDialogFragment {
    CommentItem itemR;
    RelativeLayout rlOpenKeyboard;
    CircleImageView civReceive;
    TextView tvNameReceive, tvDateDiffReceive, tvEditor;
    TextView tvContentReceive, likeCountReceive, repliesCountReceive;
    RecyclerView rvListReplies;
    RepliesCommentAdapter adapter;
    Toolbar tbReplies;
    String parentId;
    ProgressDialog progressDialog;


    // Khởi tạo fragment dialog với dữ liệu truyền vào là 1 CommentItem
    public static BottomSheetDialogRepliesFragment newInstance(CommentItem item) {
        BottomSheetDialogRepliesFragment bsdRepliesFragment =
                new BottomSheetDialogRepliesFragment();

        // Khởi tạo dialog fragment và gửi dữ liệu đi tới bước sau bằng bundle
        Bundle bundle = new Bundle();
        bundle.putSerializable(Util.BUNDLE_EXTRA_ITEM_VIDEO_TO_REPLIES_INSIDE, item);
        bsdRepliesFragment.setArguments(bundle);
        return bsdRepliesFragment;
    }

    // Khi khởi tạo thì bắt đầu nhận
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleReceive = getArguments();
        if (bundleReceive != null) {
            itemR = (CommentItem) bundleReceive
                    .getSerializable(Util.BUNDLE_EXTRA_ITEM_VIDEO_TO_REPLIES_INSIDE);
        }
    }

    // Chuyển fagment thành dialog
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog =
                (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View viewDialog = LayoutInflater.from(getContext()).inflate(
                R.layout.fragment_bottom_sheet_detail_replies, null);
        bottomSheetDialog.setContentView(viewDialog);
//        bottomSheetDialog.setCanceledOnTouchOutside(false);
        // Xoay khi đang chờ load comment
//        progressDialog = new ProgressDialog(getContext());
//        progressDialog.setMessage("Loading...");

        // chiều cao của màn hình activiy chứa fragment
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        int maxHeight = (int) (height * 0.65);

        BottomSheetBehavior bottomSheetBehavior =
                BottomSheetBehavior.from(((View) viewDialog.getParent()));
        bottomSheetBehavior.setMaxHeight(maxHeight);
        bottomSheetBehavior.setPeekHeight(maxHeight);

        // Ánh xạ view và chạy RecycleVIew
        intMain(viewDialog);
        setData();

        bottomSheetDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        bottomSheetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        tbReplies.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_close_replies:
                        bottomSheetDialog.dismiss();
                        break;
                }
                return false;
            }
        });
        return bottomSheetDialog;
    }

    private void intMain(View view) {
        Util.listReplies = new ArrayList<>();
        civReceive = view.findViewById(R.id.civ_logo_author_replies);
        tvNameReceive = view.findViewById(R.id.tv_author_name_replies);
        tvDateDiffReceive = view.findViewById(R.id.tv_date_diff_replies);
        tvContentReceive = view.findViewById(R.id.tv_cmt_content_replies);
        likeCountReceive = view.findViewById(R.id.tv_like_count_cmt_replies);
        repliesCountReceive = view.findViewById(R.id.tv_replies_count_replies);
        rvListReplies = view.findViewById(R.id.rv_item_replies);
        tbReplies = view.findViewById(R.id.tb_replies_video);
        rlOpenKeyboard = view.findViewById(R.id.rl_open_replies_keyboard);
        tvEditor = view.findViewById(R.id.tv_editor_replies);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setData() {
        if (itemR == null) {
            return;
        }
        String publishAt = itemR.getPublishedAt();
        String updateAt = itemR.getUpdateAt();
        parentId = itemR.getIdComment();

        Picasso.get().load(itemR.getAuthorLogoUrl()).into(civReceive);
        tvNameReceive.setText(itemR.getAuthorName());
        tvContentReceive.setText(itemR.getTextDisplay());
        tvDateDiffReceive.setText(" • " + Util.getTime(itemR.getPublishedAt()));
        likeCountReceive.setText(Util.convertViewCount(itemR.getLikeCount()));
        repliesCountReceive.setText(Util.convertViewCount(itemR.getTotalReplyCount()));
        if (publishAt.equals(updateAt)) {
            tvEditor.setVisibility(View.VISIBLE);
        }

        adapter = new RepliesCommentAdapter(Util.listReplies);

        callApiReplies("", parentId);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        RecyclerView.ItemDecoration decoration =
                new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL);
        rvListReplies.setLayoutManager(linearLayoutManager);
        rvListReplies.addItemDecoration(decoration);
        rvListReplies.setAdapter(adapter);
    }

    // thêm các dữ liệu vào list Replies
//    private void addListReplies(CommentItem listItem) {
//        ArrayList<Comments> listCommentRep = new ArrayList<Comments>();
//        RepliesComment repliesComment = null;
//        String authorLogoUrl = "", authorName = "", publishAt = "", updateAt = "",
//                displayContentCmt = "", dateDiff = "", authorIdChannel = "";
//        int likeCount = 0;
//
//        repliesComment = listItem.getRepliesComent();
//        if (repliesComment == null) {
//            return;
//        }
//        listCommentRep = repliesComment.getComments();
//        for (int i = 0; i < listCommentRep.size(); i++) {
//            authorLogoUrl = listCommentRep.get(i).getSnippet()
//                    .getAuthorProfileImageUrl();
//            authorName = listCommentRep.get(i).getSnippet()
//                    .getAuthorDisplayName();
//            publishAt = listCommentRep.get(i).getSnippet()
//                    .getPublishedAt();
//            updateAt = listCommentRep.get(i).getSnippet()
//                    .getUpdatedAt();
//            authorIdChannel = listCommentRep.get(i).getSnippet()
//                    .getAuthorChannelId().getValue();
//            displayContentCmt = listCommentRep.get(i).getSnippet()
//                    .getTextDisplay();
//            likeCount = listCommentRep.get(i).getSnippet()
//                    .getLikeCount();
//
//            Util.listReplies.add(new RepliesCommentItem(
//                    displayContentCmt, authorName, authorLogoUrl,
//                    authorIdChannel, likeCount, publishAt, updateAt
//            ));
//            adapter.notifyItemInserted(i);
//        }
//
//    }

    private void callApiReplies(String nextPageToken, String parentId) {
        ApiServicePlayList.apiServicePlayList.Replies(
                nextPageToken,
                "snippet",
                "100",
                parentId,
                "plainText",
                Util.API_KEY
        ).enqueue(new Callback<Replies>() {
            @Override
            public void onResponse(Call<Replies> call, Response<Replies> response) {
                Replies replies = null;
                Log.d("abbb", "Success");
                Log.d("abccc", parentId);
                Log.d("abbc", response.body().toString());
                String authorLogoUrl = "", authorName = "", publishAt = "", updateAt = "",
                        displayContentCmt = "", authorIdChannel = "";
                int likeCount = 0;

                replies = response.body();
                if (replies != null) {
                    ArrayList<ItemsR> listItem = replies.getItems();
                    for (int i = 0; i < listItem.size(); i++) {
                        authorLogoUrl = listItem.get(i).getSnippet().getAuthorProfileImageUrl();
                        authorName = listItem.get(i).getSnippet()
                                .getAuthorDisplayName();
                        publishAt = listItem.get(i).getSnippet()
                                .getPublishedAt();
                        updateAt = listItem.get(i).getSnippet()
                                .getUpdatedAt();
                        authorIdChannel = listItem.get(i).getSnippet()
                                .getAuthorChannelId().getValue();
                        displayContentCmt = listItem.get(i).getSnippet()
                                .getTextDisplay();
                        likeCount = listItem.get(i).getSnippet()
                                .getLikeCount();

                        Util.listReplies.add(new RepliesCommentItem(
                                displayContentCmt, authorName, authorLogoUrl,
                                authorIdChannel, likeCount, publishAt, updateAt
                        ));
                        adapter.notifyItemInserted(i);
                    }
                }

            }
            @Override
            public void onFailure(Call<Replies> call, Throwable t) {

            }
        });
    }
}
