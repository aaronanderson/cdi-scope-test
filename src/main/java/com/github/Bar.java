package com.github;

import javax.inject.Inject;

public class Bar {
	@Inject
	//@Instance("1")
	@FooInstance("01")
	Foo foo1;
	
	@Inject
	Foo foo2;
	
	public Foo getFoo1(){
		return foo1;
	}
	
	public Foo getFoo2(){
		return foo2;
	}

}
