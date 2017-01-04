package cn.jxzhang.rmi;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created on 2017-01-03 22:56
 * <p>Title:       HelloImpl</p>
 * <p>Description: [Description]</p>
 * <p>Company:     Ultrapower Co. Ltd.</p>
 * <p>Copyright:   Copyright (c) 2016</p>
 *
 * @author <a href=zhangjiaxing@ultrapower.com.cn>J.X.Zhang</a>
 * @version 1.0
 */
public class HelloImpl implements Hello {

    @Override
    public String sayHello() throws RemoteException {
        return "hello";
    }

    @Override
    public String sayHelloToSomebody(String name) throws RemoteException {
        return "hello, " + name;
    }
}
