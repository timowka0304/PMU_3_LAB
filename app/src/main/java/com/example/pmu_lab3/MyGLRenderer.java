package com.example.pmu_lab3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import java.io.InputStream;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

class MyGLRenderer implements GLSurfaceView.Renderer {
    static public int[] texture_name = {
            R.drawable.sun,
            R.drawable.earth,
            R.drawable.moon,
    };

    static public int[] textures = new int [texture_name.length];
    private final Sphere Sun = new Sphere(2.2f);
    private final Sphere Earth = new Sphere(1.2f);
    private final Sphere Moon = new Sphere(0.5f);

    Context c;

    public MyGLRenderer(Context context) {
        c = context;
    }

    private float p = 0.0f;
    private float angle = 40.0f;
    private float mTransY = 0f;

    private void loadGLTexture(GL10 gl) {
        gl.glGenTextures(3, textures, 0);
        for (int i = 0; i < texture_name.length; ++i) {
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[i]);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
            InputStream is = c.getResources().openRawResource(texture_name[i]);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f,1.0f);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(-10,10, -10, 10, -10, 10);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glScalef(1, 1, 1);
        loadGLTexture(gl);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {}

    @Override
    public void onDrawFrame(GL10 gl) {
        float RotationOffset;
        float RotationSpeed;
        p = (p == 360) ? 0 : p + 0.1f;
        angle = (angle == 360) ? 0 : angle + 1.3f;

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, Sun.textureBuffer);
        gl.glPushMatrix();
        gl.glRotatef(p, 0, 1, 0);
        gl.glScalef(1.5f, 1.5f, 1.5f);
        Sun.onDrawFrame(gl);

        float e_x = (float) Math.cos(mTransY) * 7f;
        float e_y = (float) Math.sin(mTransY) * 7f;
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslatef(e_x, e_y, 1.0f);
        gl.glScalef(1.5f, 1.5f, 1.5f);
        gl.glRotatef(angle, 1, 1, 1);
        gl.glPushMatrix();
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[1]);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, Earth.textureBuffer);
        Earth.onDrawFrame(gl);

        gl.glPopMatrix();
        RotationOffset = 2f;
        RotationSpeed = 2f;
        gl.glTranslatef(RotationOffset * (float)(cos(1 * RotationSpeed)),0,
                RotationOffset * (float)(sin(1 * RotationSpeed)));
        gl.glRotatef(p * 1.2f, 0, 1, 0);
        gl.glColor4f(0, 0 ,1f , 1);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[2]);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, Moon.textureBuffer);
        gl.glColor4f(1, 1 ,1, 1);
        Moon.onDrawFrame(gl);
        gl.glPopMatrix();
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D);

        mTransY+=0.005f;
    }
}

