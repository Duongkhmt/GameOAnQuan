
package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer {
    private MainGame mainGame;
    private int difficulty; // 0: Dễ, 1: Trung bình, 2: Khó
    private static final int AI_TEAM = 1; // AI luôn là team 1

    public AIPlayer(MainGame mainGame, int difficulty) {
        this.mainGame = mainGame;
        this.difficulty = difficulty;
    }

    public void makeMove() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (mainGame.process.total(AI_TEAM) == 0) {
            mainGame.nextTurn();
            return;
        }

        int pos = findBestMove();
        System.out.println("AI selected position: " + pos);
        if (pos >= 6 && pos <= 10) { // Chỉ cho phép ô của team 1
            System.out.println("AI is moving position: " + pos);
            mainGame.process.move(pos, 1, false);
        } else {
            System.out.println("Invalid move by AI: Position " + pos + " is not in AI's range (6-10)");
        }
    }

    private int findBestMove() {
        switch (difficulty) {
            case 0: // Dễ
                return findEasyMove();
            case 1: // Trung bình
                return findMediumMove();
            case 2: // Khó
                return findHardMove();
            default:
                return findEasyMove();
        }
    }

    // Mức dễ: Chọn ngẫu nhiên ô có quân trong các ô của AI (6-10)
    private int findEasyMove() {
        Random rand = new Random();
        List<Integer> validMoves = new ArrayList<>();
        for (int i = 6; i <= 10; i++) { // Chỉ chọn ô dân của team 1
            if (mainGame.process.box[i].getNum() > 0) {
                validMoves.add(i);
            }
        }
        return validMoves.isEmpty() ? -1 : validMoves.get(rand.nextInt(validMoves.size()));
    }

    // Mức trung bình: Ưu tiên nước đi ăn được quân
    private int findMediumMove() {
        for (int i = 6; i <= 10; i++) {
            if (mainGame.process.box[i].getNum() > 0) {
                int steps = mainGame.process.box[i].getNum();
                int nextPos = mainGame.process.calNewPos(i, steps + 1);
                if (mainGame.process.box[nextPos].getNum() == 0) {
                    int nextNextPos = mainGame.process.calNewPos(nextPos, 1);
                    if (mainGame.process.box[nextNextPos].getNum() > 0 && nextNextPos != 5 && nextNextPos != 11) {
                        return i; // Ưu tiên nước đi ăn được quân (trừ ô quan)
                    }
                }
            }
        }
        return findEasyMove(); // Nếu không ăn được, chọn ngẫu nhiên
    }

    // Mức khó: Tìm nước đi tối ưu dựa trên điểm số tiềm năng
    private int findHardMove() {
        int bestMove = -1;
        int maxScore = -1;

        for (int i = 6; i <= 10; i++) {
            if (mainGame.process.box[i].getNum() > 0) {
                int score = evaluateMove(i);
                if (score > maxScore) {
                    maxScore = score;
                    bestMove = i;
                }
            }
        }
        return bestMove == -1 ? findEasyMove() : bestMove;
    }

    // Đánh giá nước đi cho mức khó
    private int evaluateMove(int pos) {
        int score = 0;
        int steps = mainGame.process.box[pos].getNum();
        int currentPos = pos;

        // Mô phỏng rải đá
        for (int i = 1; i <= steps; i++) {
            currentPos = mainGame.process.calNewPos(currentPos, 1);
        }

        // Kiểm tra khả năng ăn quân
        int nextPos = mainGame.process.calNewPos(currentPos, 1);
        if (mainGame.process.box[currentPos].getNum() == 0 &&
                mainGame.process.box[nextPos].getNum() > 0 &&
                nextPos != 5 && nextPos != 11) {
            score += mainGame.process.box[nextPos].getNum();
        }
        return score;
    }
}