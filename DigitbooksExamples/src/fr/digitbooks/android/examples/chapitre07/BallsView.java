/*
 * Copyright (C) 2010   Cyril Mottier & Ludovic Perrier
 *              (http://www.digitbooks.fr/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.digitbooks.android.examples.chapitre07;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import fr.digitbooks.android.examples.R;
import fr.digitbooks.android.examples.util.RandomUtil;

public class BallsView extends View {

    /**
     * Classe permettant de repr�senter une balle dans un environnement 2D
     * disposant d'une position et d'une vitesse.
     */
    private static class Ball {
        public int pX;
        public int pY;
        public float vX;
        public float vY;
    }

    private static final int POST_DELAY = 1000 / 60;

    /**
     * D�finit les vitesses minimales et maximales qu'auront les balles.
     */
    private static final float MIN_VELOCITY = 1.0f;
    private static final float MAX_VELOCITY = 5.0f;

    /**
     * D�finit le nombre de balles minimal, maximal et par d�faut de balles
     * pr�sente dans la vue.
     */
    private static final int MAX_BALL_COUNT = 15;
    private static final int MIN_BALL_COUNT = 1;
    private static final int DEFAULT_BALL_COUNT = 5;

    private static final int RECT_MARGIN = 40;
    private static final int RECT_HEIGHT = 30;
    private static final float RECT_RADIUS = 7.0f;

    private static final int STATE_STOPPED = 0x302;
    private static final int STATE_RUNNING = 0x303;

    /**
     * Objets n�cessaires au dessin de la vue. Les d�finir comme instance de
     * classe permet d'�viter des allocations inutiles. Ils sont allou�s une
     * seule et unique fois ici.
     */
    private final Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mRectPaint = new Paint();
    private final RectF mRect = new RectF();

    private final Handler mHandler = new Handler();

    /**
     * Runnable qui sera ex�cut� de fa�on r�p�titive et permettra d'animer les
     * balles.
     */
    private final Runnable mUpdaterRunnable = new Runnable() {
        public void run() {
            for (Ball ball : mBalls) {
                ball.pX += ball.vX;
                ball.pY += ball.vY;

                final int frameRight = getWidth() - mBallBitmap.getWidth();
                final int frameBottom = getHeight() - mBallBitmap.getHeight();

                if (ball.pX < 0) {
                    ball.pX = 0;
                    ball.vX = -ball.vX;
                } else if (ball.pX > frameRight) {
                    ball.pX = frameRight;
                    ball.vX = -ball.vX;
                }

                if (ball.pY < 0) {
                    ball.pY = 0;
                    ball.vY = -ball.vY;
                } else if (ball.pY > frameBottom) {
                    ball.pY = frameBottom;
                    ball.vY = -ball.vY;
                }

            }

            invalidate();

            /*
             * On reposte ici le Runnable apr�s un temps �quivalent �
             * POST_DELAY. Ce temps est calcul� en fonction du taux de
             * rafra�chissement maximal de l'�cran (60 frames par secondes).
             * POST_DELAY �quivaut alors � 1000/60
             */
            mHandler.postDelayed(this, POST_DELAY);
        }
    };

    private String mBottomText;
    private boolean mInitialized = false;

    @SuppressWarnings("unused")
    private int mState;

    private Bitmap mBallBitmap;
    private ArrayList<Ball> mBalls;

    public BallsView(Context context) {
        this(context, null);
    }

    public BallsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BallsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeBallsView();
    }

    private void initializeBallsView() {
        /*
         * On initialise ici l'ensemble des variables n�cessaires � la vue.
         */
        mBalls = new ArrayList<Ball>();

        mBallBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball);

        mState = STATE_STOPPED;

        mTextPaint.setColor(Color.RED);
        mTextPaint.setTextAlign(Align.CENTER);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextPaint.setTextSize(13.0f);

        mRectPaint.setColor(Color.BLUE);
        mRectPaint.setStrokeWidth(3.0f);
        mRectPaint.setStyle(Style.STROKE);
        mRectPaint.setPathEffect(new DashPathEffect(new float[] {
                5, 5
        }, 0));
    }

    public void start() {
        /*
         * Cette m�thode permet d'animer la vue. Si cette derni�re n'est pas
         * visible. Il n'est pas n�cessaire de lancer le Runnable.
         */
        if (getVisibility() == View.VISIBLE) {
            mHandler.removeCallbacks(mUpdaterRunnable);
            mHandler.post(mUpdaterRunnable);
        }
        mState = STATE_RUNNING;
    }

    public void stop() {
        /*
         * Pour stopper l'animation on supprime simplement le Runnable du
         * Handler.
         */
        mHandler.removeCallbacks(mUpdaterRunnable);
        mState = STATE_STOPPED;
    }

    public void addBall() {
        if (mBalls.size() >= MAX_BALL_COUNT) {
            return;
        }

        Ball ball = new Ball();
        ball.pX = RandomUtil.getPositiveInt(0, getWidth() - mBallBitmap.getWidth());
        ball.pY = RandomUtil.getPositiveInt(0, getHeight() - mBallBitmap.getHeight());
        ball.vX = RandomUtil.getFloat(MIN_VELOCITY, MAX_VELOCITY);
        ball.vY = RandomUtil.getFloat(MIN_VELOCITY, MAX_VELOCITY);
        mBalls.add(ball);

        prepareBottomText();
    }

    public void removeBall() {
        if (mBalls.size() <= MIN_BALL_COUNT) {
            return;
        }
        mBalls.remove(mBalls.size() - 1);
        prepareBottomText();
    }

    private void prepareBottomText() {
        final int ballCount = mBalls.size();
        mBottomText = getResources().getQuantityString(R.plurals.bottom_text, ballCount, ballCount);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        /*
         * Cette m�thode est appel�e lorsque la vue est en train d'�tre
         * supprim�e. Il est donc n�cessaire de stopper l'animation.
         */
        stop();
    }
    
    /*
     * Le code suivant devrait �tre d�comment�. Malheureusement cette m�thode n'�tant disponible
     * qu'� partir de l'API 8, la d�commenter interdit l'application de fonctionner correctement
     * sur des terminaux d'API inf�rieure � 8
     */
//    @Override
//    protected void onVisibilityChanged(View changedView, int visibility) {
//        super.onVisibilityChanged(changedView, visibility);
//        /*
//         * Si la vue n'est plus visible, il n'est pas n�cessaire de continuer �
//         * faire les calculs d'animation. On sauvegarde l'�tat courant et on
//         * arr�te la vue
//         */
//        mHandler.removeCallbacks(mUpdaterRunnable);
//        if (visibility == View.VISIBLE && mState == STATE_RUNNING) {
//            /*
//             * Si la vue passe en "View.VISIBLE" et qu'elle �tait pr�c�demment
//             * "anim�e", il est possible de reprendre les calculs d'animation.
//             */
//            mHandler.post(mUpdaterRunnable);
//        }
//    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        /*
         * Le syst�me nous notifie ici d�s que la vue a chang� de dimension.
         * C'est donc l'occasion pour nous d'ajouter les balles
         */
        if (!mInitialized) {
            for (int i = 0; i < DEFAULT_BALL_COUNT; i++) {
                addBall();
            }
            mInitialized = true;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int w = resolveAdjustedSize(mBallBitmap.getWidth() << 1, widthMeasureSpec);
        final int h = resolveAdjustedSize(mBallBitmap.getHeight() << 1, heightMeasureSpec);

        setMeasuredDimension(w, h);
    }

    private int resolveAdjustedSize(int desiredSize, int measureSpec) {

        /*
         * Cette m�thode permet de r�concilier la taille d�sir�e par la vue avec
         * les contraintes impos�es par le parent.
         */
        int result = desiredSize;
        final int specMode = MeasureSpec.getMode(measureSpec);
        final int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = desiredSize;
                break;

            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }

        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // On commence par dessiner l'ensemble des balles
        for (Ball ball : mBalls) {
            canvas.drawBitmap(mBallBitmap, ball.pX, ball.pY, null);
        }

        final int w = getWidth();
        final int h = getHeight();

        // D�placement du Canvas puis dessin du rectangle
        canvas.save();
        canvas.translate(RECT_MARGIN, h - RECT_MARGIN - RECT_HEIGHT);
        mRect.set(0, 0, w - (RECT_MARGIN << 1), RECT_HEIGHT);
        canvas.drawRoundRect(mRect, RECT_RADIUS, RECT_RADIUS, mRectPaint);
        canvas.restore();

        // D�placement du Canvas puis dessin du texte
        canvas.save();
        canvas.translate(w >> 1, h - RECT_MARGIN - RECT_HEIGHT / 2 + 4);
        canvas.drawText(mBottomText, 0, 0, mTextPaint);
        canvas.restore();
    }

}
