package com.example.videoviewdemo.Addapter;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoviewdemo.R;
import com.example.videoviewdemo.model.GifFile;
import com.example.videoviewdemo.model.RefressIndecator;
import com.example.videoviewdemo.model.Sticker;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.List;

public class StrikerAddapter extends RecyclerView.Adapter<StrikerAddapter.StrikeHolder> {

    List<Sticker> strickers = new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    GifFile gifFile;
    RefressIndecator indecator;
    public StrikerAddapter(Context context,RefressIndecator indecator){
        inflater = LayoutInflater.from(context);
        this.context = context;
        gifFile = GifFile.getInstance();
        this.indecator = indecator;
    }
    @NonNull
    @Override
    public StrikeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.single_stiker,parent,false);
        return new StrikeHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StrikeHolder holder, int position) {
        if(strickers != null){
            Sticker single = strickers.get(position);
            Log.e("jaydip poss",position+"");
//            Slider slider = new Slider(context);
//            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            slider.setLayoutParams(layoutParams);
//            holder.frameLayout.addView(slider);
            Float[] values = {(float) single.start, (float) single.end};
            holder.imageView.setImageBitmap(single.image);

            holder.rSlider.setValueTo(gifFile.frames.size());
            holder.rSlider.setValueFrom(1);
            holder.rSlider.setStepSize(1);
            holder.rSlider.setValues((float)single.start,(float)single.end);
            holder.rSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
                @Override
                public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                  if(fromUser){
                      Log.e("jaydipBool",fromUser+"");
                      List<Float> val= slider.getValues();
                      Log.e("jaydipValue",val.get(0)+"   //" +val.get(1) );
                      single.start = val.get(0).intValue();
                      single.end = val.get(1).intValue();
                      Log.e("jaydipValue",single.start+"   //" +single.end );
                      gifFile.setRange(single.viewId,val.get(0).intValue(),val.get(1).intValue());
                      indecator.onRefresh();
                  }
//                    notifyDataSetChanged();
//                    gifFile.setRange(val.get(0).intValue(),val.get(1).intValue(),holder.itemView.getId());
                }
            });
        }
    }
    public void setStickers(List<Sticker> strings){
        this.strickers = strings;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return strickers.size();
    }

    public class StrikeHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        RangeSlider rSlider;

    public StrikeHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.StickerImage);
        rSlider = itemView.findViewById(R.id.rSlider);
    }
}
}
