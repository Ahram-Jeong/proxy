package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.util.PatternMatchUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogTraceFilterHandler implements InvocationHandler {
    private final Object target;
    private final LogTrace logTrace;
    private final String[] patterns;

    public LogTraceFilterHandler(Object target, LogTrace logTrace, String[] patterns) {
        this.target = target;
        this.logTrace = logTrace;
        this.patterns = patterns;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 메소드 이름 필터
        String methodName = method.getName();
        // 패턴 적용 : save, request, req*, *est
        if (!PatternMatchUtils.simpleMatch(patterns, methodName)) {
            // 패턴 불일치 시, 대상 호출하고 끝 (로그 X)
            return method.invoke(target, args);
        }


        TraceStatus status = null;
        try {
            String msg = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
            status = logTrace.begin(msg);

            // 로직 호출
            Object result = method.invoke(target, args);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
