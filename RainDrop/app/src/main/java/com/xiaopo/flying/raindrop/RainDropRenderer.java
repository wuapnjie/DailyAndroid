package com.xiaopo.flying.raindrop;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_FUNC_ADD;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_COLOR;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE1;
import static android.opengl.GLES20.GL_TEXTURE2;
import static android.opengl.GLES20.GL_TEXTURE3;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glBlendEquationSeparate;
import static android.opengl.GLES20.glBlendFuncSeparate;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform2f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * @author wupanjie
 */

public class RainDropRenderer implements GLSurfaceView.Renderer {

    private static final float[] VERTEXES = new float[] {
            -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f
    };

    private int program;
    private final String[] attributes = { "a_position" };
    private final Resources resources;

    private final FloatBuffer vertexBuffer;

    private int textureWaterMapHandle;
    private int textureShineHandle;
    private int textureFgHandle;
    private int textureBgHandle;

    private int textureWaterMapUniformHandle;
    private int textureShineUniformHandle;
    private int textureFgUniformHandle;
    private int textureBgUniformHandle;

    private int resolutionHandle;
    private int parallaxHandle;
    private int parallaxFgHandle;
    private int parallaxBgHandle;
    private int textureRatioHandle;
    private int renderShineHandle;
    private int renderShadowHandle;
    private int minRefractionHandle;
    private int refractionDeltaHandle;
    private int brightnessHandle;
    private int alphaMultiplyHandle;
    private int alphaSubtractHandle;

    private Options options = new Options();
    private RainDrop rainDrop;

    public RainDropRenderer(Resources resources) {
        this.resources = resources;

        this.vertexBuffer = ByteBuffer.allocateDirect(VERTEXES.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(VERTEXES);
        vertexBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        options.resolution = new float[] { width, height };
        options.textureRadio = 1920f / 1080f;

        BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inSampleSize = 2;

        Bitmap dropColor = BitmapFactory.decodeResource(resources, R.drawable.drop_color, options);
        Bitmap dropAlpha = BitmapFactory.decodeResource(resources, R.drawable.drop_alpha, options);

        rainDrop = new RainDrop(width, height, 1, dropColor, dropAlpha, new DropOptions());

        final String vertexShaderCode = ShaderUtil.glslToString(resources, R.raw.rain_drop_vertex);
        final String fragmentShaderCode = ShaderUtil.glslToString(resources, R.raw.rain_drop_fragment);

        final int vertexShaderHandle = ShaderUtil.loadShader(GL_VERTEX_SHADER, vertexShaderCode);
        final int fragmentShaderHandle = ShaderUtil.loadShader(GL_FRAGMENT_SHADER, fragmentShaderCode);

        program = ShaderUtil.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, attributes);

        generateTexture();
        fetchUniformLocation();
    }

    private void generateTexture() {
        textureBgHandle = TextureUtil.loadTextureFromResource(resources, R.drawable.pic_bg);
        textureFgHandle = TextureUtil.loadTextureFromResource(resources, R.drawable.pic_bg);

        if (options.renderShine) {
            textureShineHandle = TextureUtil.loadTextureFromResource(resources, R.drawable.drop_shine2);
        } else {
            textureShineHandle =
                    TextureUtil.loadTextureFromBitmap(Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888), true);
        }
        //textureWaterMapHandle = TextureUtil.loadTextureFromResource(resources, R.drawable.drop_alpha);

        textureWaterMapHandle = TextureUtil.loadTextureFromBitmap(rainDrop.getRainDropBitmap(), false);

        textureBgUniformHandle = glGetUniformLocation(program, "u_textureBg");
        textureFgUniformHandle = glGetUniformLocation(program, "u_textureFg");
        textureShineUniformHandle = glGetUniformLocation(program, "u_textureShine");
        textureWaterMapUniformHandle = glGetUniformLocation(program, "u_waterMap");
    }

    private void fetchUniformLocation() {
        resolutionHandle = glGetUniformLocation(program, "u_resolution");
        parallaxHandle = glGetUniformLocation(program, "u_parallax");
        parallaxFgHandle = glGetUniformLocation(program, "u_parallaxFg");
        parallaxBgHandle = glGetUniformLocation(program, "u_parallaxBg");
        textureRatioHandle = glGetUniformLocation(program, "u_textureRatio");
        renderShineHandle = glGetUniformLocation(program, "u_renderShine");
        renderShadowHandle = glGetUniformLocation(program, "u_renderShadow");
        minRefractionHandle = glGetUniformLocation(program, "u_minRefraction");
        refractionDeltaHandle = glGetUniformLocation(program, "u_refractionDelta");
        brightnessHandle = glGetUniformLocation(program, "u_brightness");
        alphaMultiplyHandle = glGetUniformLocation(program, "u_alphaMultiply");
        alphaSubtractHandle = glGetUniformLocation(program, "u_alphaSubtract");
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        glUseProgram(program);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureWaterMapHandle);
        glUniform1i(textureWaterMapUniformHandle, 0);

        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, textureShineHandle);
        glUniform1i(textureShineUniformHandle, 1);

        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, textureFgHandle);
        glUniform1i(textureFgUniformHandle, 2);

        glActiveTexture(GL_TEXTURE3);
        glBindTexture(GL_TEXTURE_2D, textureBgHandle);
        glUniform1i(textureBgUniformHandle, 3);

        options.config();

        glEnable(GL_BLEND);
        glBlendFuncSeparate(GL_ONE, GL_ONE_MINUS_SRC_COLOR, GL_ONE, GL_ONE_MINUS_SRC_ALPHA); // Screen blend mode

        glBlendEquationSeparate(GL_FUNC_ADD, GL_FUNC_ADD);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, vertexBuffer);

        glDrawArrays(GL_TRIANGLES, 0, 6);

        glDisableVertexAttribArray(0);
    }

    class Options {
        private float[] resolution;
        private float textureRadio;
        private boolean renderShine = false;
        private boolean renderShadow = false;
        private float minRefraction = 256f;
        private float refractionDelta = 512f - minRefractionHandle;
        private float brightness = 1.04f;
        private float alphaMultiply = 10f;
        private float alphaSubtract = 3f;
        private float parallaxBg = 5f;
        private float parallaxFg = 20f;

        public void config() {
            glUniform2f(resolutionHandle, resolution[0], resolution[1]);
            glUniform1f(textureRatioHandle, textureRadio);
            glUniform1i(renderShineHandle, renderShine ? 1 : 0);
            glUniform1i(renderShadowHandle, renderShadow ? 1 : 0);
            glUniform1f(minRefractionHandle, minRefraction);
            glUniform1f(refractionDeltaHandle, refractionDelta);
            glUniform1f(brightnessHandle, brightness);
            glUniform1f(alphaMultiplyHandle, alphaMultiply);
            glUniform1f(alphaSubtractHandle, alphaSubtract);
            glUniform1f(parallaxBgHandle, parallaxBg);
            glUniform1f(parallaxFgHandle, parallaxFg);
        }
    }
}
