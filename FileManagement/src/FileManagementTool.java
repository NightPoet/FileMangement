import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import java.awt.FlowLayout;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.SpringLayout;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class FileManagementTool extends JFrame {

	private JPanel contentPane;
	BusniessModule businessModule = new BusniessModule();
	private JTable fileTransTable;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BusniessModule bm = new BusniessModule();
					FileManagementTool frame = new FileManagementTool();
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
	public FileManagementTool() {
		setSize(new Dimension(700, 700));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 700);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(255, 255, 255));
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setPreferredSize(new Dimension(0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setForeground(new Color(0, 0, 0));
		tabbedPane.setBackground(new Color(255, 255, 255));
		tabbedPane.setPreferredSize(new Dimension(0, 0));
		tabbedPane.setToolTipText("1");
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		JPanel ktvPanel = new JPanel();
		ktvPanel.setBackground(new Color(255, 255, 0));
		ktvPanel.setPreferredSize(new Dimension(0, 0));
		tabbedPane.addTab("KTV 工具", null, ktvPanel, null);
		ktvPanel.setLayout(new BorderLayout(0, 0));

		JPanel ktvInputPanel = new JPanel();
		ktvInputPanel.setPreferredSize(new Dimension(0, 100));
		ktvInputPanel.setBackground(new Color(255, 0, 51));
		ktvPanel.add(ktvInputPanel, BorderLayout.NORTH);
		ktvInputPanel.setLayout(new BoxLayout(ktvInputPanel, BoxLayout.Y_AXIS));

		JPanel inputPanel1 = new JPanel();
		ktvInputPanel.add(inputPanel1);
		inputPanel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblNewLabel = new JLabel("源目录");
		inputPanel1.add(lblNewLabel);

		JTextPane txtpnGktvnew = new JTextPane();
		txtpnGktvnew.setText("J:\\KTV\\1new");
		inputPanel1.add(txtpnGktvnew);
		txtpnGktvnew.setBackground(new Color(153, 204, 204));
		txtpnGktvnew.setPreferredSize(new Dimension(650, 21));
		txtpnGktvnew.setSize(new Dimension(0, 21));
		txtpnGktvnew.setMinimumSize(new Dimension(20, 20));
		txtpnGktvnew.setToolTipText("目标目录");
		txtpnGktvnew.setSize(200, 20);

		JButton renamePreviewBtn = new JButton("改名预览");
		renamePreviewBtn.addActionListener(new ActionListener() {
			// 预览改名
			public void actionPerformed(ActionEvent e) {
				String source = txtpnGktvnew.getText();
				businessModule.setSourcePath(source);
				MessageDTO msg = new MessageDTO();
				businessModule.renamePreview(msg);
				if (msg.err != null) {
					JOptionPane.showMessageDialog(inputPanel1, msg.err);
					return;
				}

				if (msg.getOutput() == null) {
					JOptionPane.showMessageDialog(inputPanel1, "没有ＫＴＶ文件！");
					return;
				}
				setTableRename(fileTransTable, (ArrayList<FileRenameModule>) msg.getOutput());
			}

			
		});
		inputPanel1.add(renamePreviewBtn);

		JButton renameExecuteBtn = new JButton("改名");
		renameExecuteBtn.addActionListener(new ActionListener() {
			//改名
			public void actionPerformed(ActionEvent e) {
				MessageDTO msg = new MessageDTO();
				businessModule.rename(msg);
				setTableRename(fileTransTable, (ArrayList<FileRenameModule>) msg.getOutput());
			}
		});
		inputPanel1.add(renameExecuteBtn);

		JButton executeBtn = new JButton("执行");
		executeBtn.addActionListener(new ActionListener() {
			// 移动文件
			public void actionPerformed(ActionEvent e) {
				MessageDTO msg = new MessageDTO();
				businessModule.move(msg);
				setTable(fileTransTable, (ArrayList<FileTransferModule>) msg.getOutput());
			}
		});
		
				JButton previewBtn = new JButton("预览");
				previewBtn.addActionListener(new ActionListener() {
					// 预览歌曲
					public void actionPerformed(ActionEvent e) {
						String source = txtpnGktvnew.getText();
						businessModule.setSourcePath(source);
						MessageDTO msg = new MessageDTO();
						businessModule.preview(msg);
						if (msg.err != null) {
							JOptionPane.showMessageDialog(inputPanel1, msg.err);
							return;
						}

						if (msg.getOutput() == null) {
							JOptionPane.showMessageDialog(inputPanel1, "没有ＫＴＶ文件！");
							return;
						}
						setTable(fileTransTable, (ArrayList<FileTransferModule>) msg.getOutput());
					}
				});
				inputPanel1.add(previewBtn);
		inputPanel1.add(executeBtn);

		JPanel inputPanel2 = new JPanel();
		ktvInputPanel.add(inputPanel2);

		JScrollPane ktvOutputPanel = new JScrollPane(fileTransTable);
		ktvOutputPanel.setSize(new Dimension(700, 900));
		ktvOutputPanel.setPreferredSize(new Dimension(700, 700));
		ktvOutputPanel.setBackground(new Color(255, 255, 102));
		ktvPanel.add(ktvOutputPanel, BorderLayout.CENTER);

		fileTransTable = new JTable();
		fileTransTable.setSize(new Dimension(450, 400));
		ktvOutputPanel.setViewportView(fileTransTable);

		JPanel picPanel = new JPanel();
		tabbedPane.addTab("照片工具", null, picPanel, null);
	}

	private Dimension getSizeByPercent(Dimension org, int width, int height) {
		Dimension res = new Dimension(0, 0);
		System.out.println(org.getWidth() + " " + org.getHeight());
		res.setSize(org.getWidth() * width / 100, org.getHeight() * height / 100);
		return res;
	}

	private void setTable(JTable table, ArrayList<FileTransferModule> models) {
		TableModel tModel = new AbstractTableModel() {
			public Class getColumnClass(int column) {
				return java.lang.String.class;
			}

			@Override
			public int getRowCount() {
				return models.size();
			}

			@Override
			public int getColumnCount() {
				return 3;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				FileTransferModule m = models.get(rowIndex);
				if (columnIndex == 0) {
					return m.fileName;
				}

				if (columnIndex == 1) {
					return m.folderName;
				}

				if (columnIndex == 2) {
					return m.status;
				}

				return "error!";
			}
		};
		table.setModel(tModel);
	}
	
	private void setTableRename(JTable table, ArrayList<FileRenameModule> models) {
		TableModel tModel = new AbstractTableModel() {
			public Class getColumnClass(int column) {
				return java.lang.String.class;
			}

			@Override
			public int getRowCount() {
				return models.size();
			}

			@Override
			public int getColumnCount() {
				return 3;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				FileRenameModule m = models.get(rowIndex);
				if (columnIndex == 0) {
					return m.orgName;
				}

				if (columnIndex == 1) {
					return m.newName;
				}

				if (columnIndex == 2) {
					return m.status;
				}

				return "error!";
			}
		};
		table.setModel(tModel);
	}
}
