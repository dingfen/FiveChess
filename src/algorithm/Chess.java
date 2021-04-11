package algorithm;


public class Chess {
	
	// 棋子类型
	public enum Type {
		NoChess,
		BlackChess,
		WhiteChess
	};
	
	Type chessBoard[][];
	int width;
	int height;
	
	public Chess() {
		width = application.AppConfig.WIDTH;
		height = application.AppConfig.HEIGHT;
		chessBoard = new Type[height][width];
		
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				chessBoard[i][j] = Type.NoChess;
	}
	
	public void show() {
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				if (chessBoard[i][j]!= Type.NoChess) {
					System.out.printf("%d %d is ",i,j);
					System.out.println(chessBoard[i][j]);
				}
	}
	
	// 判断是否超出界限 不允许在棋盘边缘处下棋
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isonEdge(int x, int y) {
		if (x < 0 || x > height-2 || y < 0 || y > width-2)
			return true;
		else return false;
	}
	
	public double evaluate() {
		double scoreBlack = 0.0;
		double scoreWhite = 0.0;
		
		// 黑棋情况
		scoreBlack += scoreOfAntiOblique(Type.BlackChess, Type.WhiteChess);
		scoreBlack += scoreOfHorizontal(Type.BlackChess, Type.WhiteChess);
		scoreBlack += scoreOfOblique(Type.BlackChess, Type.WhiteChess);
		scoreBlack += scoreOfVertical(Type.BlackChess, Type.WhiteChess);
		
		// 白棋情况
		scoreWhite += scoreOfAntiOblique(Type.WhiteChess, Type.BlackChess);
		scoreWhite += scoreOfHorizontal(Type.WhiteChess, Type.BlackChess);
		scoreWhite += scoreOfOblique(Type.WhiteChess, Type.BlackChess);
		scoreWhite += scoreOfVertical(Type.WhiteChess, Type.BlackChess);
		
		return scoreWhite - scoreBlack*1.5;
	}
	
	/**
	 * 
	 * @param begin one line chess begin point
	 * @param end   one line chess end point
	 * @param len   line chess length
	 * @param type  line chess type
	 * @param chessType  chess of who's
	 * @return the accumulate score for evaluating chess
	 */
	public double addScore(Tuple begin, Tuple end, int len, int type, Type chessType) {
		boolean blocked = false;
		boolean doubleblocked = false;
		
		if (chessBoard[end.x][end.y] != Type.NoChess)
			blocked = true;
		else blocked = false;
		
		switch (type) {
		case 0:
			if (!isonEdge(begin.x - 1, begin.y + 1) 
					&& chessBoard[begin.x - 1][begin.y + 1] != Type.NoChess) {
				if (blocked)
					doubleblocked = true;
				else
					blocked = true;
			}
			break;
		case 1: 	// Vertical
			if (!isonEdge(begin.x - 1, begin.y) 
					&& chessBoard[begin.x - 1][begin.y] != Type.NoChess) {
				if (blocked)
					doubleblocked = true;
				else
					blocked = true;
			}
			break;
		case 2:
			if (!isonEdge(begin.x - 1, begin.y - 1) 
					&& chessBoard[begin.x - 1][begin.y - 1] != Type.NoChess) {
				if (blocked)
					doubleblocked = true;
				else
					blocked = true;
			}
			break;
		case 3:		// Horizon
			if (!isonEdge(begin.x, begin.y - 1) 
					&& chessBoard[begin.x][begin.y - 1] != Type.NoChess) {
				if (blocked)
					doubleblocked = true;
				else
					blocked = true;
			}
			break;
		default:
			break;
		}
		
		switch (len) {
		case 2:
			if (doubleblocked) return 0.0;
			else if (blocked) return 0.1;
			else return 0.5;
		case 3:
			if (doubleblocked) return 0.0;
			else if (blocked) return 8.0;
			else return 100;
		case 4:
			if (doubleblocked) return 0.0;
			else if (blocked) return 2000.0;
			else return 50000.0;
		case 5:
			return 5000000.0;
		default:
			return 0.0;
		}
	}

	boolean isWinning(Type type) {
		int i, j, k;
		int len = 0;
		boolean isbegin;
		boolean iswining = false;
		
		// 横向
		for (i = 0; i < height-1; i++) {
			isbegin = false;
			len = 0;
			for (j = 0; j < width-1; j++) {
				if (isbegin) {
					if (chessBoard[i][j] == type)
						len++;
					else {
						isbegin = false;
						if (len > 4)
							iswining = true;
						len = 0;
					}
				}
				else {
					if (chessBoard[i][j] == type) {
						isbegin = true;
						len = 1;
					}
					else {
						isbegin = false;
						len = 0;
					}
				}
			}
		}
		if (iswining)	return iswining;
		
		// 竖向
		for (i = 0; i < height-1; i++) {
			isbegin = false;
			len = 0;
			for (j = 0; j < width-1; j++) {
				if (isbegin) {
					if (chessBoard[j][i] == type)
						len++;
					else {
						isbegin = false;
						if (len > 4)
							iswining = true;
						len = 0;
					}
				}
				else {
					if (chessBoard[j][i] == type) {
						isbegin = true;
						len = 1;
					}
					else {
						isbegin = false;
						len = 0;
					}
				}
			}
		}
		if (iswining)	return iswining;
		
		// / 方向
		for (i = 0; i < width+height-3; i++) {
			isbegin = false;
			len = 0;
			int begin;
			if (i < height-1) begin = 0;
			else begin = i - height + 2;
			for (j = begin, k= i-begin; j < width-1 && k>=0; j++, k--) {
				if (isbegin) {
					if (chessBoard[j][k] == type)
						len++;
					else {
						isbegin = false;
						if (len > 4)
							iswining = true;
						len = 0;
					}
				}
				else {
					if (chessBoard[j][k] == type) {
						isbegin = true;
						len = 1;
					}
					else {
						isbegin = false;
						len = 0;
					}
				}
			}
		}
		if (iswining)	return iswining;
		
		// \ 方向
		for (i = -height+2; i < height-1; i++) {
			isbegin = false;
			len = 0;
			if (i < 0)
				for(j = -i, k = 0; j < width-1; j++, k++) {
					if (isbegin) {
						if (chessBoard[j][k] == type)
							len++;
						else {
							isbegin = false;
							if (len > 4)
								iswining = true;
							len = 0;
						}
					}
					else {
						if (chessBoard[j][k] == type) {
							isbegin = true;
							len = 1;
						}
						else {
							isbegin = false;
							len = 0;
						}
					}
				}
			else {
				for (j = 0, k = i; k < width-1; j++,k++) {
					if (isbegin) {
						if (chessBoard[j][k] == type)
							len++;
						else {
							isbegin = false;
							if (len > 4)
								iswining = true;
							len = 0;
						}
					}
					else {
						if (chessBoard[j][k]==type) {
							isbegin = true;
							len = 1;
						}
						else {
							isbegin = false;
							len = 0;
						}
					}
				}
			}
		}
		
		isbegin = false;
		len = 0;
		return iswining;
	}
	
	// 横向 算分
	double scoreOfHorizontal(Type me, Type you) {
		int i,j;
		int len;
		boolean isbegin;
		boolean isNoChess;
		Tuple begin = new Tuple();
		Tuple end = new Tuple();
		double score = 0.0;
		
		for(i = 0; i < height-1; i++) {
			j = 0;
			isbegin = false;
			isNoChess = false;
			len = 0;
			while (j < width-1) {
				if (!isbegin) {
					if (chessBoard[i][j] == me) {
						// 我方棋子
						isbegin = true;
						begin.setPoint(i, j);
						isNoChess = false;
						len = 1;
					}
					else {
						isbegin = false;
						isNoChess = false;
					}
					j++;
				}
				else {
					if (chessBoard[i][j]== you) {
						isbegin = false;
						end.setPoint(i, j);
						isNoChess = false;
						
						score += addScore(begin, end, len, 3, me);
						len = 0;
					}
					else if (chessBoard[i][j] == me){
						isbegin = true;
						isNoChess = false;
						len++;
					}
					else if (chessBoard[i][j] == Type.NoChess) {
						if (isNoChess) {
							isbegin =false;
							end.setPoint(i, j);
							score += addScore(begin, end, len, 3, me);
							len = 0;
						}
						else isNoChess = true;
					}
					j++;
				}
			}
		}
		return score;
	}
	
	// 竖向算分
	double scoreOfVertical(Type me, Type you) {
		int i,j;
		int len;
		boolean isbegin;
		boolean isNoChess;
		Tuple begin = new Tuple();
		Tuple end = new Tuple();
		double score = 0.0;
		
		for(i = 0; i < width-1; i++) {
			j = 0;
			isbegin = false;
			isNoChess = false;
			len = 0;
			while (j < height-1) {
				if (!isbegin) {
					if (chessBoard[j][i] == me) {
						// 我方棋子
						isbegin = true;
						begin.setPoint(j, i);
						isNoChess = false;
						len = 1;
					}
					else {
						isbegin = false;
						isNoChess = false;
					}
					j++;
				}
				else {
					if (chessBoard[j][i]== you) {
						isbegin = false;
						end.setPoint(j, i);
						isNoChess = false;
						
						score += addScore(begin, end, len, 1, me);
						len = 0;
					}
					else if (chessBoard[j][i] == me){
						isbegin = true;
						isNoChess = false;
						len++;
					}
					else if (chessBoard[j][i] == Type.NoChess) {
						if (isNoChess) {
							isbegin =false;
							end.setPoint(j, i);
							score += addScore(begin, end, len, 1, me);
							len = 0;
						}
						else isNoChess = true;
					}
					j++;
				}
			}
		}
		return score;
	}

	// /方向算分
	double scoreOfOblique(Type me, Type you) {
		int i,j,k;
		int len;
		double score = 0.0;
		Boolean isbegin;
		Boolean isNoChess;
		Tuple begin = new Tuple();
		Tuple end = new Tuple();
		
		for (i = 0; i < width+height-3; i++) {
			isbegin =false;
			isNoChess = false;
			len = 0;
			int beg;
			if (i < height-1) beg = 0;
			else beg = i - height+2;
			for(j = beg, k = i-beg; j < width-1 && k >= 0; j++, k--) {
				if (!isbegin) {
					if (chessBoard[j][k]== me) {
						isbegin = true;
						begin.setPoint(j, k);
						isNoChess = false;
						len = 1;
					}
					else {
						isbegin = false;
						isNoChess = false;
					}
				}
				else {
					if (chessBoard[j][k]== me) {
						isbegin = true;
						isNoChess = false;
						len++;
					}
					else if (chessBoard[j][k]==you) {
						isbegin = false;
						isNoChess = false;
						end.setPoint(j, k);
						score += addScore(begin, end, len, 0, me);
						len = 0;
					}
					else if (chessBoard[j][k] == Type.NoChess) {
						if (isNoChess) {
							isbegin = false;
							end.setPoint(j, k);
							score += addScore(begin, end, len, 0, me);
							len = 0;
						}
						else isNoChess = true;
					}
				}
			}
		}
		return score;
	}

	// \ 方向算分
	double scoreOfAntiOblique(Type me, Type you) {
		int i,j,k;
		int len;
		boolean isbegin;
		boolean isNoChess;
		double score = 0.0;
		Tuple begin = new Tuple();
		Tuple end = new Tuple();
		
		
		for(i = -height+2; i < height-1; i++) {
			isbegin = false;
			isNoChess = false;
			len = 0;
			if (i < 0) {
				for(j=-i, k=0;j < height-1;j++,k++) {
					if (!isbegin) {
						if (chessBoard[j][k] == me) {
							isbegin = true;
							begin.setPoint(j, k);
							isNoChess = false;
							len = 1;
						}
						else {
							isbegin = false;
							isNoChess = false;
						}
					}
					else {
						if (chessBoard[j][k] == me) {
							isbegin = true;
							isNoChess = false;
							len++;
						}
						else if (chessBoard[j][k] == you) {
							isbegin = false;
							isNoChess = false;
							end.setPoint(j, k);
							score += addScore(begin, end, len, 2, me);
							len = 0;
						}
						else if (chessBoard[j][k] == Type.NoChess) {
							if (isNoChess) {
								isbegin = false;
								end.setPoint(j, k);
								score += addScore(begin, end, len, 2, me);
								len = 0;
							}
							else isNoChess = true;
						}
					}
				}
			}
			else {
				for (j = 0,k = i;k < width-1; j++,k++) {
					if(!isbegin) {
						if (chessBoard[j][k]== me) {
							isbegin = true;
							begin.setPoint(j, k);
							isNoChess = false;
							len = 1;
						}
						else {
							isbegin = false;
							isNoChess = false;
						}
					}
					else {
						if (chessBoard[j][k]== me) {
							isbegin = true;
							isNoChess = false;
							len++;
						}
						else if (chessBoard[j][k] == you) {
							isbegin = false;
							isNoChess = false;
							end.setPoint(j, k);
							score += addScore(begin, end, len, 2, me);
							len = 0;
						}
						else if (chessBoard[j][k] == Type.NoChess) {
							if (isNoChess) {
								isbegin = false;
								end.setPoint(j, k);
								score += addScore(begin, end, len, 2, me);
								len = 0;
							}
							else isNoChess = true;
						}
					}
				}
			}
		}
		return score;
	}
	
	public static void main(String[] args) {
		Chess chess = new Chess();
		chess.isWinning(Type.BlackChess);
	}
}
