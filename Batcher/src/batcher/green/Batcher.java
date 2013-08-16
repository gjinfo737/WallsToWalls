package batcher.green;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Batcher {
	private final String BATCHTEST_TEST_PNG = "C:\\batchtest\\test.png";
	private static final int DELAY = 10;
	private static final int LEANEANCY = 190;
	private static final int makeColor = 0;
	private View view;
	private boolean stop = false;

	public enum BatchMode {
		FOLDER, SEQUENCE, TRIM
	}

	public Batcher() {
		view = new View(this);
		view.setVisible(true);
	}

	public void batch(BatchMode mode) {
		stop = false;
		BufferedImage targetImage = null;
		float[] target = new float[] { 15, 168, 120 };
		float count = 0;
		try {
			targetImage = ImageIO.read(new File(BATCHTEST_TEST_PNG));
			for (int x = 0; x < targetImage.getWidth(); x++) {
				for (int y = 0; y < targetImage.getHeight(); y++) {
					Color c = new Color(targetImage.getRGB(x, y), true);
					target[0] += c.getRed();
					target[1] += c.getGreen();
					target[2] += c.getBlue();
					count++;
				}
			}
			target[0] = target[0] / count;
			target[1] = target[1] / count;
			target[2] = target[2] / count;
			view.getTarget().setIcon(new ImageIcon(resize(targetImage, 75)));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		switch (mode) {
		case SEQUENCE:
			doSequence(target);
			break;
		case FOLDER:
			doFolder(target);
			break;
		case TRIM:
			trimFolder("");
			break;
		default:
			break;
		}
	}

	private void trimFolder(String path) {
		File folder = new File(path);
		if (!folder.exists() || !folder.isDirectory())
			throw new IllegalArgumentException("must be directory");
		File[] listFiles = folder.listFiles();
		for (int i = 0; i < listFiles.length; i++) {
			File f = listFiles[i];
			if (f.isFile() && !f.isDirectory()) {
				if (stop)
					break;
				System.out.println("trimming: " + i);
				view.setInfo("trimming: " + i + ": ");
				if (f.exists() && f.canWrite())
					trimSides(f);
			}
		}

	}

	private void doSequence(float[] target) {
		String path = "";
		String name = "candace645";
		doBySequence(target, path, name, ".png");
	}

	private void doFolder(float[] target) {
		String path = "";
		doByFolder(target, path);
	}

	private void doByFolder(float[] target, String path) {
		File folder = new File(path);
		File[] listFiles = folder.listFiles();
		for (int i = 0; i < listFiles.length; i++) {
			File f = listFiles[i];
			if (f.isFile() && !f.isDirectory()) {
				if (stop)
					break;
				System.out.println(i);
				view.setInfo(i + ": ");
				if (f.exists() && f.canWrite())
					cleanImageFile(target, f);
			}
		}

	}

	private void doBySequence(float[] target, String path, String name,
			String extension) {

		for (int i = 0; i < 300; i++) {
			if (stop)
				break;
			System.out.println(i);
			view.setInfo(i + ": ");
			File f = new File(path + File.separator + name + intToString(i, 4)
					+ extension);
			if (f.exists() && f.canWrite())
				cleanImageFile(target, f);
		}
	}

	static String intToString(int num, int digits) {
		assert digits > 0 : "Invalid number of digits";

		// create variable length array of zeros
		char[] zeros = new char[digits];
		Arrays.fill(zeros, '0');
		// format number as String
		DecimalFormat df = new DecimalFormat(String.valueOf(zeros));

		return df.format(num);
	}

	private void trimSides(File changeFile) {
		BufferedImage changeImage = null;
		int padding = 10;
		try {
			view.appendInfo(changeFile.getAbsolutePath());
			try {
				changeImage = ImageIO.read(changeFile);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			view.getOriginal().setIcon(new ImageIcon(resize(changeImage, 200)));
			int x1 = 0;
			int x2 = 0;
			for (int x = 0; x < changeImage.getWidth(); x++) {
				int imageIntersectCount = 0;
				boolean intersect = false;
				for (int y = 0; y < changeImage.getHeight(); y++) {
					Color c = new Color(changeImage.getRGB(x, y), true);
					if (c.getAlpha() != 0) {
						imageIntersectCount++;
						if (imageIntersectCount > changeImage.getHeight() / 12f) {
							intersect = true;
							break;
						}
					}
				}
				if (intersect) {
					x1 = x - padding;
					break;
				}
			}
			for (int x = x1 + padding + 30; x < changeImage.getWidth(); x++) {
				int missount = 0;
				boolean intersect = true;
				for (int y = 0; y < changeImage.getHeight(); y++) {
					Color c = new Color(changeImage.getRGB(x, y), true);
					if (c.getAlpha() != 255) {
						missount++;
						if (missount >= changeImage.getHeight()) {
							intersect = false;
							break;
						}
					}
				}
				if (!intersect) {
					x2 = x + padding;
					break;
				}
			}

			// for (int x = 0; x < x1; x++) {
			// for (int y = 0; y < changeImage.getHeight(); y++) {
			// changeImage.setRGB(x, y, Color.MAGENTA.getRGB());
			// }
			// }
			// for (int x = x2; x < changeImage.getWidth(); x++) {
			// for (int y = 0; y < changeImage.getHeight(); y++) {
			// changeImage.setRGB(x, y, Color.CYAN.getRGB());
			// }
			// }

			BufferedImage newImage = new BufferedImage(x2 - x1,
					changeImage.getHeight(), changeImage.getType());
			Graphics2D newg2d = newImage.createGraphics();
			newg2d.drawImage(changeImage, null, -x1, 0);
			view.getResult().setText("");
			view.getResult().setIcon(new ImageIcon(resize(newImage, 200)));
			save(newImage, changeFile);
			Thread.sleep(DELAY);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void cleanImageFile(float[] target, File changeFile) {
		BufferedImage changeImage = null;
		try {
			view.appendInfo(changeFile.getAbsolutePath());
			try {
				changeImage = ImageIO.read(changeFile);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			view.getOriginal().setIcon(new ImageIcon(resize(changeImage, 200)));

			System.out.println(String.format("%s %s %s", target[0], target[1],
					target[2]));
			for (int x = 0; x < changeImage.getWidth(); x++) {
				for (int y = 0; y < changeImage.getHeight(); y++) {
					Color c = new Color(changeImage.getRGB(x, y), true);
					float red = c.getRed();
					float green = c.getGreen();
					float blue = c.getBlue();

					int[] distance = new int[] {
							(int) Math.abs(target[0] - red),
							(int) Math.abs(target[1] - green),
							(int) Math.abs(target[2] - blue) };

					if (isGreen(distance, c)) {
						changeImage.setRGB(x, y, makeColor);
					}
				}

			}
			view.getResult().setText("");
			view.getResult().setIcon(new ImageIcon(resize(changeImage, 200)));

			save(changeImage, changeFile);
			Thread.sleep(DELAY);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static boolean isGreen(int[] distance, Color c) {
		int total = c.getRed() + c.getGreen() + c.getBlue();
		return inRange(distance) && c.getGreen() > c.getRed()
				&& c.getGreen() > c.getBlue() && c.getBlue() > c.getRed()
				&& total < (240 * 3);
	}

	private static boolean inRange(int[] distance) {
		return distance[0] < LEANEANCY && distance[1] < LEANEANCY
				&& distance[2] < LEANEANCY;
	}

	private static void save(BufferedImage image, File file) {
		try {
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static BufferedImage resize(BufferedImage image, int width) {
		double scale = (double) width / (double) image.getWidth();
		int height = (int) (scale * image.getHeight());
		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TRANSLUCENT);
		Graphics2D g2d = (Graphics2D) bi.createGraphics();
		g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY));
		g2d.drawImage(image, 0, 0, width, height, null);
		g2d.dispose();
		return bi;
	}

	public void onStop() {
		stop = true;
	}
}
