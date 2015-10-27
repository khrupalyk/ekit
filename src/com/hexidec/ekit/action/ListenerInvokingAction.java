/*
GNU Lesser General Public License

DummyAction
Copyright (C) 2007 Christoph Weissenborn

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

package com.hexidec.ekit.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;


/**
 * A simple Action calling the {@link ActionListener#actionPerformed(ActionEvent)} of the one given listener.
 *
 * @author <a href="mailto:christoph_w@users.sourceforge.net">Christoph Wei&szlig;enborn</a>
 */
public class ListenerInvokingAction extends AbstractAction
{
	private final ActionListener fActionListener;

	/**
	 * Creats an instance of this action with all given parameters and the {@link ActionListener}.
	 * 
	 * @param name the name of the action
	 * @param command the command name
	 * @param keyStroke the accelerator
	 * @param actionListener the via {@link #actionPerformed(ActionEvent)} called {@link ActionListener}
	 */
	public ListenerInvokingAction(String name, String command, KeyStroke keyStroke, ActionListener actionListener)
	{
		super();
		putValue(AbstractAction.NAME, name);
		putValue(AbstractAction.ACTION_COMMAND_KEY, command);
		putValue(AbstractAction.ACCELERATOR_KEY, keyStroke);
		if (actionListener == null) {
			throw new NullPointerException("actionListener is null");
		}
		fActionListener = actionListener;
	}

	/** {@inheritDoc} */
	public void actionPerformed(ActionEvent e)
	{
		fActionListener.actionPerformed(e);
	}

}
