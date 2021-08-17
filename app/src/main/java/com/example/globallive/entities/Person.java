package com.example.globallive.entities;

public class Person {
        String Firstname;
        String Lastname;
        String Username;
        String Password;
        int userID;

        public String getFirstname() {
                return Firstname;
        }

        public void setFirstname(String firstname) {
                Firstname = firstname;
        }

        public String getLastname() {
                return Lastname;
        }

        public void setLastname(String lastname) {
                Lastname = lastname;
        }

        public String getUsername() {
                return Username;
        }

        public void setUsername(String username) {
                Username = username;
        }

        public String getPassword() {
                return Password;
        }

        public void setPassword(String password) {
                Password = password;
        }

        public int getUserID() {
                return userID;
        }

        public void setUserID(int userID) {
                this.userID = userID;
        }
}
