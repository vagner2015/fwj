package br.com.smartsy.fwj.persistence.procedure;

import javax.persistence.ParameterMode;

/**
 * Parameter for inputing value on stored procedure
 * @author Vagner
 *
 */
public final class InputProcedureParameter extends ProcedureParameter {

	public InputProcedureParameter(Class<?> type, Object value) {
		super.type = type;
		super.mode = ParameterMode.IN;
		super.value = value;
	}
}
