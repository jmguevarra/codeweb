package com.app.codeweb.codeweb.Others;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;
import java.util.HashMap;


public class VideoThumbnail/* extends  RequestHandler*/{

    public static Bitmap Thumbnail(String path) throws Throwable{
        Bitmap bitmap = null;
        MediaMetadataRetriever mmr = null;

        try{
                mmr = new MediaMetadataRetriever();
                mmr.setDataSource(path);
                bitmap = mmr.getFrameAtTime();
//            bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND);

        }catch(Exception e){
            e.printStackTrace();
            throw new Throwable("Exception in MMR is "+e.getMessage());
        } finally {
            if(mmr != null){
                mmr.release();
            }
        }
        return  bitmap;
    }

   /* public String SCHEM_VIDEO = "video";
    @Override
    public boolean canHandleRequest(Request data){
        String scheme = data.uri.getScheme();

        return (SCHEM_VIDEO.equals(scheme));
    }

    @Override
    public Result load(Request request, int networkPolicy) throws IOException {
        Bitmap bm = ThumbnailUtils.createVideoThumbnail(request.uri.getPath(), MediaStore.Images.Thumbnails.MINI_KIND);

        return new Result(bm, Picasso.LoadedFrom.NETWORK);
    }*/
}
