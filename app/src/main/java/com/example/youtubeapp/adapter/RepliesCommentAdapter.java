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
import com.example.youtubeapp.model.itemrecycleview.RepliesCommentItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RepliesCommentAdapter extends RecyclerView.Adapter<RepliesCommentAdapter.RepliesViewHolder> {
    ArrayList<RepliesCommentItem> listReplies;

    public RepliesCommentAdapter(ArrayList<RepliesCommentItem> listReplies) {
        this.listReplies = listReplies;
    }

    @NonNull
    @Override
    public RepliesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_replies_comment_video, parent, false);
        return new RepliesViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RepliesViewHolder holder, int position) {
        RepliesCommentItem replies = listReplies.get(position);
        holder.setData(replies);
    }

    @Override
    public int getItemCount() {
        if (listReplies != null) {
            return listReplies.size();
        }
        return 0;
    }

    class RepliesViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civLogoAuthor;
        TextView tvAuthorName, tvDateDiff, tvCommentContent,
                tvLikeCountCmt, tvEditor;
        ImageView ivMoreSelect;

        public RepliesViewHolder(@NonNull View itemView) {
            super(itemView);
            civLogoAuthor = itemView.findViewById(R.id.civ_logo_author_replies_item);
            tvAuthorName = itemView.findViewById(R.id.tv_author_name_replies_item);
            tvDateDiff = itemView.findViewById(R.id.tv_date_diff_replies_item);
            tvCommentContent = itemView.findViewById(R.id.tv_cmt_content_replies_item);
            tvLikeCountCmt = itemView.findViewById(R.id.tv_like_count_cmt_replies_item);
            tvEditor = itemView.findViewById(R.id.tv_editor_replies_item);
            ivMoreSelect = itemView.findViewById(R.id.iv_more_select_replies_item);
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void setData(RepliesCommentItem replies) {
            if (replies == null) {
                return;
            }

            String authorLogoUrl = replies.getAuthorLogoUrl();
            String authorName = replies.getAuthorName();
            String publishedAt = replies.getPublishedAt();
            String updateAt = replies.getUpdateAt();
            String dateDiff = Util.getTime(publishedAt);
            String commentContent = replies.getTextDisplay();
            int likeCountCmt = replies.getLikeCount();

            // Đưa dữ đổ vào view
            Picasso.get().load(authorLogoUrl).into(civLogoAuthor);
            tvAuthorName.setText(authorName);
            tvCommentContent.setText(commentContent);
            tvDateDiff.setText(" • " + dateDiff);
            tvLikeCountCmt.setText(Util.convertViewCount(likeCountCmt));
            if (!publishedAt.equals(updateAt)) {
                tvEditor.setVisibility(View.VISIBLE);
            }

        }
    }
}
