package game;

import javax.swing.*;
import java.awt.*;

public class GameModeDialog extends JDialog {
    private boolean playWithAI;

    public GameModeDialog(JFrame parent) {
        super(parent, "Chọn chế độ chơi", true);
        setLayout(new BorderLayout());

        // Thêm tiêu đề
        JLabel label = new JLabel("Chọn chế độ chơi", JLabel.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(label, BorderLayout.NORTH);

        // Tạo panel chứa hình ảnh và nút
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        // Thêm hình ảnh
        JLabel imageLabel = new JLabel();
        ImageIcon icon = new ImageIcon("E:\\GameOAnQuan\\images\\game-mode.jpg"); // Đường
                                                                                  // dẫn
                                                                                  // ảnh
                                                                                  // tương
                                                                                  // đối
        imageLabel.setIcon(icon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPanel.add(imageLabel, BorderLayout.NORTH);

        // Tạo panel chứa các nút
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton humanButton = new JButton("Chơi với người");
        humanButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        humanButton.addActionListener(e -> {
            playWithAI = false;
            setVisible(false);
        });
        buttonPanel.add(humanButton);

        JButton aiButton = new JButton("Chơi với máy");
        aiButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        aiButton.addActionListener(e -> {
            playWithAI = true;
            setVisible(false);
        });
        buttonPanel.add(aiButton);

        JButton cancelButton = new JButton("Hủy");
        cancelButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        cancelButton.addActionListener(e -> setVisible(false));
        buttonPanel.add(cancelButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(contentPanel, BorderLayout.CENTER);

        // Cài đặt kích thước và vị trí
        setSize(400, 300); // Tăng kích thước để phù hợp với hình ảnh
        setLocationRelativeTo(parent);
    }

    public boolean isPlayWithAI() {
        return playWithAI;
    }
}