package com.example.youtubeapp.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.Util;
import com.example.youtubeapp.model.itemrecycleview.CommentItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentYoutubeAdapter extends RecyclerView.Adapter<CommentYoutubeAdapter.CommentViewHolder> {
    private ArrayList<CommentItem> listItemCmt;

    public CommentYoutubeAdapter(ArrayList<CommentItem> listItemCmt) {
        this.listItemCmt = listItemCmt;
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
        if (item == null) {
            return ;
        }
        String authorLogoUrl = item.getAuthorLogoUrl();
        String authorName = item.getAuthorName();
        String publishedAt = item.getPublishedAt();
        String dateDiff = Util.getTime(publishedAt);
        String commentContent = item.getTextDisplay();
        int likeCountCmt = item.getLikeCount();
        int repliesCountCmt = item.getTotalReplyCount();

        Picasso.get().load(authorLogoUrl).into(holder.civLogoAuthor);
        holder.tvAuthorName.setText(authorName);
        holder.tvCommentContent.setText(commentContent);
        holder.tvDateDiff.setText(dateDiff);
        holder.tvLikeCountCmt.setText(String.valueOf(likeCountCmt));
        holder.tvRepliesCount.setText(String.valueOf(repliesCountCmt));

    }

    @Override
    public int getItemCount() {
        if (listItemCmt != null) {
            return listItemCmt.size();
        }
        return 0;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView civLogoAuthor;
        private TextView tvAuthorName, tvDateDiff, tvCommentContent,
                tvLikeCountCmt, tvRepliesCount;
        private ImageView ivMoreSelect;
        private AppCompatButton btListReplies;

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
        }
    }
}
