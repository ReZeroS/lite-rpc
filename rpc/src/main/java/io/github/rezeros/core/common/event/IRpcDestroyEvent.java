package io.github.rezeros.core.common.event;

public class IRpcDestroyEvent implements IRpcEvent{

    private Object data;

    public IRpcDestroyEvent(Object data) {
        this.data = data;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public IRpcEvent setData(Object data) {
        this.data = data;
        return this;
    }
}