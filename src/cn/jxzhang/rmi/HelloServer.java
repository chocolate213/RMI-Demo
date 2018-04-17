package cn.jxzhang.rmi;

import org.junit.Test;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created on 2017-01-03 22:59
 * <p>Title:       HelloServer</p>
 * <p>Description: [Description]</p>
 * <p>Company:     Ultrapower Co. Ltd.</p>
 * <p>Copyright:   Copyright (c) 2016</p>
 *
 *  创建RMI注册表，启动RMI服务，并将远程对象注册到RMI注册表中。
 *
 * @author <a href=zhangjiaxing@ultrapower.com.cn>J.X.Zhang</a>
 * @version 1.0
 */
public class HelloServer {

    /**
     * 测试RMI服务端：使用Registry.bind()方法绑定目标对象
     *  注意：
     *      1. 如果目标对象没有继承UnicastRemoteObject方法，需要使用UnicastRemoteObject.exportObject()方法来转换
     *      2. 如果使用Registry.bind()绑定目标对象，必须在UnicastRemoteObject.exportObject()方法中指定端口号
     *
     * @param args
     * @throws Exception
     */
    public static void main1(String[] args) throws Exception {
        Hello hello = new HelloImpl();

        Registry registry = LocateRegistry.createRegistry(9999);

        //远程对象没有继承UnicastRemoteObject方法，采用如下方式转换目标对象
        Remote remote = UnicastRemoteObject.exportObject(hello,9999);       //如果使用registry绑定Remote对象，必须指定端口号

        registry.bind("hello",remote);          //这里必须只绑定服务名，否则客户端调用时会抛出异常java.rmi.NotBoundException

        System.out.println(">>>>>INFO:远程IHello对象绑定成功！");
    }

    /**
     * 测试RMI服务端：使用Naming.bind()方法绑定目标对象
     *  注意：
     *      1. Naming.bind()绑定目标对象，无需在UnicastRemoteObject.exportObject()对象中指定端口号
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            //1. 创建一个远程对象
            Hello hello = new HelloImpl();

            //2. 本地主机上的远程对象注册表Registry的实例，并指定端口为9999，这一步必不可少（Java默认端口是1099）缺少注册表创建，则无法绑定对象到远程注册表上
            LocateRegistry.createRegistry(9999);

            //3. 把远程对象注册到RMI注册服务器上，并命名为hello
            Naming.bind("rmi://192.168.10.172:9999/hello",hello);


            System.out.println(">>>>>INFO:远程IHello对象绑定成功！");
        } catch (RemoteException e) {
            System.out.println("创建远程对象发生异常！");
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            System.out.println("发生重复绑定对象异常！");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("发生URL异常！");
            e.printStackTrace();
        }
    }
}
