package com.app.codeweb.codeweb.Others;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import java.io.File;
import java.io.FileNotFoundException;



public class MyImageLoader {

        private String filePath;
        private static com.app.codeweb.codeweb.Others.MyImageLoader instance;
        private int width = 128;
        private int height = 128;
        private Bitmap source;

        protected MyImageLoader() {
        }

        public static MyImageLoader init() {
            if(instance == null) {
                Class var0 = com.app.codeweb.codeweb.Others.MyImageLoader.class;
                synchronized(com.app.codeweb.codeweb.Others.MyImageLoader.class) {
                    if(instance == null) {
                        instance = new com.app.codeweb.codeweb.Others.MyImageLoader();
                    }
                }
            }

            return instance;
        }

        public MyImageLoader from(String filePath) {
            this.filePath = filePath;
            return instance;
        }

        public MyImageLoader requestSize(int width, int height) {
            this.height = width;
            this.width = height;
            return instance;
        }

        public Bitmap getBitmap() throws FileNotFoundException {
            File file = new File(this.filePath);
            if(!file.exists()) {
                throw new FileNotFoundException();
            } else {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(this.filePath, options);
                options.inSampleSize = this.calculateInSampleSize(options, this.width, this.height);
                options.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeFile(this.filePath, options);
                return bitmap;
            }
        }

    public Bitmap getCircleBitmap() throws FileNotFoundException {
        File file = new File(this.filePath);
        if(!file.exists()) {
            throw new FileNotFoundException();
        } else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(this.filePath, options);
            options.inSampleSize = this.calculateInSampleSize(options, this.width, this.height);
            options.inJustDecodeBounds = false;
            Bitmap source = BitmapFactory.decodeFile(this.filePath, options);

            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size)/2;
            int y = (source.getHeight() - size)/2;
            Bitmap squaredBitmap = Bitmap.createBitmap(source, x,y,size,size);
            if(squaredBitmap != source){
                source.recycle();
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size/2f;
            canvas.drawCircle(r,r,r,paint);

            squaredBitmap.recycle();
            return bitmap;
        }
    }

        public Drawable getImageDrawable() throws FileNotFoundException {
            File file = new File(this.filePath);
            if(!file.exists()) {
                throw new FileNotFoundException();
            } else {
                Drawable drawable = Drawable.createFromPath(this.filePath);
                return drawable;
            }
        }

        private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            int height = options.outHeight;
            int width = options.outWidth;
            int inSampleSize = 1;
            if(height > reqHeight || width > reqWidth) {
                int halfHeight = height / 2;

                for(int halfWidth = width / 2; halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth; inSampleSize *= 2) {
                    ;
                }
            }

            return inSampleSize;
        }


}
