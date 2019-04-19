package com.paul.core.transactional.transactional;
import com.paul.core.transactional.util.Task;

/**
 * 事务类
 */
public class WSTransaction {

    private String groupId;
    private String transactionId;
    private TransactonType transactonType;
    private Task task;

    public WSTransaction(String groupId, String transactionId) {
        this.groupId = groupId;
        this.transactionId = transactionId;
        task = new Task();
    }

    public Task getTask() {
        return task;
    }


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public TransactonType getTransactonType() {
        return transactonType;
    }

    public void setTransactonType(TransactonType transactonType) {
        this.transactonType = transactonType;
    }
}
