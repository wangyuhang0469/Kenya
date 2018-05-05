package com.example.administrator.kenya.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.view.PhotoViewPager;

import java.util.List;

import uk.co.senab.photoview.PhotoView;


public class PreviewDialog extends Dialog{

    private List<String> urls;

    private MyImageAdapter adapter;
    private int beginPosition;


    public PreviewDialog(@NonNull Context context, List<String> urls,int beginPosition) {
        super(context,R.style.FullScreenDialog);
        this.urls = urls;
        this.beginPosition = beginPosition;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_dialog);


        PhotoViewPager viewPager = findViewById(R.id.viewPager);
        final TextView textView = findViewById(R.id.text);

        adapter = new MyImageAdapter(urls, getContext());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(beginPosition, false);
        textView.setText((beginPosition+1) + "/" + urls.size());
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                textView.setText(position + 1 + "/" + urls.size());
            }
        });

        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreviewDialog.this.dismiss();
            }
        });

    }


    public class MyImageAdapter extends PagerAdapter {
        private List<String> imageUrls;
        private Context context;

        public MyImageAdapter(List<String> imageUrls, Context context) {
            this.imageUrls = imageUrls;
            this.context = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String url = imageUrls.get(position);
            PhotoView photoView = new PhotoView(context);

            Glide.with(context)
                    .load(url)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Log.e("====================", "onResourceReady: "+isFromMemoryCache +"   "+ isFirstResource );
                            return false;
                        }
                    })
                    .into(photoView);
            Log.e("====================", "onResourceReady: "+ url );

            container.addView(photoView);

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreviewDialog.this.dismiss();
            }
        });
            return photoView;
        }

        @Override
        public int getCount() {
            return imageUrls != null ? imageUrls.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }




}
