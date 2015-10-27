/*
GNU Lesser General Public License

FileDialog
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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.hexidec.ekit.EkitCore;

public class FileDialog extends JDialog implements ActionListener, ListSelectionListener
{
	/** <code>serialVersionUID</code> */
	private static final long	serialVersionUID	= 3377907336128182933L;

	private JList fileList;
	private String fileDir;
	private String[] files;
	private String selectedFile;

	/**
	 * Creates and displays a FileDialog.
	 * @param parent the parent Frame or Dialog of the FileDialog
	 * @param fileDir the path used to initialize the dialog
	 * @param fileList a list of all files to be displayed in the JList component
	 * @param title the dialog title
	 * @param modal whether the dialog should be modal
	 * @return the FileDialog instance
	 */
	public static FileDialog open(Window parent, String fileDir, String[] fileList, String title, boolean modal)
	{
		FileDialog me;
		if (parent instanceof Frame)
		{
			me = new FileDialog((Frame) parent, fileDir, fileList, title, modal);
		}
		else if (parent instanceof Dialog)
		{
			me = new FileDialog((Dialog) parent, fileDir, fileList, title, modal);
		}
		else
		{
			throw new IllegalArgumentException("parent must be a Frame or a Dialog");
		}
	  	me.setVisible(true);
		return me;
	}

	private FileDialog(Dialog parentDialog, String fileDir, String[] fileList, String title, boolean modal)
	{
		super(parentDialog, title, modal);
		this.fileDir = fileDir;
		this.files = fileList;
		init();
	}

	private FileDialog(Frame parentFrame, String fileDir, String[] fileList, String title, boolean modal)
	{
		super(parentFrame, title, modal);
		this.fileDir = fileDir;
		this.files = fileList;
		init();
	}

	/**
	 * @deprecated use {@link #open(Window, String, String[], String, boolean)} instead
	 */
	// TODO delete constructor in next release
	public FileDialog(EkitCore parentEkit, String fileDir, String[] fileList, String title, boolean modal)
	{
		super(parentEkit.getFrame(), title, modal);
		this.fileDir = fileDir;
		this.files = fileList;
		init();
	  	setVisible(true);
	}

	/** {@inheritDoc} */
	public void actionPerformed(ActionEvent e)
	{
	  	if(e.getActionCommand().equals("save"))
		{
			hide();
		}
		else if(e.getActionCommand().equals("cancel"))
		{
			selectedFile = null;
			hide();
		}
	}

	/**
	 * @deprecated only inner class use is appropriate
	 */
	// TODO reduce visibility to private in next release
	public void init()
	{
	  	selectedFile = "";
	  	Container contentPane = getContentPane();
	  	contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
	  	setBounds(100,100,300,200);
	  	setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		fileList = new JList(files);
		fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fileList.clearSelection();
		ListSelectionModel lsm = fileList.getSelectionModel();

		lsm.addListSelectionListener(this);

		JScrollPane fileScrollPane = new JScrollPane(fileList);
		fileScrollPane.setAlignmentX(LEFT_ALIGNMENT);
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
		centerPanel.add(fileScrollPane);
		centerPanel.setBorder(BorderFactory.createTitledBorder("Files"));

		JPanel buttonPanel= new JPanel();	  	
	  	buttonPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));

	  	JButton saveButton = new JButton("Accept");
	  	saveButton.setActionCommand("save");
		saveButton.addActionListener(this);
		JButton cancelButton = new JButton("Cancel");
	  	cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);

		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		contentPane.add(centerPanel);
		contentPane.add(buttonPanel);
    }

	public String getSelectedFile()
	{
		if (selectedFile != null)
		{
			selectedFile = fileDir + '/' + selectedFile;
		}
		return selectedFile;
	}

	/** {@inheritDoc} */
	public void valueChanged(ListSelectionEvent e)
	{
		if (!e.getValueIsAdjusting())
		{
			ListSelectionModel sm = fileList.getSelectionModel();
			if(!sm.isSelectionEmpty())
			{
				selectedFile = files[sm.getMinSelectionIndex()];
			}
		}
	}

}
