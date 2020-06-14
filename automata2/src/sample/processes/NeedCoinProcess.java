package sample.processes;

import sample.automata.STATES;

public class NeedCoinProcess extends Process {
    @Override
    public Void call() {
        needCoin();
        return null;
    }

    protected void needCoin(){
        updateMessage(STATES.NEEDCOIN.toString());
    }
}