package com.yms.userservice.entities;

public enum UserRole {
    PROJECT_MANAGER("Project Manager"),
    TEAM_LEADER("Team Leader"),
    TEAM_MEMBER("Team Member");

    private final String investmentType;

    UserRole(String investmentType) {
        this.investmentType = investmentType;
    }

    public String getInvestmentType() {
        return investmentType;
    }

}
