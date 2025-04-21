package game;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    public int[] box = new int[12]; // 12 ô trên bàn
    public int[] score = new int[2]; // score[0]: người, score[1]: AI
    public int currentTeam;

    public GameState(int[] box, int[] score, int currentTeam) {
        this.box = box.clone();
        this.score = score.clone();
        this.currentTeam = currentTeam;
    }

    // Khởi tạo từ Process hiện tại
    public GameState(Process process, int currentTeam) {
        for (int i = 0; i < 12; i++) {
            this.box[i] = process.box[i].getNum();
        }

        this.score = new int[] {
                process.scBox[0].getNum(),
                process.scBox[1].getNum()
        };

        this.currentTeam = currentTeam;
    }

    public List<Integer> getValidMoves(int team) {
        List<Integer> moves = new ArrayList<>();
        int start = team == 0 ? 0 : 6;
        int end = team == 0 ? 4 : 10;
        for (int i = start; i <= end; i++) {
            if (box[i] > 0) {
                moves.add(i);
            }
        }
        return moves;
    }

    public int total(int team) {
        int sum = 0;
        int start = team == 0 ? 0 : 6;
        int end = team == 0 ? 4 : 10;
        for (int i = start; i <= end; i++) {
            sum += box[i];
        }
        return sum;
    }

    public boolean isTerminal() {
        return total(0) == 0 || total(1) == 0;
    }

    // clone deep
    public GameState clone() {
        return new GameState(this.box, this.score, this.currentTeam);
    }

    // Tính điểm (cho Minimax)
    public int evaluate(int team) {
        return score[team] - score[1 - team];
    }

    // Áp dụng 1 nước đi giả lập (đơn giản hóa từ process.move)
    public void applyMove(int pos, int team) {
        int stones = box[pos];
        if (stones == 0)
            return;
        box[pos] = 0;

        int current = pos;
        // Rải quân
        for (int i = 0; i < stones; i++) {
            current = (current + 1) % 12;
            box[current]++;
        }

        // Ăn quân nếu có thể
        int next = (current + 1) % 12;
        while (box[current] == 0 && box[next] > 0 && next != 5 && next != 11) {
            score[team] += box[next];
            box[next] = 0;
            current = (next + 1) % 12;
            next = (current + 1) % 12;
        }

        // Nếu sau lượt này hết quân thì giả lập rải lại từ score
        if (total(team) == 0) {
            int toFill = Math.min(5, score[team]);
            int start = team == 0 ? 0 : 6;
            int end = team == 0 ? 4 : 10;
            for (int i = start; i <= end && toFill > 0; i++) {
                box[i]++;
                toFill--;
                score[team]--;
            }
        }

        currentTeam = 1 - team; // Đổi lượt
    }
}
