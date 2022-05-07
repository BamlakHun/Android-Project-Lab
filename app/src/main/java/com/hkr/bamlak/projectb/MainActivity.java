package com.hkr.bamlak.projectb;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.RequestOptions;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);
    }
    //select an image
    public void pickAphoto(View v) {
        // Open up this gallery view to select a file
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        // set type (so long as it is an image open
        intent.setType("image/*");
        // set activity for the intent , the request code to state where the request comes from
        startActivityForResult(intent, 1);
    }

    public void apply(Transformation<Bitmap> filter){

        Glide
                .with(this)
                .load(image)
                .apply(RequestOptions.bitmapTransform(filter))
                .into(imageView);

    }
    public void applySepia(View v){
     apply(new SepiaFilterTransformation());

    }
    public void applyToon(View v){
        apply(new ToonFilterTransformation());

    }
    public void applySketch(View v){
        apply(new SketchFilterTransformation());

    }
    // once a photo selected we jump to ONACRIVITY
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            try {
                //Uri spacifys location
                Uri uri = data.getData();
                getContentResolver();
                ParcelFileDescriptor parcelFileDescriptor =
                        getContentResolver().openFileDescriptor(uri, "r");
                FileDescriptor fileDiscriptor = parcelFileDescriptor.getFileDescriptor();
                image = BitmapFactory.decodeFileDescriptor(fileDiscriptor);
                parcelFileDescriptor.close();
                imageView.setImageBitmap(image);





            }
            catch (IOException e){
                Log.e("hkr", "Image not found", e);

            }
        }
    }
}
