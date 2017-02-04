package finlamm;

import java.util.ArrayList;

public class main {
public static void main(String[] args) {
		
		ArrayList<block> temp = new ArrayList<block>();
		ArrayList<node> nodes = new ArrayList<node>();

		compress comp = new compress();
		comp.block_rows = 16;
		comp.block_cols = 16;
		//comp.bading(matrix);
		comp.readImage("lena.jpg");
		comp.bits=8;
		comp.compress();
		comp.saveATfile("comp1.txt");
		
		decompress decomp = new decompress();
		decomp.loadFROMfile("comp1.txt");
		decomp.decompress();
		decomp.writeImage("comp1_lena.png");
		
	}








}



//block b_temp;
//node node= new node();
//
//double[][] x1 = {{6,2}};
//b_temp = new Block(x1);
//temp.add(b_temp);
//double[][] x2 = {{7,6}};
//b_temp = new Block(x2);
//temp.add(b_temp);
//double[][] x3 = {{2,2}};
//b_temp = new Block(x3);
//temp.add(b_temp);
//double[][] x4 = {{1,6}};
//b_temp = new Block(x4);
//temp.add(b_temp);
//double[][] x5 = {{5,1}};
//b_temp = new Block(x5);
//temp.add(b_temp);
//double[][] x6 = {{6,6}};
//b_temp = new Block(x6);
//temp.add(b_temp);
//double[][] x7 = {{1,2}};
//b_temp = new Block(x7);
//temp.add(b_temp);
//double[][] x8 = {{6,3}};
//b_temp = new Block(x8);
//temp.add(b_temp);
//double[][] x9 = {{7,7}};
//b_temp = new Block(x9);
//temp.add(b_temp);
//double[][] x10 = {{2,3}};
//b_temp = new Block(x10);
//temp.add(b_temp);
//double[][] x11 = {{1,7}};
//b_temp = new Block(x11);
//temp.add(b_temp);
//double[][] x12 = {{5,2}};
//b_temp = new Block(x12);
//temp.add(b_temp);
//double[][] x13 = {{6,7}};
//b_temp = new Block(x13);
//temp.add(b_temp);
//double[][] x14 = {{1,3}};
//b_temp = new Block(x14);
//temp.add(b_temp);
//
//int[][] matrix = { 
//		{ 1, 2, 3, 4, 5, 6, 7, 8 },
//		{ 9, 10, 11, 12, 13, 14, 15, 16 },
//		{ 17, 18, 19, 20, 21, 22, 23, 24 },
//		{ 25, 26, 27, 28, 29, 30, 31, 32 },
//		{ 33, 34, 35, 36, 37, 38, 39, 40 },
//		{ 41, 42, 43, 44, 45, 46, 47, 48 },
//		{ 49, 50, 51, 52, 53, 54, 55, 56 },
//		{ 57, 58, 59, 60, 61, 62, 63, 64 } };