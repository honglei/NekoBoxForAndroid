package moe.matsuri.nb4a;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import java.util.Arrays;
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

    @Test
    public void serializesModernTunAddressWithoutLegacyFields() {
        SingBoxOptions.Inbound_TunOptions inbound = new SingBoxOptions.Inbound_TunOptions();
        inbound.type = "tun";
        inbound.tag = "tun-in";
        inbound.address = Arrays.asList("172.19.0.1/28", "fdfe:dcba:9876::1/126");

        Map<String, Object> result = inbound.asMap();

        assertEquals(inbound.address, result.get("address"));
        assertFalse(result.containsKey("inet4_address"));
        assertFalse(result.containsKey("inet6_address"));
        assertFalse(result.containsKey("endpoint_independent_nat"));
        assertFalse(result.containsKey("sniff"));
        assertFalse(result.containsKey("sniff_override_destination"));
        assertFalse(result.containsKey("domain_strategy"));
    }

    @Test
    public void serializesModernInboundRuleActions() {
        SingBoxOptions.Rule_DefaultOptions resolve = new SingBoxOptions.Rule_DefaultOptions();
        resolve.inbound = Arrays.asList("tun-in", "mixed-in");
        resolve.action = "resolve";
        resolve.strategy = "prefer_ipv4";

        Map<String, Object> result = resolve.asMap();

        assertEquals(resolve.inbound, result.get("inbound"));
        assertEquals("resolve", result.get("action"));
        assertEquals("prefer_ipv4", result.get("strategy"));
    }
}
