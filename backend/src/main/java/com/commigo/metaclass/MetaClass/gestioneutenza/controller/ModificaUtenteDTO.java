package com.commigo.metaclass.MetaClass.gestioneutenza.controller;

import lombok.Data;

import java.util.Map;

@Data
public class ModificaUtenteDTO {

    private Map<String, Object> dataMap;

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    // Metodo per ottenere un elemento dalla mappa data una chiave
    public Object getValueByKey(String key) {
        return dataMap.get(key);
    }


}