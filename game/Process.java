package game;

import java.awt.Color;

import javax.swing.JOptionPane;

public class Process {
	MainGame mainGame;
	Box[] box = new Box[12];
	ScoreBox[] scBox = new ScoreBox[2];
	Stone[] stone = new Stone[70];

	public Process(MainGame m) {
		mainGame = m;
	}

	public void init() {
		// init elements for the first time start.
		for (int i = 0; i < 12; i++)
			box[i] = new Box(mainGame, this, i);
		scBox[0] = new ScoreBox(mainGame, 0);
		scBox[1] = new ScoreBox(mainGame, 1);
		int count = 0;
		for (int i = 0; i < 12; i++) {
			if (i != 5 && i != 11)// k phỉ vị trí 5 hoặc 11 ô đó có 5 viên đá
				for (int j = 0; j < 5; j++) {
					stone[count] = new Stone(mainGame, i);
					count++;
				}
			else
				for (int j = 0; j < 10; j++) {
					stone[count] = new Stone(mainGame, i);
					count++;
				}
		}
	}

	public void reDraw() {
		// reset game.
		mainGame.currentTeam = 0;
		int count = 0;
		for (int i = 0; i < 12; i++) {
			if (i != 5 && i != 11)
				for (int j = 0; j < 5; j++) {
					stone[count].quickMove(i);
					count++;
				}
			else
				for (int j = 0; j < 10; j++) {
					stone[count].quickMove(i);
					count++;
				}
			box[i].resetStone();
		}
		scBox[0].resetStone();
		scBox[1].resetStone();
		for (int i = 0; i < 12; i++)
			box[i].change(mainGame.getNumberInBox());
		box[5].change(mainGame.getNumberInScoreBox());
		box[11].change(mainGame.getNumberInScoreBox());
		scBox[0].change(0);
		scBox[1].change(0);
		scBox[0].setTurn(true);
	}

	public void resize() {
		for (int i = 0; i < 12; i++)
			box[i].resize();
		scBox[0].resize();
		scBox[1].resize();
		for (int i = 0; i < 70; i++)
			stone[i].resize();
	}

	public void removeAllArrow() {
		for (int i = 0; i < 12; i++)
			box[i].removeArrow();
	}

	// public void move(int pos, int direction, boolean isEaten) {
	// // main function to move stones.
	// if (pos == 5 || pos == 11)
	// mainGame.nextTurn();
	// else if (box[pos].getNum() > 0) {
	// if (!isEaten) {
	// int num = box[pos].getNum();
	// box[pos].setColor(Color.BLUE);
	// for (int i = 1; i <= num; i++) {
	// int temp = calNewPos(pos, i * direction);
	// // System.out.println(" Drop stone to box[" + temp + "]");
	// box[pos].change(box[pos].getNum() - 1);
	// box[temp].change(box[temp].getNum() + 1);
	// int j = 0;

	// while (box[pos].isStone[j] == false)
	// j++;
	// box[pos].isStone[j] = false;
	// box[temp].isStone[j] = true;
	// stone[j].move(temp);
	// }
	// int vtSau = calNewPos(pos, (num + 1) * direction);
	// move(vtSau, direction, false);
	// } else
	// mainGame.nextTurn();
	// } else {
	// if (box[calNewPos(pos, direction)].getNum() > 0) {
	// kill(mainGame.getCurTeam(), calNewPos(pos, direction));
	// move(calNewPos(pos, direction * 2), direction, true);
	// } else
	// mainGame.nextTurn();
	// }
	// }

	public void move(int pos, int direction, boolean isEaten) {
		// Kiểm tra xem đội hiện tại có thể di chuyển hay không
		if (total(mainGame.getCurTeam()) == 0) {
			mainGame.nextTurn();
			return;
		}

		if (pos == 5 || pos == 11)
			mainGame.nextTurn();
		else if (box[pos].getNum() > 0) {
			if (!isEaten) {
				int num = box[pos].getNum();
				box[pos].setColor(Color.BLUE);
				for (int i = 1; i <= num; i++) {
					int temp = calNewPos(pos, i * direction);
					System.out.println("  Drop stone to box[" + temp + "]");
					box[pos].change(box[pos].getNum() - 1);
					box[temp].change(box[temp].getNum() + 1);
					int j = 0;
					while (box[pos].isStone[j] == false)
						j++;
					box[pos].isStone[j] = false;
					box[temp].isStone[j] = true;
					stone[j].move(temp);
				}
				int vtSau = calNewPos(pos, (num + 1) * direction);
				move(vtSau, direction, false);
			} else
				mainGame.nextTurn();
		} else {
			if (box[calNewPos(pos, direction)].getNum() > 0) {
				kill(mainGame.getCurTeam(), calNewPos(pos, direction));
				move(calNewPos(pos, direction * 2), direction, true);
			} else
				mainGame.nextTurn();
		}
	}

	public int check(int team) {
		// check if there is a win situation or out of stones in current team.
		if (box[5].getNum() == 0 && box[11].getNum() == 0)
			return -1;
		if (scBox[team].getNum() < 5 && total(team) == 0)
			return 0;
		if (scBox[team].getNum() > 5 && total(team) == 0)
			return 1;
		return 2;
	}

	int total(int team) {
		// get the total of stones on normal box.
		if (team == 0)
			return box[6].getNum() + box[7].getNum() + box[8].getNum() + box[9].getNum() + box[10].getNum();
		else
			return box[0].getNum() + box[1].getNum() + box[2].getNum() + box[3].getNum() + box[4].getNum();
	}

	// int calNewPos(int src, int step) {
	// return (src + 1200 + step) % 12;
	// }

	int calNewPos(int src, int step) {
		return ((src + step) % 12 + 12) % 12;
	}

	void kill(int team, int pos) {
		int boxTemp = box[pos].getNum(); // Số viên đá trong ô bị ăn
		int stonesEaten = 0; // Biến lưu số viên đá đã ăn

		for (int i = 0; i < boxTemp; i++) {
			int j = 0;
			while (box[pos].isStone[j] == false) // Tìm viên đá đầu tiên trong ô
				j++;
			box[pos].isStone[j] = false; // Xóa viên đá từ ô bị ăn
			scBox[team].isStone[j] = true; // Di chuyển viên đá vào ô điểm
			box[pos].change(box[pos].getNum() - 1); // Cập nhật số viên đá trong ô
			stone[j].move(team - 2); // Di chuyển viên đá đến ô điểm
			stonesEaten++; // Tăng số viên đá đã ăn
		}

		// Cập nhật điểm số và hiển thị ngay sau khi ăn viên đá
		updateScoreForTeam(team, stonesEaten);
	}

	// thêm hàm này để cập nhật điểm số cho người chơi và AI
	private void updateScoreForTeam(int team, int stonesEaten) {
		int currentScore = scBox[team].getNum();
		scBox[team].updateScore(currentScore + stonesEaten); // Cập nhật điểm số
		scBox[team].change(currentScore + stonesEaten); // Hiển thị điểm số mới
	}

	void nextTurn() {
		if (total(mainGame.getCurTeam()) == 0) {
			System.out.println(
					"No stones left in the normal boxes for team " + mainGame.getCurTeam() + ".Spreading stones...");
			spread(mainGame.getCurTeam()); // Gọi hàm rải quân
		}
		int result = check(mainGame.getCurTeam());

		System.out.println("Total stones for team " + mainGame.getCurTeam() + ": " +
				total(mainGame.getCurTeam()));
		System.out.println("Stones in score box: " +
				scBox[mainGame.getCurTeam()].getNum());

		if (result == -1) {
			int winTeam = (scBox[0].getNum() > scBox[1].getNum()) ? 0 : 1;
			victory(winTeam);
		} else if (result == 0) {
			victory(1 - mainGame.getCurTeam());
		} else if (result == 1) {
			spread(mainGame.getCurTeam());
		}

		// Kiểm tra xem có quân trong ô không trước khi chuyển lượt
		if (total(mainGame.getCurTeam()) == 0) {
			spread(mainGame.getCurTeam());
		}

		scBox[mainGame.getCurTeam()].setTurn(true);
		scBox[1 - mainGame.getCurTeam()].setTurn(false);
	}//

	void victory(int team) {
		if (team > -1) {
			JOptionPane.showConfirmDialog(mainGame, "Người chơi " + (team + 1) + "thắng", "Xong phim",
					JOptionPane.DEFAULT_OPTION);
		} else {
			JOptionPane.showConfirmDialog(mainGame, "Hoà", "Game over", 1);
		}
		mainGame.newGame();
	}

	// void spread(int team) {
	// // Rải quân khi không còn quân trên bàn
	// if (team == 0) {
	// for (int i = 6; i < 11; i++) {
	// if (box[i].getNum() > 0) { // Kiểm tra nếu ô có quân
	// boxSpread(team, i);
	// }
	// }
	// } else {
	// for (int i = 0; i < 5; i++) {
	// if (box[i].getNum() > 0) { // Kiểm tra nếu ô có quân
	// boxSpread(team, i);
	// }
	// }
	// }
	// }

	// void boxSpread(int team, int pos) {
	// // sub function spread to a box.
	// int j = 0;
	// scBox[team].change(scBox[team].getNum() - 1);
	// while (!scBox[team].isStone[j])
	// j++;

	// stone[j].move(pos);
	// scBox[team].isStone[j] = false;
	// box[pos].change(1);
	// box[pos].isStone[j] = true;
	// }
	void spread(int team) {
		int numStones = scBox[team].getNum();
		if (numStones == 0) {
			victory(1 - team); // Đội kia thắng nếu ô điểm trống
			return;
		}

		int[] boxes = (team == 0) ? new int[] { 6, 7, 8, 9, 10 } : new int[] { 0, 1, 2, 3, 4 };
		int stonesToSpread = Math.min(numStones, boxes.length); // Rải tối đa số ô dân

		for (int i = 0; i < stonesToSpread; i++) {
			boxSpread(team, boxes[i]);
		}

		// Vẽ lại bàn chơi sau khi rải
		mainGame.repaint();
	}

	void boxSpread(int team, int pos) {
		if (scBox[team].getNum() <= 0) {
			return;
		}

		int j = 0;
		while (j < stone.length && !scBox[team].isStone[j]) {
			j++;
		}

		if (j >= stone.length) {
			return;
		}

		scBox[team].change(scBox[team].getNum() - 1);
		stone[j].move(pos);
		scBox[team].isStone[j] = false;
		box[pos].change(box[pos].getNum() + 1);
		box[pos].isStone[j] = true;
	}
}
