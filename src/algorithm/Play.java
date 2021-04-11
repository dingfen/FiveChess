package algorithm;

import java.util.ArrayList;

import algorithm.Chess.Type;

public class Play {
	
	public Chess chess;
	
	public Play(Chess chess) {
		super();
		this.chess = chess;
	}

	public boolean addChess(Tuple pos, Type me) {
		if (!chess.isonEdge(pos.x, pos.y) && chess.chessBoard[pos.x][pos.y] == Type.NoChess) {
			chess.chessBoard[pos.x][pos.y] = me;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isManWinning() {
		return chess.isWinning(Type.BlackChess);
	}
	
	public boolean isAIWinning() {
		return chess.isWinning(Type.WhiteChess);
	}
	
	// 搜索步骤  不是真正的下棋
	void addPosition(Tuple pos, Type me) {
		if (chess.chessBoard[pos.x][pos.y] == Type.NoChess)
			chess.chessBoard[pos.x][pos.y] = me;
	}
	
	// 撤销搜索的步骤
	void undoPosition(Tuple pos, Type me) {
		if (chess.chessBoard[pos.x][pos.y] == me)
			chess.chessBoard[pos.x][pos.y] = Type.NoChess;
	}
	
	// 下一步下棋的动作 集合
	ArrayList<Tuple> getAllPositions() {
		ArrayList<Tuple> ret = new ArrayList<Tuple>();
		int up = Integer.MAX_VALUE;
		int down = Integer.MIN_VALUE;
		int right = Integer.MIN_VALUE;
		int left = Integer.MAX_VALUE;
		for(int i = 0; i < chess.height - 1; i++) {
			for (int j = 0; j < chess.width - 1; j++) {
				if (chess.chessBoard[i][j] != Type.NoChess) {
					if (up > i) up = i;
					if (down < i) down = i;
					if (right < j) right = j;
					if (left > j) left = j;
				}
			}
		}
		
		up = up - 1 > 0 ? up - 1 : 0;
		down = down + 1 < chess.height - 2 ? down + 1: chess.height - 2;
		left = left - 1 > 0 ? left - 1 : 0;
		right = right + 1 < chess.width - 2 ? right + 1:chess.width -2;
		for (int i = up; i <= down; i++)
			for (int j = left; j <= right; j++)
				if (chess.chessBoard[i][j]==Type.NoChess)
					ret.add(new Tuple(i, j));
		
		return ret;
	}
	
	Result maxValue(double min, double max, int n) {
		Result result = new Result();

		if (n > 2) {
			double v = chess.evaluate();
			result.value = v;
			return result;
		}
		result.value = -Double.MAX_VALUE;
		
		for (Tuple action : getAllPositions()) {
			addPosition(action, Type.WhiteChess);
			Result tmp = minValue(min, max, n+1);
			if (result.value < tmp.value) {
				result = tmp;
				result.postion = action;
			}
			if (result.value >= max) {
				undoPosition(action, Type.WhiteChess);
				return result;
			}
			min = min > result.value ? min : result.value;
			undoPosition(action, Type.WhiteChess);
		}
		return result;
	}
	
	Result minValue(double min, double max, int n) {
		Result result = new Result();

		if (n > 2) {
			double v = chess.evaluate();
			result.value = v;
			return result;
		}
		result.value = Double.MAX_VALUE;
		
		for(Tuple action : getAllPositions()) {
			addPosition(action, Type.BlackChess);
			Result tmp = maxValue(min, max, n+1);
			if (result.value > tmp.value) {
				result = tmp;
				result.postion = action;
			}
			if (result.value <= min) {
				undoPosition(action, Type.BlackChess);
				return result;
			}
			max = max < result.value ? max : result.value;
			undoPosition(action, Type.BlackChess);
		}
		return result;
	}
	
	public Result alphaBetaSearch() {
		Result result = maxValue(-Double.MAX_VALUE, Double.MAX_VALUE, 0);
		int x = result.postion.x;
		int y = result.postion.y;
		chess.chessBoard[x][y] = Type.WhiteChess; 
		return result;
	}
}
