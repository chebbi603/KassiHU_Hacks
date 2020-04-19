package com.javaislove.mycoronaguide;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import java.util.List;

public class Adapter extends PagerAdapter {

    private List<Model> models;
    private LayoutInflater layoutInflater;
    private Context context;

    public Adapter(List<Model> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item, container, false);

        ImageView imageView;
        TextView title, desc;
        RelativeLayout card;

        imageView = view.findViewById(R.id.image);
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);
        card = view.findViewById(R.id.card);

        imageView.setImageResource(models.get(position).getImage());
        title.setText(models.get(position).getTitle());
        desc.setText(models.get(position).getDesc());

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position == 0) {
                    //Toast.makeText(v.getContext(), "This feature is currently under development", Toast.LENGTH_LONG).show();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://covid-19.tn/لوحة-القيادة/"));
                    context.startActivity(browserIntent);
                }
                if(position == 1) {
                    Intent intent = new Intent(context, StatsActivity.class);
                    intent.putExtra("param", models.get(position).getTitle());
                    context.startActivity(intent);
                }
                if(position == 2) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("param", models.get(position).getTitle());
                    context.startActivity(intent);
                }
                if(position == 3) {
                    Intent intent = new Intent(context, TipsActivity.class);
                    intent.putExtra("param", models.get(position).getTitle());
                    context.startActivity(intent);
                }
                if(position == 4) {
                    Intent intent = new Intent(context, ReportActivity.class);
                    intent.putExtra("param", models.get(position).getTitle());
                    context.startActivity(intent);
                }
                if(position == 5) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.putExtra("param", models.get(position).getTitle());
                    context.startActivity(intent);
                }
            }
        });


        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
