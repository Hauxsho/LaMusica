package com.example.lamusica;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<MusicFiles> mFiles;

    public MusicAdapter(Context mContext, ArrayList<MusicFiles> mFiles) {
        this.mContext = mContext;
        this.mFiles = mFiles;
    }

    @NonNull
    @Override
    public MusicAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_items,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.MyViewHolder holder, final int position) {
        holder.fileName.setText(mFiles.get(position).getTitle());
        if(getAlbumArt(mFiles.get(position).getPath())!=null)
        {
            byte[] image = getAlbumArt(mFiles.get(position).getPath());
            Glide.with(mContext).asBitmap().load(image).into(holder.albumArt);
        }
        else
        {
            Glide.with(mContext).asBitmap().load(R.drawable.ic_play).into(holder.albumArt);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,Player.class);
                intent.putExtra("position",position);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView fileName;
        ImageView albumArt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.music_file_name);
            albumArt = itemView.findViewById(R.id.music_img);
        }
    }

    private  byte[] getAlbumArt(String uri){
        byte[] art= null;
        try{
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(uri);
            art = retriever.getEmbeddedPicture();
            retriever.release();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return art;
    }
}