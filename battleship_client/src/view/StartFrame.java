package view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import connector.BattleshipClient;

public class StartFrame {
	public static void main(String[] args) throws Exception {
		JFrame frame = new JFrame("StartFrame");
		frame.setLayout(null);
		frame.setSize(300, 200);
		frame.setResizable(true);
		JPanel pan=new JPanel(); //Panel
        FlowLayout bl = new FlowLayout(FlowLayout.CENTER);   //layoutManager
        pan.setLayout(bl);
		JLabel label = new JLabel("adresse serveur: ");
		JLabel sep = new JLabel(":");
		JTextField ip1 = new JTextField();
		ip1.setText("192");
		JTextField ip2 = new JTextField();
		ip2.setText("168");
		JTextField ip3 = new JTextField();
		ip3.setText("001");
		JTextField ip4 = new JTextField();
		ip4.setText("063");
		JButton button = new JButton("C'est parti !");
		JTextField port = new JTextField();
		port.setText("8051");
		pan.add(label);
		pan.add(ip1);
		pan.add(ip2);
		pan.add(ip3);
		pan.add(ip4);
		pan.add(sep);
		pan.add(port);
		pan.add(button);

		frame.setContentPane(pan);
		frame.setVisible(true); 
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Scanner sc = new Scanner(port.getText());
				new BattleshipClient(ip1.getText(),ip2.getText(),ip3.getText(),ip4.getText(), sc.nextInt());				
				sc.close();
				frame.dispose();
			}
		});
	}
}
