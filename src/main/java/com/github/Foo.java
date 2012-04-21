package com.github;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@FooScope
public class Foo {
	
	public boolean initialized=false;
	public boolean finalized=false;
	public String Id;
	
	
	@PostConstruct
	void init(){
		initialized=true;
	}
	
	@PreDestroy
	void destroy(){
		finalized=true;
	}

}
