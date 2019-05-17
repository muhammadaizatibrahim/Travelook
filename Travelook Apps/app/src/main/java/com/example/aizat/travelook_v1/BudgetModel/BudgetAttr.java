package com.example.aizat.travelook_v1.BudgetModel;

public class BudgetAttr {

    public String destination;
    public String date;
    public String budget;
    public String transportation;
    public String transportationType;
    public String accommodation;
    public String accommodationType;
    public String food;
    public String activities;
    public String cuid;
    public String budgetid;
    public String totalCost;
    public String unUsedCost;
    public String transPerc;
    public String accommPerc;
    public String foodsPerc;
    public String accPerc;


    public BudgetAttr(){

    }

    public BudgetAttr(String destination, String date, String budget, String transportation, String transportationType,
                      String accommodation, String accommodationType, String food, String activities, String cuid,
                      String budgetid, String totalCost, String unUsedCost, String transPerc, String accommPerc,
                      String foodsPerc, String accPerc) {
        this.destination = destination;
        this.date = date;
        this.budget = budget;
        this.transportation = transportation;
        this.transportationType = transportationType;
        this.accommodation = accommodation;
        this.accommodationType = accommodationType;
        this.food = food;
        this.activities = activities;
        this.cuid = cuid;
        this.budgetid = budgetid;
        this.totalCost = totalCost;
        this.unUsedCost = unUsedCost;
        this.transPerc = transPerc;
        this.accommPerc = accommPerc;
        this.foodsPerc = foodsPerc;
        this.accPerc = accPerc;


    }

    public String getDestination() { return destination; }

    public void setDestination(String destination) { this.destination = destination; }


    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }


    public String getBudget() { return budget; }

    public void setBudget(String budget) { this.budget = budget; }


    public String getTransportation() { return transportation; }

    public void setTransportation(String transportation) { this.transportation = transportation; }


    public String getTransportationType() { return transportationType; }

    public void setTransportationType(String transportationType) { this.transportationType = transportationType; }


    public String getAccommodation() { return accommodation; }

    public void setAccommodation(String accommodation) { this.accommodation = accommodation; }


    public String getAccommodationType() { return accommodationType; }

    public void setAccommodationType(String accommodationType) { this.accommodationType = accommodationType; }


    public String getFood() { return food; }

    public void setFood(String food) { this.food = food; }


    public String getActivities() { return activities; }

    public void setActivities(String activities) { this.activities = activities; }


    public String getCuid() { return cuid; }

    public void setCuid(String cuid) { this.cuid = cuid; }


    public String getBudgetid() { return budgetid; }

    public void setBudgetid(String budgetid) { this.budgetid = budgetid; }


    public String getTotalCost() { return totalCost; }

    public void setTotalCost(String totalCost) { this.totalCost = totalCost; }


    public String getUnUsedCost() { return unUsedCost; }

    public void setUnUsedCost(String unUsedCost) { this.unUsedCost = unUsedCost; }


    public String getTransPerc() { return transPerc; }

    public void setTransPerc(String transPerc) { this.transPerc = transPerc; }


    public String getAccommPerc() { return accommPerc; }

    public void setAccommPerc(String accommPerc) { this.accommPerc = accommPerc; }


    public String getFoodsPerc() { return foodsPerc; }

    public void setFoodsPerc(String foodsPerc) { this.foodsPerc = foodsPerc; }


    public String getAccPerc() { return accPerc; }

    public void setAccPerc(String accPerc) { this.accPerc = accPerc; }

}
