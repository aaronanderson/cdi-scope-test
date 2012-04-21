package com.github;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.util.AnnotationLiteral;

//@FooScope
//@Dependent
public class FooCDIInstanceProducer {
	public static final ThreadLocal<String> fooInstanceId = new ThreadLocal<String>();

	@FooInstance
	@Produces
	//@FooScope
	public Foo create(InjectionPoint ip, BeanManager bm) {
		//Get the Instance annotation value
		FooInstance instance = ip.getAnnotated().getAnnotation(FooInstance.class);
		String id = instance.value();
		Set<Bean<?>> beans = bm.getBeans(Foo.class, new AnnotationLiteral[0]);//don't use any, must be none
		if (beans.size() > 0) {
			Bean<Foo> bean = (Bean<Foo>) beans.iterator().next();
			CreationalContext<Foo> ctx = bm.createCreationalContext(bean);
			//passing qualifier information via thread locals to the CDI scope context is not ideal but given the thread specific nature of dependency injection it is acceptable
			try {
				fooInstanceId.set(id);
				return (Foo) bm.getReference(bean, Foo.class, ctx);
			} finally {
				fooInstanceId.remove();
			}
		}
		return null;

	}
}