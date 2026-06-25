package Game;

public class CalculateScore {
    private static final int BASE_PAIR_SCORE = 10;       // 消除一对的基础分
    private static final int COMBO_MULTIPLIER = 2;       // 连击倍数
    private static final int MAX_COMBO_MULTIPLIER = 5;   // 最大连击倍数
    private static final int COMBO_INTERVAL = 3;         // 触发连击奖励的消除次数
    private static final int WIN_BONUS_BASE = 100;       // 胜利基础奖励分
    private static final int TIME_BONUS_BASE = 2;


    //上方是用于计算分数的一些基础数值。

    private static int currentCombo = 0;
    private static long lastEliminateTime = 0;
    private static final long COMBO_TIME_LIMIT = 3000;   // 3秒连击窗口


    //上方是用来计算 Combo类的基础数值，都是 final的
    public static int calculatePairScore() {
        double difficultyWeight = 1.0;
        //这里本来要由难度决定分数的，后面修改掉了
        int baseScore = (int) (BASE_PAIR_SCORE * difficultyWeight);
        int comboBonus = calculateComboBonus();
        return baseScore + comboBonus;
        //这个方法返回的是当钱这次消除所得到的分
    }

    public static int calculateTimeBonus(int remainTime) {
        if (remainTime <= 0) {
            return 0;
        }
        return (int) (Math.pow(remainTime, 2) * TIME_BONUS_BASE * 1.0 / 10);
        //这个方法用在游戏末尾时，增加时间得分

    }

    public static int calculateWinBonus() {
        return (int) (WIN_BONUS_BASE * 1.0);
        //这是一个固有的胜利奖励

    }

    // 连击计算逻辑
    private static int calculateComboBonus() {
        long currentTime = System.currentTimeMillis();
        //这是给出当前时间的方法

        if (currentTime - lastEliminateTime <= COMBO_TIME_LIMIT && lastEliminateTime != 0) {
            currentCombo++;
        } else {
            currentCombo = 1;
        }
        lastEliminateTime = currentTime;

        int comboLevel = Math.min(currentCombo / COMBO_INTERVAL, MAX_COMBO_MULTIPLIER);
        return comboLevel > 0 ? (BASE_PAIR_SCORE * comboLevel * COMBO_MULTIPLIER) : 0;
        //通过这个方法，可以算出 Combo的值如何
    }


    public static void resetComboState() {
        currentCombo = 0;
        lastEliminateTime = 0;
        //这个部分用于重置 combo
    }
    // 获取当前连击数
    public static int getCurrentCombo() {
        return currentCombo;
    }
    //这个也被弃用了，好像没有用
}









