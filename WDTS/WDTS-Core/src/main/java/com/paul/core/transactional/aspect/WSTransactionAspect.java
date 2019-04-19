package com.paul.core.transactional.aspect;


import com.paul.core.transactional.annotation.WSTransactional;
import com.paul.core.transactional.transactional.TransactonType;
import com.paul.core.transactional.transactional.WSTransaction;
import com.paul.core.transactional.transactional.WSTransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class WSTransactionAspect implements Ordered {

    @Around("@annotation(com.paul.ws.transactional.annotation.WSTransactional)")
    public void invoke(ProceedingJoinPoint point){
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        WSTransactional wsTransactional = method.getAnnotation(WSTransactional.class);

        String groupId = "";
        WSTransaction wsTransaction = null;
        if(wsTransactional!=null){
            if(wsTransactional.isStart()){
                //创建事务组
                groupId = WSTransactionManager.createWSTransactionGroup();
            }else{
                //添加事务组
                groupId = WSTransactionManager.getCurrentGroupId();
            }

            wsTransaction = WSTransactionManager.createWSTransaction(groupId);
        }


        //继续走下面的逻辑，进入到spring相关的注解或者下面的代码里
        try {
            point.proceed();
            WSTransactionManager.addWSTransaction(wsTransaction,wsTransactional.isEnd(), TransactonType.commit);
        }catch (Throwable throwable){
            throwable.printStackTrace();
            WSTransactionManager.addWSTransaction(wsTransaction,wsTransactional.isEnd(),TransactonType.rollback);
        }
    }

    @Override
    public int getOrder() {
        //在系统的注解外围，这个值设置的比较大
        return 100000;
    }
}
