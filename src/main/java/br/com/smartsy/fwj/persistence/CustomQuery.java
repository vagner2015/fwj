package br.com.smartsy.fwj.persistence;

import java.util.List;

import javax.persistence.EntityManager;

public interface CustomQuery {

	public <T> List<T> perform(EntityManager em, Class<T> clazz);
	
}