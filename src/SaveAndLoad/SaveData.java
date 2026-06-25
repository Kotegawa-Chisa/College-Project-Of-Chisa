package SaveAndLoad;
import model.Cell;
import model.GameBoard;
import model.Position;
public class SaveData {
    //以下是一些传入的数据

    private GameBoard gameBoard;
    private int score;
    private int remainingTime;
    private int usedTime;
    private String difficulty;
    private int totalPairs;
    private int eliminatedPairs;
    private String saveTime;

    //构造器
    public SaveData(GameBoard gameBoard, int score, int remainingTime, int usedTime, String difficulty, int totalPairs, int eliminatedPairs) {
        this.gameBoard = gameBoard;
        this.score = score;
        this.remainingTime = remainingTime;
        this.usedTime = usedTime;
        this.difficulty = difficulty;
        this.totalPairs = totalPairs;
        this.eliminatedPairs = eliminatedPairs;
        this.saveTime = "";
    }

    //下面是一堆 getter和setter
    public GameBoard getGameBoard() {
        return gameBoard;
    }
    public int getTime() {
        return remainingTime;
    }
    public int getScore() {
        return score;
    }
    public int getUsedTime() {return usedTime;}
    public int getEliminatedPairs() {return eliminatedPairs;}
    public int getTotalPairs() {return totalPairs;}
    public String getDifficulty() {
        return difficulty;
    }
    //difficulty被弃用了，不用管
    public String getSaveTime() {
        return saveTime;
    }
    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }
    @Override
    //我们通过转换为 text来进行
    //转为存档字符串,格式：
    //第1行：难度,分数,剩余时间,已用时间,总对数,已消除对数
    //第2行：棋盘行数,棋盘列数
    //3-9行：棋盘每一行，有为 0.没有为 1,每个1/0后面都有一个图标
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(difficulty).append(",").append(score).append(",").append(remainingTime).append(",").append(usedTime).append(",").append(totalPairs).append(",").append(eliminatedPairs).append(",").append(saveTime).append("\n");
        sb.append(gameBoard.getRowCnt()).append(",").append(gameBoard.getColCnt()).append("\n");
        for(int r = 0; r < gameBoard.getRowCnt(); r++){
            for(int c = 0; c < gameBoard.getColCnt(); c++){
                Cell cell = gameBoard.getCell(r,c);
                sb.append(cell.isEmpty() ? 1 : 0).append(",").append(cell.getIconIndex());
                if(c < gameBoard.getColCnt() - 1){
                    sb.append(";");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }


    //解析存档字符串，方法同上
    public static SaveData fromString(String str){
        if(str == null||str.trim().isEmpty()){//trim():去除空白字符
            return null;
        }
        String[] lines = str.split("\n");//以行做区分
        if (lines.length < 3){
            return null;
        }
        String[] firstLineData = lines[0].split(",");
        if(firstLineData.length < 3){
            return null;
        }
        int score;
        int time;
        int remainingTime;
        int usedTime=0;
        int eliminatedPairs=0;
        int totalPairs=0;
        String saveTime = "";
        String difficulty=firstLineData[0];
        try{
            score = Integer.parseInt(firstLineData[1]);
            remainingTime = Integer.parseInt(firstLineData[2]);
            if(firstLineData.length >=7){
                usedTime = Integer.parseInt(firstLineData[3]);
                totalPairs = Integer.parseInt(firstLineData[4]);
                eliminatedPairs = Integer.parseInt(firstLineData[5]);
                saveTime = firstLineData[6];
            }
        }catch(NumberFormatException e){
            return null;
        }
        String[] secondLineData = lines[1].split(",");
        if(secondLineData.length != 2){
            return null;
        }
        //此处通过try/catch，一步步进行拆分
        int row;
        int col;
        try{
            row = Integer.parseInt(secondLineData[0]);
            col = Integer.parseInt(secondLineData[1]);
        }catch(NumberFormatException e){
            return null;
        }
        if(lines.length != row + 2) {
            return null;
        }
        Cell[][] cells = new Cell[row][col];
        for(int i = 0; i < row; i++){
            String[] data = lines[i +2].split(";");
            if(data.length != col){
                return null;
            }
            for (int j = 0; j < col; j++){
                String[] data2 = data[j].split(",");
                boolean isEmpty;
                int iconIndex;
                try{
                    isEmpty = data2[0].equals("1");
                    iconIndex = Integer.parseInt(data2[1]);
                }catch(NumberFormatException e){
                    return null;
                }
                cells[i][j] = new Cell(new Position(i,j), isEmpty, iconIndex);
            }
        }
        SaveData saveData = new SaveData(new GameBoard(row, col, cells), score, remainingTime, usedTime, difficulty,  totalPairs, eliminatedPairs);
        saveData.setSaveTime(saveTime);
        return saveData;
        //通过一行行，一个个；逐渐拆分，最终将完整的  saveData表示出来
    }
}
//注意，以上的两个方法返回的都是 SaveData类的，所以后续还需要再 SaveManager另行处理