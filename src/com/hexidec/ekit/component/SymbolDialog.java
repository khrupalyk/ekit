/*
GNU Lesser General Public License

SymbolInputDialog
Copyright (C) 2000-2003 Howard Kistler

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

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import com.hexidec.ekit.EkitCore;
import com.hexidec.util.Translatrix;

public class SymbolDialog extends JDialog implements ActionListener
{
	/** <code>serialVersionUID</code> */
	private static final long	serialVersionUID	= 4348376324266132867L;

	private final static String SYMBOLS = Translatrix.getTranslationString("Symbols");
	
	private ButtonGroup buttonGroup;
	private String returnSymbol;

	/**
	 * Creates and displays a FontSelectorDialog.
	 * @param parent the parent Frame or Dialog of the FontSelectorDialog
	 * @param title the dialog title
	 * @param bModal whether the dialog should be modal
	 * @return the SymbolDialog instance
	 */
	public static SymbolDialog open(Window parent, String title, boolean bModal)
	{
		SymbolDialog me;
		if (parent instanceof Frame)
		{
			me = new SymbolDialog((Frame) parent, title, bModal);
		}
		else if (parent instanceof Dialog)
		{
			me = new SymbolDialog((Dialog) parent, title, bModal);
		}
		else
		{
			throw new IllegalArgumentException("parent must be a Frame or a Dialog");
		}
		me.pack();
		me.setVisible(true);
		return me;
	}

	private SymbolDialog(Dialog parent, String title, boolean bModal)
	{
		super(parent, title, bModal);
		init();
	}

	private SymbolDialog(Frame parent, String title, boolean bModal)
	{
		super(parent, title, bModal);
		init();
	}

	/**
	 * @deprecated use {@link #open(Window, String, boolean)} instead
	 */
	// TODO delete constructor in next release
	public SymbolDialog(EkitCore peKit, String title, boolean bModal)
	{
		super(peKit.getFrame(), title, bModal);
		init();
		pack();
		setVisible(true);
	}

	/** {@inheritDoc} */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("accept"))
		{
			setVisible(false);
			if (buttonGroup.getSelection() == null)
			{
				returnSymbol = null;
			}
			else
			{
				returnSymbol = buttonGroup.getSelection().getActionCommand();
			}
		}
		else if (e.getActionCommand().equals("cancel"))
		{
			setVisible(false);
			returnSymbol = null;
		}
	}

	/**
	 * @deprecated use of inner method should be forbidden
	 */
	// TODO reduce visibility to private in next release
	public void init()
	{
		Container contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		setBounds(100,100,400,300);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(0, 8, 0, 0));
		buttonGroup = new ButtonGroup();
		for (int i = 0; i < SYMBOLS.length(); i++)
		{
			String symbol = Character.toString(SYMBOLS.charAt(i));
			JToggleButton b = new JToggleButton(symbol);
			b.getModel().setActionCommand(symbol);
			centerPanel.add(b);
			buttonGroup.add(b);
		}

		JPanel buttonPanel= new JPanel();
		buttonPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));

		JButton saveButton = new JButton(Translatrix.getTranslationString("DialogAccept"));
		saveButton.setActionCommand("accept");
		saveButton.addActionListener(this);

		JButton cancelButton = new JButton(Translatrix.getTranslationString("DialogCancel"));
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);

		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		contentPane.add(centerPanel);
		contentPane.add(buttonPanel);
	}

	/**
	 * Getter for choosen symbols
	 * @return the choosen symbols as text
	 */
	public String getInputText()
	{
		return returnSymbol;
	}

}
