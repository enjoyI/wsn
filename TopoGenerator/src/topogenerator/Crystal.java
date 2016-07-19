package topogenerator;

import java.util.ArrayList;

public class Crystal {

	public int xsize;// 表示x方向有多少个晶胞
	public int ysize;
	public static double xstart=1;
	public static double ystart=1;
	CrystalCell[][] crystalCellSet;

	Crystal(int x, int y) {
		initial(x, y);
	}

	public void initial(int x, int y) {
		xsize = x;
		ysize = y;
		crystalCellSet = new CrystalCell[xsize][ysize];
		CrystalCell tCell;
		for (int i = 0; i < xsize; ++i) {
			// crystalCellSet[i] = new CrystalCell[ysize];
			for (int j = 0; j < ysize; ++j) {
				tCell = new CrystalCell();
				crystalCellSet[i][j] = tCell;
				if (j > 0) {
					crystalCellSet[i][j - 1].upAdd(tCell);
					if (i > 0) {
						crystalCellSet[i - 1][j].rightAdd(tCell);
						crystalCellSet[i - 1][j - 1].quadrant1Add(tCell);
					} else {
					}
				} else {

					if (i > 0) {
						crystalCellSet[i - 1][j].rightAdd(tCell);
					} else {

					}
				}
			}
		}
	}

	public String serilizeAllInfo() {
		String info = "";
		info += String.valueOf(xsize);
		info += " ";
		info += String.valueOf(ysize);
		info += "\n";
		for (int i = 0; i < xsize; ++i) {
			for (int j = 0; j < ysize; ++j) {
				info += String.valueOf(i);
				info += " ";
				info += String.valueOf(j);
				info += "\n";
				info += crystalCellSet[i][j].serilizeAllInfo();
			}
		}
		return info;
	}

	public static void main(String arg[]) {
		Crystal ct = new Crystal(1, 1);
		System.out.println(ct.serilizeAllInfo());
	}

	public void changeAtomPos(double floatRadio) {
		for (int i = 0; i < xsize; ++i) {
			for (int j = 0; j < ysize; ++j) {
				crystalCellSet[i][j].changeAtomPos(floatRadio);
			}
		}
		for (int i = 0; i < xsize; ++i) {
			for (int j = 0; j < ysize; ++j) {
				for (int k = 0; k < crystalCellSet[i][j].atoms.size(); ++k) {
					if (neighborAmount(i, j, k) == 0) {
						crystalCellSet[i][j].atoms.get(k).recycleID();
						crystalCellSet[i][j].atoms.remove(k);
					} else {
					}
				}
			}
		}

	}

	private int neighborAmount(int xp, int yp, int atomp) {
		double[] src = null;
		ArrayList<double[]> dst = new ArrayList<>();
		for (int i = xp - 1; i <= xp + 1; ++i) {
			for (int j = yp - 1; j <= yp + 1; ++j) {
				if (i >= 0 && i < xsize && j >= 0 && j < ysize) {
					for (int k = 0; k < crystalCellSet[i][j].atoms.size(); ++k) {
						if (i == xp && j == yp && k == atomp) {
							src = absolutePos(i, j, atomp);
						} else {
							dst.add(absolutePos(i, j, k));
						}
					}

				} else {
				}
			}
		}
		return connectedAmount(src, dst);
	}

	private double[] absolutePos(int xp, int yp, int atomp) {
		double[] pos = new double[2];
		pos[0] = xstart+xp * CrystalCell.sideLength * 3 + crystalCellSet[xp][yp].atoms.get(atomp).x;
		pos[1] = ystart+yp * CrystalCell.sideLength * Math.sqrt(3) + crystalCellSet[xp][yp].atoms.get(atomp).y;
		crystalCellSet[xp][yp].atoms.get(atomp).changeAbsPos(pos[0], pos[1], 0);
		return pos;
	}

	private int connectedAmount(double[] src, ArrayList<double[]> dst) {
		int amount = 0;
		for (int i = 0; i < dst.size(); ++i) {
			if (Math.sqrt(Math.pow((dst.get(i)[0] - src[0]), 2)
					+ Math.pow((dst.get(i)[1] - src[1]), 2)) < CrystalCell.sideLength * 1.365) {
				amount++;
			} else {
			}
		}
		return amount;
	}

	public String atomPixInfo() {
		String info = "";
		for (int i = 0; i < xsize; ++i) {
			for (int j = 0; j < ysize; ++j) {
				info += crystalCellSet[i][j].atomPixInfo();
			}
		}
		return info;
	}
}
