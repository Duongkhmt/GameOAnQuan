package game;

import javax.swing.*;
import java.awt.*;

public class MainGame extends JPanel {
	Process process;
	int currentTeam = 0,
			numberInBox = 5,
			numberInScoreBox = 10,
			multiple = GUI.multiple;
	final int xUnit = GUI.xUnit,
			yUnit = GUI.yUnit;
	public int speed = 4;
	private boolean isPlayingWithAI = false;
	private AIPlayer aiPlayer;
	private int aiDifficulty = 0;

	public MainGame() {
		init();
	}

	void init() {
		setLocation(0, 0);
		setLayout(null);
		process = new Process(this);
		process.init();
		process.reDraw();
		resize();
		aiPlayer = new AIPlayer(this, aiDifficulty);
	}

	public void setAIDifficulty(int difficulty) {
		this.aiDifficulty = difficulty;
		aiPlayer = new AIPlayer(this, difficulty); // Tạo lại AI với mức độ mới
	}

	void resize() {
		multiple = GUI.multiple;
		setSize(GUI.WIDTH, GUI.HEIGHT);
		process.resize();
	}

	void newGame() {
		int option = JOptionPane.showConfirmDialog(this, "Bạn có muốn chơi lại không?", "Retard alert",
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		if (option == JOptionPane.YES_OPTION) {
			process.reDraw(); // Đặt lại mọi thứ
			currentTeam = 0; // Đặt lại đội hiện tại về đội 0
		} else {
			System.exit(0); // Thoát trò chơi
		}
	}

	public void nextTurn() {
		currentTeam = 1 - currentTeam;
		process.nextTurn();

		for (ScoreBox scBox : process.scBox) {
			scBox.switchTurn();
		} //
			// process.nextTurn();
		if (isPlayingWithAI && currentTeam == 1) {
			System.out.println("AI turn, currentTeam: " + currentTeam);
			aiPlayer.makeMove(); // Máy thực hiện bước đi nếu đang chơi với máy
		}
	}

	public int getCurTeam() {
		return currentTeam;
	}

	public void setCurTeam(int team) {
		currentTeam = team;
	}

	public int getNumberInBox() {
		return numberInBox;
	}

	public int getNumberInScoreBox() {
		return numberInScoreBox;
	}

	public void setNumberInBox(int n) {
		numberInBox = n;
	}

	public void setNumberInScoreBox(int n) {
		numberInScoreBox = n;
	}

	public void setPlayingWithAI(boolean isPlayingWithAI) {
		this.isPlayingWithAI = isPlayingWithAI;
	}

	// Phương thức bắt đầu trò chơi với AI
	public void startGameWithAI() {//
		setPlayingWithAI(true); // Thiết lập chơi với AI
		newGame(); // Khởi động trò chơi
	}

	public boolean isPlayingWithAI() {
		return isPlayingWithAI;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Image i = new ImageIcon("E:\\GameOAnQuan\\images\\background.jpg").getImage();
		g2d.drawImage(i, 0, 0, xUnit * multiple, yUnit * multiple, null);
	}
}