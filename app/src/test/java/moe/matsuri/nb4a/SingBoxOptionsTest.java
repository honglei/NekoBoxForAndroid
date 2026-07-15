package moe.matsuri.nb4a;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SingBoxOptionsTest {

    @Test
    public void serializesOptionAndAppliesConfigOverrides() {
        SingBoxOptions.Outbound outbound = new SingBoxOptions.Outbound();
        outbound.type = "direct";
        outbound.tag = "original";
        outbound._hack_config_map.put("tag", "overridden");
        outbound._hack_config_map.put("domain_strategy", "prefer_ipv4");

        Map<String, Object> result = outbound.asMap();

        assertEquals("direct", result.get("type"));
        assertEquals("overridden", result.get("tag"));
        assertEquals("prefer_ipv4", result.get("domain_strategy"));
        assertFalse(result.containsKey("_hack_config_map"));
    }

    @Test
    public void appliesOverridesToNestedOptions() {
        SingBoxOptions.Outbound outbound = new SingBoxOptions.Outbound();
        outbound.type = "direct";
        outbound.tag = "original";
        outbound._hack_config_map.put("tag", "nested-override");

        SingBoxOptions.MyOptions options = new SingBoxOptions.MyOptions();
        options.outbounds = Collections.singletonList(outbound);

        Map<String, Object> result = options.asMap();
        List<?> outbounds = (List<?>) result.get("outbounds");
        Map<?, ?> serializedOutbound = (Map<?, ?>) outbounds.get(0);

        assertEquals("nested-override", serializedOutbound.get("tag"));
    }

    @Test
    public void serializesCustomOptionAndAppliesOverrides() {
        SingBoxOptions.CustomSingBoxOption option =
                new SingBoxOptions.CustomSingBoxOption("{\"type\":\"direct\",\"tag\":\"custom\"}");
        option._hack_custom_config = "{\"tag\":\"merged\",\"domain_strategy\":\"ipv4_only\"}";

        Map<String, Object> result = option.asMap();

        assertEquals("direct", result.get("type"));
        assertEquals("merged", result.get("tag"));
        assertEquals("ipv4_only", result.get("domain_strategy"));
    }
}
