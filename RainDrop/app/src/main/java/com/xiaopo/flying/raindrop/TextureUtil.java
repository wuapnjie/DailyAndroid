package com.xiaopo.flying.raindrop;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glTexParameteri;

/**
 * @author wupanjie
 */

public class TextureUtil {

  private TextureUtil() {
    //no instance
  }

  //TODO
  public static int loadTextureFromResource(final Resources resources, final int resourceId) {
    final BitmapFactory.Options options = new BitmapFactory.Options();
    //options.inScaled = false;  // No pre-scaling
    options.inSampleSize = 4;

    // Read in the resource
    final Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId, options);

    return loadTextureFromBitmap(bitmap, true);
  }

  public static int loadTextureFromBitmap(final Bitmap bitmap, boolean needRecycle) {
    final int[] textureHandle = new int[1];

    glGenTextures(1, textureHandle, 0);

    if (textureHandle[0] != 0) {
      glBindTexture(GL_TEXTURE_2D, textureHandle[0]);

      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
      GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

      if (needRecycle) bitmap.recycle();
    }

    if (textureHandle[0] == 0) {
      throw new RuntimeException("Error loading texture");
    }

    return textureHandle[0];
  }
}
