package mpoljak.dsim.intro;

public class Gen {
    /**
     * Generators
     * X_0 = 5
     * a = 3
     * c = 4 (prirastok)
     * m = 6 (modul)
     * X_i = (a*X_(i-1) + c) mod m
     * X_1 = (3*5+4) mod 6 = 1
     * X_2 = (3*1+4) mod 6 = 1
     * LCG (linear. congrueation  generator) - bottleneck is value of m.
     * If we want to normalise interval, we divide X_i by m: X_i/m
     *
     * - na kazdy nahodny jav potrebujem jeden generator, aby som zabezpecil nezavislost javov
     * - pouzite uz vytvoreneho generatora nemenime (rozsah sa nemeni)
     *      -> uzivatelovi nedovolim to modifikovat, dam si to ako private att
     * - dodrziavame dosledne zaradenie cisel a konci intervalu
     *      -> v jave je generovanie cisel na intervale <0;1), teda dodrziavame, ze rnd.sample < 0.3 a nie '<='
     * - generatory si inicializujem vsetky na zaciatku simulacie a potom sa ich uz nechytam
     *      Random rndSeedGen = new Random();
     *      EmpiricalRnd rnd1 = new EmpiricalRnd(rndSeedGen); // toto je inicializacia generatora s kvalitnou nasadou volanim nextLong()
     *      - tym zabezpecim aj rovnakost spustenia simulacie
     * - Nezahddzujeme vysledky generatorov - kazde vygenerovane cislo musim nejako pouzit
     */

    /**
     * SimulationCore
     *      - neda sa to robit cez interface, lebo tam mam template metodu s for cyklom
     *      - musi byt vseobecne, ktore budem dedit pre vsetky simulacie
     *      - musi obsahovat:
     *              + simulate()
     *              + experiment()
     *              + beforeSim()
     *              + afterSim()
     *              + beforeRep()
     *              + afterRep()
     *              + getReplications() // aktualny pocet replikacii??
     *                  ^----vsetko to bude asi abstraktnej(ak by nieco vychadzalo, ze sa da zovseobecnit, tak pridam)
     *      - simulaciu treba vediet spustit a predcasne ukoncit
     *          * staci to iba do bodu zastavenia mat, netreba vediet pokracovat, moze byt, nemusi
     */

    /**
     * Graph
     *      - real-time vykreslovanie
     *      - vypovedna hodnota, aby bola dodrzana
     *          * netreba vykreslovat kazdu hodnotu, to nedava zmysel => vykreslujeme len nieco (odskusat si, ale asi len
     *             1000 hodnot vykreslovat)
     *             + napr. pocetRep/pocetVykreslovanych = n-ta hodnota, ktoru vykreslim
     *             + vykreslovanie sa nesmie spomalovat na zaklade poctu replikacii
     *                      - neukladat hodnoty do pola!!!!
     *             + do grafu vykreslujeme priemer z vysledkov replikacii
     *      - v grafe treba mat vykreslovanie tak, aby som odfiltroval prvych n-hodnot (bud 10% alebo vyniest na GUI)
     *      - Y-os grafu sa musi skalovat podla rozsahu hodnot
     *          * jFreeChart je automaticka funkcia, alebo si pamatat max a min
     */

    // v strategii skusam, ktore tyzdne od ktoreho dodavatela budem brat
    // do konstruktora Empirickeho rozdelenia si zadam vsetky parametre, aby som to vedel takto menit bez zasahu do
    //   classy
}
