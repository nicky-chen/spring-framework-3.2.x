/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.transaction.interceptor;

import org.springframework.transaction.TransactionDefinition;

/**
 * 事务的属性用于保存@Trasacation注解的信息
 * This interface adds a {@code rollbackOn} specification to {@link TransactionDefinition}.
 * As custom {@code rollbackOn} is only possible with AOP, this class resides
 * in the AOP transaction package.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 16.03.2003
 * @see DefaultTransactionAttribute
 * @see RuleBasedTransactionAttribute
 */
public interface TransactionAttribute extends TransactionDefinition {

	/**
	 * Return a qualifier value associated with this transaction attribute.
	 * <p>This may be used for choosing a corresponding transaction manager
	 * to process this specific transaction.
     * 事务管理器名称
	 */
	String getQualifier();

	/**
     * 出发回滚的事务
	 * Should we roll back on the given exception?
	 * @param ex the exception to evaluate
	 * @return whether to perform a rollback or not
	 */
	boolean rollbackOn(Throwable ex);

}
