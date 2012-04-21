package com.github;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessBean;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.enterprise.inject.spi.ProcessObserverMethod;
import javax.enterprise.inject.spi.ProcessProducer;



public class CDIScopeTestExtension implements Extension {
	// We will bootstrap all CDI beans rather than using beans.xml for
	// autodiscovery

	public void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd, BeanManager bm) {
		bbd.addAnnotatedType(bm.createAnnotatedType(FooCDIInstanceProducer.class));
		bbd.addAnnotatedType(bm.createAnnotatedType(FooScopeContext.class));
		bbd.addAnnotatedType(bm.createAnnotatedType(Foo.class));
		bbd.addAnnotatedType(bm.createAnnotatedType(Bar.class));
		bbd.addQualifier(FooInstance.class);
		bbd.addScope(FooScope.class, false, false);
	}

	public void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
		abd.addContext(new FooCDIContextImpl());
	}

	public void afterDeployment(@Observes AfterDeploymentValidation adv, BeanManager bm) {
	}

	public void beforeShutdown(@Observes BeforeShutdown bfs, BeanManager bm) {
	}

	public void processAnnotatedType(@Observes ProcessAnnotatedType<?> pa, BeanManager bm) {
	}

	public void processInjectionTarget(@Observes ProcessInjectionTarget<?> pit, BeanManager bm) {
	}

	public void processProducer(@Observes ProcessProducer<?, ?> ppd, BeanManager bm) {
	}

	public void processBean(@Observes ProcessBean<?> pb, BeanManager bm) {
	}

	public void processObserverMethod(@Observes ProcessObserverMethod<?, ?> pom, BeanManager bm) {
	}

}