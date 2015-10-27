/*
GNU Lesser General Public License

UserInputDialog
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.hexidec.util.Translatrix;

/** Class for providing a dialog that lets the user specify values for tag attributes
  */
public class UserInputDialog extends JDialog implements PropertyChangeListener
{
	/** <code>serialVersionUID</code> */
	private static final long	serialVersionUID	= -5481134586944503153L;

	private String inputText = "";
	JOptionPane jOptionPane;
	private JTextField jtxfInput;
	Object[] buttonLabels;

	/**
	 * Creates and displays a FontSelectorDialog.
	 * @param parent the parent Frame or Dialog of the FontSelectorDialog
	 * @param title the dialog title
	 * @param bModal whether the dialog should be modal
	 * @param attribName
	 * @return the FontSelectorDialog instance
	 */
	public static UserInputDialog show(Window parent, String title, boolean bModal, String attribName)
	{
		return show(parent, title, bModal, attribName, "");
	}

	/**
	 * Creates and displays a FontSelectorDialog.
	 * @param parent the parent Frame or Dialog of the FontSelectorDialog
	 * @param title the dialog title
	 * @param bModal whether the dialog should be modal
	 * @param attribName
	 * @param defaultText
	 * @return the FontSelectorDialog instance
	 */
	public static UserInputDialog show(Window parent, String title, boolean bModal, String attribName, String defaultText)
	{
		UserInputDialog me;
		if (parent instanceof Frame)
		{
			me = new UserInputDialog((Frame) parent, title, bModal, attribName, defaultText);
		}
		else if (parent instanceof Dialog)
		{
			me = new UserInputDialog((Dialog) parent, title, bModal, attribName, defaultText);
			me.pack();
			me.show();
			me.jtxfInput.requestFocus();
		}
		else
		{
			throw new IllegalArgumentException("parent must be a Frame or a Dialog");
		}
		return me;
	}

	private UserInputDialog(Dialog parent, String title, boolean bModal, String attribName, String defaultText)
	{
		super(parent, title, bModal);
		init(attribName, defaultText);
	}

	/**
	 * @deprecated use {@link #show(Window, String, boolean, String, String)} instead
	 */
	// TODO reduce visibility to private in next release
	public UserInputDialog(Frame parent, String title, boolean bModal, String attribName, String defaultText)
	{
		super(parent, title, bModal);
		init(attribName, defaultText);
		pack();
		show();
		jtxfInput.requestFocus();
	}

	private void init(String attribName, String defaultText)
	{
		jtxfInput = new JTextField(32);
		jtxfInput.setText(defaultText);
		Object[] panelContents = { attribName, jtxfInput };
		buttonLabels = new Object[] { Translatrix.getTranslationString("DialogAccept"), Translatrix.getTranslationString("DialogCancel") };

		jOptionPane = new JOptionPane(panelContents, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, buttonLabels, buttonLabels[0]);
		setContentPane(jOptionPane);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we)
			{
				jOptionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
			}
		});

		jtxfInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				jOptionPane.setValue(buttonLabels[0]);
			}
		});

		jOptionPane.addPropertyChangeListener(this);
	}

	/** {@inheritDoc} */
	public void propertyChange(PropertyChangeEvent e)
	{
		String prop = e.getPropertyName();
		if(isVisible() 
			&& (e.getSource() == jOptionPane)
			&& (prop.equals(JOptionPane.VALUE_PROPERTY) || prop.equals(JOptionPane.INPUT_VALUE_PROPERTY)))
		{
			Object value = jOptionPane.getValue();
			if(value == JOptionPane.UNINITIALIZED_VALUE)
			{
				return;
			}
			jOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
			if(value.equals(buttonLabels[0]))
			{
				inputText = jtxfInput.getText();
				setVisible(false);
			}
			else
			{
				inputText = null;
				setVisible(false);
			}
		}
	}

	/**
	 * @deprecated use {@link #show(Window, String, boolean, String)} instead
	 */
	// TODO reduce visibility to private in next release
	public UserInputDialog(Frame parent, String title, boolean bModal, String attribName)
	{
		this(parent, title, bModal, attribName, "");
	}

	public String getInputText()
	{
		return inputText;
	}
}

