package io.github.rezeros.test.v5;

import io.github.rezeros.service.v5.PetStoreService;
import org.junit.Assert;
import org.junit.Test;
import io.github.rezeros.aop.MethodMatcher;
import io.github.rezeros.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;


public class PointcutTest {
	@Test
	public void testPointcut() throws Exception{
		
		String expression = "execution(* io.github.rezeros.service.v5.*.placeOrder(..))";
		
		AspectJExpressionPointcut pc = new AspectJExpressionPointcut();
		pc.setExpression(expression);
		
		MethodMatcher mm = pc.getMethodMatcher();
		
		{
			Class<?> targetClass = PetStoreService.class;
			
			Method method1 = targetClass.getMethod("placeOrder");		
			Assert.assertTrue(mm.matches(method1));
			
			Method method2 = targetClass.getMethod("getAccountDao");		
			Assert.assertFalse(mm.matches(method2));
		}
		
		{
			Class<?> targetClass = PetStoreService.class;
		
			Method method = targetClass.getMethod("getAccountDao");		
			Assert.assertFalse(mm.matches(method));
		}
		
	}
}
