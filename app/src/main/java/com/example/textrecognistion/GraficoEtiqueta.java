package com.example.textrecognistion;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;

import java.util.List;

public class GraficoEtiqueta extends SuperposicionGrafica.Graphic {
    //se crean dos pinceles y se llama a la clase SuperposicionGrafica
    private final Paint textPaint;
    private final Paint bgPaint;
    private final SuperposicionGrafica overlay;
    //en este arrayList se mostraran los textos reconocidos en las imagenes.
    private final List<String> labels;

    //esto permtira que se muestren todos los textos reconocidos de la imagen
    public GraficoEtiqueta(SuperposicionGrafica overlay, List<String> labels){
        super(overlay);
        this.overlay = overlay;
        this.labels = labels;
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(60f);
        bgPaint = new Paint();
        bgPaint.setColor(Color.BLACK);
        bgPaint.setAlpha(50);
    }
    //se obtiene la ubicaion del Label que se mostrara sobre la imagen
    @Override
    public synchronized void draw(Canvas canvas){
        float x = overlay.getWidth() / 4.0f;
        float y = overlay.getHeight() / 4.0f;

        for (String label : labels){
            drawTextWithBackground(label, x, y, new TextPaint(textPaint), bgPaint, canvas); y = y - 62.0f;
        }
    }
    // y con el drawTextWithBackgrond se dibujo el texto sobre de imagen
    private void drawTextWithBackground(String text,float x, float y, TextPaint paint, Paint bgPaint, Canvas canvas){
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        canvas.drawRect(new Rect((int) (x), (int) (y + fontMetrics.top),
                (int)(x + paint.measureText(text)), (int) (y + fontMetrics.bottom)),bgPaint);
        canvas.drawText(text, x, y, textPaint);
    }
}
