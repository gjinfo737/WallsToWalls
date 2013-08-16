package com.aj.walls.hot;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.aj.walls.hot.R.drawable;

public class TheWall extends WallpaperService {
	private final Handler handler = new Handler();

	public TheWall() {
		super();
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public Engine onCreateEngine() {
		return new GalleryEngine();
	}

	public class GalleryEngine extends Engine implements
			OnSharedPreferenceChangeListener {
		private int index = 0;
		private static final int STEP = 1;
		private PointF lastPoint;

		private int[] drawables = new int[] { drawable._001, drawable._002,
				drawable._003, drawable._004, drawable._005 };

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			drawFrame();
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			super.onVisibilityChanged(visible);
			drawFrame();
		}

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
		}

		public void onTouchEvent(MotionEvent event) {
			int action = event.getAction();
			PointF point = new PointF(event.getX(), event.getY());
			if (lastPoint == null)
				lastPoint = point;
			switch (action) {
			case MotionEvent.ACTION_MOVE:
				if (lastPoint.x - point.x > -10) {
					goRight();
				} else if (lastPoint.x - point.x < 10) {
					goLeft();
				}
				lastPoint = point;
				break;

			default:
				break;
			}

		}

		private void goLeft() {
			index += STEP;
			if (index >= drawables.length)
				index = drawables.length - 1;
			drawFrame();
		}

		private void goRight() {
			index -= STEP;
			if (index < 0)
				index = 0;
			drawFrame();
		}

		private Bitmap getBitmapForCurrentIndex() {
			return BitmapFactory.decodeResource(getResources(),
					drawables[index]);
		}

		private void drawFrame() {

			Rect surfaceFrame = getSurfaceHolder().getSurfaceFrame();
			float surfaceHeight = (float) surfaceFrame.height();
			if (surfaceHeight == 0)
				return;
			Bitmap bitmapOfFrame = getBitmapForCurrentIndex();

			float scale = (float) bitmapOfFrame.getHeight() / surfaceHeight;

			float allowedWidth = ((float) bitmapOfFrame.getWidth()) / scale;
			float padding = 0;
			if (allowedWidth < surfaceFrame.width()) {
				padding = (surfaceFrame.width() - allowedWidth) / 2f;
			}
			Rect window = new Rect((int) padding, 0,
					(int) (allowedWidth + padding), surfaceFrame.height());
			drawBitmap(bitmapOfFrame, window);
		}

		private void drawBitmap(Bitmap bitmapOfFrame, Rect window) {
			Canvas c = null;
			final SurfaceHolder holder = getSurfaceHolder();
			try {
				c = holder.lockCanvas();
				if (c != null && bitmapOfFrame != null) {
					c.drawColor(Color.BLACK);
					c.drawBitmap(bitmapOfFrame, null, window, null);
				}
			} finally {
				if (c != null) {
					holder.unlockCanvasAndPost(c);

				}
			}
		}

		// private Bitmap getTestBM() {
		// int squareSize = 3000;
		// int step = (int) ((float) squareSize * .05f);
		// Bitmap createBitmap = Bitmap.createBitmap(squareSize, squareSize,
		// Config.ARGB_8888);
		// Canvas c = new Canvas(createBitmap);
		// c.drawColor(Color.WHITE);
		// Paint paint = new Paint();
		//
		// c.drawColor(Color.MAGENTA);
		// Rect r = new Rect(step, step, c.getWidth() - step, c.getHeight()
		// - step);
		// paint.setColor(Color.CYAN);
		// c.drawRect(r, paint);
		// paint.setTextSize(80);
		// paint.setColor(Color.WHITE);
		// c.drawText("" + squareSize, c.getWidth() / 2f, c.getHeight() / 2f,
		// paint);
		//
		// return createBitmap;
		// }
	}

}
