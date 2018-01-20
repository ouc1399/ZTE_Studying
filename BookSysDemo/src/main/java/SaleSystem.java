import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class SaleSystem {
    public static List<Book> getBookListData()
    {
        List<Book> list = new ArrayList<Book>();
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
//            System.out.println("开始尝试连接数据库！");
            String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";// 127.0.0.1是本机地址，XE是精简版Oracle的默认数据库名
            String user = "zte";// 用户名,系统默认的账户名
            String password = "1234";// 你安装时选设置的密码
            con = DriverManager.getConnection(url, user, password);// 获取连接
            String sql = "select * from booklist";// 预编译语句，“？”代表参数
            pre = con.prepareStatement(sql);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            while (result.next()){
                // 当结果集不为空时
//                System.out.println("书籍编号"+result.getString("BOOKNO")+"  书籍价格"
//                        +result.getFloat("PRICE")+"   书籍类型"+result.getString("TYPE")+
//                        "   书籍数目"+result.getInt("BOOKNUM"));
                Book book =  new Book();
                book.setBookNo(result.getString("BOOKNO"));
                book.setPrice(result.getFloat("PRICE"));
                book.setType(result.getString("TYPE"));
                book.setBookNum(result.getInt("BOOKNUM"));
                list.add(book);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }

    public static Book getOneData(String bookNum)
    {
        Book book = new Book();
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
//            System.out.println("获取一条数据 ！");
            String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";// 127.0.0.1是本机地址，XE是精简版Oracle的默认数据库名
            String user = "zte";// 用户名,系统默认的账户名
            String password = "1234";// 你安装时选设置的密码
            con = DriverManager.getConnection(url, user, password);// 获取连接
//            System.out.println("连接成功！");
            String sql = "select * from  booklist WHERE BOOKNO=?";// 预编译语句，“？”代表参数
            pre = con.prepareStatement(sql);// 实例化预编译语句
            pre.setString(1, bookNum);// 设置参数，前面的1表示参数的索引，而不是表中列名的索引
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            while (result.next()){
                // 当结果集不为空时
                book.setBookNo(result.getString("BOOKNO"));
                book.setPrice(result.getFloat("PRICE"));
                book.setType(result.getString("TYPE"));
                book.setBookNum(result.getInt("BOOKNUM"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return book;
    }

    public static void updateBooList(String bookNo,int bookNum){
        Book book = getOneData(bookNo);
        int num = book.getBookNum();
        num -= bookNum;
        int i=0;
        String str = "";
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
            String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";// 127.0.0.1是本机地址，XE是精简版Oracle的默认数据库名
            String user = "zte";// 用户名,系统默认的账户名
            String password = "1234";// 你安装时选设置的密码
            con = DriverManager.getConnection(url,user,password);
            String sql = "update  booklist set BOOKNUM ="+ num+"WHERE BOOKNO="+bookNo;// 预编译语句，“？”代表参数
            pre = (PreparedStatement) con.prepareStatement(sql);
            i = pre.executeUpdate();
            str = (i==1)?"执行成功":"执行失败";
            System.out.println("resutl: " +str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static double getCount(String type){
        double count = 0;
        if(type.equals("新书")){
            count = 1.2;
        }else if (type.equals("常规书")){
            count = 1.0;
        }else
            count=0.6;
        return count;
    }



    public static void main(String[] args) {
        int i=0;
//        Book test  = getOneData("A0001");
//        System.out.println(test.getBookNo()+"  "+test.getPrice()+"  "+test.getType()+"   "+test.getBookNum());
        Scanner in = new Scanner(System.in);
        List<Book> list = getBookListData();
        System.out.println("----------------------欢迎进入OES无线俱乐部旗下大吉大利书店----------------------");
        System.out.println("现有图书的内容如下，请您挑选...");
        System.out.println("    书目编号     书目价格     书目类型     书目个数 ");
        Iterator<Book> it = list.iterator();
        while (it.hasNext()){
            Book book = it.next();
            System.out.println("    "+book.getBookNo()+"        "+book.getPrice()+"        "+book.getType()+"        "+book.getBookNum());
        }
        List<String>  qd = new ArrayList<String>();
        double cost = 0;
        while(i==0){
            System.out.println("请选择你要购买的书籍编号和数量.......输入格式为(编号-数量)");
            String str="";
            while (in.hasNext()) {
                str = in.next();
                break;
            }
//            System.out.println(str);
            String[] bookStr = str.split("-");
//            System.out.println(bookStr.length);
            String singleNo = "";
            int num = 0;
            for (int j=0;j<bookStr.length;j++){
                if(j==0){
                    singleNo=bookStr[j];
//                    System.out.println(singleNo);
                }else
                    num = Integer.parseInt(bookStr[j]);
            }
//            if(!qd.contains("singleNo")){
//                qd.add(singleNo);
//            }
            //计算书的价格
            Book singleBook = getOneData(singleNo);
            int shengYnum  = singleBook.getBookNum();
//            getCount(singleBook.getType());
            if(num<=shengYnum && shengYnum!=0){
                cost += num*singleBook.getPrice()*getCount(singleBook.getType());
                System.out.println("账单明细如下：\n    书籍编号："+singleBook.getBookNo()+"    书籍价格："+singleBook.getPrice()+
                        "    所购数量："+num+"    折扣为:"+getCount(singleBook.getType())+"折");
            }else
                System.out.println("没这么多书了，请重新选择！");

            System.out.println("是否结账 （Y/N）？ ");
            while (in.hasNext()) {
                String res = in.next();
                if (res.equals("Y"))
                    i=1;
                break;
            }
        }
        System.out.println("您总共消费"+cost+"元。");
    }
}
