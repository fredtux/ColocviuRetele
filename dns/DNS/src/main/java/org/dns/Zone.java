package org.dns;

// Converted an element of zones.json to Zone class using ChatGPT 3.5

import java.util.List;

public class Zone {
    public String $origin;
    public int $ttl;
    public SOA soa;
    public List<NS> ns;
    public List<A> a;

    public static class SOA {
        public String mname;
        public String rname;
        public String serial;
        public int refresh;
        public int retry;
        public int expire;
        public int minimum;
    }

    public static class NS {
        public String host;
    }

    public static class A {
        public String name;
        public int ttl;
        public String value;
    }
}
