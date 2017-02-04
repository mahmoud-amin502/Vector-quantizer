package finlamm;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class decompress {

	ArrayList<block> decompressed_data = new ArrayList<block>();
	ArrayList<String> compressed_data = new ArrayList<String>();
	ArrayList<block> code_book_vector = new ArrayList<block>();
	// String[][] compressed_matrix = null;
	int block_rows = 0;
	int block_cols = 0;
	int height = 0;
	int width = 0;
	int[][] pixels;

	public void decompress() {
		for (int i = 0; i < compressed_data.size(); i++) {
			decompressed_data.add(code_book_vector.get(Integer
					.parseInt(compressed_data.get(i))));
		}

		fill_matrix();

	}

	public void fill_matrix() {

		int spliter = (width / code_book_vector.get(0).num_cols);
		int b_rows = code_book_vector.get(0).num_rows;
		int b_cols = code_book_vector.get(0).num_cols;
		block b_temp = null;
		int count1 = 0;
		int count2 = 1;
		int cuter = 0;
		int index = 0;

		pixels = new int[height][width];

		 System.out.println(spliter);

		for (int k = 0; k < spliter; k++) {
			for (int i = 0; i < height; i++) {
				if (cuter == 0) {
					b_temp = decompressed_data.get(index);
					index++;
				}
				for (int j = count1 * (width / spliter); j < count2 * (width / spliter); j++) {
					if (cuter == b_rows)
						cuter = 0;
					pixels[i][j] = b_temp.block[i % b_rows][j % b_cols]
							.intValue();
				}
				cuter++;
				if (cuter == b_rows && index < height * 2) {
					b_temp = decompressed_data.get(index);
					index++;
				}
			}
			count1++;
			count2++;
		}

		// System.out.println("c="+pixels[0].length);
		// System.out.println("r="+pixels.length);

		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < pixels[0].length; j++)
				System.out.print(pixels[i][j] + " ");
			System.out.println();
		}
	}

	public void writeImage(String outputFilePath) {
		File fileout = new File(outputFilePath);
		BufferedImage image2 = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image2.setRGB(x, y, (pixels[y][x] << 16) | (pixels[y][x] << 8)
						| (pixels[y][x]));
			}
		}
		try {
			ImageIO.write(image2, "jpg", fileout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadFROMfile(String f1) {

		try {
			int code_book_vector_size = 0;
			File file1 = new File(f1);
			InputStream fi1 = new FileInputStream(file1);
			ObjectInputStream in1 = new ObjectInputStream(fi1);
			DataInputStream in2 = new DataInputStream(fi1);
			
			code_book_vector_size = in2.readInt();
			block_rows = in2.readInt();
			block_cols = in2.readInt();
			height = in2.readInt();
			width = in2.readInt();
			
			for (int k = 0; k < code_book_vector_size; k++) {
				block temp = new block(block_rows,block_cols);
				for (int i = 0; i < block_rows; i++) {
					for (int j = 0; j < block_cols; j++) {
						temp.block[i][j] = in2.readDouble();
					}
				}
				code_book_vector.add(temp);
			}

			ArrayList<String> arrayInFile2 = (ArrayList<String>) in1.readObject();
			compressed_data = arrayInFile2;
			
			in1.close();
			in2.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
