package com.example.oriaso.kidsnestadmin;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;

/**
 * Created by oriaso on 3/12/18.
 */

public class KidsNestViewHolder extends RecyclerView.ViewHolder {
    TextView textViewKidsNestListTitle, textViewNoticeListSource;
    ImageView imageViewNoticeListImage;
    RelativeTimeTextView textViewNoticeListSince;
    View mView;


    public KidsNestViewHolder(View itemView){
        super(itemView);
        this.mView = itemView;
        textViewKidsNestListTitle = (TextView) itemView.findViewById(R.id.textViewKidsNestListName);
//        textViewKidsNestListTitle = (TextView) itemView.findViewById(R.id.textViewKidsNestListTitle);
//        textViewNoticeListSource = (TextView) itemView.findViewById(R.id.textViewNewsListAuthor);
//        textViewNoticeListSince = (RelativeTimeTextView) itemView.findViewById(R.id.textViewNoticeListSince);
//        imageViewNoticeListImage = (ImageView) itemView.findViewById(R.id.imageViewKidsNestListImage);
    }
}
