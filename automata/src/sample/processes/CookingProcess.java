package sample.processes;

import sample.automata.STATES;

public class CookingProcess extends Process {

    protected final int cookingMaxTime = 10;
    protected final int timePart = 500;

    @Override
    public Void call() {
        cooking();
        return null;
    }

    protected void cooking(){
        updateMessage(STATES.COOK.toString());
        for (int i = 0; i <= cookingMaxTime; i++) {
            this.updateProgress(i, cookingMaxTime);
            try {
                Thread.sleep(timePart);
            } catch (InterruptedException interrupted) {
                System.out.println("Ошибка приготовления");
                return;
            }
        }
        this.updateProgress(0, 0);
        for (int i = 0; i < 4; i++) {
            try {
                updateMessage(STATES.READY.toString());
                Thread.sleep(timePart);
                updateMessage("");
                Thread.sleep(timePart);
            } catch (InterruptedException interrupted) {
                System.out.println("Ошибка приготовления");
                return;
            }
        }
        updateMessage(STATES.WAIT.toString());
    }
}