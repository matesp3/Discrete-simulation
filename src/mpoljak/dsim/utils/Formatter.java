package mpoljak.dsim.utils;

public class Formatter {
    /**
     * @param timeSecs time to be formatted, which is given in seconds
     * @param dayHours amount of hours that one day lasts
     * @param startHour if one day lasts {@code dayHours} and its less than {@code 24}, then you may want to specify,
     *                  which hour should be considered as 00:00:00 time.
     * @return formatted date-time
     */
    public static String getStrDateTime(double timeSecs, int dayHours, int startHour) {
        if (timeSecs < 0)
            return "Unknown";
        int day = (int) timeSecs / (dayHours*3600) + 1;
        timeSecs -= (day-1) * dayHours * 3600;
        int hours = (int) Math.floor(timeSecs/3600.0);
        timeSecs -= (hours) * 3600;
        int min = (int)Math.floor(timeSecs/60.0);
        timeSecs -= min * 60;
        int secs = (int)Math.ceil(timeSecs);
        return String.format("Day-%d %02d:%02d:%02d", day, (min == 60 ? hours+1 : hours)%dayHours+startHour, min%60, secs%60);
    }

    /**
     * @param p decimal precision of numbers. If {@code p < 0}, then values are not rounded.
     * @param divisor number by which will {@code h} and {@code mean} divided before formatting.
     * @return formatted confidence interval representation
     */
    public static String getStrCI(double h, double mean, int p, double divisor) {
        h /= divisor;
        mean /= divisor;
        return p < 0 ? String.format(("95%% < %f | %f | %f >"), mean-h, mean, mean+h)
                     : String.format(("95%% < %."+p+"f | %."+p+"f | %."+p+"f >"), mean-h, mean, mean+h);
    }
}
