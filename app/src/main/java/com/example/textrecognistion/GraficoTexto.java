package com.example.textrecognistion;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.google.mlkit.vision.text.Text;

public class GraficoTexto extends SuperposicionGrafica.Graphic {

    private static final String TAG = "GraficoTexto";
    private static final int TEXT_COLOR = Color.RED;
    private static final float TEXT_SIZE = 54.0f;
    private static final float STROKE_WIDTH = 4.0f;

    private final Paint rectPaint;
    private final Paint textPaint;
    //se importa de mlkit
    private final Text.Element element;

    GraficoTexto(SuperposicionGrafica overlay, Text.Element element){
        super(overlay);
        this.element = element;
        rectPaint = new Paint();
        rectPaint.setColor(TEXT_COLOR);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(STROKE_WIDTH);
        textPaint = new Paint();
        textPaint.setColor(TEXT_COLOR);
        textPaint.setTextSize(TEXT_SIZE);

        postInvalidate();
    }


    @Override
    public void draw(Canvas canvas) {
        Log.d(TAG, "dibujar gráfico de texto");
        if(element == null){
            throw new IllegalStateException("Intentando dibujar un texto nulo.");
        }
        RectF  rect = new RectF(element.getBoundingBox());
        canvas.drawRect(rect,rectPaint);

        canvas.drawText(element.getText(), rect.left, rect.bottom, textPaint);
    }
}
