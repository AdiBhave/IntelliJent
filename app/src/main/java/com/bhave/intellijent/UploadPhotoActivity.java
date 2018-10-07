package com.bhave.intellijent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.FaceDetector;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.util.List;

public class UploadPhotoActivity extends AppCompatActivity {

    Button Continue;
    ImageView uploaded;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap;
    ProgressBar pb;
    public static String mood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);
        Continue = (Button) findViewById(R.id.Continue);
        Continue.setVisibility(View.GONE);
        uploaded = (ImageView) findViewById(R.id.uploadedImg);
        pb = (ProgressBar) findViewById(R.id.uploadProgress);
        pb.setVisibility(View.GONE);
    }

    public void openCamera(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            uploaded.setImageBitmap(imageBitmap);
            Continue.setVisibility(View.VISIBLE);
        }
    }

    public void uploadImage(View view0){
        pb.setVisibility(View.VISIBLE);
        FirebaseVisionFaceDetectorOptions options =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setModeType(FirebaseVisionFaceDetectorOptions.ACCURATE_MODE)
                        .setLandmarkType(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .setClassificationType(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .setMinFaceSize(0.15f)
                        .setTrackingEnabled(true)
                        .build();

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);

        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance()
                .getVisionFaceDetector(options);
        Task<List<FirebaseVisionFace>> result =
                detector.detectInImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionFace>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionFace> faces) {
                                        for(FirebaseVisionFace face :faces){
                                            float s = face.getSmilingProbability();
                                            float left = face.getLeftEyeOpenProbability();
                                            float right = face.getRightEyeOpenProbability();
                                            if(s < 0.5){
                                                mood = "happy";
                                            }
                                            else{
                                                mood = "sad";
                                            }
                                            pb.setVisibility(View.INVISIBLE);
                                            uploaded.setImageResource(android.R.drawable.ic_menu_gallery);
                                            Toast.makeText(UploadPhotoActivity.this,String.valueOf(s),Toast.LENGTH_SHORT).show();
                                        }
                                        Intent i = new Intent(UploadPhotoActivity.this,Recommendations.class);
                                        i.putExtra("mood",mood);
                                        //startActivity(i);
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                        Toast.makeText(UploadPhotoActivity.this,"Failed to upload",Toast.LENGTH_SHORT).show();
                                    }
                                });
    }


}
