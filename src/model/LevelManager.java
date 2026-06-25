package model;
import utils.Utils;
import Game.Manager;
//本类负责 level类的管理

public class LevelManager {
    // 表示Level的一些参数
    private static class LevelConfig {
        int patterns;
        String difficulty; // "easy" 或 "hard"
        LevelConfig(String difficulty,int patterns) {
            this.difficulty = difficulty;
            this.patterns=patterns;
        }
    }
    //下方为Level数组，储存不同的 level参数
    private LevelConfig[] configs = {
            new LevelConfig("easy",5),
            new LevelConfig("easy",7),
            new LevelConfig("easy",9),
            new LevelConfig("hard",12),
            new LevelConfig("hard",12)
    };

    private int currentLevelIndex;

    public LevelManager() {
        currentLevelIndex = 0;
    }

    // 生成当前关卡棋盘，通过关卡 id实现，但具体还是通过 Util中的实现
    public GameBoard generateCurrentLevelBoard() {
        LevelConfig cfg = configs[currentLevelIndex];
        int rows, cols, patterns;
        if ("easy".equals(cfg.difficulty)) {
            rows = Manager.easyROWS;
            cols = Manager.easyCOLUMNS;
            patterns = cfg.patterns;
        } else {
            rows = Manager.hardROWS;
            cols = Manager.hardCOLUMNS;
            patterns = Manager.hardPATTERNS;
        }
        return Utils.generateBoard(rows, cols, patterns);
    }
    //以下为获得 level相关参数的方法
    public boolean hasNextLevel() {
        return currentLevelIndex < configs.length - 1;
    }

    public void nextLevel() {
        if (hasNextLevel()) currentLevelIndex++;
    }

    public void reset() {
        currentLevelIndex = 0;
    }

    public int getCurrentLevel() {
        return currentLevelIndex + 1;
    }
}


















