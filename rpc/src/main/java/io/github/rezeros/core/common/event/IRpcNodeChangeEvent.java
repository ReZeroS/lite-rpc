package io.github.rezeros.core.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public class IRpcNodeChangeEvent implements IRpcEvent {

    private Object data;

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
