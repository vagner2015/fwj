package br.com.smartsy.fwj.persistence.procedure;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;

/**
 * A Builder for constructing {@link ProcedureCall}
 * @author Vagner
 *
 */
public class ProcedureCallBuilder {

	private final String procedureName;
	private Map<Integer, ProcedureParameter> parameters = new HashMap<Integer, ProcedureParameter>();
	private ProcedureParameter output;
	private int outputIndex;
	
	/**
	 * Prepares the builder
	 * @param procedureName - The name of the procedure
	 */
	public ProcedureCallBuilder(String procedureName) {
		this.procedureName = procedureName;
	}
	
	/**
	 * Add an input parameter
	 * @param index - The index of the parameter
	 * @param input - The parameter value
	 * @return The builder itself
	 */
	public ProcedureCallBuilder addInputParam(int index,InputProcedureParameter input){
		this.parameters.put(index, input);
		return this;
	}
	
	/**
	 * Add an input parameter
	 * @param index - The index of the parameter
	 * @param type - The class of the parameter
	 * @param value - The value to be passed as parameter
	 * @return The builder itself
	 */
	public ProcedureCallBuilder addInputParam(int index,Class<?> type,Object value){
		return this.addInputParam(index, new InputProcedureParameter(type, value));
	}
	
	/**
	 * Register an output parameter
	 * @param index - The index of the parameter
	 * @param output - The parameter
	 * @return The builder itself
	 */
	public ProcedureCallBuilder withOutputParameter(int index,OutputProcedureParameter output){
		this.output = output;
		this.outputIndex = index;
		return this;
	}
	
	/**
	 * Register an output parameter
	 * @param index - The index of the parameter
	 * @param type - The class of the parameter
	 * @return The builder itself
	 */
	public ProcedureCallBuilder withOutputParameter(int index,Class<?> type){
		return this.withOutputParameter(index, new OutputProcedureParameter(type));
	}
	
	/**
	 * Execute the {@link ProcedureCall} build
	 * @param manager - A {@link EntityManager} instance, so the builder can connect to the database an prepare the call
	 * @return A procedure call
	 */
	public ProcedureCall build(EntityManager manager){
		StoredProcedureQuery query = manager.createStoredProcedureQuery(procedureName);
		registerInputParameters(query);
		registerOutputParameters(query);
		return new ProcedureCall(query, outputIndex);
	}
	
	private void registerInputParameters(StoredProcedureQuery query){
		Iterator<Integer> it = parameters.keySet().iterator();
		while(it.hasNext()){
			Integer index = it.next();
			ProcedureParameter param = parameters.get(index);
			query.registerStoredProcedureParameter(index, param.getType(), param.getMode());
			query.setParameter(index, param.getValue());
		}
	}
	
	private void registerOutputParameters(StoredProcedureQuery query) {
		query.registerStoredProcedureParameter(outputIndex, output.getType(), output.getMode());
	}
	
}
