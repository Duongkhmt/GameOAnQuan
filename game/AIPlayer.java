
// package game;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.Random;

// public class AIPlayer {
//     private MainGame mainGame;
//     private int difficulty; // 0: Dễ, 1: Trung bình, 2: Khó
//     private static final int AI_TEAM = 1; // AI luôn là team 1

//     public AIPlayer(MainGame mainGame, int difficulty) {
//         this.mainGame = mainGame;
//         this.difficulty = difficulty;
//     }

//     public void makeMove() {
//         try {
//             Thread.sleep(1000);
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }

//         if (mainGame.process.total(AI_TEAM) == 0) {
//             mainGame.nextTurn();
//             return;
//         }

//         int pos = findBestMove();
//         System.out.println("AI selected position: " + pos);
//         if (pos >= 6 && pos <= 10) { // Chỉ cho phép ô của team 1
//             System.out.println("AI is moving position: " + pos);
//             mainGame.process.move(pos, 1, false);
//         } else {
//             System.out.println("Invalid move by AI: Position " + pos + " is not in AI's range (6-10)");
//         }
//     }

//     private int findBestMove() {
//         switch (difficulty) {
//             case 0: // Dễ
//                 return findEasyMove();
//             case 1: // Trung bình
//                 return findMediumMove();
//             case 2: // Khó
//                 return findHardMove();
//             default:
//                 return findEasyMove();
//         }
//     }

//     // Mức dễ: Chọn ngẫu nhiên ô có quân trong các ô của AI (6-10)
//     private int findEasyMove() {
//         Random rand = new Random();
//         List<Integer> validMoves = new ArrayList<>();
//         for (int i = 6; i <= 10; i++) { // Chỉ chọn ô dân của team 1
//             if (mainGame.process.box[i].getNum() > 0) {
//                 validMoves.add(i);
//             }
//         }
//         return validMoves.isEmpty() ? -1 : validMoves.get(rand.nextInt(validMoves.size()));
//     }

//     // Mức trung bình: Ưu tiên nước đi ăn được quân
//     private int findMediumMove() {
//         for (int i = 6; i <= 10; i++) {
//             if (mainGame.process.box[i].getNum() > 0) {
//                 int steps = mainGame.process.box[i].getNum();
//                 int nextPos = mainGame.process.calNewPos(i, steps + 1);
//                 if (mainGame.process.box[nextPos].getNum() == 0) {
//                     int nextNextPos = mainGame.process.calNewPos(nextPos, 1);
//                     if (mainGame.process.box[nextNextPos].getNum() > 0 && nextNextPos != 5 && nextNextPos != 11) {
//                         return i; // Ưu tiên nước đi ăn được quân (trừ ô quan)
//                     }
//                 }
//             }
//         }
//         return findEasyMove(); // Nếu không ăn được, chọn ngẫu nhiên
//     }

//     // Mức khó: Tìm nước đi tối ưu dựa trên điểm số tiềm năng
//     private int findHardMove() {
//         int bestMove = -1;
//         int maxScore = -1;

//         for (int i = 6; i <= 10; i++) {
//             if (mainGame.process.box[i].getNum() > 0) {
//                 int score = evaluateMove(i);
//                 if (score > maxScore) {
//                     maxScore = score;
//                     bestMove = i;
//                 }
//             }
//         }
//         return bestMove == -1 ? findEasyMove() : bestMove;
//     }

//     // Đánh giá nước đi cho mức khó
//     private int evaluateMove(int pos) {
//         int score = 0;
//         int numStones = mainGame.process.box[pos].getNum();
//         int currentPos = pos;

//         for (int i = 1; i <= numStones; i++) {
//             currentPos = mainGame.process.calNewPos(currentPos, 1);
//         }

//         int eatScore = 0;
//         int checkPos = mainGame.process.calNewPos(currentPos, 1);
//         while (checkPos != 5 && checkPos != 11 &&
//                 mainGame.process.box[currentPos].getNum() == 0 &&
//                 mainGame.process.box[checkPos].getNum() > 0) {

//             eatScore += mainGame.process.box[checkPos].getNum();
//             currentPos = mainGame.process.calNewPos(checkPos, 1);
//             checkPos = mainGame.process.calNewPos(currentPos, 1);
//         }

//         score += eatScore;

//         // Ưu tiên các ô có nhiều đá để tạo chuỗi rải lớn
//         score += numStones / 2;

//         return score;
//     }

// }
package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer {
    private MainGame mainGame;
    private int difficulty; // 0: Dễ, 1: Trung bình, 2: Khó
    private static final int AI_TEAM = 1;

    public AIPlayer(MainGame mainGame, int difficulty) {
        this.mainGame = mainGame;
        this.difficulty = difficulty;
    }

    public void makeMove() {
        try {
            Thread.sleep(1000); // Delay tạo cảm giác suy nghĩ
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (mainGame.process.total(AI_TEAM) == 0) {
            mainGame.nextTurn(); // Không có quân, bỏ lượt
            return;
        }

        int pos = findBestMove();
        System.out.println("AI selected position: " + pos);
        if (pos >= 6 && pos <= 10) {
            mainGame.process.move(pos, 1, false);
        } else {
            System.out.println("Invalid move by AI: Position " + pos + " is not in AI's range (6-10)");
        }
    }

    private int findBestMove() {
        switch (difficulty) {
            case 0:
                return findEasyMove();
            case 1:
                return findMediumMove();
            case 2:
                return findMinimaxMove(); // Minimax cho độ khó cao
            default:
                return findEasyMove();
        }
    }

    private int findEasyMove() {
        Random rand = new Random();
        List<Integer> validMoves = new ArrayList<>();
        for (int i = 6; i <= 10; i++) {
            if (mainGame.process.box[i].getNum() > 0) {
                validMoves.add(i);
            }
        }
        return validMoves.isEmpty() ? -1 : validMoves.get(rand.nextInt(validMoves.size()));
    }

    private int findMediumMove() {
        for (int i = 6; i <= 10; i++) {
            if (mainGame.process.box[i].getNum() > 0) {
                int steps = mainGame.process.box[i].getNum();
                int nextPos = mainGame.process.calNewPos(i, steps + 1);
                if (mainGame.process.box[nextPos].getNum() == 0) {
                    int nextNextPos = mainGame.process.calNewPos(nextPos, 1);
                    if (mainGame.process.box[nextNextPos].getNum() > 0 && nextNextPos != 5 && nextNextPos != 11) {
                        return i;
                    }
                }
            }
        }
        return findEasyMove();
    }

    // ✅ Mức độ khó: dùng thuật toán Minimax với Alpha-Beta
    private int findMinimaxMove() {
        GameState currentState = new GameState(mainGame.process, AI_TEAM);

        int bestMove = -1;
        int bestValue = Integer.MIN_VALUE;

        for (int move : currentState.getValidMoves(AI_TEAM)) {
            GameState nextState = currentState.clone();
            nextState.applyMove(move, AI_TEAM);
            int value = minimax(nextState, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        }

        return bestMove == -1 ? findEasyMove() : bestMove;
    }

    private int minimax(GameState state, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || state.isTerminal()) {
            return state.evaluate(AI_TEAM);
        }

        int team = maximizingPlayer ? AI_TEAM : 1 - AI_TEAM;
        List<Integer> moves = state.getValidMoves(team);

        if (moves.isEmpty()) {
            return state.evaluate(AI_TEAM); // Không còn nước đi
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int move : moves) {
                GameState next = state.clone();
                next.applyMove(move, team);
                int eval = minimax(next, depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha)
                    break;
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int move : moves) {
                GameState next = state.clone();
                next.applyMove(move, team);
                int eval = minimax(next, depth - 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha)
                    break;
            }
            return minEval;
        }
    }
}
