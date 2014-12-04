package com.example.reversi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class playController {
	public int mTable[][][];//定义一个棋盘
	public int mStepNum;	//表示当前是第几步
	private int whoseTurn;	//表示该谁下棋了
	private int c,b;
	public int winner = 0;
//	private boolean mRecieved;//判断是否接受了一个信息
	boolean skipblack;
	boolean skipwhite;
	//构造函数
	playController(){
		mStepNum = 0;//从第0步开始
		mTable = new int[61][9][9];//最多可以走60步（初始化棋盘上已经有了4个棋子）
		whoseTurn = 0;
		c = b = 0;
//		mRecieved = false;
		skipwhite = false;
		skipblack = false;
		//对于某一位置mTable[k][i][j],其数值为0表示空，1表示白棋，2表示黑棋，3表示下一个人可以放棋子
		//初始化棋盘为空
		for(int k = 0; k < 60; k ++){
			for(int i = 0; i < 9; i ++){
				for (int j = 0; j < 9; j ++){
					mTable[k][i][j] = 0;
				}
			}
		}
		//设置棋盘初始时最中间的四个棋子
		mTable[0][3][3] = 1;
		mTable[0][3][4] = 2;
		mTable[0][4][3] = 2;
		mTable[0][4][4] = 1;
	}
	
	//用来判断某一方是否还有地方可以下棋，实际是在数标号为3的点的个数
	boolean judge2()
	{
		int i,j,num = 0;//num表示可以下棋位置的个数
		for ( i = 0; i < 8; i ++)
			for ( j = 0; j < 8; j ++)
				if ( mTable[mStepNum][i][j] == 3) num++;//cout << num << endl;
		if (num > 0)   return true;
		else return false;
	}
	
	int turn(int c,int b)//c表示第几行，b表示第几列
	{
		System.out.println("Turn function called! "+c+" "+b);
		int i,j,row,col;
		c++;
		b++;
		if ( mTable[mStepNum][c-1][b] != 0 && mTable[mStepNum][c-1][b] != 3 && mTable[mStepNum][c-1][b] != mTable[mStepNum][c-1][b-1])//往右找
			for( j = b+1; j < 8 ; j++)
			{
				if ( mTable[mStepNum][c-1][j] == 0 || mTable[mStepNum][c-1][j] == 3)  break;
				if ( mTable[mStepNum][c-1][j] == mTable[mStepNum][c-1][b-1])  
				{
					for( col = b; col < j; col ++)  mTable[mStepNum][c-1][col] = mTable[mStepNum][c-1][b-1];
					break;
				}
			}
		
		if ( b > 1 && mTable[mStepNum][c-1][b-2] != 0 && mTable[mStepNum][c-1][b-2] != 3 && mTable[mStepNum][c-1][b-2] != mTable[mStepNum][c-1][b-1])//往左找
			for( j = b - 3; j >= 0 ; j --)
			{
				if ( mTable[mStepNum][c-1][j] == 0 || mTable[mStepNum][c-1][j] == 3)  break;
				if ( mTable[mStepNum][c-1][j] == mTable[mStepNum][c-1][b-1])  
				{
					for( col = b -2; col > j; col --)  mTable[mStepNum][c-1][col] = mTable[mStepNum][c-1][b-1];
					break;
				}
			}
		
		if ( mTable[mStepNum][c][b-1] != 0 && mTable[mStepNum][c][b-1] != 3 && mTable[mStepNum][c][b-1] != mTable[mStepNum][c-1][b-1])//find downwards
			for ( i = c + 1; i < 8; i ++) 
			{
				if ( mTable[mStepNum][i][b-1] == 0 || mTable[mStepNum][i][b-1] == 3) break;
				if ( mTable[mStepNum][i][b-1] == mTable[mStepNum][c-1][b-1]) 
				{
					for ( row = c; row < i; row ++)  mTable[mStepNum][row][b-1] = mTable[mStepNum][c-1][b-1];
					break;
				}
			}
		if ( c>1&&mTable[mStepNum][c-2][b-1] != 0 && mTable[mStepNum][c-2][b-1] != 3 && mTable[mStepNum][c-2][b-1] != mTable[mStepNum][c-1][b-1])//find upwards
			for ( i = c - 3; i >= 0; i --) 
			{
				if ( mTable[mStepNum][i][b-1] == 0 || mTable[mStepNum][i][b-1] == 3) break;
				if ( mTable[mStepNum][i][b-1] == mTable[mStepNum][c-1][b-1]) 
				{
					for ( row = c -2; row > i; row --)  mTable[mStepNum][row][b-1] = mTable[mStepNum][c-1][b-1];
					break;
				}
			}
		if ( mTable[mStepNum][c][b] != 0 && mTable[mStepNum][c][b] != 3 && mTable[mStepNum][c][b] != mTable[mStepNum][c-1][b-1])//向右下方向找
			for ( i = c + 1, j = b + 1; i < 8 && j < 8; i ++, j ++) 
			{
				if ( mTable[mStepNum][i][j] == 0 || mTable[mStepNum][i][j] == 3 ) break;
				if ( mTable[mStepNum][i][j] == mTable[mStepNum][c-1][b-1]) 
				{
					for ( row = c,col = b; row < i && col < j; row ++,col ++)  mTable[mStepNum][row][col] = mTable[mStepNum][c-1][b-1];
					break;
				}
			}
		if (c>1&&b>1&& mTable[mStepNum][c-2][b-2] != 0 && mTable[mStepNum][c-2][b-2] != 3 && mTable[mStepNum][c-2][b-2] != mTable[mStepNum][c-1][b-1]) //向左上方向找
			for ( i = c - 3, j = b - 3; i >= 0 && j >= 0; i --, j --)
			{
				if ( mTable[mStepNum][i][j] == 0 || mTable[mStepNum][i][j] == 3 ) break;
				if ( mTable[mStepNum][i][j] == mTable[mStepNum][c-1][b-1]) 
				{
					for ( row = c - 2,col = b - 2; row > i && col > j; row --,col --)  mTable[mStepNum][row][col] = mTable[mStepNum][c-1][b-1];
					break;
				}
			}
		if ( b>1&&mTable[mStepNum][c][b-2] != 0 && mTable[mStepNum][c][b-2] != 3 && mTable[mStepNum][c][b-2] != mTable[mStepNum][c-1][b-1])//向左下方向找
			for ( i = c + 1, j = b - 3; i < 8 && j >= 0; i ++, j --) 
			{
				if ( mTable[mStepNum][i][j] == 0 || mTable[mStepNum][i][j] == 3 ) break;
				if ( mTable[mStepNum][i][j] == mTable[mStepNum][c-1][b-1]) 
				{
					for ( row = c,col = b - 2; row < i && col > j; row ++,col --)  mTable[mStepNum][row][col] = mTable[mStepNum][c-1][b-1];
					break;
				}
			}
		if (c>1&& mTable[mStepNum][c-2][b] != 0 && mTable[mStepNum][c-2][b] != 3 && mTable[mStepNum][c-2][b] != mTable[mStepNum][c-1][b-1])//向右上方向找
			for ( i = c  - 3, j = b + 1; i >= 0 && j < 8; i --, j ++) 
			{
				if ( mTable[mStepNum][i][j] == 0 || mTable[mStepNum][i][j] == 3 ) break;
				if ( mTable[mStepNum][i][j] == mTable[mStepNum][c-1][b-1]) 
				{
					for ( row = c - 2,col = b; row > i && col < j; row --,col ++)  mTable[mStepNum][row][col] = mTable[mStepNum][c-1][b-1];
					break;
				}
			}
		return 0;
	}
	
	//----------------------下黑棋----------------------------------------
	int gob(int c, int b)
	{	
		int i,j;
		/*	存盘读盘功能
		if (c==10 && b == 10)
		{
			if (go == 1) 
			{
				//通知黑方下棋
				return 0;
			}
			if (go == 2) 
			{
				//通知白方下棋
				return 0;
			}
			if (go == 0) {//读盘失败，需要特殊处理 }
		}*/
		/*	表示存盘，还未实现
		 * if ( c == 9 && b == 9 )  {go = 1;save();gob();return 0;}
		 */
		//之后要实现考虑在不合理位置放棋子的情况
		/*if (mTable[mStepNum][c-1][b-1] != 3) {cout << "此处不能放棋子!!  请重新输入!!" << endl;gob();return 0;}
		  if (judge1(c,b))  { gob();return 0;}//如果此处不能放棋子，重新下黑棋//k ++;*/
		mStepNum ++;
		for ( i = 0; i < 8 ; i ++ )
			for ( j = 0; j < 8 ; j ++ )
			{
				if ( i == c && j == b )  mTable[mStepNum][i][j] = 2;
				else mTable[mStepNum][i][j] = mTable[mStepNum-1][i][j];
			}
		turn(c,b);
		findw();//下完黑棋后寻找白棋可下的位置
		if (judge2()) whoseTurn = 1;
		else whoseTurn = 2;
		//此处要通知白方客户端
		//judge2();
		
			
		return 0;
	}
	
	//--------------------下白棋------------------------- 
	int gow(int c, int b)
	{
		int i,j;
		//cin >> c >> b ;
		//读盘功能
		//if ( c == 9 && b == 9 )  {go = 2;save();gow2();return 0;}
		/*	此处要实现下棋位置不合法的情况
		if (mTable[mStepNum][c-1][b-1] != 3) {cout << "此处不能放棋子!!  请重新输入!!" << endl;gow2();return 0;}
		*/
		
		mStepNum ++;
		for ( i = 0; i < 8 ; i ++ ){
			for ( j = 0; j < 8 ; j ++ )
			{
				if ( i == c && j == b )  mTable[mStepNum][i][j] = 1;
				else mTable[mStepNum][i][j] = mTable[mStepNum-1][i][j];
			}
		}
		turn(c,b);
		findb();
		if (judge2()) whoseTurn = 2;
		else whoseTurn = 1;
		return 0;
	}
	
	//--------------------找黑棋可下棋的位置-----------------
	int findb()
	{
		int c,b,i,j;
		for (i = 0; i < 8; i ++)//清除上一次可下棋的位置 
			for ( j = 0; j < 8; j ++)  if (mTable[mStepNum][i][j] == 3)  mTable[mStepNum][i][j] = 0;		
		for ( c = 1; c <= 8; c ++)//c表示第几行，b表示第几列
			for ( b = 1; b <= 8; b ++)
				if ( mTable[mStepNum][c-1][b-1] == 2)
				{
					if ( mTable[mStepNum][c-1][b] != 0 && mTable[mStepNum][c-1][b] != mTable[mStepNum][c-1][b-1] && mTable[mStepNum][c-1][b] != 3 )//往右找,旁边不是空的，也不和自己相同(即是不同颜色的棋)
						for( j = b+1; j < 8 ; j++)
						{
							if ( mTable[mStepNum][c-1][j] == 0)  //这个地方可以下棋
							{
								mTable[mStepNum][c-1][j] = 3;
								break;
							}
							if ( mTable[mStepNum][c-1][j] == mTable[mStepNum][c-1][b-1] || mTable[mStepNum][c-1][j] == 3)  break;	//如果隔一个或以上还有这个颜色的就无效
						}
					if ( b>1&&mTable[mStepNum][c-1][b-2] != 0 && mTable[mStepNum][c-1][b-2] != mTable[mStepNum][c-1][b-1] && mTable[mStepNum][c-1][b-2] != 3)//往左找
						for( j = b - 3; j >= 0 ; j --)
						{
							if ( mTable[mStepNum][c-1][j] == 0)   
							{
								mTable[mStepNum][c-1][j] = 3;
								break;
							}
							if ( mTable[mStepNum][c-1][j] == mTable[mStepNum][c-1][b-1] || mTable[mStepNum][c-1][j] == 3)  break;   //如果隔一个或以上还有这个颜色的就无效
						}
					if ( mTable[mStepNum][c][b-1] != 0 && mTable[mStepNum][c][b-1] != mTable[mStepNum][c-1][b-1] && mTable[mStepNum][c][b-1] != 3)//find downwards
						for ( i = c + 1; i < 8; i ++) 
						{
							if ( mTable[mStepNum][i][b-1] == 0) 
							{
								mTable[mStepNum][i][b-1] = 3;
								break;
							}
							if ( mTable[mStepNum][i][b-1] == mTable[mStepNum][c-1][b-1]  ||mTable[mStepNum][i][b-1] == 3)  break;//如果已经标记过则跳出循环
						}
					if ( c>1&&mTable[mStepNum][c-2][b-1] != 0 && mTable[mStepNum][c-2][b-1] != mTable[mStepNum][c-1][b-1] && mTable[mStepNum][c-2][b-1] != 3)//find upwards
						for ( i = c - 3; i >= 0; i --) 
						{
							if ( mTable[mStepNum][i][b-1] == 0) 
							{
								mTable[mStepNum][i][b-1] = 3;
								break;
							}
							if ( mTable[mStepNum][i][b-1] == mTable[mStepNum][c-1][b-1] || mTable[mStepNum][i][b-1] == 3)  break;
						}
					if ( mTable[mStepNum][c][b] != 0 && mTable[mStepNum][c][b] != mTable[mStepNum][c-1][b-1] && mTable[mStepNum][c][b] != 3)//向右下方向找
						for ( i = c + 1, j = b + 1; i < 8 && j < 8; i ++, j ++) 
						{
							if ( mTable[mStepNum][i][j] == 0) 
							{
								mTable[mStepNum][i][j] = 3;
								break;
							}
							if ( mTable[mStepNum][i][j] == mTable[mStepNum][c-1][b-1] || mTable[mStepNum][i][j] == 3 )  break;
						}
					if ( c>1&&b>1&&mTable[mStepNum][c-2][b-2] != 0 && mTable[mStepNum][c-2][b-2] != mTable[mStepNum][c-1][b-1] && mTable[mStepNum][c-2][b-2] != 3) //向左上方向找
						for ( i = c - 3, j = b - 3; i >= 0 && j >= 0; i --, j --)
						{
							if ( mTable[mStepNum][i][j] == 0) 
							{
								mTable[mStepNum][i][j] = 3;
								break;
							}
							if ( mTable[mStepNum][i][j] == mTable[mStepNum][c-1][b-1] || mTable[mStepNum][i][j] == 3 )  break;
						}
					if ( b>1&&mTable[mStepNum][c][b-2] != 0 && mTable[mStepNum][c][b-2] != mTable[mStepNum][c-1][b-1] && mTable[mStepNum][c][b-2] != 3)//向左下方向找
						for ( i = c + 1, j = b - 3; i < 8 && j >= 0; i ++, j --) 
						{
							if ( mTable[mStepNum][i][j] == 0) 
							{
								mTable[mStepNum][i][j] = 3;
								break;
							}
							if ( mTable[mStepNum][i][j] == mTable[mStepNum][c-1][b-1] || mTable[mStepNum][i][j] == 3)  break;
						}
					if ( c>1&&mTable[mStepNum][c-2][b] != 0 && mTable[mStepNum][c-2][b] != mTable[mStepNum][c-1][b-1] && mTable[mStepNum][c-2][b] != 3)//向右上方向找
						for ( i = c  - 3, j = b + 1; i >= 0 && j < 8; i --, j ++) 
						{
							if ( mTable[mStepNum][i][j] == 0) 
							{
								mTable[mStepNum][i][j] = 3;
								break;
							}
							if ( mTable[mStepNum][i][j] == mTable[mStepNum][c-1][b-1] || mTable[mStepNum][i][j] == 3)  break;
						}
				}
		return 0;
	}
	//-------------------------找白棋可下的位置------------------------------
	int findw()
	{
		int c,b,i,j;
		for (i = 0; i < 8; i ++)//清除上一次可下棋的位置 
			for ( j = 0; j < 8; j ++)  if (mTable[mStepNum][i][j] == 3)  mTable[mStepNum][i][j] = 0;
		for ( c = 1; c <= 8; c ++)
			for ( b = 1; b <= 8; b ++)
				if ( mTable[mStepNum][c-1][b-1] == 1)
				{
					if ( mTable[mStepNum][c-1][b] != 0 && mTable[mStepNum][c-1][b] != mTable[mStepNum][c-1][b-1] && mTable[mStepNum][c-1][b] != 3 )//往右找,旁边不是空的，也不和自己相同(即是不同颜色的棋)
						for( j = b+1; j < 8 ; j++)
						{
							if ( mTable[mStepNum][c-1][j] == 0)  //这个地方可以下棋
							{
								mTable[mStepNum][c-1][j] = 3;
								break;
							}
							if ( mTable[mStepNum][c-1][j] == mTable[mStepNum][c-1][b-1] || mTable[mStepNum][c-1][j] == 3)  break;	//如果隔一个或以上还有这个颜色的就无效
						}
					if ( b>1&&mTable[mStepNum][c-1][b-2] != 0 && mTable[mStepNum][c-1][b-2] != mTable[mStepNum][c-1][b-1] && mTable[mStepNum][c-1][b-2] != 3)//往左找
						for( j = b - 3; j >= 0 ; j --)
						{
							if ( mTable[mStepNum][c-1][j] == 0)   
							{
								mTable[mStepNum][c-1][j] = 3;
								break;
							}
							if ( mTable[mStepNum][c-1][j] == mTable[mStepNum][c-1][b-1] || mTable[mStepNum][c-1][j] == 3)  break;   //如果隔一个或以上还有这个颜色的就无效
						}
					if ( mTable[mStepNum][c][b-1] != 0 && mTable[mStepNum][c][b-1] != mTable[mStepNum][c-1][b-1] && mTable[mStepNum][c][b-1] != 3)//find downwards
						for ( i = c + 1; i < 8; i ++) 
						{
							if ( mTable[mStepNum][i][b-1] == 0) 
							{
								mTable[mStepNum][i][b-1] = 3;
								break;
							}
							if ( mTable[mStepNum][i][b-1] == mTable[mStepNum][c-1][b-1]  ||mTable[mStepNum][i][b-1] == 3)  break;//如果已经标记过则跳出循环
						}
					if ( c>1&&mTable[mStepNum][c-2][b-1] != 0 && mTable[mStepNum][c-2][b-1] != mTable[mStepNum][c-1][b-1] && mTable[mStepNum][c-2][b-1] != 3)//find upwards
						for ( i = c - 3; i >= 0; i --) 
						{
							if ( mTable[mStepNum][i][b-1] == 0) 
							{
								mTable[mStepNum][i][b-1] = 3;
								break;
							}
							if ( mTable[mStepNum][i][b-1] == mTable[mStepNum][c-1][b-1] || mTable[mStepNum][i][b-1] == 3)  break;
						}
					if ( mTable[mStepNum][c][b] != 0 && mTable[mStepNum][c][b] != mTable[mStepNum][c-1][b-1] && mTable[mStepNum][c][b] != 3)//向右下方向找
						for ( i = c + 1, j = b + 1; i < 8 && j < 8; i ++, j ++) 
						{
							if ( mTable[mStepNum][i][j] == 0) 
							{
								mTable[mStepNum][i][j] = 3;
								break;
							}
							if ( mTable[mStepNum][i][j] == mTable[mStepNum][c-1][b-1] || mTable[mStepNum][i][j] == 3 )  break;
						}
					if (c>1&&b>1&& mTable[mStepNum][c-2][b-2] != 0 && mTable[mStepNum][c-2][b-2] != mTable[mStepNum][c-1][b-1] && mTable[mStepNum][c-2][b-2] != 3) //向左上方向找
						for ( i = c - 3, j = b - 3; i >= 0 && j >= 0; i --, j --)
						{
							if ( mTable[mStepNum][i][j] == 0) 
							{
								mTable[mStepNum][i][j] = 3;
								break;
							}
							if ( mTable[mStepNum][i][j] == mTable[mStepNum][c-1][b-1] || mTable[mStepNum][i][j] == 3 )  break;
						}
					if (b>1&& mTable[mStepNum][c][b-2] != 0 && mTable[mStepNum][c][b-2] != mTable[mStepNum][c-1][b-1] && mTable[mStepNum][c][b-2] != 3)//向左下方向找
						for ( i = c + 1, j = b - 3; i < 8 && j >= 0; i ++, j --) 
						{
							if ( mTable[mStepNum][i][j] == 0) 
							{
								mTable[mStepNum][i][j] = 3;
								break;
							}
							if ( mTable[mStepNum][i][j] == mTable[mStepNum][c-1][b-1] || mTable[mStepNum][i][j] == 3)  break;
						}
					if (c>1&& mTable[mStepNum][c-2][b] != 0 && mTable[mStepNum][c-2][b] != mTable[mStepNum][c-1][b-1] && mTable[mStepNum][c-2][b] != 3)//向右上方向找
						for ( i = c  - 3, j = b + 1; i >= 0 && j < 8; i --, j ++) 
						{
							if ( mTable[mStepNum][i][j] == 0) 
							{
								mTable[mStepNum][i][j] = 3;
								break;
							}
							if ( mTable[mStepNum][i][j] == mTable[mStepNum][c-1][b-1] || mTable[mStepNum][i][j] == 3)  break;
						}
				}
		return 0;
	}
	
	
	
	public boolean judge() {
		// TODO Auto-generated method stub
		int numb = 0;
		int white=0,black=0;
		int winlose = 0;
		findb();
		for (int i = 0; i < 8; i ++)
			for (int j = 0; j < 8; j ++){
				if (mTable[mStepNum][i][j] == 1) 
				{
					white ++;
				}
				if (mTable[mStepNum][i][j] == 2)
				{
					black ++;
				}
				if (mTable[mStepNum][i][j] == 3)  numb ++;
			}
		if (numb == 0) 
		{
			findw();
			for (int i = 0 ; i < 8 ; i++)
				for (int j = 0; j < 8; j ++)
					if (mTable[mStepNum][i][j] == 3) numb ++;
		}
		if (numb == 0)
		{
			if ( black > white)   winner = 2;		
			if ( black == white)   winner = 3;
			if ( black < white)   winner = 1;
			return true;//表示游戏结束
		}
		return false;//表示游戏还没有结束
	}

	
	
	public void printinfo()
	{
		for (int i = 0; i < 8; i ++){
			for (int j = 0; j < 8; j ++)
				System.out.print(mTable[mStepNum][i][j]+" ");
			System.out.print("\n");
		}
	}
	

}
