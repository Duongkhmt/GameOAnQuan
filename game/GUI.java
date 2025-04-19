package game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class GUI extends JFrame implements ActionListener {
	public static final int xUnit = 16, yUnit = 9;
	public static int multiple = 80;
	private JMenuBar mb;
	private JMenu mGame = new JMenu("Trò chơi"),
			mHelp = new JMenu("Trợ giúp");
	private JMenuItem miGameMoi = new JMenuItem("Trò chơi mới"), // Đã xóa
			miThoat = new JMenuItem("Kết thúc"),
			miThongTin = new JMenuItem("Thông tin");
	MainGame mainGame;
	private Font fontMenu = new Font("SansSerif", Font.BOLD, 18);

	// amnhac
	private Clip backgroundMusic; // Biến để quản lý âm thanh
	private boolean isMusicPlaying = false; // Trạng thái âm thanh
	private JMenuItem miToggleMusic; // Menu item để bật/tắt nhạc

	public GUI() {
		init();
	}

	private void initBackgroundMusic() {
		try {
			File musicFile = new File("E:\\GameOAnQuan\\images\\nhactrochoi.wav");
			AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicFile);
			backgroundMusic = AudioSystem.getClip();
			backgroundMusic.open(audioInput);
			// backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // Lặp vô hạn
		} catch (Exception e) {
			System.out.println("Lỗi khi tải nhạc nền: " + e.getMessage());
		}
	}

	// Phương thức bật/tắt nhạc
	private void toggleMusic() {
		if (backgroundMusic != null) {
			if (isMusicPlaying) {
				backgroundMusic.stop();
				isMusicPlaying = false;
				miToggleMusic.setText("Bật nhạc");
			} else {
				backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
				backgroundMusic.start();
				isMusicPlaying = true;
				miToggleMusic.setText("Tắt nhạc");
			}
		}
	}

	// void init() {
	// setTitle("Ô ăn quan 3.0");
	// setResizable(false);
	// addMenu();
	// ImageIcon icon = new
	// ImageIcon("E:\\btlttnt\\O-an-quan\\src\\images\\stone.jpg");
	// setIconImage(icon.getImage());
	// setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	// mainGame = new MainGame();
	// add(mainGame);
	// resize();

	// // Hiển thị hộp thoại chọn chế độ chơi khi khởi động trò chơi
	// showGameModeDialog();
	// }

	void init() {
		setTitle("Ô ăn quan 3.0");
		setResizable(false);
		addMenu();
		initBackgroundMusic(); // Thêm khởi tạo nhạc
		ImageIcon icon = new ImageIcon("E:\\GameOAnQuan\\images\\stone.jpg");
		setIconImage(icon.getImage());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainGame = new MainGame();
		add(mainGame);
		resize();
		showGameModeDialog();
	}

	void showGameModeDialog() {
		JDialog dialog = new JDialog(this, "Chọn chế độ chơi", true);
		dialog.setLayout(new BorderLayout());

		// Tạo panel chứa hình ảnh
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());

		// Thêm hình ảnh làm nền
		JLabel imageLabel = new JLabel();
		ImageIcon icon = new ImageIcon("E:\\GameOAnQuan\\images\\anhdaidien.jpg");
		imageLabel.setIcon(icon);
		imageLabel.setHorizontalAlignment(JLabel.CENTER);
		contentPanel.add(imageLabel, BorderLayout.CENTER);

		// Tạo panel chứa các nút
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(7, 1, 10, 10)); // 6hàng, 1 cột

		// Nút chơi
		JButton humanButton = new JButton("Chơi với người");
		humanButton.addActionListener(e -> {
			mainGame.setPlayingWithAI(false);
			dialog.dispose();
		});
		buttonPanel.add(humanButton);

		JButton aiButton = new JButton("Chơi với máy");
		aiButton.addActionListener(e -> {
			mainGame.setPlayingWithAI(true);
			dialog.dispose();
		});
		buttonPanel.add(aiButton);

		JButton LevelButton = new JButton("Mức độ");
		LevelButton.addActionListener(e -> showMucDoDialog());
		buttonPanel.add(LevelButton);

		// Nút cài đặt
		JButton settingsButton = new JButton("Cài đặt");
		settingsButton.addActionListener(e -> showCaiDatDialog());
		buttonPanel.add(settingsButton);

		// Nút trợ giúp
		JButton helpButton = new JButton("Luật chơi");
		helpButton.addActionListener(e -> showLuatChoi());
		buttonPanel.add(helpButton);

		// Nút kết thúc
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(e -> System.exit(0));
		buttonPanel.add(exitButton);

		contentPanel.add(buttonPanel, BorderLayout.SOUTH);
		dialog.add(contentPanel, BorderLayout.CENTER);

		// Cài đặt kích thước và hiển thị
		dialog.setSize(400, 400);
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}

	void resize() {
		setSize(xUnit * multiple, yUnit * multiple + 50);
		mainGame.resize();
	}

	void addMenu() {
		mb = new JMenuBar();
		mb.setBounds(0, 0, 500, 30);
		setJMenuBar(mb);

		createMI(mGame);
		createMI(mHelp);
		createMI(miThoat);
		createMI(miThongTin);

		mGame.add(miThoat);
		mHelp.add(miThongTin);

		mb.add(mGame);
		mb.add(mHelp);
	}

	void createMI(Object mi) {
		((AbstractButton) mi).addActionListener(this);
		((JComponent) mi).setFont(fontMenu);
	}

	@Override
	// public void actionPerformed(ActionEvent e) {
	// if (e.getSource() == miThoat) {
	// System.exit(0);
	// }
	// if (e.getSource() == miThongTin) {
	// showHang();
	// }
	// }

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == miThoat) {
			System.exit(0);
		}
		if (e.getSource() == miThongTin) {
			showHang();
		}
		if (e.getSource() == miToggleMusic) {
			toggleMusic(); // Xử lý bật/tắt nhạc
		}
	}

	// void showCaiDatDialog() {
	// JDialog setupDialog = new JDialog(this, "Cài đặt", true);
	// final int stWidth = 400, stHeight = 220;
	// setupDialog.setSize(stWidth, stHeight);
	// setupDialog.setLayout(null);
	// setupDialog.setLocationRelativeTo(this);

	// JLabel lbSpeed = new JLabel("Tốc độ di chuyển");
	// lbSpeed.setBounds(20, 20, 200, 30);
	// lbSpeed.setFont(fontMenu);
	// setupDialog.add(lbSpeed);

	// JSpinner spSpeed = new JSpinner(new SpinnerNumberModel(mainGame.speed, 1, 4,
	// 1));
	// spSpeed.setBounds(300, 20, 50, 30);
	// setupDialog.add(spSpeed);

	// JLabel lbSize = new JLabel("Độ thu phóng");
	// lbSize.setBounds(20, 70, 200, 30);
	// lbSize.setFont(fontMenu);
	// setupDialog.add(lbSize);

	// JSpinner spSize = new JSpinner(new SpinnerNumberModel(multiple / 20, 1, 4,
	// 1));
	// spSize.setBounds(300, 70, 50, 30);
	// setupDialog.add(spSize);

	// JButton dongY = new JButton("Chấp nhận");
	// dongY.setBounds(55, 130, 130, 35);
	// dongY.setFont(fontMenu);
	// dongY.addActionListener(e -> {
	// mainGame.speed = (int) spSpeed.getValue();
	// multiple = (int) spSize.getValue() * 20;
	// resize();
	// setupDialog.dispose();
	// });
	// JButton huyBo = new JButton("Huỷ bỏ");
	// huyBo.setBounds(200, 130, 100, 35);
	// huyBo.setFont(fontMenu);
	// huyBo.addActionListener(e -> setupDialog.dispose());
	// setupDialog.add(dongY);
	// setupDialog.add(huyBo);

	// setupDialog.setVisible(true);
	// }

	void showCaiDatDialog() {
		JDialog setupDialog = new JDialog(this, "Cài đặt", true);
		final int stWidth = 400, stHeight = 260; // Tăng chiều cao để chứa thêm nút
		setupDialog.setSize(stWidth, stHeight);
		setupDialog.setLayout(null);
		setupDialog.setLocationRelativeTo(this);

		JLabel lbSpeed = new JLabel("Tốc độ di chuyển");
		lbSpeed.setBounds(20, 20, 200, 30);
		lbSpeed.setFont(fontMenu);
		setupDialog.add(lbSpeed);

		JSpinner spSpeed = new JSpinner(new SpinnerNumberModel(mainGame.speed, 1, 4, 1));
		spSpeed.setBounds(300, 20, 50, 30);
		setupDialog.add(spSpeed);

		JLabel lbSize = new JLabel("Độ thu phóng");
		lbSize.setBounds(20, 70, 200, 30);
		lbSize.setFont(fontMenu);
		setupDialog.add(lbSize);

		JSpinner spSize = new JSpinner(new SpinnerNumberModel(multiple / 20, 1, 4, 1));
		spSize.setBounds(300, 70, 50, 30);
		setupDialog.add(spSize);

		// Thêm nút bật/tắt nhạc trong cài đặt
		JButton musicButton = new JButton(isMusicPlaying ? "Tắt nhạc" : "Bật nhạc");
		musicButton.setBounds(20, 120, 130, 35);
		musicButton.setFont(fontMenu);
		musicButton.addActionListener(e -> {
			toggleMusic();
			musicButton.setText(isMusicPlaying ? "Tắt nhạc" : "Bật nhạc");
		});
		setupDialog.add(musicButton);

		JButton dongY = new JButton("Chấp nhận");
		dongY.setBounds(55, 170, 130, 35);
		dongY.setFont(fontMenu);
		dongY.addActionListener(e -> {
			mainGame.speed = (int) spSpeed.getValue();
			multiple = (int) spSize.getValue() * 20;
			resize();
			setupDialog.dispose();
		});
		JButton huyBo = new JButton("Huỷ bỏ");
		huyBo.setBounds(200, 170, 100, 35);
		huyBo.setFont(fontMenu);
		huyBo.addActionListener(e -> setupDialog.dispose());
		setupDialog.add(dongY);
		setupDialog.add(huyBo);

		setupDialog.setVisible(true);
	}

	void showMucDoDialog() {
		String[] levels = { "Dễ", "Trung Bình", "Khó" };
		JComboBox<String> levelComboBox = new JComboBox<>(levels);

		int result = JOptionPane.showConfirmDialog(this, levelComboBox, "Chọn Mức Độ", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			String selectedLevel = (String) levelComboBox.getSelectedItem();
			int difficulty = levelComboBox.getSelectedIndex(); // 0: Dễ, 1: Trung bình, 2: Khó
			mainGame.setAIDifficulty(difficulty);
			JOptionPane.showMessageDialog(this, "Bạn đã chọn mức độ: " + selectedLevel, "Thông Báo",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	void showHang() {
		String s = "Trò chơi mô phỏng ô ăn quan.\nTác giả: Dương Mạnh Tiến \nChúc các bạn vui vẻ.";
		JOptionPane.showConfirmDialog(this, s, "Ô ăn quan 2.0", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
	}

	void showLuatChoi() {
		String s = "Người chơi chọn 1 ô dân trên bàn cờ của mình có chứa \nít nhất 1 quân. \nNgười chơi nhặt hết quân trong ô được chọn rồi phân phát theo \nchiều từ trái sang phải (hoặc phải sang trái) tùy theo ý nghĩ \nvà chiến thuật của mình vào các ô tiếp theo trên bàn cờ \nNếu ô cuối cùng vừa nhận quân đã có sẵn quân, người chơi \nnhặt hết quân ở đó và tiếp tục rải theo cùng quy tắc, \nquá trình này lặp lại cho đến khi nước đi kết \nthúc theo quy định \nKhi nước đi dừng tại một ô dân trống (không phải ô quan) \nvà ô kế tiếp (theo chiều rải quân) chứa quân, người chơi được\n “ăn” toàn bộ quân ở ô đó, số quân “ăn” được sẽ được\n cộng vào điểm số của người chơi \nTrò chơi kết thúc khi không còn ô dân nào \ncó đủ quân để thực hiện nước đi hợp lệ.";
		JOptionPane.showConfirmDialog(this, s, "Luật chơi", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
	}
}