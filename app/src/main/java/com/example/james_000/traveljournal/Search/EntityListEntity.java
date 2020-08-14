package com.example.james_000.traveljournal.Search;


public class EntityListEntity {
    public String getTicketName() {
        return TicketName;
    }

    public void setTicketName(String ticketName) {
        TicketName = ticketName;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getAmountAdvice() {
        return AmountAdvice;
    }

    public void setAmountAdvice(String amountAdvice) {
        AmountAdvice = amountAdvice;
    }

    private String TicketName;
    private String Amount;
    private String AmountAdvice;
}
