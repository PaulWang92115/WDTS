package com.paul.core.transactional.aspect;


import com.paul.core.transactional.connection.WSConnection;
import com.paul.core.transactional.transactional.WSTransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Aspect
@Component
public class WSDataSourceAspect {
    //datasource用于获取connection（getConnection方法），切这个类是为了让
    //connection 切换到我们自己定义的

    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Connection around(ProceedingJoinPoint point){
        try {
            Connection connection = (Connection) point.proceed();
            return new WSConnection(connection, WSTransactionManager.getCurrent());
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
        return null;
    }
}
