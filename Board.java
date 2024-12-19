package mines;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Board extends JPanel {
   private static final long serialVersionUID = 6195235521361212179L;
   private static final int numImages = 13;
   private static final int cellSize = 15;
   private static final int coverOverForCell = 10;
   private static final int markForCell = 10;
   private static final int EmptyCell = 0;
   private static final int mineCell = 9;
   private static final int coveredMineCell = 19;
   private static final int markedMineCell = 29;
   private static final int drawMine = 9;
   private static final int drawCover = 10;
   private final int drawMark = 11;
   private static final int drawWrongMark = 12;
   private int[] field;
   private boolean inGame;
   private int minesLeft;
   private transient Image[] img;
   private int mines = 40;
   private int rows = 16;
   private int cols = 16;
   private int allCells;
   private JLabel statusbar;

   public Board(JLabel statusbar) {
      this.statusbar = statusbar;
      this.img = new Image[13];
      String basePath = "/Users/macbook/Library/CloudStorage/OneDrive-univ-constantine2.dz/University/M2 ILSI S1/MEL/TP/TP MEL M2/JeuMines/images/";

      for(int i = 0; i < 13; ++i) {
         this.img[i] = (new ImageIcon(basePath + i + ".gif")).getImage();
      }

      this.setDoubleBuffered(true);
      this.addMouseListener(new mines.Board.MinesAdapter(this));
      this.newGame();
   }

   public void newGame() {
      Random random = new Random();
      this.inGame = true;
      this.minesLeft = this.mines;
      this.allCells = this.rows * this.cols;
      this.field = new int[this.allCells];

      int placedMines;
      for(placedMines = 0; placedMines < this.allCells; ++placedMines) {
         this.field[placedMines] = 10;
      }

      this.statusbar.setText(Integer.toString(this.minesLeft));
      placedMines = 0;

      while(placedMines < this.mines) {
         int position = random.nextInt(this.allCells);
         if (this.field[position] != 19) {
            this.field[position] = 19;
            ++placedMines;
            this.updateAdjacentCells(position);
         }
      }

   }

   private void updateAdjacentCells(int position) {
      int currentCol = position % this.cols;
      int[] directions = new int[]{-1 - this.cols, -this.cols, 1 - this.cols, -1, 1, this.cols - 1, this.cols, this.cols + 1};
      int[] var4 = directions;
      int var5 = directions.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         int direction = var4[var6];
         int adjacentPosition = position + direction;
         if (this.isValidCell(adjacentPosition, currentCol, direction) && this.field[adjacentPosition] != 19) {
            int var10002 = this.field[adjacentPosition]++;
         }
      }

   }

   private boolean isValidCell(int position, int currentCol, int direction) {
      if (position >= 0 && position < this.allCells) {
         int adjacentCol = position % this.cols;
         return Math.abs(currentCol - adjacentCol) <= 1;
      } else {
         return false;
      }
   }

   public void findEmptyCells(int index) {
      int currentCol = index % this.cols;
      int[] directions = new int[]{-1 - this.cols, -this.cols, 1 - this.cols, -1, 1, this.cols - 1, this.cols, this.cols + 1};
      int[] var4 = directions;
      int var5 = directions.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         int direction = var4[var6];
         int adjacentIndex = index + direction;
         if (this.isValidCell(adjacentIndex, currentCol, direction) && this.field[adjacentIndex] > 9) {
            int[] var10000 = this.field;
            var10000[adjacentIndex] -= 10;
            if (this.field[adjacentIndex] == 0) {
               this.findEmptyCells(adjacentIndex);
            }
         }
      }

   }

   public void paint(Graphics g) {
      int uncover = 0;

      for(int i = 0; i < this.rows; ++i) {
         for(int j = 0; j < this.cols; ++j) {
            int index = i * this.cols + j;
            int cell = this.field[index];
            if (this.inGame && cell == 9) {
               this.inGame = false;
            }

            cell = this.determineCellAppearance(cell);
            if (this.inGame && cell == 10) {
               ++uncover;
            }

            g.drawImage(this.img[cell], j * 15, i * 15, this);
         }
      }

      this.updateGameStatus(uncover);
   }

   private int determineCellAppearance(int cell) {
      if (!this.inGame) {
         if (cell == 19) {
            return 9;
         }

         if (cell == 29) {
            return 11;
         }

         if (cell > 19) {
            return 12;
         }

         if (cell > 9) {
            return 10;
         }
      } else {
         if (cell > 19) {
            return 11;
         }

         if (cell > 9) {
            return 10;
         }
      }

      return cell;
   }

   private void updateGameStatus(int uncover) {
      if (uncover == 0 && this.inGame) {
         this.inGame = false;
         this.statusbar.setText("Game won");
      } else if (!this.inGame) {
         this.statusbar.setText("Game lost");
      }

   }

   // $FF: synthetic method
   static boolean access$000(Board x0) {
      return x0.inGame;
   }

   // $FF: synthetic method
   static int access$100(Board x0) {
      return x0.cols;
   }

   // $FF: synthetic method
   static int access$200(Board x0) {
      return x0.rows;
   }

   // $FF: synthetic method
   static int[] access$300(Board x0) {
      return x0.field;
   }

   // $FF: synthetic method
   static int access$400(Board x0) {
      return x0.minesLeft;
   }

   // $FF: synthetic method
   static int access$410(Board x0) {
      return x0.minesLeft--;
   }

   // $FF: synthetic method
   static JLabel access$500(Board x0) {
      return x0.statusbar;
   }

   // $FF: synthetic method
   static int access$408(Board x0) {
      return x0.minesLeft++;
   }

   // $FF: synthetic method
   static boolean access$002(Board x0, boolean x1) {
      return x0.inGame = x1;
   }
}
