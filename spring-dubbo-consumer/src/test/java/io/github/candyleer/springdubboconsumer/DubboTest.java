package io.github.candyleer.springdubboconsumer;

import org.apache.dubbo.rpc.RpcContext;

import java.util.Map;

public class DubboTest {

    public static void main(String[] args) {
        RpcContext.getContext().getAttachments().put("a", "b");

        System.out.println("before");
        for (Map.Entry<String, String> entry : RpcContext.getContext().getAttachments().entrySet()) {
            System.out.println(entry.getKey() + "==>" + entry.getValue());
        }

        System.out.println("after");
    }
}
