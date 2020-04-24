package org.springframework.context.support.test;

/**
 * @author nicky_chin
 * @description:
 * @date: 2020/4/24 下午3:42
 * @since JDK 1.8
 */
public class TestA {

    public TestA() {

    }

    public TestA(TestB testB) {
        this.testB = testB;
    }

    private TestB testB;

    public TestB getTestB() {
        return testB;
    }

    public void setTestB(TestB testB) {
        this.testB = testB;
    }
}
