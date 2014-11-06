package view;

import model.Graph;

import javax.swing.table.AbstractTableModel;

public class VertexTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private final Object columnNames[] = { "#", "Degree" , "Uniform Resource Locator" };
	private Graph internetModel;
	
	public VertexTableModel(Graph internetModel) {
       this.internetModel = internetModel;
	}

	@Override
	public int getColumnCount() {




        return 0;
	}

	@Override
	public int getRowCount() {
		return 0;
	}

	@Override
	public Object getValueAt(int row, int column) {
		return null;
	}

	@Override
	public String getColumnName(int column) {
		return null;
	}
}
