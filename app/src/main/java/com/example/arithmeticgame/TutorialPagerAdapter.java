package com.example.arithmeticgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TutorialPagerAdapter extends RecyclerView.Adapter<TutorialPagerAdapter.TutorialViewHolder> {

    private Context context;
    private List<TutorialActivity.TutorialPage> pages;

    public TutorialPagerAdapter(Context context, List<TutorialActivity.TutorialPage> pages) {
        this.context = context;
        this.pages = pages;
    }

    @NonNull
    @Override
    public TutorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tutorial_page_item, parent, false);
        return new TutorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorialViewHolder holder, int position) {
        TutorialActivity.TutorialPage page = pages.get(position);

        holder.tutorialImageView.setImageResource(page.getImageResId());
        holder.tutorialTitleTextView.setText(page.getTitle());
        holder.tutorialDescriptionTextView.setText(page.getDescription());
    }

    @Override
    public int getItemCount() {
        return pages.size();
    }

    public static class TutorialViewHolder extends RecyclerView.ViewHolder {
        ImageView tutorialImageView;
        TextView tutorialTitleTextView;
        TextView tutorialDescriptionTextView;

        public TutorialViewHolder(@NonNull View itemView) {
            super(itemView);
            tutorialImageView = itemView.findViewById(R.id.tutorialImageView);
            tutorialTitleTextView = itemView.findViewById(R.id.tutorialTitleTextView);
            tutorialDescriptionTextView = itemView.findViewById(R.id.tutorialDescriptionTextView);
        }
    }
}
