package com.example.textrecognistion;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;

import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ImageView mImageView;
    private Button mTextButton;
    private Bitmap mSelectedImage;
    private SuperposicionGrafica mSuperposicionGrafica;
    private Integer mImageMaxWidth;
    private Integer mImageMaxHeight;
    private static final int RESULTS_TO_SHOW = 10;
    Button btnCamara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextButton = findViewById(R.id.button_text);
        mImageView = findViewById(R.id.image_view);
        mSuperposicionGrafica = findViewById(R.id.graphic_overlay);
        btnCamara = findViewById(R.id.btn_camera);

        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camara();
            }
        });

        mTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runTextRecognition();
            }
        });
        Spinner dropdown = findViewById(R.id.spinner);
        String[] item = new String[]{
                "Prueba Imagen1 (Texto)",
                "Prueba Imagen2 (Texto)",
                "Prueba Imagen3 (Texto)",
                "Prueba Imagen4 (Texto)",
                "Prueba Imagen5 (Texto)",
                "Prueba Imagen6 (Texto)",
                "Prueba Imagen7 (Texto)",
                "Prueba Imagen8 (Texto)",
                "Prueba Imagen9 (Texto)",
                "Prueba Imagen10 (Texto)"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
    }

    private void camara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent, 1);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imgBitmap = (Bitmap)  extras.get("data");
            mImageView.setImageBitmap(imgBitmap);
        }
    }

    private void runTextRecognition(){
        InputImage image=InputImage.fromBitmap(mSelectedImage, 0);
        TextRecognizer recognizer= TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        mTextButton.setEnabled(false);
        recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(@NonNull Text text) {
                mTextButton.setEnabled(true);
                processTextRecognitionResult(text);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mTextButton.setEnabled(true);
                e.printStackTrace();
            }
        });
    }


    private void processTextRecognitionResult(Text texts){
        List<Text.TextBlock> blocks = texts.getTextBlocks();
        if (blocks.size()==0) {
            showToast("No hay texto");
            return;
        }
        mSuperposicionGrafica.clear();
        for (int i=0; i< blocks.size(); i++){
            List<Text.Line> lines = blocks.get(i).getLines();
            for (int j=0; j<lines.size(); j++){
                List<Text.Element> elements = lines.get(j).getElements();
                for (int k=0;k<elements.size();k++){
                    SuperposicionGrafica.Graphic textGraphic=new GraficoTexto(mSuperposicionGrafica, elements.get(k));
                    mSuperposicionGrafica.add(textGraphic);
                }
            }
        }
    }

    private void  showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private Integer getmImageMaxWidth(){
        if (mImageMaxWidth==null){
            mImageMaxWidth=mImageView.getWidth();
        }
        return mImageMaxWidth;
    }

    private Integer getmImageMaxHeight(){
        if (mImageMaxHeight==null){
            mImageMaxHeight=mImageView.getWidth();
        }
        return mImageMaxHeight;
    }
    private Pair<Integer, Integer> getTargetedWidthHeight(){
        int targetWidth;
        int targetHeight;
        int maxWidthForPortraidMode=getmImageMaxWidth();
        int maxHeightForPortraidMode=getmImageMaxHeight();
        targetWidth=maxWidthForPortraidMode;
        targetHeight=maxHeightForPortraidMode;
        return new Pair<>(targetWidth, targetHeight);
    }
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
        mSuperposicionGrafica.clear();
        switch (position){
            case 0: mSelectedImage = getBitmapFromAsset(this, "1.jpg"); break;
            case 1: mSelectedImage = getBitmapFromAsset(this, "2.jpg"); break;
            case 2: mSelectedImage = getBitmapFromAsset(this, "3.jpg"); break;
            case 3: mSelectedImage = getBitmapFromAsset(this, "4.jpg"); break;
            case 4: mSelectedImage = getBitmapFromAsset(this, "5.jpg"); break;
            case 5: mSelectedImage = getBitmapFromAsset(this, "6.jpg"); break;
            case 6: mSelectedImage = getBitmapFromAsset(this, "7.jpg"); break;
            case 7: mSelectedImage = getBitmapFromAsset(this, "8.jpg"); break;
            case 8: mSelectedImage = getBitmapFromAsset(this, "9.jpg"); break;
        }
        if (mSelectedImage!= null){
            Pair<Integer, Integer> targetedSize = getTargetedWidthHeight();
            int targeteWidth = targetedSize.first;
            int maxHeight = targetedSize.second;
            float scaleFactor = Math.max((float) mSelectedImage.getWidth() / (float) targeteWidth,
                    (float) mSelectedImage.getHeight() / (float) maxHeight);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(mSelectedImage,
                    (int)(mSelectedImage.getWidth()/ scaleFactor),
                    (int)(mSelectedImage.getHeight()/ scaleFactor),true);
            mImageView.setImageBitmap(resizedBitmap);
            mSelectedImage = resizedBitmap;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent){ }

    public static Bitmap getBitmapFromAsset(Context context, String filePath){
        AssetManager assetManager = context.getAssets();
        InputStream is;
        Bitmap bitmap = null;
        try{
            is = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(is);
        }catch (IOException e){
            e.printStackTrace();
        }
        return bitmap;
    }



    /*private void detectTextFromImage()
    {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector firebaseVisionTextDetector= getInstance().getVisionTextDetector();
        firebaseVisionTextDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>(){

        })
    }*/
}