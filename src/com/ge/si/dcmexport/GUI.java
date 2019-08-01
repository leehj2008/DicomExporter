package com.ge.si.dcmexport;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ListIterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import joinery.DataFrame;


@SuppressWarnings("serial")
public class GUI extends JFrame implements ActionListener{  
    JButton sourceBtn=null;  
    JButton viewExcelBtn=null;  
    JLabel sourceLbl = null;
    JTextField sourceTxt = null;
    

    JButton destBtn=null;  
    JLabel destLbl = null;
    JTextField destTxt = null;
    JEditorPane metaLabel = null;
    JLabel statusLabel = null;
    JButton startBtn = null;
	static Logger log = Logger.getLogger(GUI.class);
	AppConfig config;
	
	DicomService dcmService;
	
	ReadExcelJoiner readExcelJoiner = new ReadExcelJoiner();
	
    public static void main(String[] args) {  
        GUI gui = new GUI();
    }  
    public GUI(){  
    	super("DICOM Exporter");
    	String sourceDir = null,destDir = null;
    	try{
    		sourceDir = FileUtils.readFileToString(new File("sourcedir.txt"));
    		destDir = FileUtils.readFileToString(new File("destdir.txt"));
    	}catch(Exception e){
    		
    	}
        this.setLayout(new GridBagLayout());
    	sourceBtn=new JButton("选择Excel");  
    	viewExcelBtn=new JButton("预览Excel");  
    	sourceLbl=new JLabel("Excel 文件:");  
    	sourceTxt=new JTextField(100);
    	if(!StringUtils.isEmpty(sourceDir)){
    		sourceTxt.setText(sourceDir);
    	}
    	
    	destBtn=new JButton("选择存放DCM位置");  
    	destLbl=new JLabel("DICOM 文件位置:");  
    	destTxt=new JTextField(100);
    	if(!StringUtils.isEmpty(destDir)){
    		destTxt.setText(destDir);
    	}
    	metaLabel =  new JEditorPane();
    	metaLabel.setSize(600,200);
    	metaLabel.setEditable(false);
    	statusLabel = new JLabel("信息");
    	
    	
    	destTxt.setMinimumSize(new Dimension(400,20));
    	sourceTxt.setMinimumSize(new Dimension(400,20));
    	GridBagConstraints cons = new GridBagConstraints();
    	cons.gridx=2;cons.gridy=0;cons.gridheight=1;cons.gridwidth=1;
        this.add(sourceBtn,cons);  
    	cons.gridx=3;cons.gridy=0;cons.gridheight=1;cons.gridwidth=1;
        this.add(viewExcelBtn,cons);  
        cons.gridx=0;cons.gridy=0;
        this.add(sourceLbl,cons); 
        cons.gridx=1;cons.gridy=0;
        this.add(sourceTxt,cons); 
        
        //dest button
        cons.gridx=2;cons.gridy=1;
        this.add(destBtn,cons);  
        cons.gridx=0;cons.gridy=1;
        this.add(destLbl,cons);  
        cons.gridx=1;cons.gridy=1;
        this.add(destTxt,cons); 
        
        //start button
        startBtn = new JButton("开始导出");
        cons.gridx=3;cons.gridy=1;
        this.add(startBtn,cons);
        startBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				startAction();
				
			}

			
		});
        
        //meta and status
        cons.gridx=0;cons.gridy=2;cons.gridwidth=3;cons.gridheight=1;
        this.add(metaLabel,cons);
        cons.gridx=0;cons.gridy=3;cons.gridwidth=3;cons.gridheight=1;
        this.add(statusLabel,cons);
        
        this.setBounds(200, 200, 800, 400); 
        this.setVisible(true);  
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        sourceBtn.addActionListener(this);  
        viewExcelBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DataFrame df = readExcelJoiner.readExcel(sourceTxt.getText());
				df.show();
			}
		});  
        destBtn.addActionListener(this);  
        
        
        config = AppConfig.getInstance();
    	dcmService = new DicomService();
    	dcmService.storeSCP();
    }  
    private void startAction() {
    	File sf = new File(sourceTxt.getText());
    	File df = new File(sourceTxt.getText());
    	String meta="",status="";
    	if(sf.isDirectory()){  
            meta="src 文件夹:"+sf.getAbsolutePath();
            status="Src 选 择了文件夹";
        }else if(sf.isFile()){  
            meta="src 文件:"+sf.getAbsolutePath();
            status="Src 选 择了文件";
            
        } 
    	
    	if(df.isDirectory()){  

            meta+=" \ndest 文件夹:"+df.getAbsolutePath();
            status="dest 选 择了文件夹";
            
        }else if(df.isFile()){  
            meta+=" \ndest 文件:"+df.getAbsolutePath();
            status="dest 选 择了文件";
        } 
    	metaLabel.setText(meta);
    	statusLabel.setText(status);
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				DataFrame df = readExcelJoiner.readExcel(sourceTxt.getText());

				statusLabel.setText("读取Excel成功,正在导出...");
				ListIterator<Map> rows = df.itermap();
				for(;rows.hasNext();){
					Map row = rows.next();
					metaLabel.setText(row.toString());
					dcmService.dcmMove(row);
				}
				startBtn.setEnabled(true);
				statusLabel.setText("任务结束");
			}
		}).start();
    	startBtn.setEnabled(false);
	}
    
    @Override  
    public void actionPerformed(ActionEvent e) {  
    	JButton source = (JButton)e.getSource();
    	String defDir = "c:\\";
    	if(!StringUtils.isEmpty(sourceTxt.getText())){
    		defDir=sourceTxt.getText();
    	}if(!StringUtils.isEmpty(destTxt.getText())){
    		defDir=destTxt.getText();
    	}
        JFileChooser jfc=new JFileChooser(defDir);  
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
        jfc.showDialog(new JLabel(), "Choose "+source.getText());  
        File file=jfc.getSelectedFile();  
        if(file==null){
        	System.out.println("没有选择");
        	return;
        }
        if(file.isDirectory()){  
            System.out.println("文件夹:"+file.getAbsolutePath()); 
            
        }else if(file.isFile()){  
            System.out.println("文件:"+file.getAbsolutePath());  
        }  
        System.out.println(jfc.getSelectedFile().getName());  
        
        System.out.println(source.getText());
        if(sourceBtn==source){
        	sourceTxt.setText(file.getAbsolutePath());
        	try{
        		FileUtils.writeStringToFile(new File("sourcedir.txt"), file.getAbsolutePath());
        	}catch(Exception ee){
        		
        	}
        }
        if(destBtn==source){
        	destTxt.setText(file.getAbsolutePath());
        	try{
        		FileUtils.writeStringToFile(new File("destdir.txt"), file.getAbsolutePath());
        	}catch(Exception ee){
        		
        	}
        	
        }
        
          
    }  
    

  
}