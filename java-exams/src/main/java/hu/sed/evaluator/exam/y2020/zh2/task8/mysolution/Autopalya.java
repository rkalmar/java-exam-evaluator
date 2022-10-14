package hu.sed.evaluator.exam.y2020.zh2.task8.mysolution;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public final class Autopalya {
    public String nev;
    public List<Jarmu> jarmuvek;


    public Autopalya(String nev) {
        this.nev = nev;
        this.jarmuvek = new ArrayList<>();
    }

    public boolean felhajt(Jarmu jarmu) {
        if (jarmuvek.contains(jarmu) || jarmu.getSebesseg() < 90) {
            return false;
        }
        jarmuvek.add(jarmu);
        return true;
    }

    public void sebessegetNovel(int sebesseg) {
        for (Jarmu j : jarmuvek) {
            try {
                j.gyorsit(sebesseg);
            } catch (TulNagySebesseg t) {
                System.err.println(t.getMessage());
            }
        }
    }

    public void sebessegetCsokkent(int sebesseg) {
        for (Jarmu j : jarmuvek) {
            j.lassit(sebesseg);
        }
    }

    public void magyar() {
        if (nev.startsWith("M")) {
            System.out.println("Ez egy magyar autopalya.");
        } else {
            System.out.println("Ez nem magyar autopalya.");
        }
    }

    public int kamionTobbseg() {
        int kamion = 0;
        int auto = 0;
        for (Jarmu j : jarmuvek) {
            if (j instanceof Auto) {
                auto++;
            } else {
                kamion++;
            }
        }
        return kamion - auto;
    }

    public int megallit() {
        int megallitott = 0;
        for (Iterator<Jarmu> i = jarmuvek.iterator(); i.hasNext(); ) {
            Jarmu jarmu = i.next();

            if (jarmu instanceof Auto) {
                Auto auto = (Auto) jarmu;
                if (!auto.isMuszakis()) {
                    i.remove();
                    megallitott++;
                }
            }
        }
        return megallitott;
    }

    public Jarmu[] szabalyszego() {
        int szamlalo = 0;
        for (Jarmu j : jarmuvek) {
            if (j instanceof Auto && j.getSebesseg() > 150) {
                szamlalo++;
            } else if (j instanceof Kamion && j.getSebesseg() > 90) {
                szamlalo++;
            }
        }
        Jarmu[] jarm = new Jarmu[szamlalo];
        int index = 0;
        for (Jarmu j : jarmuvek) {
            if (j instanceof Auto && j.getSebesseg() > 150) {
                jarm[index] = j;
                index++;
            } else if (j instanceof Kamion && j.getSebesseg() > 90) {
                jarm[index] = j;
                index++;
            }
        }
        return jarm;
    }

    public boolean betolt(String file) {
        try {
            Scanner sc = new Scanner(new File(file));
            while (sc.hasNextLine()) {
                String[] reszek = sc.nextLine().split(":");
                if ("auto".equals(reszek[0])) {
                    jarmuvek.add(new Auto(Integer.parseInt(reszek[1]), Integer.parseInt(reszek[2]), Double.parseDouble(reszek[3]), reszek[4]));
                } else if ("kamion".equals(reszek[0])) {
                    jarmuvek.add(new Kamion(Integer.parseInt(reszek[1]), Double.parseDouble(reszek[2]), Double.parseDouble(reszek[3]), Integer.parseInt(reszek[4])));
                }
            }

            sc.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }
}
