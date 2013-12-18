package com.kk.binding.converter;

import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * Created by hk on 13-12-4.
 */
public abstract class Converter implements IConverter {
    @Override
    public Object from(byte[] data, Type type) {
        return null;
    }

    @Override
    public Object from(InputStream inputStreams, Type type) {
        return null;
    }

    @Override
    public Object from(String string, Type type) {
        return null;
    }
}
