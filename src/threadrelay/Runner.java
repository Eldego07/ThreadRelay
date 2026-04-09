/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package threadrelay;

/**
 *
 * @author Diego
 */
import java.util.*;
import javax.swing.*;

public class Runner implements Runnable {
    // Attributi
    private int pos;
    private int progresso;
    private double velocita;
    private boolean inPausa;
    private JProgressBar barra;
    

    public Runner(JProgressBar barra, double velocita, int pos) {
        this.pos = pos;
        this.progresso = 0;
        this.velocita = velocita;
        this.inPausa = false;
        this.barra = barra;
    }
    
    public void riprendi() {
        synchronized (RelayRunForm.class) {
            inPausa = false;
            RelayRunForm.class.notifyAll();
        }
    }
    
    @Override
    public void run() {
        Random r = new Random();
        while (progresso < 100) {
            
            synchronized (RelayRunForm.class) {
                while (inPausa || pos > RelayRunForm.posAttuale) {
                    try {
                        RelayRunForm.class.wait();
                    } catch (InterruptedException ex) {
                        System.getLogger(Runner.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    }
                }
            }
            
            progresso += r.nextInt(5) * velocita;
            
            if (pos == RelayRunForm.posAttuale) {
                synchronized (RelayRunForm.class) {
                    if (progresso >= 90) {
                        RelayRunForm.posAttuale++;
                        RelayRunForm.class.notifyAll();
                    }
                }
            }
                
            if (progresso > 100) progresso = 100;
            
            barra.setValue(progresso);
            barra.revalidate();
            barra.repaint();
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                System.getLogger(Runner.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }

    public int getProgresso() {
        return progresso;
    }

    public void setProgresso(int progresso) {
        this.progresso = progresso;
    }

    public boolean isInPausa() {
        return inPausa;
    }

    public void setInPausa(boolean inPausa) {
        this.inPausa = inPausa;
    }

    public JProgressBar getBarra() {
        return barra;
    }

    public void setBarra(JProgressBar barra) {
        this.barra = barra;
    }
}
