#RMI(Remote Method Invoking)
##1. RMI概述
作为J2EE核心技术之一的RMI，它允许客服端调用一个远程服务器的组件，并返回调用结果(返回值或异常)，可以完成分布式应用。整个调用过程由RMI实现，对使用者透明。

##2. Stub和Skeleton
###2.1 概述
做个比方说明这两个概念。 假如你是A，你想借D的工具，但是又不认识D的管家C，所以你找来B来帮你，B认识C。B在这时就是一个代理，代理你的请求，依靠自己的话语去借。C呢他负责D家东西收回和借出 ，但是要有D的批准。在得到D的批准以后，C再把东西给B，B呢再转给A。stub和skeleton在RMI中就是角色就是B和C，他们都是代理角色，在现实开发中隐藏系统和网络的的差异， 这一部分的功能在RMI开发中对程序员是透明的。Stub为客户端编码远程命令并把他们发送到服务器。而Skeleton则是把远程命令解码，调用服务端的远程对象的方法，把结果在编 码发给stub，然后stub再解码返回调用结果给客户端。所有的操作如下图所示

![](http://img.my.csdn.net/uploads/201112/17/0_13241393120QzB.gif)

Stub为远程对象在客户端的一个代理，当客户端调用远程对象的方法时，实际是委托Stub这个代理去调用远程的对象，这个调用过程如下：
1. 初始化一个与远程JVM的连接
2. 写入并传输参数给远程JVM
3. 执行远程对象的方法调用，并等待调用结果的返回(return)
4. 读取调用的返回值(也可能是一个异常)
5. 返回调用的结果给调用者
这些操作(序列号参数, 建立Socket连接等等)，都由这个Stub来透明化。


在远程的JVM中，每一个对象(需要被远程调用的对象)都有一个相应的skeleton(在Java2环境中，这个skeleton不是必须的，这个先不说)，skeleton的作用是分发客户端的调用到具体的实现类，skeleton接受 一个客户端过来的调用过程如下：
1. 读取客户端传递过来的参数
2. 调用实现类的方法
(3)写入并传输返回结果给调用者，同样的，这个结果也是函数调用结果或异常


###2.2 Stub和Skeleton在什么位置产生
Stub和Skeleton都是在服务器产生的。
Stub存在于客户端，作为客户端的代理，让我们总是认为客户端产生了stub，接口没有作用。实际上stub类是通过Java动态类下载 机制下载的，它是由服 务端产生，然后根据需要动态的加载到客户端，如果下次再运行这个客户端该存根类存在于classpath中，它就不需要再下载了，而是直接加载。总的来说，stub是在服务端产生的，如果服务端的stub内容改变，那么客户端的也是需要同步更新。

##3. RMI调用流程分析
1. 首先，远程对象和调用传递的参数都必须实现接口Serializable接口
2. 目标对象继承UnicastRemoteObject的作用与好处

UnicastRemoteObject继承结构

java.lang.Object 
    java.rmi.server.RemoteObject 
        java.rmi.server.RemoteServer 
            java.rmi.server.UnicastRemoteObject 

All Implemented Interfaces: 
Serializable, Remote 

(1)先来看看java.rmi.server.RemoteObject这个类，首先它实现了Serializable, Remote这两个接口。RemoteObject的真正作用在于，他**重写了Object的hashCode、equals、和toString方法**，重写hashCode方法使得远程对象可以存放在Hashtable等一些哈希结果的集合中，重写equals方法使得远程对象可以进行比较，而重写后的toString方法返回远程对象的描述串。该类还实现专门的（私用）方法writeObject 和方法readObject，用于序列化和反序列化。

(2)java.rmi.server.RemoteServer类
在这个类中，则提供了一些对调试非常有用的方法，它一共有3个方法：

    static String getClientHost()：返回一个客户机主机的字符串表示形式，用于在当前线程中正在处理的远程方法调用。
    static PrintStream getLog()：返回用于 RMI 调用日志的流。
    static void setLog(OutputStream out)：将 RMI 调用记录到输出流 out 中。

同时要注意，该类是abstract的。

(3)java.rmi.server.UnicastRemoteObject类
再看看这个类，通常，远程对象都继承UnicastRemoteObject类，UnicastRemoteObject类提供远程对象所需的基本行为。在这个类中提供了支持创建和导出远程对象的一系列方法，一个对象继承UnicastRemoteObject它将获得以下特性：

A. 对这种对象的引用至多仅在创建该远程对象的进程生命期内有效

B. 使得远程对象既有使用TCP协议通信的能力(Socket)

C. 对于客户端与服务器的调用、传参、返回值等操作使用流的方式来处理

其他的，java.rmi.registry.LocateRegistry类提供了一系列的方法用于创建、获取Registry实例的方法；在Registry接口中，定义了一系列的方法，用于操作远程对象包括：绑定对象(bind)、获取对象(lookup)、重写绑定(rebind)、解除绑定(unbind)和返回注册表绑定列表(list)方法(也可以使用java.rmi.Naming来操作)。