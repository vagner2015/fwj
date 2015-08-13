package br.com.smartsy.fwj.persistence.procedure;

import javax.persistence.StoredProcedureQuery;

/**
 * A stored procudure caller
 * @author Vagner
 *
 */
public class ProcedureCall {
	
	private StoredProcedureQuery query;
	private Integer outIndex;

	/**
	 * Prepares the call
	 * @param query - The query for the procedure
	 * @param outIndex - The position of the output parameter
	 */
	public ProcedureCall(StoredProcedureQuery query,Integer outIndex) {
		this.query = query;
		this.outIndex = outIndex;
	}
	
	/**
	 * Execute the procedure call
	 * @return An object containing the result
	 */
	public Object call(){
		query.execute();
		return query.getOutputParameterValue(outIndex);
	}
	
}
