package cn.jxzhang.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created on 2017-01-03 22:55
 * <p>Title:       Hello</p>
 * <p>Description: [Description]</p>
 * <p>Company:     Ultrapower Co. Ltd.</p>
 * <p>Copyright:   Copyright (c) 2016</p>
 *
 *  定义一个远程接口，必须继承Remote接口，其中需要远程调用的方法必须抛出RemoteException异常
 *
 * @author <a href=zhangjiaxing@ultrapower.com.cn>J.X.Zhang</a>
 * @version 1.0
 */
public interface Hello extends Remote {
    String sayHello() throws RemoteException;

    String sayHelloToSomebody(String name) throws RemoteException;

    String sayHelloToObject(User user) throws RemoteException;
}
