package view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import connector.Tester;

public class Main {
	public static void main(String[] args) throws Exception {
		JFrame frame = new JFrame("Start");
		frame.setLayout(null);
		frame.setSize(360, 120);
		frame.setResizable(true);
		JPanel panel = new JPanel();
		FlowLayout fl = new FlowLayout(FlowLayout.CENTER);
		panel.setLayout(fl);
		JLabel label = new JLabel("adresse serveur: ");
		JLabel sep = new JLabel(":");
		JTextField ip1 = new JTextField();
		JTextField ip2 = new JTextField();
		JTextField ip3 = new JTextField();
		JTextField ip4 = new JTextField();
		String[] s = InetAddress.getLocalHost().getHostAddress().split("\\.");
		for (int i = 0; i < s.length; i++) {
			if(s[i].length()==1){
				s[i]="00"+s[i];
			}else if(s[i].length()==2){
				s[i]="0"+s[i];
			}
		}
		ip1.setText(s[0]);
		ip2.setText(s[1]);
		ip3.setText(s[2]);
		ip4.setText(s[3]);
		JButton button = new JButton("C'est parti !");
		JTextField port = new JTextField();
		port.setText("8051");
		panel.add(label);
		panel.add(ip1);
		panel.add(ip2);
		panel.add(ip3);
		panel.add(ip4);
		panel.add(sep);
		panel.add(port);
		panel.add(button);

		frame.setContentPane(panel);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Tester bsC = new Tester(ip1.getText(), ip2.getText(), ip3.getText(), ip4.getText(),
						Integer.parseInt(port.getText()));
				try {
					if(bsC.test()){
						frame.dispose();
						return;
					}
				} catch (UnknownHostException  err){
					label.setText("Inconnu, rééssayez:");
					err.printStackTrace();
				} catch(IOException err) {
					// TODO: handle exception
					label.setText("Indisponible, rééssayez:");
					err.printStackTrace();
				}
			}
		});
	}
}
