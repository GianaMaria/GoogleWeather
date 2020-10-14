package com.example.googleauthfirebase.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.squareup.picasso.Transformation;

public class CircleTransformation implements Transformation {

    public CircleTransformation() {
    }

    @Override
    public Bitmap transform(Bitmap source) {
        // Определяем шаблон обрезки...
        Path path = new Path();
        // ...как круг
        path.addCircle(source.getWidth() / 2.0f, source.getHeight() / 2.0f, source.getWidth() / 2.0f, Path.Direction.CCW);
        // Создаём битмап, который и будет результирующим
        Bitmap answerBitMap = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        // Создаём холст для нового битмапа
        Canvas canvas = new Canvas(answerBitMap);
        // Обрезаем холст по кругу (по шаблону)
        canvas.clipPath(path);
        // А теперь рисуем на этом холсте исходное изображение
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawBitmap(source, 0, 0, paint);
        source.recycle();
        return answerBitMap;

    }

    @Override
    public String key() {
        return "circle";
    }
}
