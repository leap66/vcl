package com.grade.vcl.widget.throttle;

import com.grade.unit.util.LogUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * SingleClickAspect :
 * <p>
 * explain: https://www.jianshu.com/p/f577aec99e17
 * <p>
 * </> Created by ylwei on 2019/1/9.
 */
@Aspect
public class SingleClickAspect {
  private long mLastClickTime; // 最近一次点击的时间

  /**
   * 定义切点，标记切点为所有被@SingleClick注解的方法
   */
  @Pointcut("execution(@com.grade.vcl.widget.throttle.SingleClick * *(..))")
  public void methodAnnotated() {
  }

  /**
   * 定义切点，标记切点为所有被@SingleClick注解的方法
   */
  @Pointcut("execution(@com.grade.vcl.widget.throttle.FilterException * *(..))")
  public void filterException() {
  }

  /**
   * 定义一个切面方法，包裹切点方法
   */
  @Around("methodAnnotated()")
  public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
    // 判断是否快速点击
    if (!throttle()) {
      // 不是快速点击，执行原方法
      try {
        LogUtil.e(" SingleClick", "start");
        joinPoint.proceed();
        LogUtil.e(" SingleClick", "end");
      } catch (Exception e) {
        LogUtil.e(" SingleClick", "Exception");
      } finally {
        LogUtil.e(" SingleClick", "finally");
      }
    }
  }

  @Around("filterException()")
  public void filterJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
    try {
      joinPoint.proceed();
    } catch (Exception e) {
//      ToastUtil.showFailure(e.getMessage());
      e.printStackTrace();
    } finally {
    }
  }

  /**
   * 是否是快速点击
   *
   * @return true:是，false:不是
   */
  private boolean throttle() {
    long time = System.currentTimeMillis();
    long timeInterval = Math.abs(time - mLastClickTime);
    if (timeInterval < 500) {
      return true;
    } else {
      mLastClickTime = time;
      return false;
    }
  }
}
