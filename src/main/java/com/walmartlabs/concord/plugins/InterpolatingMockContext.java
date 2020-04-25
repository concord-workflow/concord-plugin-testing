package com.walmartlabs.concord.plugins;

import com.walmartlabs.concord.sdk.MockContext;

import java.util.Iterator;
import java.util.Map;

public class InterpolatingMockContext
        extends MockContext
{

    private Map<String, Object> delegate;

    public InterpolatingMockContext(Map<String, Object> delegate)
    {
        super(delegate);
        this.delegate = delegate;
    }

    // TODO: show Ivan the implementation to try and get the real interpolation mechanism in here for testing
    public Object interpolate(Object v)
    {
        return interpolate(v.toString(), delegate);
    }

    @Override
    public String toString()
    {
        return delegate.toString();
    }

    public static String interpolate(String text, Map<?, ?> namespace)
    {
        Iterator<?> keys = namespace.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next().toString();
            Object obj = namespace.get(key);
            if (obj == null) {
                throw new NullPointerException("The value of the key '" + key + "' is null.");
            }
            String value = obj.toString();
            text = text.replace("${" + key + "}", value);
        }
        return text;
    }
}
