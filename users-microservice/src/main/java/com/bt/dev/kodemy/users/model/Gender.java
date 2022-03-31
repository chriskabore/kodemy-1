package com.bt.dev.kodemy.users.model;

public enum Gender {

    MALE(1), FEMALE(2);

    private int gender;

    Gender (int gender){
        this.gender= gender;
    }

    public int getGender(){
        return this.gender;
    }

    public static Gender getValidGender (String genderName){
        Gender gender;

        try{
            gender = Gender.valueOf(genderName);
        }catch (IllegalArgumentException e){
            throw  new IllegalArgumentException(String.format("Invalid gender string %s. Are supported only: MALE or FEMALE strings", genderName));
        }
        return gender;
    }
}
