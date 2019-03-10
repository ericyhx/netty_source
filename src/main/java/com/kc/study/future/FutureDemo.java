package com.kc.study.future;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 此种方法可实现基本目标，任务并行且按照完成顺序获取结果。使用很普遍，老少皆宜，就是CPU有消耗，可以使用！
 */
public class FutureDemo {
    public static void main(String[] args) throws InterruptedException {
        Long start = System.currentTimeMillis();
        ExecutorService executor= Executors.newFixedThreadPool(10);
            List<Future<Integer>> futureList=new ArrayList<>();
            List<Integer> list=new ArrayList<>();
            //1.高速提交10个任务，每个任务返回一个Future入list
            for (int i = 0; i < 10; i++) {
                futureList.add(executor.submit(new CallableTask(i)));
            }
            Long getResultStart = System.currentTimeMillis();
            System.out.println("结果归集开始时间=" + new Date());
            //2.结果归集，用迭代器遍历futureList,高速轮询（模拟实现了并发），任务完成就移除
            while (futureList.size()>0){
                futureList.stream().filter(f->f.isDone()&&!f.isCancelled()).collect(Collectors.toList()).forEach(r->{
                    try {
                        Integer i = r.get();
                        list.add(i);
                        System.out.println("任务i=" + i + "获取完成，移出任务队列！" + new Date());
                        futureList.remove(r);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                });
                Thread.sleep(1);
            }
        System.out.println("list=" + list);
        System.out.println("总耗时=" + (System.currentTimeMillis() - start) + ",取结果归集耗时=" + (System.currentTimeMillis() - getResultStart));
    }


    private static final class CallableTask implements Callable<Integer>{
        private Integer i;

        public CallableTask(Integer i) {
            this.i = i;
        }

        @Override
        public Integer call() throws Exception {
            switch (i){
                case 1:
                    Thread.sleep(3000);//任务1耗时3秒
                    break;
                case 5:
                    Thread.sleep(5000);//任务5耗时5秒
                    break;
                    default:
                        Thread.sleep(1000);//其它任务耗时1秒
                        break;
            }
            System.out.println("task线程：" + Thread.currentThread().getName() + "任务i=" + i + ",完成！" + new Date());
            return i;
        }
    }
}
