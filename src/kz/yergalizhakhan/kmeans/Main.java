package kz.yergalizhakhan.kmeans;


import java.util.Scanner;

import static jdk.nashorn.internal.objects.Global.print;

public class Main {

    public static void main(String []args) {

        Scanner in = new Scanner(System.in);
        System.out.print("Write down your num per order: ");
        int perOrder = in.nextInt();
        System.out.print("Write down your demand: ");
        int demand = in.nextInt();
        System.out.print("Write down your order size: ");
        int orderSize = in.nextInt();
        System.out.print("Write down your carrying cost: ");
        int carryingCost = in.nextInt();
        System.out.print("Write down your rate of usage: ");
        int rateOfUsage = in.nextInt();

        double orderingCost = (demand / orderSize) * perOrder;
        double carryingCostFinal = (orderSize / 2) * carryingCost;
        double totalCostFinal = orderingCost + carryingCostFinal;

        System.out.println("Per Order: " + perOrder);
        System.out.println("Demand: " + demand);
        System.out.println("Order Size: " + orderSize);
        System.out.println("Carrying Cost: " + carryingCost);
        System.out.println("Rate Of Usage: " + rateOfUsage);
        System.out.println("Ordering Cost: " + orderingCost);
        System.out.println("Carrying Cost: " + carryingCostFinal);
        System.out.println("Total Cost: " + totalCostFinal);

        System.out.print("Write down your leadTime: ");
        int leadTime = in.nextInt();

        double EOQ = Math.sqrt((carryingCost*perOrder*demand)/carryingCost);
        System.out.println("EOQ: " + EOQ);

        double ROP = leadTime * rateOfUsage;
        System.out.println("ROP " + ROP);
        double totalCostFinal2 = (demand / EOQ * perOrder) + (EOQ / carryingCost*carryingCost);
        System.out.println("Total Cost2: " + totalCostFinal2);
        double safetyStock = leadTime * (60 - rateOfUsage);
        System.out.println("Safety Stock: " + safetyStock);
        double ropSafetyStock = ROP + safetyStock;
        System.out.println("SUM of ROP and Safety Stock " + ropSafetyStock);

    }

}
