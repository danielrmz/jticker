import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import java.awt.Dimension;

public class Table extends JPanel {
	private static final long serialVersionUID = -4058326085312364394L;
	public JTable table;
	public SportModel sm = new SportModel();
	
	public Table(int width, int height){
		this.setSize(new Dimension(width,height));
		table = new JTable(this.sm);
		table.setPreferredScrollableViewportSize(new Dimension(width, height));
		JScrollPane scrollPane = new JScrollPane(table);
	    this.add(scrollPane);
	}
	
	public void setData(Object[][] data){
		this.sm.data = data;
		this.sm.fireTableDataChanged();
	}
	
	class SportModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		public String[] columnNames = {"Tiempo",
                "Equipos",
                "Marcador"};
		public Object[][] data;

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }
    }
}
