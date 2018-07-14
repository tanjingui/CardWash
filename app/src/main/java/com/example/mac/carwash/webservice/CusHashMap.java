package com.example.mac.carwash.webservice;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by xk on 2017/7/4.
 */

public class CusHashMap <K,V> extends HashMap<K,V> implements Map<K,V> {

    @Override
    public void clear() {
        // TODO Auto-generated method stub
        super.clear();
    }

    @Override
    public Object clone() {
        // TODO Auto-generated method stub
        return super.clone();
    }

    @Override
    public boolean containsKey(Object key) {
        // TODO Auto-generated method stub
        String tkey=String.valueOf(key).toLowerCase();
        return super.containsKey(tkey);
    }

    @Override
    public boolean containsValue(Object value) {
        // TODO Auto-generated method stub
        return super.containsValue(value);
    }

    @Override
    public Set<Entry<K,V>> entrySet() {
        // TODO Auto-generated method stub
        return super.entrySet();
    }

    @Override
    public V get(Object key) {
        // TODO Auto-generated method stub
        String tkey=String.valueOf(key).toLowerCase();
        return super.get(tkey);
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return super.isEmpty();
    }

    @Override
    public Set<K> keySet() {
        // TODO Auto-generated method stub
        return super.keySet();
    }

    @Override
    public V put(K key, V value) {
        // TODO Auto-generated method stub
        String tkey=key.toString().toLowerCase();
        return super.put((K)tkey, value);
    }

    @Override
    public void putAll(Map<? extends K,? extends V> map) {
        // TODO Auto-generated method stub
        for(Entry<? extends K, ? extends V> v:map.entrySet()){
            K k=(K)v.getKey().toString().toLowerCase();
            super.put(k, v.getValue());
        }
        //super.putAll(map);
    }

    @Override
    public V remove(Object key) {
        // TODO Auto-generated method stub
        String tkey=String.valueOf(key).toLowerCase();
        return super.remove(tkey);
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return super.size();
    }

    @Override
    public Collection<V> values() {
        // TODO Auto-generated method stub
        return super.values();
    }
}
