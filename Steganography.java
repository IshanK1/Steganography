
import java.awt.Color;

//import javax.swing.text.html.HTMLDocument.HTMLReader.HiddenAction;

public class Steganography {
    public static void main(String[] args) {

        Picture beach2 = new Picture(
                "/Users/ritutrakru/Downloads/CSA_383_StegoLab_StudentFiles (1)/StegoLab_Code/beach.jpg");
        // beach2.explore();
        // Picture copy2 = testSetLow(beach2, Color.PINK);
        // copy2.explore();

        // Picture copy3 = revealPicture(copy2);
        // copy3.explore();
        Picture arch = new Picture(
                "/Users/ritutrakru/Downloads/CSA_383_StegoLab_StudentFiles (1)/StegoLab_Code/arch.jpg");

        // if (canHide(beach2, arch) == true) {
        // System.out.println("Secret can be hidden in source");
        // } else {
        // System.out.println("Secret cannot be hidden in source");
        // }

        // Picture combinedPicture = hidePicture(beach2, arch);
        // combinedPicture.explore();

        // Picture secret = revealPicture(combinedPicture);
        // secret.explore();

        if (canHide(beach2, arch)) {
            Picture combinedPicture = hidePicture(beach2, arch);
            combinedPicture.explore();

            Picture pixelatedSecret = revealPicture(combinedPicture);
            pixelatedSecret.explore();
        }
    }

    public static void clearLow(Pixel p) {
        int red = p.getRed();
        int blue = p.getBlue();
        int green = p.getGreen();

        p.setRed((red / 4) * 4);
        p.setBlue((blue / 4) * 4);
        p.setGreen((green / 4) * 4);

    }

    public static Picture testClearLow(Picture picture) {
        Pixel[] pixels = picture.getPixels();
        for (Pixel p : pixels) {
            clearLow(p);
        }
        return picture;
    }

    public static void setLow(Pixel p, Color c) {
        clearLow(p);
        // Fix the Red
        int redFromPixel = p.getRed();
        int redFromColor = c.getRed() / 64;
        int newRed = redFromPixel + redFromColor;
        p.setRed(newRed);

        // Fix the Blue
        int blueFromPixel = p.getBlue();
        int blueFromColor = c.getBlue() / 64;
        int newBlue = blueFromPixel + blueFromColor;
        p.setBlue(newBlue);

        // Fix the Green
        int greenFromPixel = p.getGreen();
        int greenFromColor = c.getGreen() / 64;
        int newGreen = greenFromPixel + greenFromColor;
        p.setGreen(newGreen);

    }

    public static Picture testSetLow(Picture picture, Color c) {
        Pixel[] pixels = picture.getPixels();
        for (Pixel p : pixels) {
            setLow(p, c);
        }
        return picture;
    }

    /**
     * Sets the highest two bits of each pixel's color
     * to the lowest two bits of each pixel's color
     */
    public static Picture revealPicture(Picture hidden) {
        Picture copy = new Picture(hidden);
        Pixel[][] pixels = copy.getPixels2D();
        Pixel[][] source = hidden.getPixels2D();
        for (int r = 0; r < pixels.length; r++) {
            for (int c = 0; c < pixels[0].length; c++) {

                // get color of source pixel
                Color color = source[r][c].getColor();

                int lastTwoRedBits = color.getRed() - (color.getRed() / 4) * 4;
                int lastTwoGreenBits = color.getGreen() - (color.getGreen() / 4) * 4;
                int lastTwoBlueBits = color.getBlue() - (color.getBlue() / 4) * 4;

                pixels[r][c].setRed(lastTwoRedBits * 64);
                pixels[r][c].setGreen(lastTwoGreenBits * 64);
                pixels[r][c].setBlue(lastTwoBlueBits * 64);

            }
        }
        return copy;
    }

    public static boolean canHide(Picture source, Picture secret) {
        if (source.getHeight() >= secret.getHeight() && source.getWidth() >= secret.getWidth()) {
            return true;
        }
        return false;
    }

    public static Picture hidePicture(Picture source, Picture secret) {
        if (!canHide(source, secret)) {
            System.out.println("Secret cannot be hidden in source");
            return null;
        }

        // Make a copy of the given source picture
        Picture copy = new Picture(source);

        // Get Pixels of copy as well as secret
        Pixel[][] copyPixels = copy.getPixels2D();
        Pixel[][] secretPixels = secret.getPixels2D();

        // Iterate over dimensions of the secret
        for (int r = 0; r < secretPixels.length; r++) {
            for (int c = 0; c < secretPixels[0].length; c++) {
                setLow(copyPixels[r][c], secretPixels[r][c].getColor());
            }
        }
        return copy;
    }

    public static Picture hidePicture(Picture source, Picture secret,
            int startRow, int startColumn)

    {
        Picture hidden = new Picture(source);
        Pixel[][] hiddenPixels = hidden.getPixels2D();
        Pixel[][] secretPixels = secret.getPixels2D();
        int width = secretPixels[0].length;
        int height = secretPixels.length;
        for (int r = startRow, srow = 0; r < hiddenPixels.length && srow < height; r++, srow++) {
            for (int c = startColumn, scol = 0; c < hiddenPixels[0].length - 1 &&
                    scol < width; c++, scol++) {
                Pixel s = secretPixels[srow][scol];
                setLow(hiddenPixels[r][c], s.getColor());
            }
        }
        return hidden;
    }

    public static boolean isSame(Picture first, Picture second) {
        Pixel[][] firstPixels = first.getPixels2D();
        Pixel[][] secondPixels = second.getPixels2D();
        Pixel firstPixel = null;
        Pixel secondPixel = null;

        // return false if dimensions do not match
        if (first.getWidth() != second.getWidth() ||
                first.getHeight() != second.getHeight()) {
            return false;
        }

        // iterate through all pixels and make sure the colors match
        for (int row = 0; row < firstPixels.length; row++) {
            for (int col = 0; col < firstPixels[0].length; col++) {
                firstPixel = firstPixels[row][col];
                secondPixel = secondPixels[row][col];
                if (firstPixel.getRed() != secondPixel.getRed() ||
                        firstPixel.getGreen() != secondPixel.getGreen() ||
                        firstPixel.getBlue() != secondPixel.getBlue()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static ArrayList<Point> findDifferences(Picture first,
            Picture second)

    {
        ArrayList<Point> list = new ArrayList<Point>();
        Pixel[][] pixels = first.getPixels2D();
        Pixel[][] otherPixels = second.getPixels2D();
        Pixel pixel = null;
        Pixel otherPixel = null;

        // if dimensions do not match, return empty list
        if (first.getWidth() != second.getWidth() ||
                first.getHeight() != second.getHeight()) {
            return list;
        }

        // iterate through all pixels and record differences in arryalist
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[0].length; col++) {
                pixel = pixels[row][col];
                otherPixel = otherPixels[row][col];
                if (pixel.getRed() != otherPixel.getRed()
                        || pixel.getGreen() != otherPixel.getGreen()
                        || pixel.getBlue() != otherPixel.getBlue()) {
                    list.add(new Point(row, col));
                }
            }
        }
        return list;
    }

    public static Picture showDifferentArea(Picture pic,
            ArrayList<Point> points)

    {
        Picture result = new Picture(pic);

        int minRow = pic.getHeight() - 1, minCol = pic.getWidth() - 1;
        int maxRow = 0, maxCol = 0;

        for (Point p : points) {
            int row = p.getRow();
            int col = p.getCol();
            if (row < minRow) {
                minRow = row;
            }
            if (row > maxRow) {
                maxRow = row;
            }
            if (col < minCol) {
                minCol = col;
            }
            if (col > maxCol) {
                maxCol = col;
            }
        }
        Pixel pixel = null;
 
        for (int col = minCol; col <= maxCol; col++) {
            pixel = result.getPixel(col, minRow);
            pixel.setColor(Color.red);
            pixel = result.getPixel(col, maxRow);
            pixel.setColor(Color.red);
        }
        for (int row = minRow + 1; row < maxRow; row++) {
            pixel = result.getPixel(minCol, row);
            pixel.setColor(Color.red);
            pixel = result.getPixel(maxCol, row);
            pixel.setColor(Color.red);
        }
        return result;
    }
}
