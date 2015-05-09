import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import util.ECDSA;

/**
 * GUI and main program
 */

/**
 * @author nashir
 *
 */
public class Main {

	private static Main instance;
	
	private static int MAINFRAME_WIDTH = 600;
	private static int MAINFRAME_HEIGHT = 300;
	
	private static JFrame mainFrame;
	private static GroupLayout mainLayout;
	
	private static JPanel inputPanel;
	private static GroupLayout inputLayout;
	private static JPanel actionPanel;
	private static GroupLayout actionLayout;

	private static JLabel inputLabel;
	private static JTextField inputField;
	private static JButton inputButton;
	private static JFileChooser inputChooser;
	
	private static JLabel privateLabel;
	private static JTextField privateField;
	private static JButton privateButton;
	private static JFileChooser privateChooser;
	
	private static JLabel publicLabel;
	private static JTextField publicXField;
	private static JTextField publicYField;
	private static JButton publicButton;
	private static JFileChooser publicChooser;

	private static JButton signButton;
	private static JButton checkButton;

	private static BigInteger privateKey;
	private static BigInteger[] publicKey;
	private static byte[] message;
	
	public static Main getInstance() {
		instance = new Main();
		prepareGUI();
		return instance;
	}

	private static void prepareGUI() {
		prepareInput();
		prepareAction();
		prepareMain();
		postProcess();
	}
	
	private static void prepareInput() {
		// panel
		inputPanel = new JPanel();
		inputPanel.setBorder(BorderFactory.createTitledBorder("Input"));
		
		// layout
		inputLayout = new GroupLayout(inputPanel);
		inputLayout.setAutoCreateGaps(true);
		inputLayout.setAutoCreateContainerGaps(true);
		inputPanel.setLayout(inputLayout);
		
		// form
		inputLabel = new JLabel("Audio");
		inputField = new JTextField();
		inputField.setEditable(false);
		inputButton = new JButton("Browse Audio");
		inputButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (inputChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					inputField.setText(inputChooser.getSelectedFile().getAbsolutePath());
					try {
						message = Files.readAllBytes(Paths.get(inputChooser.getSelectedFile().getAbsolutePath()));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		inputChooser = new JFileChooser("res/audio");
		inputChooser.setAcceptAllFileFilterUsed(false);
		inputChooser.setFileFilter(new FileNameExtensionFilter("Audio Files", "mid", "mp3", "ogg", "wav", "wma"));
		
		privateLabel = new JLabel("Private Key");
		privateField = new JTextField();
		privateButton = new JButton("Browse Key");
		privateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (privateChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					try {
						byte[] privateKey = Files.readAllBytes(Paths.get(privateChooser.getSelectedFile().getAbsolutePath()));
						privateField.setText(new String(privateKey));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		privateChooser = new JFileChooser("res/key");
		privateChooser.setAcceptAllFileFilterUsed(false);
		privateChooser.setFileFilter(new FileNameExtensionFilter("Private Keys", "pri"));
		
		publicLabel = new JLabel("Public Key");
		publicXField = new JTextField();
		publicYField = new JTextField();
		publicButton = new JButton("Browse Key");
		publicButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (publicChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					try {
						byte[] publicKey = Files.readAllBytes(Paths.get(publicChooser.getSelectedFile().getAbsolutePath()));
						String[] publicKeyPoint = new String(publicKey).split(",");
						publicXField.setText(publicKeyPoint[0]);
						publicYField.setText(publicKeyPoint[1]);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		publicChooser = new JFileChooser("res/key");
		publicChooser.setAcceptAllFileFilterUsed(false);
		publicChooser.setFileFilter(new FileNameExtensionFilter("Public Keys", "pub"));
		
		// position
		inputLayout.setHorizontalGroup(inputLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(inputLayout.createSequentialGroup()
				.addComponent(inputLabel, 65, 65, 65)
				.addComponent(inputField)
				.addComponent(inputButton, 120, 120, 120))
			.addGroup(inputLayout.createSequentialGroup()
				.addComponent(privateLabel, 65, 65, 65)
				.addComponent(privateField)
				.addComponent(privateButton, 120, 120, 120))
			.addGroup(inputLayout.createSequentialGroup()
				.addComponent(publicLabel, 65, 65, 65)
				.addComponent(publicXField)
				.addComponent(publicButton, 120, 120, 120))
			.addGroup(inputLayout.createSequentialGroup()
				.addGap(77)
				.addComponent(publicYField)
				.addGap(126)));
		inputLayout.setVerticalGroup(inputLayout.createSequentialGroup()
			.addGroup(inputLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(inputLabel)
				.addComponent(inputField)
				.addComponent(inputButton))
			.addGroup(inputLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(privateLabel)
				.addComponent(privateField)
				.addComponent(privateButton))
			.addGroup(inputLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(publicLabel)
				.addComponent(publicXField)
				.addComponent(publicButton))
			.addGroup(inputLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(publicYField)));
	}

	private static void prepareAction() {
		// panel
		actionPanel = new JPanel();
		actionPanel.setBorder(BorderFactory.createTitledBorder("Action"));
		
		// layout
		actionLayout = new GroupLayout(actionPanel);
		actionLayout.setAutoCreateGaps(true);
		actionLayout.setAutoCreateContainerGaps(true);
		actionPanel.setLayout(actionLayout);
		
		// form
		signButton = new JButton("Sign");
		signButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				long start = System.currentTimeMillis();
				privateKey = new BigInteger(privateField.getText());
				String result = ECDSA.sign(message, privateKey);
				
				byte[] resultByte = result.getBytes();
				byte[] output = new byte[message.length+resultByte.length];
				for (int i = 0; i < message.length+resultByte.length; i++) {
					if (i < message.length) output[i] = message[i];
					else output[i] = resultByte[i-message.length];
				}
				
				try {
					String ext = inputField.getText();
					ext = ext.substring(ext.length()-3, ext.length());
					FileOutputStream fos = new FileOutputStream("res/result/test." + ext);
					fos.write(output);
					fos.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println((System.currentTimeMillis() - start) + " ms");
			}
		});

		checkButton = new JButton("Check");
		checkButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				long start = System.currentTimeMillis();
				publicKey = new BigInteger[2];
				publicKey[0] = new BigInteger(publicXField.getText(), 16);
				publicKey[1] = new BigInteger(publicYField.getText(), 16);
				boolean result = ECDSA.verify(message, publicKey);
				if (result) JOptionPane.showMessageDialog(null, "Valid audio file.");
				else JOptionPane.showMessageDialog(null, "Invalid audio file.");
				System.out.println((System.currentTimeMillis() - start) + " ms");
			}
		});
		
		// position
		actionLayout.setHorizontalGroup(actionLayout.createSequentialGroup()
			.addComponent(checkButton, MAINFRAME_WIDTH/3, MAINFRAME_WIDTH/2, MAINFRAME_WIDTH/2)
			.addComponent(signButton, MAINFRAME_WIDTH/3, MAINFRAME_WIDTH/2, MAINFRAME_WIDTH/2));
		actionLayout.setVerticalGroup(actionLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
			.addComponent(checkButton)
			.addComponent(signButton));
	}

	private static void prepareMain() {
		// main frame
		mainFrame = new JFrame("Digital Audio ECDSA");
		mainFrame.setSize(MAINFRAME_WIDTH, MAINFRAME_HEIGHT);
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// main layout
		mainLayout = new GroupLayout(mainFrame.getContentPane());
		mainLayout.setAutoCreateGaps(true);
		mainLayout.setAutoCreateContainerGaps(true);
		mainFrame.getContentPane().setLayout(mainLayout);
		
		// position
		mainLayout.setHorizontalGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addComponent(inputPanel)
			.addComponent(actionPanel));
		mainLayout.setVerticalGroup(mainLayout.createSequentialGroup()
			.addComponent(inputPanel)
			.addComponent(actionPanel));
	}

	private static void postProcess() {
		mainFrame.add(inputPanel);
		mainFrame.add(actionPanel);
		mainFrame.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Main.getInstance();
	}

}
