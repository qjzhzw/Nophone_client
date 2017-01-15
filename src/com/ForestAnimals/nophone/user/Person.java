package com.ForestAnimals.nophone.user;

/**
 * Created by dell on 2017/1/15.
 */
public class Person {
    String identification;

    int level, experience;

    int[] level_experience = new int[5];

    public Person(String identification) {
        this.identification = identification;

        level_experience[0] = 10;
        level_experience[1] = 50;
        level_experience[2] = 100;
        level_experience[3] = 500;
        level_experience[4] = 1000;
    }

    public Person(){
        level_experience[0] = 10;
        level_experience[1] = 50;
        level_experience[2] = 100;
        level_experience[3] = 500;
        level_experience[4] = 1000;
    }

    public int getExperience_remained(int level, int experience)
    //剩余升级经验计算方法
    {
        return level_experience[level] - experience;
    }

}