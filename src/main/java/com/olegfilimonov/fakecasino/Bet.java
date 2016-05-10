package com.olegfilimonov.fakecasino;

import java.util.List;

/**
 * @author Oleg Filimonov
 */
public class Bet {
    int amount;
    List<Integer> numbers;

    public Bet(int amount, List<Integer> numbers) {
        this.amount = amount;
        this.numbers = numbers;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Integer> numbers) {
        this.numbers = numbers;
    }
}
