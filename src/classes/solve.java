package classes;

import java.io.Serializable;

public class solve implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     *
     */


    public solve(int h, int m, int s, int c, boolean p2, solveStates state, String scramble) {
        time[0] = h;
        time[1] = m;
        time[2] = s;
        time[3] = c;
        stateOfSolve = state;
        setScramble(scramble);
    }
    private String comment = "";
    private solveStates stateOfSolve;
    private int time[] = new int[4];
    private String scramble;


    public String getScramble() {
        return scramble;
    }
    public void setScramble(String scramble) {
        this.scramble = scramble;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public solveStates getStateOfSolve() {
        return stateOfSolve;
    }
    public int[] getTime() {
        return time;
    }
    public void setStateOfSolve(solveStates stateOfSolve) {
        this.stateOfSolve = stateOfSolve;
    }
    public void setTime(int h, int m, int s, int c) {
        time[0] = h;
        time[1] = m;
        time[2] = s;
        time[3] = c;

    }
    public void addHours(int h) {
        time[0] += h;
    }
    public void addMinutes(int m) {
        time[1] += m;
        while (time[1] > 60) {
            addHours(1);
            time[1] -= 60;
        }
    }
    public void addSeconds(int s) {
        time[2] += s;
        while (time[2] > 60) {
            addMinutes(1);
            time[2] -= 60;
        }
    }
    public void addCenti(int c) {
        time[3] += c;
        while (time[3] > 100) {
            addSeconds(1);
            time[3] -= 100;
        }
    }

    public void subHours(int h) {
        if (h > time[0]) {
            time[0] = 00;
        } else {
            time[0] -= h;
        }
    }
    public void subMinutes(int m) {
        if (m > time[1]) {
            time[1] = 00;
        } else {
            time[1] -= m;
        }
    }
    public void subSeconds(int s) {
        if (s > time[2]) {
            time[2] = 00;
        } else {
            time[2] -= s;
        }
    }
    public void subCenti(int c) {
        if (c > time[3]) {
            time[3] = 00;
        } else {
            time[3] -= c;
        }
    }

    public String timeFormated() {
        if (stateOfSolve == solveStates.NORMAL){
            if (time[0] == 0 && time[1] == 0) {
                return (time[2] + "." + String.format("%02d", (time[3])));
            } else if (time[0] == 0) {
                return (time[1]+ ":" + time[2] + "." + String.format("%02d", (time[3])));
            } else {
                return (time[0] + ":" + time[1] + ":" + time[2] + "." + String.format("%02d", (time[3])));
            }
        } else if (stateOfSolve == solveStates.DNF){
            return "DNF";
        } else {
            if (time[0] == 0 && time[1] == 0) {
                return (time[2] + "." + String.format("%02d", (time[3])))+"+";
            } else if (time[0] == 0) {
                return (time[1]+ ":" + time[2] + "." + String.format("%02d", (time[3])))+"+";
            } else {
                return (time[0] + ":" + time[1] + ":" + time[2] + "." + String.format("%02d", (time[3]))) +"+";
            }
        }
    }

    @Override
    public String toString(){
        return timeFormated();
    }

}
