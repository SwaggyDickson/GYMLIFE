package tw.gymlife.com.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import tw.gymlife.com.model.ComLog;
import tw.gymlife.com.service.ComLogService;

//將當前類別標示為Spring 容器管理的類別
@Component
@Aspect
public class ComFrontAspect {

	private static final Logger logger = LoggerFactory.getLogger(ComFrontAspect.class);
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private ComLogService logService;

	// 設定切入點
	@Pointcut("execution(* tw.gymlife.com.controller..*(..))")
	public void pointcut() {
	}

	@Before("pointcut()")
	public void before(JoinPoint joinPoint) {
		System.out.println("=====Before advice starts=====");

		logger.info("訪問 " + joinPoint.getSignature().getName());
		Arrays.stream(joinPoint.getArgs()).forEach(System.out::println);

		System.out.println("=====Before advice ends=====");
	}

	@Around("pointcut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println("=====Around advice starts=====");

		long startTime = System.currentTimeMillis();

		// 呼叫proceed() 方法開始執行原方法
		Object result = joinPoint.proceed();
		long spentTime = System.currentTimeMillis() - startTime;
		logger.info("訪問 " + joinPoint.getSignature().getName() + " Time spent: " + spentTime);

		System.out.println("=====Around advice ends=====");

		return result;
	}

	@After("pointcut()")
	public void after(JoinPoint joinPoint) {
		System.out.println("=====After advice starts=====");

		logger.info("訪問 /" + joinPoint.getSignature().getName());
		Arrays.stream(joinPoint.getArgs()).forEach(System.out::println);
		
		// 获取用户的IP地址
		String ipAddress = request.getRemoteAddr();
		System.out.println("IP: " + ipAddress);
		// 生成時間
		LocalDateTime currentDateTime = LocalDateTime.now(); // 現在時間
		// 定義日期和時間的格式（不包含毫秒）
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		// 格式化日期和時間
		String formattedDateTime = currentDateTime.format(formatter);
		 String action = joinPoint.getSignature().getName().substring(joinPoint.getSignature().getName().indexOf(":") + 1).trim();
		ComLog comLog = new ComLog();
		comLog.setUserIp(ipAddress);
		comLog.setConnectTime(formattedDateTime);
		comLog.setUserAction(action);
		logService.newLog(comLog);
		System.out.println("新增完成");
		
		System.out.println("=====After advice ends=====");
	}

}