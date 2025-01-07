package com.example.caller;


    public class CallLogHisItem {
        private final String name;
        private final String number;
        private final String details;

        public CallLogHisItem(String name, String number, String details) {
            this.name = name;
            this.number = number;
            this.details = details;
        } public String getName() {
            return name;
        }

        public String getNumber() {
            return number;
        }

        public String getDetails() {
            return details;
        }
    }

