package com.soqi.system.thymeleaf;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

public class BuildinJsonUtilDialect extends AbstractDialect implements
		IExpressionObjectDialect {
	private final IExpressionObjectFactory EXPRESSION_OBJECTS_FACTORY = new BuildinJsonUtilExpressionFactory();
	
	public BuildinJsonUtilDialect(String name) {
		super(name);
	}

	@Override
	public IExpressionObjectFactory getExpressionObjectFactory() {
		return this.EXPRESSION_OBJECTS_FACTORY;
	}

}
