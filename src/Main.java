import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

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
	private static int MAINFRAME_HEIGHT = 400;
	
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

	private static JButton signButton;
	private static JButton checkButton;

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
		inputLabel = new JLabel("Input");
		inputField = new JTextField();
		inputField.setEditable(false);
		inputButton = new JButton("Browse Audio");
		inputButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (inputChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					inputField.setText(inputChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		inputChooser = new JFileChooser("res/audio");
		inputChooser.setAcceptAllFileFilterUsed(false);
		inputChooser.setFileFilter(new FileNameExtensionFilter("Audio Files", "mp3", "wav"));
		
		// position
		inputLayout.setHorizontalGroup(inputLayout.createSequentialGroup()
			.addComponent(inputLabel)
			.addComponent(inputField)
			.addComponent(inputButton));
		inputLayout.setVerticalGroup(inputLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
			.addComponent(inputLabel)
			.addComponent(inputField)
			.addComponent(inputButton));
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
		checkButton = new JButton("Check");
		
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
