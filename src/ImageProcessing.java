/**
 * This application modifies images and creates new images using 2D arrays.
 * 
 * Images can be stretched horizontally, shrunk vertically, negate color, apply color filter and invert the image. 
 * New images can be created consisting of random pixels, placing a rectangle in the image, and using the method 
 * to randomly place many rectangles in the image. 
 * 
 * @author Sungbin Kang
 */

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;
public class ImageProcessing {
	public static void main(String[] args) {

		// The provided images are apple.jpg, flower.jpg, and kitten.jpg

		int[][] imageData = imgToTwoD("source/apple.jpeg");
		viewImageData(imageData);

		int[][] trimmed = trimBorders(imageData, 60);
		twoDToImage(trimmed, "source/trimmed_apple.jpg");

		int[][] nagatived = negativeColor(imageData);
		twoDToImage(nagatived, "source/negatived_apple.jpg");

		int[][] stretched = stretchHorizontally(imageData);
		twoDToImage(stretched, "source/stretched_apple.jpg");

		int[][] shrunk = shrinkVertically(imageData);
		twoDToImage(shrunk, "source/shrunk_apple.jpg");

		int[][] inverted = invertImage(imageData);
		twoDToImage(inverted, "source/inverted_apple.jpg");

		int[][] filtered = colorFilter(imageData, -75, 30, -30);
		twoDToImage(filtered, "source/filtered_apple.jpg");

		int[][] randomed = paintRandomImage(new int[500][500]);
		twoDToImage(randomed, "source/randomed_apple.jpg");

		int[] rgba = {255, 255, 0, 255};
		int[][] rectangleImg = paintRectangle(randomed, 200, 200, 100, 100, getColorIntValFromRGBA(rgba));
		twoDToImage(rectangleImg, "source/rectangle.jpg");

		int[][] rectangles = generateRectangles(new int[500][500], 1000);
		twoDToImage(rectangles, "source/rectangles_apple.jpg");

	}


	// --------------------------------- Image Processing Methods  ---------------------------------

	public static int[][] trimBorders(int[][] imageTwoD, int pixelCount) {
		// Example Method
		if (imageTwoD.length > pixelCount * 2 && imageTwoD[0].length > pixelCount * 2) {
			int[][] trimmedImg = new int[imageTwoD.length - pixelCount * 2][imageTwoD[0].length - pixelCount * 2];
			for (int i = 0; i < trimmedImg.length; i++) {
				for (int j = 0; j < trimmedImg[i].length; j++) {
					trimmedImg[i][j] = imageTwoD[i + pixelCount][j + pixelCount];
				}
			}
			return trimmedImg;
		} else {
			System.out.println("Cannot trim that many pixels from the given image.");
			return imageTwoD;
		}
	}


	public static int[][] negativeColor(int[][] imageTwoD) {
		int[][] newImage = new int[imageTwoD.length][imageTwoD[0].length];

		for (int i = 0; i < newImage.length; i++) {
			for (int j = 0; j < newImage[0].length; j++) {
				int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);

				for (int k = 0; k < 3; k++) {
					rgba[k] = 255 - rgba[k];
				}

				newImage[i][j] = getColorIntValFromRGBA(rgba);

			}
		}

		return newImage;
	}


	public static int[][] stretchHorizontally(int[][] imageTwoD) {

		int[][] newImage = new int[imageTwoD.length][imageTwoD[0].length * 2];

		for (int i = 0; i < imageTwoD.length; i++) {
			for (int j = 0; j < imageTwoD[0].length; j++) {
				int it = j * 2;
				newImage[i][it] = imageTwoD[i][j];
				newImage[i][it+1] = imageTwoD[i][j];
			}
		}

		return newImage;
	}


	public static int[][] shrinkVertically(int[][] imageTwoD) {
		int[][] newImage = new int[imageTwoD.length / 2][imageTwoD[0].length];

		for (int i = 0; i < newImage.length; i++) {
			for (int j = 0; j < newImage[0].length; j++) {
				newImage[i][j] = imageTwoD[i*2][j];
			}
		}

		return newImage;
	}


	public static int[][] invertImage(int[][] imageTwoD) {
		int[][] newImage = new int[imageTwoD.length][imageTwoD[0].length];

		for (int i = 0; i < imageTwoD.length; i++) {
			for (int j = 0; j < imageTwoD[0].length; j++) {
				newImage[i][j] = imageTwoD[imageTwoD.length-1-i][imageTwoD[0].length-1-j];
			}
		}

		return newImage;
	}


	public static int[][] colorFilter(int[][] imageTwoD, int redChangeValue, int greenChangeValue, int blueChangeValue) {
		int[][] newImage = new int[imageTwoD.length][imageTwoD[0].length];

		for (int i = 0; i < newImage.length; i++) {
			for (int j = 0; j < newImage[0].length; j++) {
				int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);

				int newRed = rgba[0] + redChangeValue;
				int newGreen = rgba[1] + greenChangeValue;
				int newBlue = rgba[2] + blueChangeValue;

				if (newRed > 255) {
					newRed = 255;
				} else if (newRed < 0 ) {
					newRed = 0;
				}

				if (newGreen > 255) {
					newGreen = 255;
				} else if (newGreen < 0 ) {
					newGreen = 0;
				}

				if (newBlue > 255) {
					newBlue = 255;
				} else if (newBlue < 0 ) {
					newBlue = 0;
				}

				rgba[0] = newRed;
				rgba[1] = newGreen;
				rgba[2] = newBlue;

				newImage[i][j] = getColorIntValFromRGBA(rgba);

			}
		}

		return newImage;
	}


	//  --------------------------------- Painting Methods  ---------------------------------

	public static int[][] paintRandomImage(int[][] canvas) {

		Random rand = new Random();

		for (int i = 0; i < canvas.length; i++) {
			for (int j = 0; j < canvas[0].length; j++) {
				int randRed = rand.nextInt(256);
				int randGreen = rand.nextInt(256);
				int randBlue = rand.nextInt(256);

				int[] rgba = {randRed, randGreen, randBlue, 255};

				canvas[i][j] = getColorIntValFromRGBA(rgba);

			}
		}

		return canvas;
	}


	public static int[][] paintRectangle(int[][] canvas, int width, int height, int rowPosition, int colPosition, int color) {

		for (int i = 0; i < canvas.length; i++) {
			for (int j = 0; j < canvas[0].length; j++) {
				if (i >= rowPosition && i <= canvas.length+rowPosition && j >= colPosition && j <= canvas[0].length+colPosition) {
					canvas[i][j] = color;
				} 
			}
		}

		return canvas;
	}


	public static int[][] generateRectangles(int[][] canvas, int numRectangles) {
		Random rand = new Random();

		for (int rectangle = 0; rectangle < numRectangles; rectangle++) {
			int width = rand.nextInt(canvas[0].length);
			int height = rand.nextInt(canvas.length);

			int rowPos = rand.nextInt(canvas.length);
			int colPos = rand.nextInt(canvas[0].length);

			int[] rgba = {rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)};
			int color = getColorIntValFromRGBA(rgba);

			paintRectangle(canvas, width, height, rowPos, colPos, color); 
		}

		return canvas;
	}


	//  --------------------------------- Utility Methods  ---------------------------------

	public static int[][] imgToTwoD(String inputFileOrLink) {
		try {
			BufferedImage image = null;
			if (inputFileOrLink.substring(0, 4).toLowerCase().equals("http")) {
				URL imageUrl = new URL(inputFileOrLink);
				image = ImageIO.read(imageUrl);
				if (image == null) {
					System.out.println("Failed to get image from provided URL.");
				}
			} else {
				image = ImageIO.read(new File(inputFileOrLink));
			}
			int imgRows = image.getHeight();
			int imgCols = image.getWidth();
			int[][] pixelData = new int[imgRows][imgCols];
			for (int i = 0; i < imgRows; i++) {
				for (int j = 0; j < imgCols; j++) {
					pixelData[i][j] = image.getRGB(j, i);
				}
			}
			return pixelData;
		} catch (Exception e) {
			System.out.println("Failed to load image: " + e.getLocalizedMessage());
			return null;
		}
	}


	public static void twoDToImage(int[][] imgData, String fileName) {
		try {
			int imgRows = imgData.length;
			int imgCols = imgData[0].length;
			BufferedImage result = new BufferedImage(imgCols, imgRows, BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < imgRows; i++) {
				for (int j = 0; j < imgCols; j++) {
					result.setRGB(j, i, imgData[i][j]);
				}
			}
			File output = new File(fileName);
			ImageIO.write(result, "jpg", output);
		} catch (Exception e) {
			System.out.println("Failed to save image: " + e.getLocalizedMessage());
		}
	}


	public static int[] getRGBAFromPixel(int pixelColorValue) {
		Color pixelColor = new Color(pixelColorValue);
		return new int[] { pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), pixelColor.getAlpha() };
	}


	public static int getColorIntValFromRGBA(int[] colorData) {
		if (colorData.length == 4) {
			Color color = new Color(colorData[0], colorData[1], colorData[2], colorData[3]);
			return color.getRGB();
		} else {
			System.out.println("Incorrect number of elements in RGBA array.");
			return -1;
		}
	}


	public static void viewImageData(int[][] imageTwoD) {
		if (imageTwoD.length > 3 && imageTwoD[0].length > 3) {
			int[][] rawPixels = new int[3][3];
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					rawPixels[i][j] = imageTwoD[i][j];
				}
			}
			System.out.println("Raw pixel data from the top left corner.");
			System.out.print(Arrays.deepToString(rawPixels).replace("],", "],\n") + "\n");
			int[][][] rgbPixels = new int[3][3][4];
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					rgbPixels[i][j] = getRGBAFromPixel(imageTwoD[i][j]);
				}
			}
			System.out.println();
			System.out.println("Extracted RGBA pixel data from top the left corner.");
			for (int[][] row : rgbPixels) {
				System.out.print(Arrays.deepToString(row) + System.lineSeparator());
			}
		} else {
			System.out.println("The image is not large enough to extract 9 pixels from the top left corner");
		}
	}
}