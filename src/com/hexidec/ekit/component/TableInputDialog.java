/*
GNU Lesser General Public License

TableInputDialog
Copyright (C) 2000 Howard Kistler

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package com.hexidec.ekit.component;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.hexidec.util.Translatrix;

/** Class for providing a dialog that lets the user specify values for tag attributes
  */
public class TableInputDialog extends JDialog implements PropertyChangeListener
{
	/** <code>serialVersionUID</code> */
	private static final long	serialVersionUID	= -9188362125649252749L;
	private Number inputRows;
	private Number inputCols;
	private Number inputBorder;
	private Number inputSpace;
	private Number inputPad;
	JOptionPane jOptionPane;

	private JFormattedTextField jtxfRows;
	private JFormattedTextField jtxfCols;
	private JFormattedTextField jtxfBorder;
	private JFormattedTextField jtxfSpace;
	private JFormattedTextField jtxfPad;
	private Object[] buttonLabels;

	/**
	 * Creates a TableInputDialog.
	 * @param parent the parent Frame or Dialog of the TableInputDialog
	 * @param title the dialog title
	 * @param bModal whether the dialog should be modal
	 * @return the TableInputDialog instance
	 */
	public TableInputDialog create(Window parent, String title, boolean bModal)
	{
		TableInputDialog me;
		if (parent instanceof Frame)
		{
			me = new TableInputDialog((Frame) parent, title, bModal);
		}
		else if (parent instanceof Dialog)
		{
			me = new TableInputDialog((Dialog) parent, title, bModal);
			me.pack();
		}
		else
		{
			throw new IllegalArgumentException("parent must be a Frame or a Dialog");
		}
		return me;
	}

	private TableInputDialog(Dialog parent, String title, boolean bModal)
	{
		super(parent, title, bModal);
		init();
	}

	/**
	 * @deprecated use {@link #create(Window, String, boolean)} instead
	 */
	// TODO reduce visibility to private in next release
	public TableInputDialog(Frame parent, String title, boolean bModal)
	{
		super(parent, title, bModal);
		init();
		pack();
	}

	private void init()
	{
		jtxfRows     = new JFormattedTextField(NumberFormat.getInstance());
		jtxfCols     = new JFormattedTextField(NumberFormat.getInstance());
		jtxfBorder   = new JFormattedTextField(NumberFormat.getInstance());
		jtxfSpace    = new JFormattedTextField(NumberFormat.getInstance());
		jtxfPad      = new JFormattedTextField(NumberFormat.getInstance());
		buttonLabels = new Object[] { Translatrix.getTranslationString("DialogAccept"), Translatrix.getTranslationString("DialogCancel") };
		Object[] panelContents = {
			Translatrix.getTranslationString("TableRows"),        jtxfRows,
			Translatrix.getTranslationString("TableColumns"),     jtxfCols,
			Translatrix.getTranslationString("TableBorder"),      jtxfBorder,
			Translatrix.getTranslationString("TableCellSpacing"), jtxfSpace,
			Translatrix.getTranslationString("TableCellPadding"), jtxfPad
		};
		jOptionPane = new JOptionPane(panelContents, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, buttonLabels, buttonLabels[0]);

		setContentPane(jOptionPane);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we)
			{
				jOptionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
			}
		});

		jOptionPane.addPropertyChangeListener(this);
	}

	/** {@inheritDoc} */
	public void propertyChange(PropertyChangeEvent e)
	{
		String prop = e.getPropertyName();
		if (isVisible() 
			&& (e.getSource() == jOptionPane)
			&& (prop.equals(JOptionPane.VALUE_PROPERTY) || prop.equals(JOptionPane.INPUT_VALUE_PROPERTY)))
		{
			Object value = jOptionPane.getValue();
			if (value == JOptionPane.UNINITIALIZED_VALUE)
			{
				return;
			}
			jOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
			if (value.equals(buttonLabels[0]))
			{
				inputRows   = (Number) jtxfRows.getValue();
				inputCols   = (Number) jtxfCols.getValue();
				inputBorder = (Number) jtxfBorder.getValue();
				inputSpace  = (Number) jtxfSpace.getValue();
				inputPad    = (Number) jtxfPad.getValue();
				setVisible(false);
			}
			else
			{
				inputRows   = new Integer(-1);
				inputCols   = inputRows;
				inputBorder = inputRows;
				inputSpace  = inputRows;
				inputPad    = inputRows;
				setVisible(false);
			}
		}
	}

	public int getRows()
	{
		return inputRows.intValue();
	}

	public int getCols()
	{
		return inputCols.intValue();
	}

	public int getBorder()
	{
		return inputBorder.intValue();
	}

	public int getSpacing()
	{
		return inputSpace.intValue();
	}

	public int getPadding()
	{
		return inputPad.intValue();
	}
}

