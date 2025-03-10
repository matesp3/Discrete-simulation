package mpoljak.dsim.assignment_01.logic.experiments;

import java.io.*;

public class CustomSupply extends SupplyStrategy {

    public CustomSupply(String fileWithStrategy) {
        if (fileWithStrategy == null || fileWithStrategy.isBlank())
            throw new IllegalArgumentException("Path to file with strategy not provided");
    }

    @Override
    public SupplierResult supply(int week, boolean printDecisionDetails) {
        return null;
    }

    private void readStrategy() {
        String configPath = "somePath";
        boolean[] aConfigSet = {false, false};
        try (BufferedReader br = new BufferedReader(new FileReader(configPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.trim().split("=");
                File f = new File(data[1]);
                if (f.exists())
                    configPath = f.getAbsolutePath();
                aConfigSet[0] = true;
                }
            } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!aConfigSet[0]) {
            configPath = "someDefaultPaht";
            File f = new File(configPath);
            if (f.getParent() != null)
                new File(f.getParent()).mkdirs();
            if (!f.exists()) {
                try {
                    f.createNewFile();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveConfig() {
        try (FileWriter fw = new FileWriter("somePath")) {
            fw.write("smh\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
