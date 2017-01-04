package cn.jxzhang.rmi;

import org.junit.Test;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created on 2017-01-03 23:04
 * <p>Title:       HelloClient</p>
 * <p>Description: [Description]</p>
 * <p>Company:     Ultrapower Co. Ltd.</p>
 * <p>Copyright:   Copyright (c) 2016</p>
 *
 *  客户端测试，在客户端调用远程对象上的远程方法，并返回结果。
 *
 * @author <a href=zhangjiaxing@ultrapower.com.cn>J.X.Zhang</a>
 * @version 1.0
 */
public class HelloClient {

    /**
     * 客户端创建方式1：使用Naming.lookup方法返回远程对象
     *  注意：
     *   使用Naming.lookup()绑定url必须指定端口号（协议（默认rmi：），主机名（默认localhost）可以不用指定）以及目标对象名;
     */
    @Test
    public void testClient1(){
        try {
            /*
             * 在RMI服务注册表中查找名称为RHello的对象并调用其上的方法。以下三种URL均正确：
             *      rmi://<host>:<port>/hello
             *      rmi://:<port>/hello               （自动添加localhost）
             *      //:<port>/hello                   （自动添加localhost）
             */
            Hello hello =(Hello) Naming.lookup("rmi://localhost:9999/hello");

            //调用返回对象的方法：返回对象为Remote，目标对象继承了Remote对象，所以可以强转为Hello并调用Hello的方法
            System.out.println(hello.sayHello());
            System.out.println(hello.sayHelloToSomebody("zhangSan"));

        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 客户端创建方式2：使用LocateRegistry.getRegistry来获取RMI注册表并通过其获取远程对象
     * 注意：
     *  使用Registry.lookup()方法可以直接指定远程对象名，而不用写出完整URL,写出完整URL反而会抛出异常：java.rmi.NotBoundException
     *
     */
    @Test
    public void testClient2(){
        try {
            Registry registry = LocateRegistry.getRegistry("192.168.10.172", 9999);
            Hello hello = (Hello)registry.lookup("hello");
            System.out.println(hello.sayHello());
            System.out.println(hello.sayHelloToSomebody("zhangSan"));
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
        }
    }
}
