package loo;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.CellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.omg.CORBA.PUBLIC_MEMBER;



//import org.pushingpixels.substance.api.painter.overlay.BottomLineOverlayPainter;

public abstract class Main_Work extends JFrame implements ActionListener{

	public int main_screen_width = 1136;
	public int main_screen_height = 640;
	
	public static String title = "";
	
	public JPanel bottom = null;
	public JScrollPane scrollPane = null;
	public JButton[] pack = null;
	public JTable jt = null;
	
	public Main_Work() {
		super(title);
		this.setBounds((Login.screen_width-main_screen_width)/2, (Login.screen_height-main_screen_height)/2, main_screen_width	, main_screen_height);
		bottom = new JPanel();
		this.setContentPane(bottom);
		bottom.setBackground(new Color(230,240,245));
		bottom.setLayout(null);
		
		
		
		
		
		this.setVisible(true);
		this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				try {
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
	

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	
	public static void FitTableColumns(JTable myTable) {
	    JTableHeader header = myTable.getTableHeader();
	    int rowCount = myTable.getRowCount();

	    Enumeration columns = myTable.getColumnModel().getColumns();
	    while (columns.hasMoreElements()) {
	        TableColumn column = (TableColumn) columns.nextElement();
	        int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
	        int width = (int) myTable.getTableHeader().getDefaultRenderer()
	                .getTableCellRendererComponent(myTable, column.getIdentifier(), false, false, -1, col)
	                .getPreferredSize().getWidth();
	        for (int row = 0; row < rowCount; row++) {
	            int preferedWidth = (int) myTable.getCellRenderer(row, col)
	                    .getTableCellRendererComponent(myTable, myTable.getValueAt(row, col), false, false, row, col)
	                    .getPreferredSize().getWidth();
	            width = Math.max(width, preferedWidth);
	        }
	        header.setResizingColumn(column);
	        column.setWidth(width + myTable.getIntercellSpacing().width + 10);
	    }
	}
}


class Student extends Main_Work{

	
	public Student() {
		super();
		this.setTitle("Student");
		JButton btnNewButton = new JButton("\u67E5\u8BE2\u81EA\u8EAB\u4FE1\u606F");
		btnNewButton.addActionListener(this);
		btnNewButton.setBounds(30, 395, 128, 40);
		bottom.add(btnNewButton);
		
		JButton button = new JButton("\u67E5\u8BE2\u6210\u7EE9");
		button.addActionListener(this);
		button.setBounds(240, 395, 128, 40);
		bottom.add(button);
		
		JButton button_1 = new JButton("\u67E5\u8BE2\u5E73\u5747\u6210\u7EE9");
		button_1.setBounds(455, 395, 128, 40);
		button_1.addActionListener(this);
		bottom.add(button_1);
		
		JButton button_2 = new JButton("\u67E5\u8BE2\u9009\u4FEE\u8BFE\u7A0B");
		button_2.setBounds(662, 395, 128, 40);
		button_2.addActionListener(this);
		bottom.add(button_2);
		
		JButton button_3 = new JButton("\u4FEE\u6539\u5BC6\u7801");
		button_3.setBounds(882, 395, 128, 40);
		button_3.addActionListener(this);
		bottom.add(button_3);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 13, 866, 309);
		bottom.add(scrollPane);
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String sql = null;
		Vector<String> columnNames= null;
		Vector<Vector> data = new Vector<Vector>();
		int row=0,col=0;
		if(e.getActionCommand()=="修改密码"){
			new pw(this, "修改密码");
			return;
		}
		
		
		else if(e.getActionCommand()=="查询自身信息"){
			sql = "select * from student where Sno='"+Login.get_user_name()+"'";
			
			columnNames = new Vector<String>();
			columnNames.add("Sno");
			columnNames.add("Sname");
			columnNames.add("Ssex");
			columnNames.add("Sage");
			columnNames.add("Class_Name");
			columnNames.add("S_Phone_Number");
			columnNames.add("Dept_Name");
			columnNames.add("Initial_PW");
		}
		
		else if(e.getActionCommand()=="查询成绩"){
			sql = "select * from sc where Sno='"+Login.get_user_name()+"'";
			
			columnNames = new Vector<String>();
			columnNames.add("Sno");
			columnNames.add("Cno");
			columnNames.add("Tno");
			columnNames.add("Grade");
			
		}
		
		else if(e.getActionCommand()=="查询平均成绩"){
			sql = "select sno,AVG(Grade) AVG from sc where Sno='"+Login.get_user_name()+"' group by sno";
			
			columnNames = new Vector<String>();
			columnNames.add("Sno");
			columnNames.add("AVG");
		}
		else if(e.getActionCommand()=="查询选修课程"){
			sql = "select sno,SC.cno, cname from sc, course where  course.cno=SC.cno and sno='"+Login.get_user_name()+"'";
		
			columnNames = new Vector<String>();
			columnNames.add("Sno");
			columnNames.add("Cno");
			columnNames.add("Cname");
		
		}
			try {
				SQL_Server.rs = SQL_Server.stmt.executeQuery(sql);
				if(!SQL_Server.rs.next()) System.out.println("content is empty!");
				SQL_Server.rs.last();
				row = SQL_Server.rs.getRow();
				SQL_Server.rs.beforeFirst();
				ResultSetMetaData rsmd = SQL_Server.rs.getMetaData();
				col = rsmd.getColumnCount();
				while(SQL_Server.rs.next()){
					Vector<String> rowData = new Vector<String>();
					for(int index=1; index<=col; index++){
					
						rowData.addElement(SQL_Server.rs.getString(index).trim());
					}

					data.addElement(rowData);
				}
				jt = new JTable(data,columnNames);
				FitTableColumns(jt);
				jt.setEnabled(false);
				bottom.remove(scrollPane);
				scrollPane = new JScrollPane(jt);
				scrollPane.setBounds(14, 13, 866, 309);
				bottom.add(scrollPane);
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			
		
		
		
	}
	
	
}

class Teacher extends Main_Work{
	int row_before=0,row_after=0;
	private JButton button_4, button_5;
	private boolean is_disable_column_except_grade = false;
 	public Teacher() {
		super();
		this.setTitle("Teacher");
		
		JButton btnNewButton = new JButton("添加学生成绩");
		btnNewButton.addActionListener(this);
		btnNewButton.setBounds(30, 395, 128, 40);
		bottom.add(btnNewButton);
		
		JButton button = new JButton("修改学生成绩");
		button.addActionListener(this);
		button.setBounds(240, 395, 128, 40);
		bottom.add(button);
		
		JButton button_1 = new JButton("查询学生成绩");
		button_1.setBounds(455, 395, 128, 40);
		button_1.addActionListener(this);
		bottom.add(button_1);
		
		JButton button_2 = new JButton("查询授课信息");
		button_2.setBounds(662, 395, 128, 40);
		button_2.addActionListener(this);
		bottom.add(button_2);
		
		JButton button_3 = new JButton("修改密码");
		button_3.setBounds(882, 395, 128, 40);
		button_3.addActionListener(this);
		bottom.add(button_3);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 13, 866, 309);
		bottom.add(scrollPane);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String sql = "";
		Vector<String> columnNames= null;
		Vector<Vector> data = new Vector<Vector>();
		int row=0,col=0;
		
		//清除右侧子按钮
		if(e.getActionCommand()!="添加行"){
			for(Component c: bottom.getComponents()){
				if(c.getName()!=null){
					if(c.getName().compareTo("has_button_4")==0)bottom.remove(button_4);
					if(c.getName().compareTo("has_button_5_1")==0)bottom.remove(button_5);
					if(c.getName().compareTo("has_button_5_2")==0)bottom.remove(button_5);
					
				}
			
			}
		}
		
		repaint();
		
		if(e.getActionCommand()=="添加学生成绩"){
			button_4 = new JButton("\u6DFB\u52A0\u884C");
			button_4.setName("has_button_4");
			button_4.setBounds(939, 81, 128, 40);
			
			button_5 = new JButton("\u63D0\u4EA4");
			button_5.setName("has_button_5_1");
			button_5.setBounds(939, 210, 128, 40);
			bottom.add(button_4);
			bottom.add(button_5);
			repaint();
			
			button_4.addActionListener(this);
			button_5.addActionListener(this);
			sql = "select * from sc where tno='"+Login.get_user_name()+"'";
			columnNames = new Vector<String>();
        	columnNames.add("Sno");
        	columnNames.add("Cno");
        	columnNames.add("Tno");
        	columnNames.add("Grade");
			
		}
		else if(e.getActionCommand()=="修改学生成绩"){
			button_5 = new JButton("\u63D0\u4EA4");
			button_5.setName("has_button_5_2");
			button_5.setBounds(939, 210, 128, 40);
			bottom.add(button_5);
			repaint();
			
			button_5.addActionListener(this);
			is_disable_column_except_grade = true;
			sql = "select * from sc where tno='"+Login.get_user_name()+"'";
			columnNames = new Vector<String>();
        	columnNames.add("Sno");
        	columnNames.add("Cno");
        	columnNames.add("Tno");
        	columnNames.add("Grade");
			
		}
		else if (e.getActionCommand()=="查询学生成绩") {

			sql = "select * from SC where tno='"+Login.get_user_name()+"'";
			
			columnNames = new Vector<String>();
			columnNames.add("Sno");
			columnNames.add("Cno");
			columnNames.add("tno");
			columnNames.add("Grade");
		}
		
		else if (e.getActionCommand()=="查询授课信息") {
			sql = "select * from teach where tno='"+Login.get_user_name()+"'";
			
			columnNames = new Vector<String>();
			columnNames.add("Tno");
			columnNames.add("Cno");
			columnNames.add("Troom");
			columnNames.add("Ttime");
		}
		else if(e.getActionCommand()=="修改密码"){
			new pw(this, "修改密码");
			return;
		}
		else if (e.getActionCommand()=="添加行") {
			jt.setEnabled(true);
			Vector<String> new_row = new Vector<String>();
			((DefaultTableModel)jt.getModel()).addRow(new_row);
			row_after = jt.getRowCount();
			int count = row_after;
			jt.requestFocus();
			jt.setRowSelectionInterval(count-1, count-1);
        	return;
		}
		else if(e.getActionCommand()=="提交"){	
			if(button_5.getName().compareTo("has_button_5_1")==0){//如果为插入学生成绩的提交按钮
			
				if(row_before!=row_after){	//如果有添加内容
					
				//取消当前单元格的编辑状态

					int columnId = jt.getSelectedColumn();
			        int rowId = jt.getSelectedRow();
			        if(columnId!=-1&&rowId!=-1){
			        	CellEditor ce = jt.getCellEditor(rowId, columnId);
			        	ce.stopCellEditing();
			        }
			        
			        
			        DefaultTableModel dtm = (DefaultTableModel) jt.getModel();
		        
		        	for(int i=row_before;i<row_after;i++){
		        		StringBuffer sb = new StringBuffer();
			        	for(int k=0; k<dtm.getColumnCount();k++){
			        		Object value = dtm.getValueAt(i, k);
			        		if(value==null){
			        			new JOptionPane().showMessageDialog(null, "Insert Null!");
			        			return;
			        		}
			        		else {
								sb.append(value);
								sb.append(",");
							}
			        		
			        	}
			        	sb.delete(sb.length()-1, sb.length());
			        	System.out.println("insert into sc values("+sb.toString()+");");	
			        	sql += "insert into sc values("+sb.toString()+");";
			        }
					try {
						SQL_Server.stmt.executeUpdate(sql);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					new JOptionPane().showMessageDialog(null, "Commit Successfully!");
					row_before = jt.getRowCount();
		        	
		        }
		        else {
		        	new JOptionPane().showMessageDialog(null, "Insert Null!");
		        	
				}
		        
			}
			else if(button_5.getName().compareTo("has_button_5_2")==0){
				int columnId = jt.getSelectedColumn();
		        int rowId = jt.getSelectedRow();
		        if(columnId!=-1&&rowId!=-1){
		        	CellEditor ce = jt.getCellEditor(rowId, columnId);	        
		        	ce.stopCellEditing();      
		        }
		        
		        is_disable_column_except_grade = false;
		        int row_count = jt.getRowCount();
		        DefaultTableModel dtm = (DefaultTableModel) jt.getModel();
		        for(int i=0; i<row_count; i++){
		        	String[] row_value = new String[dtm.getColumnCount()];
		        	for(int k=0; k<dtm.getColumnCount();k++){
		        		row_value[k] = "";
		        		Object value = dtm.getValueAt(i, k);
	        			row_value[k] = (String)value;
	        			if(row_value[k].compareTo("")==0)new JOptionPane().showMessageDialog(null, "grade null!");
		        	}
		        	
		        	System.out.println("update sc set grade="+row_value[3]+" where sno='"+row_value[0]+"' and cno='"+
		        			row_value[1]+"' and tno='"+row_value[2]+"'");
		        	sql = "update sc set grade="+row_value[3]+" where sno='"+row_value[0]+"' and cno='"+
		        			row_value[1]+"' and tno='"+row_value[2]+"'";
		        	
		        	try {
						SQL_Server.stmt.executeUpdate(sql);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
		        	
		        	
		        }
		        new JOptionPane().showMessageDialog(null, "Commit Successfully!");
			}
			return;
		}
		
		
		try {
    		SQL_Server.rs = SQL_Server.stmt.executeQuery(sql);
			if(!SQL_Server.rs.next()) System.out.println("content is empty!");
			SQL_Server.rs.last();
			row = SQL_Server.rs.getRow();
			SQL_Server.rs.beforeFirst();
			ResultSetMetaData rsmd = SQL_Server.rs.getMetaData();
			col = rsmd.getColumnCount();
			while(SQL_Server.rs.next()){
				Vector<String> rowData = new Vector<String>();
				for(int index=1; index<=col; index++){
					rowData.addElement(SQL_Server.rs.getString(index).trim());
				}

				data.addElement(rowData);
			}
			jt = new JTable(data,columnNames);
			FitTableColumns(jt);
			jt.setEnabled(false);
			if(is_disable_column_except_grade){
				DefaultTableModel dt = (DefaultTableModel) jt.getModel();
		        jt = new JTable(dt){
		        	public boolean isCellEditable(int row,int column){
						if(column == 3){  
							return true;  
						}else{  
						    return false;  
						}  
		        	}
		        };
			}
			
			row_before = jt.getRowCount();
			row_after = row_before;
			
			bottom.remove(scrollPane);
			scrollPane = new JScrollPane(jt);
			scrollPane.setBounds(14, 13, 866, 309);
			bottom.add(scrollPane);

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	
}

class Admin extends Main_Work{
	JButton button_3, button_4, button_5;
	int row_before = 0, row_after = 0;
	String table_name = "";
	public Admin() {
		super();
		this.setTitle("Admin");
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 13, 866, 309);
		bottom.add(scrollPane);
	
		JButton button = new JButton("学生表");
		button.setBounds(56, 427, 128, 40);
		bottom.add(button);
		button.addActionListener(this);
		
		JButton button_1 = new JButton("课程表");
		button_1.setBounds(375, 427, 128, 40);
		bottom.add(button_1);
		button_1.addActionListener(this);
		
		JButton button_2 = new JButton("教师表");
		button_2.setBounds(695, 427, 128, 40);
		bottom.add(button_2);
		button_2.addActionListener(this);
		
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String sql = "";
		
		Vector<String> columnNames= null;
		Vector<Vector> data = new Vector<Vector>();
		int row=0,col=0;
		if(e.getActionCommand()=="学生表"){
			table_name = "student";
			
			columnNames = new Vector<String>();
			columnNames.add("Sno");
			columnNames.add("Sname");
			columnNames.add("Ssex");
			columnNames.add("Sage");
			columnNames.add("Class_Name");
			columnNames.add("S_phone_number");
			columnNames.add("Dept_Name");
			columnNames.add("Initial_PW");
			
			button_3 = new JButton("添加行");
			button_3.setBounds(934, 50, 128, 40);
			bottom.add(button_3);
			button_3.addActionListener(this);
			
			
			button_4 = new JButton("删除行");
			button_4.setBounds(934, 156, 128, 40);
			bottom.add(button_4);
			button_4.addActionListener(this);
			
			button_5 = new JButton("提交更新");
			button_5.setBounds(934, 263, 128, 40);
			bottom.add(button_5);
			button_5.addActionListener(this);
			repaint();
			
			sql = "select * from student";
		}
		else if(e.getActionCommand()=="课程表"){
			table_name = "course";
			
			columnNames = new Vector<String>();
			columnNames.add("Cno");
			columnNames.add("Cname");
			columnNames.add("Ctype");
			columnNames.add("Cperiod");
			
			button_3 = new JButton("添加行");
			button_3.setBounds(934, 50, 128, 40);
			bottom.add(button_3);
			button_3.addActionListener(this);
			
			
			button_4 = new JButton("删除行");
			button_4.setBounds(934, 156, 128, 40);
			bottom.add(button_4);
			button_4.addActionListener(this);
			
			button_5 = new JButton("提交更新");
			button_5.setBounds(934, 263, 128, 40);
			bottom.add(button_5);
			button_5.addActionListener(this);
			repaint();
			
			sql = "select * from course";
		}
		else if(e.getActionCommand()=="教师表"){
			table_name = "teacher";
			
			columnNames = new Vector<String>();
			columnNames.add("Tno");
			columnNames.add("Tname");
			columnNames.add("Tsex");
			columnNames.add("T_phone_number");
			columnNames.add("Dept_Name");
			columnNames.add("Initial_PW");
			
			button_3 = new JButton("添加行");
			button_3.setBounds(934, 50, 128, 40);
			bottom.add(button_3);
			button_3.addActionListener(this);
			
			
			button_4 = new JButton("删除行");
			button_4.setBounds(934, 156, 128, 40);
			bottom.add(button_4);
			button_4.addActionListener(this);
			
			button_5 = new JButton("提交更新");
			button_5.setBounds(934, 263, 128, 40);
			bottom.add(button_5);
			button_5.addActionListener(this);
			repaint();
			
			sql = "select * from teacher";
		}
		else if(e.getActionCommand()=="添加行"){
			Vector<String> new_row = new Vector<String>();
			((DefaultTableModel)jt.getModel()).addRow(new_row);
			row_after = jt.getRowCount();
			int count = row_after;
			jt.requestFocus();
			jt.setRowSelectionInterval(count-1, count-1);
        	return;
		}
		else if(e.getActionCommand()=="删除行"){
			DefaultTableModel dtm = (DefaultTableModel) jt.getModel();
			int select_row_num = jt.getSelectedRows().length;
			String deleteable_row = "";
			String primary_key = "";
			System.out.println(select_row_num);
			if(select_row_num==0)new JOptionPane().showMessageDialog(null, "Selected is Null!");
			for(int i=0;i<select_row_num;i++){
				Object value = dtm.getValueAt(jt.getSelectedRow(), 0);
				deleteable_row = (String) value;
				primary_key = dtm.getColumnName(0);
				System.out.println("delete from "+table_name+" where "+primary_key+"='"+deleteable_row+"'");
				sql = "delete from "+table_name+" where "+primary_key+"='"+deleteable_row+"'";
				try {
					SQL_Server.stmt.executeUpdate(sql);
				} catch (SQLException e1) {

					e1.printStackTrace();
				}
				dtm.removeRow(jt.getSelectedRow());
				
			}
			return;	
		}
		else if(e.getActionCommand()=="提交更新"){
			int columnId = jt.getSelectedColumn();//取消当前单元格的编辑状态
	        int rowId = jt.getSelectedRow();
	        if(columnId!=-1&&rowId!=-1){
	        	CellEditor ce = jt.getCellEditor(rowId, columnId);	        
	        	ce.stopCellEditing();      
	        }
	        DefaultTableModel dtm = (DefaultTableModel) jt.getModel();
	        
	        
	        
	        
	        String[] column_name = new String[dtm.getColumnCount()];
	        
	        for(int j=0;j<dtm.getColumnCount();j++) column_name[j] = dtm.getColumnName(j);//获取列名	
	        	    
	        for(int i=0; i<row_before; i++){
	        	String[] row_value = new String[dtm.getColumnCount()];
	        	for(int k=0; k<dtm.getColumnCount();k++){
	        		row_value[k] = "";
	        		Object value = dtm.getValueAt(i, k);
        			row_value[k] = (String)value;
        			if(value==null)new JOptionPane().showMessageDialog(null, "Input null!");
	        	}
	        	StringBuffer sb = new StringBuffer();
	        	for(int j=0; j<dtm.getColumnCount();j++){
	        		sb.append(column_name[j]+"='"+row_value[j]+"',"); 		
	        	}
	        	sb.delete(sb.length()-1, sb.length());
        		System.out.println("update "+table_name+" set "+sb.toString()+" where "+column_name[0]+"='"+row_value[0]+"'");
	        	sql = "update "+table_name+" set "+sb.toString()+" where "+column_name[0]+"='"+row_value[0]+"'";
	        	
	        	try {
					SQL_Server.stmt.executeUpdate(sql);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
	        	
	        }
	        if(row_before!=row_after){	//如果有添加内容
	        	
	        	for(int i=row_before;i<row_after;i++){
	        		StringBuffer sb = new StringBuffer();
		        	for(int k=0; k<dtm.getColumnCount();k++){
		        		Object value = dtm.getValueAt(i, k);
		        		if(value==null){
		        			new JOptionPane().showMessageDialog(null, "Insert Null!");
		        			return;
		        		}
		        		else {
							sb.append("'"+value+"'");
							sb.append(",");
						}
		        		
		        	}
		        	sb.delete(sb.length()-1, sb.length());
		        	System.out.println("insert into "+table_name+" values("+sb.toString()+");");	
		        	sql = "insert into "+table_name+" values("+sb.toString()+");";
		        	try {
						SQL_Server.stmt.executeUpdate(sql);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
		        }	
				row_before = jt.getRowCount();//点击提交按钮后，新添加行计入固有行数
		    }
	        
	        new JOptionPane().showMessageDialog(null, "Commit Successfully!");
	        return;
		}
		
		try {
    		SQL_Server.rs = SQL_Server.stmt.executeQuery(sql);
			if(!SQL_Server.rs.next()) System.out.println("content is empty!");
			SQL_Server.rs.last();
			row = SQL_Server.rs.getRow();
			SQL_Server.rs.beforeFirst();
			ResultSetMetaData rsmd = SQL_Server.rs.getMetaData();
			col = rsmd.getColumnCount();
			while(SQL_Server.rs.next()){
				Vector<String> rowData = new Vector<String>();
				for(int index=1; index<=col; index++){
					rowData.addElement(SQL_Server.rs.getString(index).trim());
				}

				data.addElement(rowData);
			}
			jt = new JTable(data,columnNames);
			row_before = jt.getRowCount();
			row_after = row_before;
			FitTableColumns(jt);	
			bottom.remove(scrollPane);
			scrollPane = new JScrollPane(jt);
			scrollPane.setBounds(14, 13, 866, 309);
			bottom.add(scrollPane);

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
	}
}