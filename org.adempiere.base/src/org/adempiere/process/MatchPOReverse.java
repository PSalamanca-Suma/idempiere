/******************************************************************************
 * Copyright (C) 2012 Heng Sin Low                                            *
 * Copyright (C) 2012 Trek Global                 							  *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package org.adempiere.process;

import java.sql.Timestamp;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MMatchPO;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

/**
 * Process to reverse PO Matching
 * @author hengsin
 *
 */
@org.adempiere.base.annotation.Process
public class MatchPOReverse extends SvrProcess {
	private int		p_M_MatchPO_ID = 0;
	
	@Override
	protected void prepare() {
		p_M_MatchPO_ID = getRecord_ID();
	}

	/**
	 *	@return message
	 *	@throws Exception
	 */
	@Override
	protected String doIt() throws Exception {
		if (log.isLoggable(Level.INFO))
			log.info ("M_MatchPO_ID=" + p_M_MatchPO_ID);
	
		MMatchPO po = new MMatchPO (getCtx(), p_M_MatchPO_ID, get_TrxName());
		if (po.get_ID() != p_M_MatchPO_ID)
			throw new AdempiereException("@NotFound@ @M_MatchPO_ID@ " + p_M_MatchPO_ID);
		if (po.isProcessed())
		{		
			Timestamp reversalDate = Env.getContextAsDate(getCtx(), Env.DATE);
			if (reversalDate == null) {
				reversalDate = new Timestamp(System.currentTimeMillis());
			}
			if (!po.reverse(reversalDate))
				throw new AdempiereException("Failed to reverse matching");
		}
		return "@OK@";
	}

}
