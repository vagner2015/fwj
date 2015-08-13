package br.com.smartsy.fwj.persistence.procedure;

import javax.persistence.ParameterMode;

/**
 * Parameter for outputing values on stored procedure
 * @author Vagner
 *
 */
public final class OutputProcedureParameter extends ProcedureParameter {

	public OutputProcedureParameter(Class<?> type) {
		super.type = type;
		super.mode = ParameterMode.OUT;
		super.value = "";
	}

}