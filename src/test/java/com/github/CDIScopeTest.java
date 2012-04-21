package com.github;

import static org.junit.Assert.fail;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class CDIScopeTest {
	private static WeldContainer container;
	private static Weld weld;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		weld = new Weld();
		container = weld.initialize();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		weld.shutdown();

	}

	@Test
	public void testCustomScope() throws Exception {
		CDIBean<FooScopeContext> fooScopeBean = newInstance(FooScopeContext.class);
		FooScopeContext fooScope = fooScopeBean.instance;
		
		fooScope.create();
		Bar bar1;
		try {
			fooScope.begin();
			CDIBean<Bar> bar1Bean = newInstance(Bar.class);
			bar1 = bar1Bean.instance;
			CDIBean<Bar> bar2Bean = newInstance(Bar.class);
			Bar bar2 = bar2Bean.instance;
			assertNotSame(bar1,bar2);
			assertTrue(bar1.getFoo1().initialized);
			assertTrue(bar1.getFoo2().initialized);
			assertNotSame(bar1.getFoo1(),bar1.getFoo2());
			assertSame(bar1.getFoo1(),bar2.getFoo1());
			assertSame(bar1.getFoo2(),bar2.getFoo2());
			bar1Bean.destroy();
			bar2Bean.destroy();

		} finally {
			fooScope.end();
		}
		
		assertFalse(bar1.getFoo1().finalized);
		assertFalse(bar1.getFoo2().finalized);
		fooScope.destroy();
		assertTrue(bar1.getFoo1().finalized);
		assertTrue(bar1.getFoo2().finalized);
		
		fooScopeBean.destroy();
		

	}

	public static class CDIBean<T> {
		public Bean<T> bean;
		public CreationalContext<T> cCtx;
		public T instance;
		
		public void destroy(){
			bean.destroy(instance, cCtx);
		}

	}

	public static <T> CDIBean<T> newInstance(Class<T> clazz) throws Exception {
		CDIBean cdiBean = new CDIBean();
		Set<Bean<?>> beans = container.getBeanManager().getBeans(clazz, new AnnotationLiteral<Any>() {
		});
		if (beans.size() > 0) {
			cdiBean.bean = beans.iterator().next();
			cdiBean.cCtx = container.getBeanManager().createCreationalContext(cdiBean.bean);
			cdiBean.instance = container.getBeanManager().getReference(cdiBean.bean, clazz, cdiBean.cCtx);
			return cdiBean;
		} else {
			throw new Exception(String.format("Can't find class %s", FooScopeContext.class));
		}
	}

}
