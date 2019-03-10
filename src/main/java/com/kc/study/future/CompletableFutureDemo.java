package com.kc.study.future;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class CompletableFutureDemo {
    public static void main(String[] args) throws IOException {
        //进行变换
        String result = CompletableFuture.supplyAsync(() -> "hello").thenApply(c ->c+" world").join();
        System.out.println(result);
        //进行消耗
        //thenAccept是针对结果进行消耗，因为他的入参是Consumer，有入参无返回值。
        CompletableFuture.supplyAsync(()->"hello").thenAccept(s-> System.out.println(s+ " world"));
        //对上一步的计算结果不关心，执行下一个操作
        //thenRun它的入参是一个Runnable的实例，表示当得到上一步的结果时的操作
        CompletableFuture.supplyAsync(()->{
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        }).thenRun(()->{
            System.out.println("hello world");
        });

        //.结合两个CompletionStage的结果，进行转化后返回
        //它需要原来的处理返回值，并且other代表的CompletionStage也要返回值之后，利用这两个返回值，进行转换后返回指定类型的值。
        String r3 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hi,hello";
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return " world";
        }), (s1, s2) -> s1 + " " + s2).join();
        System.out.println(r3);

        //结合两个CompletionStage的结果，进行消耗
        //它需要原来的处理返回值，并且other代表的CompletionStage也要返回值之后，利用这两个返回值，进行消耗。
        CompletableFuture.supplyAsync(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello ";
        }).thenAcceptBoth(CompletableFuture.supplyAsync(()->{
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return " world both";
        }),(s1,s2)-> System.out.println(s1+" "+s2));

        //在两个CompletionStage都运行完执行
        //不关心这两个CompletionStage的结果，只关心这两个CompletionStage执行完毕，之后在进行操作（Runnable）。
        CompletableFuture.supplyAsync(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello ";
        }).runAfterBoth(CompletableFuture.supplyAsync(()->{
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return " world both";
        }),()-> System.out.println("runAfterBoth"));
        //两个CompletionStage，谁计算的快，我就用那个CompletionStage的结果进行下一步的转化操作
        //我们现实开发场景中，总会碰到有两种渠道完成同一个事情，所以就可以调用这个方法，找一个最快的结果进行处理。
        String r2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello ";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return " world both";
        }), s -> s).join();
        System.out.println(r2);
        //两个CompletionStage，谁计算的快，我就用那个CompletionStage的结果进行下一步的消耗操作
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello ";
        }).acceptEither(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return " world both";
        }), System.out::println);

        //两个CompletionStage，任何一个完成了都会执行下一步的操作（Runnable）
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello ";
        }).runAfterEither(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return " world both";
        }),()-> System.out.println("test"));
        //当运行时出现了异常，可以通过exceptionally进行补偿
        String r4 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (1 == 1) {
                throw new RuntimeException("测试一下异常情况");
            }
            return "s1";
        }).exceptionally(e -> {
            System.out.println(e.getMessage());
            return "hello world exception";
        }).join();
        System.out.println(r4);

        //当运行完成时，对结果的记录。这里的完成时有两种情况，一种是正常执行，返回值。
        // 另外一种是遇到异常抛出造成程序的中断。
        // 这里为什么要说成记录，因为这几个方法都会返回CompletableFuture，
        // 当Action执行完毕后它的结果返回原始的CompletableFuture的计算结果或者返回异常。
        // 所以不会对结果产生任何的作用

        String r5 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (true) {
                throw new RuntimeException("测试一下异常情况");
            }
            return "s1";
        }).whenComplete((s, t) -> {
            System.out.println(s);
            System.out.println(t);
        }).exceptionally(e -> {
            System.out.println(e.getMessage());
            return "hello world after exception";
        }).join();
        System.out.println(r5);

        //这里也可以看出，如果使用了exceptionally，
        // 就会对最终的结果产生影响，它没有口子返回如果没有异常时的正确的值，
        // 这也就引出下面我们要介绍的handle

        //运行完成时，对结果的处理。这里的完成时有两种情况，
        // 一种是正常执行，返回值。
        // 另外一种是遇到异常抛出造成程序的中断。

        String r6 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //出现异常
            if (1 == 1) {
                throw new RuntimeException("测试一下异常情况");
            }
            return "s1";
        }).handle((s, t) -> {
            if (t != null) {
                return "hello world";
            }
            return s;
        }).join();
        System.out.println(r6);
        System.in.read();
    }
}
