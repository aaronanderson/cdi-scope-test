package com.github;

import java.lang.annotation.Annotation;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import com.github.FooScopeContext.ScopedInstance;
import com.github.FooScopeContext.ThreadLocalState;

public class FooCDIContextImpl implements Context {

	public Class<? extends Annotation> getScope() {
		return FooScope.class;
	}

	public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
		Bean bean = (Bean) contextual;
		ScopedInstance si = null;
		ThreadLocalState tscope = FooScopeContext.state.get();
		if (Foo.class.isAssignableFrom(bean.getBeanClass())) {
			//Check if qualifier is present
			String id = FooCDIInstanceProducer.fooInstanceId.get();
			if (id == null) {//no scope present, get scope singleton
				id = "";
			}
			si = tscope.fooInstances.get(id);
			if (si == null) {
				si = new ScopedInstance();
				si.bean = bean;
				si.ctx = creationalContext;
				si.instance = bean.create(creationalContext);
				tscope.fooInstances.put(id, si);
				tscope.allInstances.add(si);
			}

			return (T) si.instance;
		} else {
			si = new ScopedInstance();
			si.bean = bean;
			si.ctx = creationalContext;
			si.instance = bean.create(creationalContext);
			tscope.allInstances.add(si);
		}
		return (T)si.instance;
	}

	public <T> T get(Contextual<T> contextual) {
		throw new IllegalArgumentException();
	}

	public boolean isActive() {
		return FooScopeContext.state.get() != null ? true : false;
	}

}