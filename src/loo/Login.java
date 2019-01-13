package loo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.interfaces.RSAKey;
import java.sql.*;
import java.util.Properties;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.util.Properties;

import javax.swing.*;

import org.omg.CORBA.PRIVATE_MEMBER;

//import org.pushingpixels.substance.api.SubstanceLookAndFeel;

public class Login extends JFrame implements ActionListener {
	public static int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
	public static int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
	//获取屏幕分辨率
	public int window_width = 400;
	public int window_height = 300;
	//设置login窗口的大小
	
	private static String user_name, password;
	//设置用户账户和密码的缓存
	private JPanel jp = null;
	private JLabel[] jl = {new JLabel("User"), new JLabel("Password"), new JLabel("")};
	
	private JButton[] login_and_out = {new JButton("Login"),new JButton("Clear")};
	private JTextField jtf = new JTextField();
	private JPasswordField jpf = new JPasswordField();
	//定义组件
	
	//构造函数
	public Login(){
		
	super("Log in");
	this.setBounds((screen_width-window_width)/2, (screen_height-window_height)/2, window_width, window_height);
	//设置窗口居中
	jp = new JPanel();
	this.setContentPane(jp);
	jp.setBackground(new Color(230,240,245));
	//设置背景色
	jp.setLayout(null);
	
	for(int i=0;i<2;i++){//确定laber和button的位置
		jl[i].setBounds(50, 40+i*50, 80, 26);
		login_and_out[i].setBounds(75+i*160, 180, 80,26);
		jp.add(jl[i]);
		jp.add(login_and_out[i]);
		login_and_out[i].addActionListener(this);
	}
	
	jtf.setBounds(110, 40, 180, 30);
	jp.add(jtf);
	jtf.addActionListener(this);
	jpf.setBounds(110, 90, 180, 30);
	jp.add(jpf);
	jpf.setEchoChar('*');
	jpf.addActionListener(this);
	jl[2].setBounds(110, 130, 300, 30);
	jp.add(jl[2]);
	//确定输入框位置等
	try {
		//创建数据库连接
		SQL_Server.stmt = SQL_Server.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
		System.out.println("Connect to object database... succsses.");
	} catch (SQLException e2) {
		e2.printStackTrace();
	}
	this.setVisible(true);
	this.setResizable(false);
	//设置窗口关闭响应事件
	this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
	this.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e){
			try {
				//设置用户登录状态为离线
				String sql = "update account set is_online=0 where user_='"+Login.get_user_name()+"'";
				SQL_Server.stmt.executeUpdate(sql);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			SQL_Server.close(SQL_Server.conn, SQL_Server.stmt, SQL_Server.rs);
			System.out.println("Close connection ...success.");
			System.exit(0);
		}
	});
	}
	
	public static String get_user_name(){
		return user_name;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand()=="Login"){//登陆按钮触发事件
			user_name = jtf.getText();
			password = String.valueOf(jpf.getPassword());

			try {
				String sql = "select * from account where user_='" + user_name + "'";
				SQL_Server.rs = SQL_Server.stmt.executeQuery(sql);
				
				if(SQL_Server.rs.next()){
					if(user_name.compareTo(SQL_Server.rs.getString("user_").trim())==0&&(password.compareTo(SQL_Server.rs.getString("pw").trim())==0)){
						sql = "select is_online from account where user_='"+user_name+"'";
						SQL_Server.rs = SQL_Server.stmt.executeQuery(sql);
						SQL_Server.rs.next();
						
						if(SQL_Server.rs.getString("is_online").trim().compareTo("1")==0){//如果此账户在线则禁止登陆
							new JOptionPane().showMessageDialog(null, "Sorry,but this user is online.");
						}
						else {
							sql = "update account set is_online=1 where user_='"+user_name+"'";
							
							SQL_Server.stmt.executeUpdate(sql);
							//设置该用户的登陆状态为在线
							
							sql = "select user_class from account where user_='"+user_name+"'";
							SQL_Server.rs = SQL_Server.stmt.executeQuery(sql);
							SQL_Server.rs.next();
							String rs_return = SQL_Server.rs.getString("user_class").trim();
							if(rs_return.compareTo("admin")==0){new Admin();}
							else if(rs_return.compareTo("student")==0){new Student();}
							else if(rs_return.compareTo("teacher")==0){new Teacher();}
							else{
								System.out.println("Exception in getting user_class.");
							}//根据登陆用户的类别创建新窗体
							
							this.dispose();
						}
						
						
						
					}
					else {
						jl[2].setForeground(Color.red);
						jl[2].setText("Wrong in User or Password!");
					}
				}
				else {
					jl[2].setForeground(Color.red);
					jl[2].setText("Wrong in User or Password!");
				}
			}
			catch (Exception e1) {
				new JOptionPane().showMessageDialog(null, "Error in connectting object server!\nPlease try again later.");
				e1.printStackTrace();
			}
		}
		
		else if (e.getActionCommand()=="Clear") {
			jtf.setText("");
			jpf.setText("");
			jl[2].setText("");
		}
		
	}

	public static void main(String[] args) {
		try {
			
			JFrame.setDefaultLookAndFeelDecorated(true);
			UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");
			new Login();
					
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}

class SQL_Server{
	
	static String db_url = null;  
	static String db_username = null;  
	static String db_password = null;  
	static String db_driver = null;

	static Connection conn = null;
	static Statement stmt = null;
	static ResultSet rs = null;
	
	public static Statement getStatement(){
		return stmt;
	}
	
	public static ResultSet getResultSet(){
		return rs;
	}

	
	public static Connection getConnection(){
		Properties properties = new Properties();
		InputStream in = SQL_Server.class.getClassLoader().getResourceAsStream("loo/db.properties");
		try {
			properties.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		db_url = properties.getProperty("db_url");
		db_username = properties.getProperty("db_username");  
        db_password = properties.getProperty("db_password");  
        db_driver = properties.getProperty("db_driver");
        
        try {
			Class.forName(db_driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(db_url, db_username, db_password);
			return conn;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	

	public static void close(Connection conn,Statement st,ResultSet rs){  
        if(conn!=null){  
            try{  
                conn.close();  
            }catch (Exception e) {  
                throw new RuntimeException(e);  
            }  
        }  
          
        if(st!=null){  
            try{  
                st.close();  
            }catch (Exception e) {  
                throw new RuntimeException(e);  
            }  
        }  
          
        if(rs!=null){  
            try{  
                rs.close();  
            }catch (Exception e) {  
                throw new RuntimeException(e);  
            }  
        }  
    }
	
	public static void close(Connection conn,Statement st){  
        if(conn!=null){  
            try{  
                conn.close();  
            }catch (Exception e) {  
                throw new RuntimeException(e);  
            }  
        }  
          
        if(st!=null){  
            try{  
                st.close();  
            }catch (Exception e) {  
                throw new RuntimeException(e);  
            }  
        }  
    }
}
