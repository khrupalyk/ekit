/*
GNU Lesser General Public License

SimpleInfoDialog
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
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import com.hexidec.util.Translatrix;

/** Class for providing a dialog that lets the user specify values for tag attributes.
  */
public class SimpleInfoDialog extends JDialog implements PropertyChangeListener
{
	/** <code>serialVersionUID</code> */
	private static final long	serialVersionUID	= -1105675946549911520L;

	public static final int ERROR    = JOptionPane.ERROR_MESSAGE;
	public static final int INFO     = JOptionPane.INFORMATION_MESSAGE;
	public static final int WARNING  = JOptionPane.WARNING_MESSAGE;
	public static final int QUESTION = JOptionPane.QUESTION_MESSAGE;
	public static final int PLAIN    = JOptionPane.PLAIN_MESSAGE;

	private JOptionPane jOptionPane;
	private Object[] buttonLabels;

	/**
	 * Creates and displays a SimpleInfoDialog.
	 * @param parent the parent Frame or Dialog of the SimpleInfoDialog
	 * @param title the dialog title
	 * @param bModal whether the dialog should be modal
	 * @param message the message to be displayed
	 * @return the SimpleInfoDialog instance
	 */
	public static SimpleInfoDialog show(Window parent, String title, boolean bModal, String message)
	{
		return show(parent, title, bModal, message, WARNING);
	}

	/**
	 * Creates and displays a SimpleInfoDialog.
	 * @param parent the parent Frame or Dialog of the SimpleInfoDialog
	 * @param title the dialog title
	 * @param bModal whether the dialog should be modal
	 * @param message the message to be displayed
	 * @param type one of the type constants: {@link #ERROR}, {@link #WARNING}, {@link #INFO}, {@link #QUESTION},
	 *            {@link #PLAIN}
	 * @return the SimpleInfoDialog instance
	 */
	public static SimpleInfoDialog show(Window parent, String title, boolean bModal, String message, int type)
	{
		SimpleInfoDialog me;
		if (parent instanceof Frame)
		{
			me = new SimpleInfoDialog((Frame) parent, title, bModal, message, type);
		}
		else if (parent instanceof Dialog)
		{
			me = new SimpleInfoDialog((Dialog) parent, title, bModal, message, type);
			me.showDialog(parent);
		}
		else
		{
			throw new IllegalArgumentException("parent must be a Frame or a Dialog");
		}
		return me;
	}

	private SimpleInfoDialog(Dialog parentDialog, String title, boolean bModal, String message, int type)
	{
		super(parentDialog, title, bModal);
		init(message, type);
	}

	/**
	 * @deprecated use {@link #show(Window, String, boolean, String, int)} instead
	 */
	// TODO reduce visibility to private in next release
	public SimpleInfoDialog(Frame parent, String title, boolean bModal, String message, int type)
	{
		super(parent, title, bModal);
		init(message, type);
		showDialog(parent);
	}

	private void init(String message, int type)
	{
		if(type == QUESTION)
		{
			buttonLabels = new Object[]{ Translatrix.getTranslationString("DialogAccept"), Translatrix.getTranslationString("DialogCancel") };
			jOptionPane = new JOptionPane(new JLabel(message, SwingConstants.CENTER), JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, buttonLabels, buttonLabels[0]);
		}
		else
		{
			buttonLabels = new Object[]{ Translatrix.getTranslationString("DialogClose") };
			jOptionPane = new JOptionPane(new JLabel(message, SwingConstants.CENTER), JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, buttonLabels, buttonLabels[0]);
		}

		setContentPane(jOptionPane);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		jOptionPane.addPropertyChangeListener(this);

	}

	/** {@inheritDoc} */
	public void propertyChange(PropertyChangeEvent e)
	{
		String prop = e.getPropertyName();
		if(isVisible() && (e.getSource() == jOptionPane) && (prop.equals(JOptionPane.VALUE_PROPERTY) || prop.equals(JOptionPane.INPUT_VALUE_PROPERTY)))
		{
			setVisible(false);
		}
	}

	private void showDialog(Window parent) {
		this.pack();
		final Dimension parentSize = parent.getSize();
		final Point parentLocation = parent.getLocation();
		final Dimension mySize = getSize();
		int centerX = (int) ((parentSize.getWidth()  / 2 + parentLocation.getX()) - (mySize.getWidth()  / 2));
		int centerY = (int) ((parentSize.getHeight() / 2 + parentLocation.getY()) - (mySize.getHeight() / 2));
		if (centerX < 0) { centerX = 0; }
		if (centerY < 0) { centerY = 0; }
		this.setLocation(centerX, centerY);
		this.show();
	}

	/**
	 * @deprecated use {@link #show(Window, String, boolean, String)} instead
	 */
	// TODO reduce visibility to private in next release
	public SimpleInfoDialog(Frame parent, String title, boolean bModal, String message)
	{
		this(parent, title, bModal, message, WARNING);
	}

	public String getDecisionValue()
	{
		return jOptionPane.getValue().toString();
	}
}
