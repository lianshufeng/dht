package com.jpznm.dht.sniffercore.core.dht.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Node {

    private String ip;
    private int port;
    private byte[] nid;


    @Override
    public boolean equals(Object obj) {
        return ((Node) obj).getIp().equals(ip);
    }

    @Override
    public int hashCode() {
        return ip.hashCode();
    }

}
