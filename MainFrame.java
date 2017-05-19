import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.*;
import javax.swing.filechooser.*;
import java.io.*;
//
import javax.print.DocFlavor.*;
//
public class MainFrame extends JFrame {
	
	private JTextArea asmArea;
	private JTextArea hexArea;
	private JTextArea logArea;
	private JScrollPane scrollAsm;
	private JScrollPane scrollHex;
	private JScrollPane scrollLog;
	private JButton encodeButton;
	private JButton decodeButton;
	private MenuBar menuBar;
	private Menu fileMenu;
	private MenuItem openASM;
	private MenuItem openTXT;
	private MenuItem saveASM;
	private MenuItem saveTXT;



	public MainFrame(){
		initUI();
	}

	private void initUI(){
		JPanel panel = new JPanel(new BorderLayout());

		setTitle("MIPSEncoderDecoder");

		asmArea = new JTextArea(20,25);
		//asmArea.setWrapStyleWord(true);
		hexArea = new JTextArea(20,25);
		logArea = new JTextArea(3,10);
		scrollAsm = new JScrollPane(asmArea);
		//scrollAsm.setBounds(10,60,780,500);
		scrollHex = new JScrollPane(hexArea);
		scrollLog = new JScrollPane(logArea);
		scrollAsm.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollHex.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		encodeButton = new JButton("Encode");
		encodeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				hexArea.setText("");
				Parser p = new Parser(asmArea.getText());
				try{
				hexArea.append(p.parseASMString());
				}catch(Exception exc){
					System.out.println("Erro! \n"+exc.getMessage());
				}
			}
		});
		decodeButton = new JButton("Decode");
		decodeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				asmArea.setText("");
				Parser p = new Parser(hexArea.getText());
				try{
				//asmArea.append(p.parseCodeString());
				String decodedASM = p.parseCodeString();
				asmArea.append(decodedASM);
				}catch(Exception exc){
					JOptionPane.showMessageDialog(null,"Failed to Decode","Please verify hex codes",JOptionPane.ERROR_MESSAGE);
					System.out.println("Erro! \n"+exc.getMessage());
				}
			}
		});		

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
		buttonPanel.add(encodeButton);
		buttonPanel.add(decodeButton);



		panel.add(scrollAsm,BorderLayout.LINE_START);
		panel.add(buttonPanel,BorderLayout.CENTER);
		

		panel.add(scrollHex,BorderLayout.LINE_END);
		panel.add(scrollLog,BorderLayout.PAGE_END);

		//Menus
		menuBar = new MenuBar();
		fileMenu = new Menu("File");
		openASM = new MenuItem("Open .asm");
		openASM.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
        		"ASM Files", "asm");
        		chooser.setFileFilter(filter);
        		int returnVal = chooser.showOpenDialog(getParent());
        		if(returnVal == JFileChooser.APPROVE_OPTION){
        			File selectedFile = chooser.getSelectedFile();

        			asmArea.setText("");
        			try{
        			String line = "";
        			//
        			//

        			BufferedReader br = new BufferedReader(new FileReader(selectedFile));
        			while((line = br.readLine()) != null){
        				asmArea.append(line+"\n");
        			}

        			}catch(Exception ex){

        			}
        		}

			}
		});
		openTXT = new MenuItem("Open .txt");
		openTXT.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
        		"TXT Files", "txt");
        		chooser.setFileFilter(filter);
        		int returnVal = chooser.showOpenDialog(getParent());
        		if(returnVal == JFileChooser.APPROVE_OPTION){
        			File selectedFile = chooser.getSelectedFile();
        			hexArea.setText("");
        			try{
        			String line = "";
        			BufferedReader br = new BufferedReader(new FileReader(selectedFile));
        			while((line = br.readLine()) != null){
        				hexArea.append(line+"\n");
        			}

        			}catch(Exception ex){
        				
        			}
        		}

			}
		});	

		saveASM = new MenuItem("Save .asm");
		saveASM.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showSaveDialog(getParent());
				if(returnVal  == JFileChooser.APPROVE_OPTION){
					try{
					String filePath = chooser.getSelectedFile().getAbsolutePath();
					FileWriter fw = new FileWriter(filePath+".asm");
					fw.write(asmArea.getText());
					fw.close();
					}catch(Exception fwexc){
						System.out.println("Failed writing file");
						System.out.println(fwexc.getMessage());
					}
				}


			}
		});

		saveTXT = new MenuItem("Save .txt");
		saveTXT.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showSaveDialog(getParent());
				if(returnVal  == JFileChooser.APPROVE_OPTION){
					try{
					String filePath = chooser.getSelectedFile().getAbsolutePath();
					FileWriter fw = new FileWriter(filePath+".txt");
					fw.write(hexArea.getText());
					fw.close();
					}catch(Exception fwexc){
						System.out.println("Failed writing file");
						System.out.println(fwexc.getMessage());
					}
				}
			}
		});
		fileMenu.add(openASM);
		fileMenu.add(openTXT);
		fileMenu.add(saveASM);
		fileMenu.add(saveTXT);
		menuBar.add(fileMenu);

		setMenuBar(menuBar);
		//

		setContentPane(panel);
		pack();
		//setResizable(false);
		setSize(800,500);

		setLocationRelativeTo(null);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setVisible(true);
	}

































}