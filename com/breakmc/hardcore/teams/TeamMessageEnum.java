package com.breakmc.hardcore.teams;

public enum TeamMessageEnum
{
    NOT_ON_TEAM("NOT_ON_TEAM", 0, "You are not on any team."), 
    NOT_A_MANAGER("NOT_A_MANAGER", 1, "You must be a manager to perform this command.");
    
    String s;
    
    private TeamMessageEnum(final String s2, final int n, final String s) {
        this.s = s;
    }
    
    public String getMessage() {
        return this.s;
    }
}
