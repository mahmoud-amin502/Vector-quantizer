package finlamm;

public class block {
	int num_rows = 0;
	int num_cols = 0;
	Double[][] block;

	block(double[][] x) {
		num_rows = x.length;
		num_cols = x[0].length;
		block = new Double[num_rows][num_cols];
		for (int i = 0; i < num_rows; i++)
			for (int j = 0; j < num_cols; j++)
				block[i][j] = x[i][j];
	}

	block(int r, int c) {
		num_rows = r;
		num_cols = c;
		block = new Double[num_rows][num_cols];
		for (int i = 0; i < num_rows; i++)
			for (int j = 0; j < num_cols; j++)
				block[i][j] = 0.0;
	}

	block() {
	}

	public void print_block() {
		for (int i = 0; i < this.num_rows; i++) {
			for (int j = 0; j < this.num_cols; j++) {
				System.out.print(this.block[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("----------------");
	}
}

