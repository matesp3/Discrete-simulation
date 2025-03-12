package mpoljak.dsim.assignment_01.logic.experiments;

import java.io.*;

public class CustomSupply extends SupplyStrategy {
    private static final int IDX_A = 0; // absorbers
    private static final int IDX_B = 1; // break pads
    private static final int IDX_H = 2; // headlights

    private final Supplier supplier1;
    private final Supplier supplier2;
    private int[] supplier; // supplier for i-th week
    private int[][] amounts; // amounts of ordered components of each type for i-th week

    public CustomSupply(File fileWithStrategy, Supplier supplier1, Supplier supplier2) {
        if (fileWithStrategy == null || !fileWithStrategy.exists())
            throw new IllegalArgumentException("File with strategy not provided or does not exist");
        if (supplier1 == null || supplier2 == null)
            throw new IllegalArgumentException("Supplier 1 or Supplier 2 not provided");

        this.supplier1 = supplier1;
        this.supplier2 = supplier2;
        this.loadStrategy(fileWithStrategy);
    }

    @Override
    public SupplierResult supply(int week, boolean printDecisionDetails) {
        week -= 1; // week 1 starts at index 0
        Supplier supplier = this.supplier[week] == 1 ? this.supplier1 : this.supplier2;
        if (supplier.orderDelivered(week+1, printDecisionDetails)) {
            this.results.setDeliveredAmounts(this.amounts[week][IDX_A], this.amounts[week][IDX_B],
                    this.amounts[week][IDX_H]);
            if (printDecisionDetails)
                System.out.printf(" SUPPLIED: [A=%dx B=%dx H=%dx]", this.amounts[week][IDX_A], this.amounts[week][IDX_B]
                        , this.amounts[week][IDX_H]);
        }
        else
            this.results.resetResults();
        return this.results;
    }

    private void loadStrategy(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            int weeks = Integer.parseInt(line); // first row defines number of weeks
            this.supplier = new int[weeks];
            this.amounts = new int[weeks][3];
            int w = 0;
            while ((line = br.readLine()) != null) {
                String[] data = line.trim().split(";");
                this.supplier[w] = Integer.parseInt(data[0]);
                this.amounts[w][IDX_A] = Integer.parseInt(data[1]);
                this.amounts[w][IDX_B] = Integer.parseInt(data[2]);
                this.amounts[w][IDX_H] = Integer.parseInt(data[3]);
                w++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
