package com.xiaopo.flying.raindrop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class DropActivity extends AppCompatActivity {

  private ImageView imageView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_drop);

    imageView = (android.widget.ImageView) findViewById(R.id.imageView);

    BitmapFactory.Options options = new BitmapFactory.Options();
    //options.inSampleSize = 2;

    Bitmap dropColor = BitmapFactory.decodeResource(getResources(), R.drawable.drop_color, options);
    Bitmap dropAlpha = BitmapFactory.decodeResource(getResources(), R.drawable.drop_alpha, options);

    RainDrop rainDrop =
        new RainDrop(DipPixelUtil.getDeviceWidth(this), DipPixelUtil.getDeviceHeight(this), 1,
            dropColor, dropAlpha, new DropOptions());
    imageView.setImageBitmap(rainDrop.getRainDropBitmap());
  }
}
