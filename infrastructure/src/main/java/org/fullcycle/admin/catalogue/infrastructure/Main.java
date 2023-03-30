package org.fullcycle.admin.catalogue.infrastructure;

import org.fullcycle.admin.catalogue.application.UseCase;

public class Main {
    public static void main(String[] args){
        System.out.println(new UseCase().execute());
    }

}
