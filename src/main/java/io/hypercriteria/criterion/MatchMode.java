/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion;

public enum MatchMode {

    EXACT, START, END, ANYWHERE;

    public String toMatchString(Object string) {
        switch (this) {
            case START:
                return string + "%";
            case END:
                return "%" + string;
            case ANYWHERE:
                return "%" + string + "%";
            default:
                return String.valueOf(string); //EXACT
        }
    }
}
