package loo;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JButton;

import org.omg.CORBA.PRIVATE_MEMBER;

public class pw extends JDialog implements ActionListener{
	public int pw_screen_width = 400;
	public int pw_screen_heigth = 300;
	
	private JPanel jp = null;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JPasswordField passwordField_2;
	
	private String old_pw,new_pw, comfirm_pw;
	
	public pw() {
		super();
		this.setBounds((Login.screen_width-pw_screen_width)/2, (Login.screen_height-pw_screen_heigth)/2, pw_screen_width, pw_screen_heigth);
		jp = new JPanel();
		jp.setBackground(new Color(230,240,245));
		this.setContentPane(jp);
		jp.setLayout(null);
		
		JLabel label = new JLabel("\u65E7\u5BC6\u7801\uFF1A");
		label.setBounds(33, 36, 81, 27);
		jp.add(label);
		
		JLabel label_1 = new JLabel("\u65B0\u5BC6\u7801\uFF1A");
		label_1.setBounds(33, 76, 81, 27);
		jp.add(label_1);
		
		JLabel label_2 = new JLabel("\u786E\u8BA4\u5BC6\u7801\uFF1A");
		label_2.setBounds(33, 116, 81, 27);
		jp.add(label_2);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(128, 36, 202, 26);
		jp.add(passwordField);
		
		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(128, 76, 202, 26);
		jp.add(passwordField_1);
		
		passwordField_2 = new JPasswordField();
		passwordField_2.setBounds(128, 116, 202, 26);
		jp.add(passwordField_2);
		
		JButton button = new JButton("\u786E\u8BA4");
		button.setBounds(44, 181, 113, 27);
		jp.add(button);
		button.addActionListener(this);
		
		JButton button_1 = new JButton("\u5FD8\u8BB0\u5BC6\u7801");
		button_1.setBounds(217, 181, 113, 27);
		jp.add(button_1);
		button_1.addActionListener(this);
		this.setVisible(true);
	}
	
	public pw(JFrame owner, String title){
		super(owner,title);
		this.setBounds((Login.screen_width-pw_screen_width)/2, (Login.screen_height-pw_screen_heigth)/2, pw_screen_width, pw_screen_heigth);
		jp = new JPanel();
		jp.setBackground(new Color(230,240,245));
		this.setContentPane(jp);
		jp.setLayout(null);
		
		JLabel label = new JLabel("\u65E7\u5BC6\u7801\uFF1A");
		label.setBounds(33, 36, 81, 27);
		jp.add(label);
		
		JLabel label_1 = new JLabel("\u65B0\u5BC6\u7801\uFF1A");
		label_1.setBounds(33, 76, 81, 27);
		jp.add(label_1);
		
		JLabel label_2 = new JLabel("\u786E\u8BA4\u5BC6\u7801\uFF1A");
		label_2.setBounds(33, 116, 81, 27);
		jp.add(label_2);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(128, 36, 202, 26);
		jp.add(passwordField);
		
		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(128, 76, 202, 26);
		jp.add(passwordField_1);
		
		passwordField_2 = new JPasswordField();
		passwordField_2.setBounds(128, 116, 202, 26);
		jp.add(passwordField_2);
		
		JButton button = new JButton("\u786E\u8BA4");
		button.setBounds(44, 181, 113, 27);
		jp.add(button);
		button.addActionListener(this);
		
		JButton button_1 = new JButton("\u5FD8\u8BB0\u5BC6\u7801");
		button_1.setBounds(217, 181, 113, 27);
		jp.add(button_1);
		button_1.addActionListener(this);
		
		this.setVisible(true);
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand()=="确认"){
			old_pw = String.valueOf(passwordField.getPassword());
			new_pw = String.valueOf(passwordField_1.getPassword());
			comfirm_pw = String.valueOf(passwordField_2.getPassword());
			
			String sql = "select pw from account where user_='"+Login.get_user_name()+"'";
			try {
				SQL_Server.rs = SQL_Server.stmt.executeQuery(sql);
				SQL_Server.rs.next();
				if(old_pw.compareTo(SQL_Server.rs.getString("pw").trim())==0){//检测旧密码是否与正确
					if(new_pw.compareTo(comfirm_pw)==0){//新密码与确认密码是否相同
						if(new_pw.compareTo("")!=0){//新密码不能为空
							if(old_pw.compareTo(new_pw)!=0){//新旧密码不能相同
								sql = "update account set pw='"+new_pw+"'where user_='"+Login.get_user_name()+"'";
								SQL_Server.stmt.executeUpdate(sql);
								new JOptionPane().showMessageDialog(null, "Successful password change!");
								this.dispose();
							}
							else{
								new JOptionPane().showMessageDialog(null, "New password should not be same as old password!");
								passwordField_1.setText("");
								passwordField_2.setText("");
							}
						}
						else {
							new JOptionPane().showMessageDialog(null, "Password cannot be empty!");
						}
					}
					else {
						new JOptionPane().showMessageDialog(null, "Inconsistencies password and confirm password!");
						passwordField_1.setText("");
						passwordField_2.setText("");
					}
				}
				else {
					new JOptionPane().showMessageDialog(null, "Password Error!");
					passwordField.setText("");
					passwordField_1.setText("");
					passwordField_2.setText("");
				}
				
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			
		}
		
	}
}