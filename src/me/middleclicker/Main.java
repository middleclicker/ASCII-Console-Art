package me.middleclicker;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedImage image = ImageIO.read(new File("cb.jpg"));
        System.out.println("Finished loading the image.");

        File output = new File("output.txt");
        if (output.createNewFile()) {
            System.out.println("File created: " + output.getName());
        } else {
            System.out.println("File already exists.");
        }

        int pixelSize = 1;

        char[][] charTable = new char[image.getHeight()][image.getWidth()];
        int[][][] rgbTable = new int[image.getHeight()][image.getWidth()][3];
        ArrayList<Double> charMappings = new ArrayList<>();

        String characters = "`^\\\",:;Il!i~+_-?][}{1)(|\\\\/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$";
        //                   `^ \ ",:;Il!i~+_-?][}{1)(| \ \/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$;

        System.out.println("Using the following characters: " + characters);
        System.out.println("Length: " + characters.toCharArray().length);

        double chunkSize = 255.0 / characters.length();
        for (int i = 0; i < characters.length(); i++) {
            charMappings.add(i*chunkSize);
            // System.out.println(i + " * " + chunkSize + " = " + i*chunkSize);
            System.out.println("Mapped " + characters.charAt(i) + " to " + i*chunkSize);
        }

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int  clr   = image.getRGB(x, y);
                int  R   = (clr & 0x00ff0000) >> 16;
                int  G = (clr & 0x0000ff00) >> 8;
                int  B  =  clr & 0x000000ff;

                double target = 0.21 * R + 0.72 * G + 0.07 * B;
                charTable[y][x] = characters.charAt(findClosest(target, charMappings));
                rgbTable[y][x][0] = R;
                rgbTable[y][x][1] = G;
                rgbTable[y][x][2] = B;
            }
        }

        System.out.println("Constructed lightness table.");

        System.out.println("Saving image");
        FileWriter filewriter = new FileWriter(output);
        for (int y = 0; y < image.getHeight(); y+=pixelSize) {
            for (int x = 0; x < image.getWidth(); x+=pixelSize) {
                filewriter.write(charTable[y][x]);
                filewriter.write(charTable[y][x]);
                System.out.print("\033[38;2;" + rgbTable[y][x][0] + ";" + rgbTable[y][x][1] + ";" + rgbTable[y][x][2] + "m" + charTable[y][x]);
                System.out.print("\033[38;2;" + rgbTable[y][x][0] + ";" + rgbTable[y][x][1] + ";" + rgbTable[y][x][2] + "m" + charTable[y][x]);
            }
            filewriter.write("\n");
            System.out.println();
        }
    }

    public static int findClosest(double target, ArrayList<Double> list) {
        int n = list.size();

        if (target <= list.get(0)) { // Smaller than smallest element
            return 0;
        }
        if (target >= list.get(n-1)) { // Larger than largest element
            return n-1;
        }

        int i = 0, j = n, mid = 0;
        while (i < j) {
            mid = (i + j) / 2;
            if (list.get(mid) == target) {
                return mid;
            }

            if (target < list.get(mid)) {
                if (mid > 0 && target > list.get(mid-1)) {
                    return getClosest(list, mid-1, mid, target);
                }
                j = mid;
            } else {
                if (mid > 0 && target > list.get(mid-1)) {
                    return getClosest(list, mid - 1, mid, target);
                }
                j = mid;
            }
        }

        return mid;
    }

    public static int getClosest(ArrayList<Double> list, int index1, int index2, double target) {
        if (target - list.get(index1) >= list.get(index2) - target)
            return index2;
        else
            return index1;
    }

}
