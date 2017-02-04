package finlamm;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class compress {
	ArrayList<level> levels = new ArrayList<level>();
	ArrayList<block> basic_data = new ArrayList<block>();
	ArrayList<String> compressed_data = new ArrayList<String>();
	ArrayList<block> code_book_vector = new ArrayList<block>();

	int block_rows = 0;
	int block_cols = 0;
	int height = 0;
	int width = 0;
	int bits = 0;

	public void compress() {

		level level1 = new level();
		node node = new node();

		node.data = basic_data;
		node.average = get_average(basic_data);
		level1.level.add(node);
		levels.add(level1);

		for (int i = 0; i < bits; i++) {
			ArrayList<node> association_result = new ArrayList<node>();
			level level2__ = new level();
			for (int j = 0; j < levels.get(i).level.size(); j++) {
				association_result = associate(levels.get(i).level.get(j));
				level2__.level.add(association_result.get(0));
				level2__.level.add(association_result.get(1));
			}
			levels.add(level2__);
		}

		for (int i = 0; i < levels.get(levels.size() - 1).level.size(); i++) {
			block b_temp = levels.get(levels.size() - 1).level.get(i).average;
			for (int j = 0; j < block_rows; j++)
				for (int k = 0; k < block_cols; k++) {
					if (b_temp.block[j][k] % 1 >= 0.5)
						b_temp.block[j][k] = (double) b_temp.block[j][k]
								.intValue() + 1;
					else if (b_temp.block[j][k] % 1 < 0.5)
						b_temp.block[j][k] = (double) b_temp.block[j][k]
								.intValue();
				}
			code_book_vector.add(b_temp);
		}

		int index = 0;
		for (int i = 0; i < basic_data.size(); i++) {
			index = 0;
			block most_neer = code_book_vector.get(0);
			for (int j = 0; j < code_book_vector.size(); j++) {
				if (cledian_distance(basic_data.get(i), code_book_vector.get(j)) <= (cledian_distance(
						basic_data.get(i), most_neer))) {
					most_neer = code_book_vector.get(j);
					index = j;
				}
			}
			compressed_data.add(index + "");
		}

	}

	public int get_numOFcompinations(int bits) {
		int numOFcompinations = 1;
		for (int i = 0; i < bits; i++)
			numOFcompinations *= 2;
		return numOFcompinations;
	}

	public ArrayList<node> associate(node node) {

		ArrayList<block> less = new ArrayList<block>();
		ArrayList<block> great = new ArrayList<block>();
		ArrayList<node> x = new ArrayList<node>();
		node n_temp1 = new node();
		node n_temp2 = new node();

		block[] s_f = spliting(node.average);

		for (int i = 0; i < node.data.size(); i++) {
			if (cledian_distance(node.data.get(i), s_f[0]) <= cledian_distance(
					node.data.get(i), s_f[1])) {
				less.add(node.data.get(i));
			} else if (cledian_distance(node.data.get(i), s_f[0]) > cledian_distance(
					node.data.get(i), s_f[1])) {
				great.add(node.data.get(i));
			} else
				System.out.println("There is Something Wrong!!");

		}
		n_temp1.data = less;
		n_temp1.average = get_average(less);
		x.add(n_temp1);

		n_temp2.data = great;
		n_temp2.average = get_average(great);
		x.add(n_temp2);

		return x;
	}

	public int cledian_distance(block x, block y) {
		int sum = 0;
		for (int i = 0; i < block_rows; i++)
			for (int j = 0; j < block_cols; j++) {
				sum += (x.block[i][j] - y.block[i][j])
						* (x.block[i][j] - y.block[i][j]);
			}
		return sum;
	}

	public block[] spliting(block AVG) {
		block[] x = { null, null };
		block sell = new block(block_rows, block_cols);
		block floor = new block(block_rows, block_cols);

		for (int i = 0; i < block_rows; i++)
			for (int j = 0; j < block_cols; j++) {

				if (AVG.block[i][j] % 1 == 0) {
					sell.block[i][j] = (double) AVG.block[i][j].intValue() + 1;
					floor.block[i][j] = (double) AVG.block[i][j].intValue() - 1;
				} else {
					sell.block[i][j] = (double) AVG.block[i][j].intValue() + 1;
					floor.block[i][j] = (double) AVG.block[i][j].intValue();
				}
			}
		x[0] = floor;
		x[1] = sell;
		return x;
	}

	public block get_average(ArrayList<block> temp) {

		block sum = new block(block_rows, block_cols);

		for (int i = 0; i < temp.size(); i++) {
			for (int j = 0; j < block_rows; j++)
				for (int k = 0; k < block_cols; k++) {
					sum.block[j][k] += temp.get(i).block[j][k];
				}
		}
		for (int j = 0; j < block_rows; j++)
			for (int k = 0; k < block_cols; k++)
				sum.block[j][k] = sum.block[j][k] / (double) temp.size();
		return sum;
	}

	public void Print_levels() {
		for (int i = 0; i < levels.size(); i++) {
			for (int j = 0; j < levels.get(i).level.size(); j++) {
				for (int k = 0; k < levels.get(i).level.get(j).data.size(); k++) {
					levels.get(i).level.get(j).data.get(k).print_block();
				}
				System.out.println("-----------------------------------");
			}
			System.out.println("===================================");
		}
	}

	
	
	public void bading(int[][] pixels) {
		int pixels_r = height;
		int pixels_c = width;
		int[][] pixels2 = null;
		int pixels2_r = 0;
		int pixels2_c = 0;
		boolean change = false;

		if (pixels_c % block_cols != 0) {
			pixels2_r = pixels_r;
			pixels2_c = pixels_c + (block_cols - (pixels_c % block_cols));
			pixels2 = new int[pixels2_r][pixels2_c];
			for (int i = 0; i < pixels2_r; i++)
				for (int j = 0; j < pixels2_c; j++)
					pixels2[i][j] = 0;
			change = true;
		}

		if (pixels_r % block_rows != 0) {
			pixels2_r = pixels_r + (block_rows - (pixels_r % block_rows));
			if (change != true)
				pixels2_c = pixels_c;
			pixels2 = new int[pixels2_r][pixels2_c];
			for (int i = 0; i < pixels2_r; i++)
				for (int j = 0; j < pixels2_c; j++)
					pixels2[i][j] = 0;
			change = true;
		}

		if (change == true) {
			for (int i = 0; i < pixels_r; i++) {
				for (int j = 0; j < pixels_c; j++) {
					pixels2[i][j] = pixels[i][j];
				}
			}
			get_blocks(pixels2, pixels2_r, pixels2_c);
		} else {
			get_blocks(pixels, pixels_r, pixels_c);
		}
	}
	
	
	
	

	public void get_blocks(int[][] pixels, int pixels_r, int pixels_c) {

		int spliter = (pixels_c / block_cols);
		block b_temp = null;
		int count1 = 0;
		int count2 = 1;
		int cuter = 0;

		for (int k = 0; k < spliter; k++) {
			for (int i = 0; i < pixels_r; i++) {
				if (cuter == 0)
					b_temp = new block(block_rows, block_cols);
				for (int j = count1 * (pixels_c / spliter); j < count2
						* (pixels_c / spliter); j++) {
					if (cuter == block_rows)
						cuter = 0;
					b_temp.block[i % block_rows][j % block_cols] = (double) pixels[i][j];
				}
				cuter++;
				if (cuter == block_rows) {
					basic_data.add(b_temp);
					b_temp = new block(block_rows, block_cols);
				}
			}
			count1++;
			count2++;
		}
	}

	public void readImage(String filePath) {
		File file = new File(filePath);
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		width = image.getWidth();
		height = image.getHeight();
		int[][] pixels = new int[height][width];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int rgb = image.getRGB(x, y);
				int r = (rgb >> 16) & 0xff;
				pixels[y][x] = r;
			}
		}

		bading(pixels);

	}

	public void saveATfile(String f1) {

		try {
			File file1 = new File(f1);
			OutputStream fo1 = new FileOutputStream(file1);
			ObjectOutputStream out1 = new ObjectOutputStream(fo1);
			DataOutputStream out2 = new DataOutputStream(fo1);

			out2.writeInt(code_book_vector.size());
			out2.writeInt(block_rows);
			out2.writeInt(block_cols);
			out2.writeInt(height);
			out2.writeInt(width);
			
			for (int k = 0; k < code_book_vector.size(); k++) {
				code_book_vector.get(k);
				for (int i = 0; i < block_rows; i++) {
					for (int j = 0; j < block_cols; j++) {
						out2.writeDouble(code_book_vector.get(k).block[i][j]);
					}
				}
			}
			
			out1.writeObject(compressed_data);

			out1.close();
			out2.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
