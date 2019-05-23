package com.veriff.demo.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.HashMap;

public class TokenPayload {

    private Verification verification;

    public TokenPayload(Verification verification) {
        this.verification = verification;
    }

    public Verification getVerification() {
        return verification;
    }

    public static class Verification {
        private Document document;
        private HashMap additionalData;
        private Date timestamp = new Date();
        private String lang = "en";
        private String[] features = new String[]{"selfid"};
        private Person person;
        private String callback;

        public Document getDocument() {
            return document;
        }

        public HashMap getAdditionalData() {
            return additionalData;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public String getLang() {
            return lang;
        }

        public String[] getFeatures() {
            return features;
        }

        public Person getPerson() {
            return person;
        }

        public String getCallback() {
            return callback;
        }

        public Verification(Person person, Document document) {
            this.person = person;
            this.document = document;


        }

        public static class Person {
            private String firstName;
            private String idNumber;
            private String lastName;

            public Person(@NonNull String firstName, @NonNull String lastName, @NonNull String idNumber) {
                this.firstName = firstName;
                this.lastName = lastName;
                this.idNumber = idNumber;
            }

            public String getFirstName() {
                return firstName;
            }

            public String getIdNumber() {
                return idNumber;
            }

            public String getLastName() {
                return lastName;
            }
        }

        public static class Document {
            private String number;
            private String type;
            private String country;

            public Document(@Nullable String number, String type, String country) {
                this.number = number;
                this.type = type;
                this.country = country;
            }

            public Document() {
            }

            public String getNumber() {
                return number;
            }

            public String getType() {
                return type;
            }

            public String getCountry() {
                return country;
            }
        }
    }
}
