package view;

import javax.swing.table.AbstractTableModel;

import model.Graph;

public class VertexTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private final Object columnNames[] = { "#", "Degree" , "Uniform Resource Locator" };
	private Graph internetModel;
	
	public VertexTableModel(Graph internetModel) {

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
