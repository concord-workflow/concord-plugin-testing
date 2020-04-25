package com.walmartlabs.concord.plugins;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InterpolatingMockContextTest
{
    @Test
    public void validateMockContextInterpolation() {
        InterpolatingMockContext context = new InterpolatingMockContext(ImmutableMap.of("name", "Concord"));
        String result = (String) context.interpolate("My name is ${name}.");
        assertThat(result).isEqualTo("My name is Concord.");
    }
}
