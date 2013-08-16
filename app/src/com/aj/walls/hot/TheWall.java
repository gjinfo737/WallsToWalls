package com.aj.walls.hot;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

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

	public class GalleryEngine extends Engine implements OnSharedPreferenceChangeListener {
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
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			// TODO Auto-generated method stub

		}

		private void drawFrame() {

			Rect window = null;
			Rect surfaceFrame = getSurfaceHolder().getSurfaceFrame();
			Bitmap bitmapOfFrame = getTestBM();
			final SurfaceHolder holder = getSurfaceHolder();
			window = new Rect(0, 0, bitmapOfFrame.getWidth(), bitmapOfFrame.getHeight());

			// float scale = (float) bitmapOfFrame.getHeight() / (float)
			// surfaceFrame.height();

			Canvas c = null;
			try {
				c = holder.lockCanvas();
				if (c != null && bitmapOfFrame != null) {
					c.drawColor(Color.BLACK);
					c.drawBitmap(bitmapOfFrame, null, window, null);
				}
			} finally {
				if (c != null)
					holder.unlockCanvasAndPost(c);
			}

		}

		private Bitmap getTestBM() {
			Bitmap createBitmap = Bitmap.createBitmap(30, 30, Config.ARGB_8888);
			Canvas c = new Canvas(createBitmap);
			c.drawColor(Color.MAGENTA);
			return createBitmap;
		}
	}

}
