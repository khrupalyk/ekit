/*
GNU Lesser General Public License

UserInputAnchorDialog
Copyright (C) 2000 Howard Kistler & other contributors

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
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.hexidec.ekit.EkitCore;

public class UserInputAnchorDialog extends JDialog implements ActionListener
{
	/** <code>serialVersionUID</code> */
	private static final long	serialVersionUID	= -8427125651412346735L;

	private String inputText = null;
	private final JTextField jtxfInput = new JTextField(32);

	/**
	 * Creates and displays a FontSelectorDialog.
	 * @param parent the parent Frame or Dialog of the FontSelectorDialog
	 * @param title the dialog title
	 * @param bModal whether the dialog should be modal
	 * @param defaultAnchor the default anchor
	 * @return the FontSelectorDialog instance
	 */
	public static UserInputAnchorDialog open(Window parent, String title, boolean bModal, String defaultAnchor)
	{
		UserInputAnchorDialog me;
		if (parent instanceof Frame)
		{
			me = new UserInputAnchorDialog((Frame) parent, title, bModal, defaultAnchor);
		}
		else if (parent instanceof Dialog)
		{
			me = new UserInputAnchorDialog((Dialog) parent, title, bModal, defaultAnchor);
		}
		else
		{
			throw new IllegalArgumentException("parent must be a Frame or a Dialog");
		}
 		me.pack();
		me.setVisible(true);
		return me;
	}

	private UserInputAnchorDialog(Dialog parent, String title, boolean bModal, String defaultAnchor)
	{
		super(parent, title, bModal);
		jtxfInput.setText(defaultAnchor);
		init();
	}

	private UserInputAnchorDialog(Frame parent, String title, boolean bModal, String defaultAnchor)
	{
		super(parent, title, bModal);
		jtxfInput.setText(defaultAnchor);
		init();
	}

	/**
	 * @deprecated use {@link #open(Window, String, boolean, String)} instead
	 */
	// TODO delete constructor in next release
	public UserInputAnchorDialog(EkitCore peKit, String title, boolean bModal, String defaultAnchor)
	{		
		super(peKit.getFrame(), title, bModal);
		jtxfInput.setText(defaultAnchor);
		init();
 		pack();
		setVisible(true);
	}

	/** {@inheritDoc} */
   	public void actionPerformed(ActionEvent e)
   	{
		if (e.getActionCommand().equals("accept"))
		{
			inputText = jtxfInput.getText();
			setVisible(false);
		}	
	  	if (e.getActionCommand().equals("cancel"))
		{
			inputText = null;
			setVisible(false);
		}
	}

	/**
	 * @deprecated use of inner class method should be forbidden
	 */
	// TODO reduce visibility to private in next release
	public void init()
	{
	  	Container contentPane = getContentPane();
	  	contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
	  	setBounds(100,100,400,300);
	  	setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

	  	JPanel centerPanel = new JPanel();
       	centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
	  	JLabel anchorLabel = new JLabel("Anchor:", SwingConstants.LEFT);
	  	centerPanel.add(anchorLabel);
	  	centerPanel.add(jtxfInput);

		JPanel buttonPanel= new JPanel();	  	
//	  	buttonPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));

		JButton saveButton = new JButton("Accept");
	  	saveButton.setActionCommand("accept");
		saveButton.addActionListener(this);

	  	JButton cancelButton = new JButton("Cancel");
	  	cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);

		JButton filesButton = new JButton("Server Files...");
	  	filesButton.setActionCommand("files");
		filesButton.addActionListener(this);

		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);
		buttonPanel.add(filesButton);

		contentPane.add(centerPanel);
		contentPane.add(buttonPanel);
   	}

	public String getInputText()
	{
		return inputText;
	}

	public void setAnchor(String anchor)
	{
		jtxfInput.setText(anchor);
	}
}

