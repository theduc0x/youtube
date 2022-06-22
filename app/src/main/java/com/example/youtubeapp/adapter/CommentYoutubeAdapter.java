package com.example.youtubeapp.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.Util;
import com.example.youtubeapp.model.itemrecycleview.CommentItem;
import com.example.youtubeapp.model.listcomment.RepliesComment;
import com.example.youtubeapp.my_interface.IItemOnClickCommentListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentYoutubeAdapter extends RecyclerView.Adapter<CommentYoutubeAdapter.CommentViewHolder> {
    private ArrayList<CommentItem> listItemCmt;
    private IItemOnClickCommentListener clickCommentListener;
    private RepliesComment repliesComment;

    public CommentYoutubeAdapter(ArrayList<CommentItem> listItemCmt, IItemOnClickCommentListener clickCommentListener) {
        this.listItemCmt = listItemCmt;
        this.clickCommentListener = clickCommentListener;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_comment_video, parent, false);
        return new CommentViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentItem item = listItemCmt.get(position);
        holder.setData(item);
        holder.rlOpenReplies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCommentListener.onClickItemComment(repliesComment);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listItemCmt != null) {
            return listItemCmt.size();
        }
        return 0;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civLogoAuthor;
        TextView tvAuthorName, tvDateDiff, tvCommentContent,
                tvLikeCountCmt, tvRepliesCount, tvEditor;
        ImageView ivMoreSelect;
        AppCompatButton btListReplies;
        RelativeLayout rlOpenReplies;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            civLogoAuthor = itemView.findViewById(R.id.civ_logo_author);
            tvAuthorName = itemView.findViewById(R.id.tv_author_name);
            tvDateDiff = itemView.findViewById(R.id.tv_date_diff);
            tvCommentContent = itemView.findViewById(R.id.tv_cmt_content);
            tvLikeCountCmt = itemView.findViewById(R.id.tv_like_count_cmt);
            tvRepliesCount = itemView.findViewById(R.id.tv_replies_count);
            ivMoreSelect = itemView.findViewById(R.id.iv_more_select);
            btListReplies = itemView.findViewById(R.id.bt_list_replies);
            rlOpenReplies = itemView.findViewById(R.id.rl_open_replies);
            tvEditor = itemView.findViewById(R.id.tv_editor);
        }
        @RequiresApi(api = Build.VERSION_CODES.O)

        void setData(CommentItem item) {
            if (item == null) {
                return ;
            }
            String authorLogoUrl = item.getAuthorLogoUrl();
            String authorName = item.getAuthorName();
            String publishedAt = item.getPublishedAt();
            String updateAt = item.getUpdateAt();
            String dateDiff = Util.getTime(publishedAt);
            String commentContent = item.getTextDisplay();
            int likeCountCmt = item.getLikeCount();
            int repliesCountCmt = item.getTotalReplyCount();
            repliesComment = item.getRepliesComent();

                    Picasso.get().load(authorLogoUrl).into(civLogoAuthor);
            tvAuthorName.setText(authorName);
            tvCommentContent.setText(commentContent);
            tvDateDiff.setText(" • " + dateDiff);
            tvLikeCountCmt.setText(Util.convertViewCount(likeCountCmt));
            tvRepliesCount.setText(String.valueOf(repliesCountCmt));
            if (!publishedAt.equals(updateAt)) {
                tvEditor.setVisibility(View.VISIBLE);
            }
            if (repliesCountCmt > 0) {
                btListReplies.setVisibility(View.VISIBLE);
                btListReplies.setText(repliesCountCmt + " REPLIES");
            }


        }
    }


}
