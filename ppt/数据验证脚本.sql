
## �������ݣ������׶����̻���0.8%�������ѣ�ÿ��֧���������10Ԫ���൱��ÿ����8��Ǯ�����ѣ�
## ����ϵͳ���ɹ�������ʧ�ܶ������ȴ�֧��״̬����������״̬����
## �ʽ��˻���ÿ�ʳɹ�֧����������9.92Ԫ
## �����˻���ÿ�ʳɹ�֧����������10�����֣���������=�ɹ�֧��������*10��
## ���ϵͳ��ÿ�ʳɹ�֧��������¼һ�����ԭʼƾ֤��¼����¼��=�ɹ�֧����������
## �̻�֪ͨ���յ�֧��������ͻ����̻�����֪ͨ


##=======�����⣺rc_pay_dubbo_order
##
## �����ܱ�����
select count(id) from rp_trade_payment_order; ## 

## ֧���ɹ��Ķ���
select count(id) from rp_trade_payment_order where status='SUCCESS'; ## 

## ֧����״̬�Ķ�������ģ��֧�����̹ر��ǻ������
select count(id) from rp_trade_payment_order where status='WAITING_PAYMENT'; ## 


##=======���ϵͳ�⣺rc_pay_dubbo_accounting
##
## ���ԭʼƾ֤��
select count(id) from rp_accounting_voucher; ## 


##=======�ʽ��˻��⣺rc_pay_dubbo_account
##
## �˻��������������/9.92=�ɹ�������
select sum(balance) from rp_account; ## 

## �˻��䶯��ʷ��¼����
select count(id) from rp_account_history; ## 

## �ɹ�֧��������Ӧ���˻��䶯��ʷ��¼��
select count(id) from rp_account_history where fund_direction='ADD' and status='CONFORM'; ## 


##=======�����⣺rc_pay_dubbo_base
##
## �̻�֪ͨ��¼��
select count(id) from rp_notify_record; ## 

## �̻�֪ͨ��¼��־
select count(id) from rp_notify_record_log; ## 
