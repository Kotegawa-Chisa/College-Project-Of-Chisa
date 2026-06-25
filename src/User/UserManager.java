package User;


import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
public class UserManager {
    private List<User> userList;//所有注册用户
    private static final String USERFILE = "user.txt";//数据路径
    private User userNow;//当前用户


    public UserManager() {
        userList = new ArrayList<>();
        loadUsersFromFile();
        userNow = null;
    }

    //从user读取所有用户
    private void loadUsersFromFile(){
        File file = new File(USERFILE);
        if(!file.exists()){
            return;
        }
        try{
            List<String> lines = Files.readAllLines(file.toPath());
            for(String line : lines){
                User u = User.getString(line);
                if(u != null){
                    userList.add(u);
                }
            }
        } catch(IOException e){
            e.printStackTrace();//捕获文件读取异常
        }
    }

    //保存到文件
    private void saveUsersToFile(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(USERFILE))){//自动关闭流
            for(User u : userList){
                writer.write(u.toString());
                writer.newLine();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    //注册 成功true 失败false
    public boolean register (String username, String password){
        for(User user : userList){
            if(user.getUserName().equals(username)){
                return false;
            }
        }
            userList.add(new User(username, password));
            saveUsersToFile();
            return true;
    }


    //登录 成功true 失败false
    public boolean login(String username, String password){
        for(User user : userList){
            if(user.getUserName().equals(username)){
                if(user.getPassword().equals(password)){
                    userNow = user;
                    return true;
                }
            }
        }
        return false;
    }

    //游客登陆
    public void touristLogin(){
        userNow = null;
    }
    //登出
    public void logout(){
        userNow = null;
    }


    //更新最高分数
    public void newHighestSCORE(int score1){
        if(userNow != null && userNow.getHighestSCORE() < score1){
            userNow.setHighestSCORE(score1);
            saveUsersToFile();
        }
    }
    //获取当前登录者
    public User getuserNow(){
        return userNow;
    }
    //确定是否在线
    public boolean isOnline(){
        return userNow != null;
    }

    public List<User> getRankingList() {
        // 返回一个按最高分降序排序的新列表（不影响原始顺序）
        List<User> sortedList = new ArrayList<>(userList);
        sortedList.sort((u1, u2) -> Integer.compare(u2.getHighestSCORE(), u1.getHighestSCORE()));
        return sortedList;
    }








}
