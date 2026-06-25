package SaveAndLoad;
import User.UserManager;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//本类负责处理 SaveData
public class SaveManager {
    //用户专属存档
    private final UserManager userManager;
    //所有用户的存档文件都存储在这
    private static final String SAVE_DIR = "saves/";
    private static final int MAXsaves = 3;

    public SaveManager(UserManager userManager){
        this.userManager = userManager;
        // mkdirs()会创建所有不存在的父目录，比 mkdir()更安全
        new File(SAVE_DIR).mkdirs();
        //这一段表示创建一个 saves文件夹
    }


    /**
     * @param data 要保存的游戏存档数据对象，包含棋盘、分数、时间、难度等完整状态
     * @return 保存成功返回true；失败返回false（未登录、IO异常、data为null等）
     */
    public boolean saveGame(SaveData data,int slot){
        if(!userManager.isOnline() || slot < 1 || slot > MAXsaves){
            return false;
        }
        // 生成当前用户专属的存档文件路径：saves/用户名.save
        String username = userManager.getuserNow().getUserName();
        String saveFilePath = SAVE_DIR + username + "_save" + slot + ".save";
        data.setSaveTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(saveFilePath))){
            writer.write(data.toString());
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }




    public SaveData loadGame(int slot){
        if(!userManager.isOnline() || slot < 1  || slot > MAXsaves){
            return null;
        }
        String username = userManager.getuserNow().getUserName();
        String saveFilePath = SAVE_DIR + username + "_save" + slot + ".save";
        File file = new File(saveFilePath);

        if(!file.exists()){
            return null;
        }
        try(BufferedReader reader = new BufferedReader(new FileReader(saveFilePath))){
            StringBuilder builder = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                builder.append(line).append("\n");
            }
            return SaveData.fromString(builder.toString());
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<SaveInfo> getSaveList(){
        List<SaveInfo> list = new ArrayList<>();
        if(!userManager.isOnline()){
            return list;
        }
        String username = userManager.getuserNow().getUserName();
        for(int i =1;i<MAXsaves;i++){
            String saveFilePath = SAVE_DIR + username + "_save" + i + ".save";
            File file = new File(saveFilePath);
            if(file.exists()){
                SaveData data = loadGame(i);
                if(data != null){
                    list.add(new SaveInfo(i,data.getSaveTime(),data.getScore()));
                }else{
                    list.add(new SaveInfo(i,"空存档",0));
                }
            }else{
                list.add(new SaveInfo(i,"空存档",0));
            }
        }
        return list;
    }

    public static class SaveInfo{
        public int slot;
        public String saveTime;
        public int score;
        public SaveInfo(int slot, String saveTime, int score){
            this.slot = slot;
            this.saveTime = saveTime;
            this.score = score;
        }
    }


}
