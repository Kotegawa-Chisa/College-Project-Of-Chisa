package User;

public class User {
    private String userName;
    private String password;
    private int highestSCORE;



    //新用户
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.highestSCORE = 0;
    }
    //老玩家
    public User(String userName, String password, int highestSCORE) {
        this.userName = userName;
        this.password = password;
        this.highestSCORE = highestSCORE;
    }


    public String getUserName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }
    public int getHighestSCORE() {
        return highestSCORE;
    }
    public void setHighestSCORE(int highestSCORE) {
        this.highestSCORE = highestSCORE;
    }

    //存为字符串
    public String toString() {
        return userName + ","+password + ","+highestSCORE;
    }


    //解析字符串
    public  static User getString(String str){
        String[] elements = str.split(",");
        if(elements.length==3){
            return new User(elements[0],elements[1],Integer.parseInt(elements[2]));
        }
        return null;
    }


}
