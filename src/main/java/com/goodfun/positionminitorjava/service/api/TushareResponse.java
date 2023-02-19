package com.goodfun.positionminitorjava.service.api;


import lombok.Data;

@Data
public class TushareResponse {

    private String code;
    private String msg;
    private ResponseDate data;


    public class ResponseDate{

        String[] fields;
        Object items[];


        public String[] getFields() {
            return fields;
        }

        public void setFields(String[] fields) {
            this.fields = fields;
        }

        public Object[] getItems() {
            return items;
        }

        public void setItems(Object[] items) {
            this.items = items;
        }


    }

}
