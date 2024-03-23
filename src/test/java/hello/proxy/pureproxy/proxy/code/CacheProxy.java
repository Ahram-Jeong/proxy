package hello.proxy.pureproxy.proxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheProxy implements Subject{
    // client가 proxy를 호출하면 proxy도 실제 객체를 호출해야 하기 때문에 실제 객체의 참조를 가지고 있음
    private Subject target;
    private String cacheValue;

    public CacheProxy(Subject target) {
        this.target = target;
    }

    @Override
    public String operation() {
        log.info("프록시 호출");
        if (cacheValue == null) {
            cacheValue = target.operation(); // "Success!"
        }
        return cacheValue;
    }
}
