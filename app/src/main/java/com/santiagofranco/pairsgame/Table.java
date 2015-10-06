package com.santiagofranco.pairsgame;


public class Table {

	private static final int TABLE_WIDTH = 4;
	private static final int TABLE_HEIGHT = 5;
	private static final int LIMIT_VALUE = ((TABLE_HEIGHT*TABLE_WIDTH)/2)-1;
	
	private int[][] table;

	public Table() {
		table = new int[TABLE_HEIGHT][TABLE_WIDTH];
	}
	

	public int[][] getTable() {
		return table;
	}



	public void initMe() {
		
		fillTableOfMinusOne();
		int values = 0;
		while (values <= LIMIT_VALUE) {

			for (int i = 0; i < 2; i++) {
				if(!putValues(values))
					i--;
			}
			
			values++;

		}

	}

	private void fillTableOfMinusOne() {
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[0].length; j++) {
				table[i][j] = -1;
			}
		}
	}

	private boolean putValues(int value) {
		int x = (int) Math.floor(Math.random() * 5 + 0);
		int y = (int) Math.floor(Math.random() * 4 + 0);
		if(table[x][y]==-1)
			table[x][y] = value;
		else
			return false;
		
		return true;
	}

	public void printMe() {
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[0].length; j++) {
				System.out.print(table[i][j] + " ");
			}
			System.out.println();
		}
	}
}
