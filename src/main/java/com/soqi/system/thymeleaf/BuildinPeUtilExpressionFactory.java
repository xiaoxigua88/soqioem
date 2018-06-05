package com.soqi.system.thymeleaf;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

import com.soqi.common.utils.PeUtils;

public class BuildinPeUtilExpressionFactory implements
		IExpressionObjectFactory {
	public static final String STRING_UTILS_EXPRESSION_OBJECT_NAME = "PeUtils";
	private static final PeUtils peUtils = new PeUtils();
	public static final Set<String> ALL_EXPRESSION_OBJECT_NAMES;
	static {

	    final Set<String> allExpressionObjectNames = new LinkedHashSet<String>();
	    allExpressionObjectNames.add(STRING_UTILS_EXPRESSION_OBJECT_NAME);
	    ALL_EXPRESSION_OBJECT_NAMES = Collections.unmodifiableSet(allExpressionObjectNames);

	}
	public BuildinPeUtilExpressionFactory(){
		super();
	}
	// 返回该工厂类能创建的工具类对象的集合。
	@Override
	public Set<String> getAllExpressionObjectNames() {
		return ALL_EXPRESSION_OBJECT_NAMES;
	}
	// 根据表达式的名称,创建工具类对象
	@Override
	public Object buildObject(IExpressionContext context,
			String expressionObjectName) {
		return STRING_UTILS_EXPRESSION_OBJECT_NAME.equals(expressionObjectName) ?peUtils : null;
	}
	// 返回该工具对象是否可缓存。(可能理解的不太到位)
	@Override
	public boolean isCacheable(String expressionObjectName) {
		return expressionObjectName != null && "peUtils".equals(expressionObjectName);
	}

}
