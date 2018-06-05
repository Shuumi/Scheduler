package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Scheduler;

import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class SwingMain extends JFrame {

	private SwingMain frame;
	private JPanel contentPane;
	private int width, height;
	public PlaceholderTextField input;
	public JLabel lblLU;
	public JLabel lblSerialize;
	public JLabel lblFsr;
	public JLabel lblVsr;
	public JLabel lblCsr;
	private JButton btnSerialize;

	final public Color red = new Color(0xFF, 0x00, 0x0D);
	final public Color yellow = new Color(0xFF, 0xAE, 0x42);
	final public Color green = new Color(0, 255, 0);
	private JScrollPane scrollPane;
	private DrawGraph panel;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SwingMain frame = new SwingMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the frame.
	 */
	public SwingMain() {
		frame = this;
		width = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
		height = Toolkit.getDefaultToolkit().getScreenSize().height / 2;

		setTitle("Scheduler");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(width / 2, height - 500 / 2, width, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 1, 776, 83, 0 };
		gbl_contentPane.rowHeights = new int[] { 60, 30, 30, 30, 45, 251, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		input = new PlaceholderTextField();
		input.setPlaceholder("insert your schedule here and make sure there is no whitespace character in it");
		GridBagConstraints gbc_input = new GridBagConstraints();
		gbc_input.fill = GridBagConstraints.BOTH;
		gbc_input.insets = new Insets(0, 0, 5, 5);
		gbc_input.gridx = 1;
		gbc_input.gridy = 1;
		contentPane.add(input, gbc_input);
		input.setColumns(10);

		lblFsr = new JLabel("FSR");
		lblFsr.setHorizontalAlignment(SwingConstants.CENTER);
		lblFsr.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblFsr.setBackground(red);
		lblFsr.setOpaque(true);
		GridBagConstraints gbc_fsr = new GridBagConstraints();
		gbc_fsr.fill = GridBagConstraints.BOTH;
		gbc_fsr.insets = new Insets(0, 0, 5, 0);
		gbc_fsr.gridx = 2;
		gbc_fsr.gridy = 1;
		contentPane.add(lblFsr, gbc_fsr);
		
		lblSerialize = new JLabel("");
		GridBagConstraints gbc_lblSerialize = new GridBagConstraints();
		gbc_lblSerialize.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblSerialize.insets = new Insets(0, 0, 5, 5);
		gbc_lblSerialize.gridx = 1;
		gbc_lblSerialize.gridy = 2;
		contentPane.add(lblSerialize, gbc_lblSerialize);

		lblVsr = new JLabel("VSR");
		lblVsr.setBackground(red);
		lblVsr.setHorizontalAlignment(SwingConstants.CENTER);
		lblVsr.setOpaque(true);
		GridBagConstraints gbc_lblVsr = new GridBagConstraints();
		gbc_lblVsr.fill = GridBagConstraints.BOTH;
		gbc_lblVsr.insets = new Insets(0, 0, 5, 0);
		gbc_lblVsr.gridx = 2;
		gbc_lblVsr.gridy = 2;
		contentPane.add(lblVsr, gbc_lblVsr);

		lblCsr = new JLabel("CSR");
		lblCsr.setOpaque(true);
		lblCsr.setBackground(red);
		lblCsr.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblCsr = new GridBagConstraints();
		gbc_lblCsr.fill = GridBagConstraints.BOTH;
		gbc_lblCsr.insets = new Insets(0, 0, 5, 0);
		gbc_lblCsr.gridx = 2;
		gbc_lblCsr.gridy = 3;
		contentPane.add(lblCsr, gbc_lblCsr);

		btnSerialize = new JButton("Serialize");
		btnSerialize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!input.getText().equals("")) {
					rightInput();
					Scheduler.beginScheduling(input.getText(), frame);
				} else {
					wrongInput();
				}
			}
		});
		
		lblLU = new JLabel("");
		lblLU.setOpaque(true);
		GridBagConstraints gbc_lblLU = new GridBagConstraints();
		gbc_lblLU.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblLU.insets = new Insets(0, 0, 5, 5);
		gbc_lblLU.gridx = 1;
		gbc_lblLU.gridy = 4;
		contentPane.add(lblLU, gbc_lblLU);
		GridBagConstraints gbc_btnSerialize = new GridBagConstraints();
		gbc_btnSerialize.fill = GridBagConstraints.BOTH;
		gbc_btnSerialize.insets = new Insets(0, 0, 5, 0);
		gbc_btnSerialize.gridx = 2;
		gbc_btnSerialize.gridy = 4;
		contentPane.add(btnSerialize, gbc_btnSerialize);
		
		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 5;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		scrollPane.setViewportView(panel);
	}
	
	public void setGraph(DrawGraph dg){
		scrollPane.setViewportView(dg);
		scrollPane.repaint();
	}

	private void wrongInput() {
		input.setBackground(red);
	}
	
	private void rightInput(){
		input.setBackground(Color.white);
	}
}
