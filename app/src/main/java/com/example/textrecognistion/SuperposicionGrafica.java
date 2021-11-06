package com.example.textrecognistion;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import java.util.HashSet;
import java.util.Set;

public class SuperposicionGrafica extends View {
    //se inicializa las variables
    private final Object lock = new Object();
    private int previewWidth; //ancho de la vista previa
    private float widthScaleDactor = 1.0f; //ancho del factor de escala
    private int previewHeight; //altura de la vista previa
    private float heightScaleFactor = 1.0f; //altura del factor de escala
    private Set<Graphic> graphics = new HashSet<>();

    public abstract static class  Graphic{
        private SuperposicionGrafica overlay;
        public Graphic(SuperposicionGrafica overlay) { this.overlay = overlay; }
        public abstract void draw(Canvas canvas);
        //con esemetodo se invalida una vista
        public void postInvalidate() {
            overlay.postInvalidate();
        }//con ese metod se incalida unavista
    }
    public SuperposicionGrafica(Context context, AttributeSet attrs) { super(context, attrs);  }
    /*Elimina todos losgraficos de la superposicion y sesincroniza
    para mentarner todos los subprocesos concurrentes enejecución y sincronización.*/
    public void clear(){
        synchronized (lock){
            graphics.clear();
        }
        postInvalidate();
    }

    public void add(Graphic graphic) {
        synchronized (lock){
            graphics.add(graphic);
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        synchronized (lock){
            if((previewWidth !=0) && (previewHeight != 0)){
                widthScaleDactor = (float) canvas.getWidth()/ (float) previewWidth;
                heightScaleFactor = (float) canvas.getHeight()/ (float) previewHeight;
            }
            for(Graphic graphic : graphics){
                graphic.draw(canvas);
            }
        }
    }
}
