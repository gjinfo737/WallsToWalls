package com.aj.walls.hot;

import java.util.Random;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

			float scale = (float) bitmapOfFrame.getHeight() / (float) surfaceFrame.height();

			float allowedWidth = scale * ((float) bitmapOfFrame.getWidth());
			int sidePadding = (int) (allowedWidth / 2f);

			window = new Rect(0, 0, bitmapOfFrame.getWidth(), bitmapOfFrame.getHeight());
			Canvas c = null;
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

		private Bitmap getTestBM() {
			int squareSize = 300;
			int step = (int) ((float) squareSize * .1f);
			Bitmap createBitmap = Bitmap.createBitmap(squareSize, squareSize, Config.ARGB_8888);
			Canvas c = new Canvas(createBitmap);
			c.drawColor(Color.WHITE);
			Paint paint = new Paint();
			int[] colors = new int[] { Color.MAGENTA, Color.RED, Color.CYAN, Color.YELLOW, Color.DKGRAY, Color.BLUE, Color.BLACK, Color.WHITE };
			Random rand = new Random();
			for (int x = 0; x < createBitmap.getWidth(); x += step) {
				for (int y = 0; y < createBitmap.getHeight(); y += step) {
					int color = colors[rand.nextInt(colors.length)];
					paint.setColor(color);
					Rect r = new Rect(x, y, x + step, y + step);
					c.drawRect(r, paint);
				}
			}
			return createBitmap;
		}
	}

}
