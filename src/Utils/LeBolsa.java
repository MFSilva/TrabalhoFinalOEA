package Utils;

import java.io.*;
import java.util.ArrayList;

public class LeBolsa {

    public static void main(String[] args) throws Exception {
        String uf = args[0].toUpperCase();
        String registro, nis;
        String dados[];
        long posArqPrincipal, posArqLocal;
        RandomAccessFile arqPrincipal = new RandomAccessFile("bolsa.csv", "r");
        RandomAccessFile arqLocal = new RandomAccessFile("BolsaRJ.csv", "rw");
        arqLocal.setLength(0);
        RandomAccessFile index = new RandomAccessFile("IndexRJ.dat", "rw");
        index.setLength(0);
        arqPrincipal.readLine();
        int Progresso = 0;
        long novo;
        
        while (arqPrincipal.getFilePointer() < arqPrincipal.length()) {
            posArqPrincipal = arqPrincipal.getFilePointer();
            registro = arqPrincipal.readLine();

            dados = registro.split("\t");

            if (dados[0].equals(uf)) {
                nis = dados[7];
                posArqLocal = arqLocal.getFilePointer();
                arqLocal.writeBytes(registro + "\n");

                String idPrincipal = String.format("%09d", posArqPrincipal);
                String idLocal = String.format("%09d", posArqLocal);

                index.writeBytes(nis + " " + idPrincipal + " " + idLocal + "\n");
            }

            novo = posArqPrincipal / (arqPrincipal.length() / 100);

            if (novo > Progresso) {
                syso(novo + " Por cento concluído");
                Progresso++;
            }
        }
        int TamBloc = 81920;
        int n = 0;
        while (index.getFilePointer() < index.length()) {
            ArrayList<LeIndex> alIndex = new ArrayList();
            RandomAccessFile RAFSaida = new RandomAccessFile("SaidaIt" + n, "rw");
            while (alIndex.size() < TamBloc && index.getFilePointer() < index.length()) {
                alIndex.add(new LeIndex(index));
            }
            alIndex.sort(null);
            int i = 0;
            for (LeIndex alIndex1 : alIndex) {
                syso(alIndex.get(i));
                alIndex.get(i).escreveIndex(RAFSaida);
                i++;
            }
            n++;
        }
        
        int contador1 = 0, contador2 = 1;
        RandomAccessFile RafAux[];
        
        if(n%2 == 0){
            RafAux = new RandomAccessFile[n/2];
        } else {
            RafAux = new RandomAccessFile[(n/2)+1];
        }
        
        while(RafAux.length > 1){
            for(int i = 0; i<RafAux.length;i++){
                RafAux[i] = intercala(new RandomAccessFile("SaidaIt"+contador1, "r"), new RandomAccessFile("SaidaIt"+contador2, "r"), n);
                contador1 += 2;
                contador2 += 2;
            }
        }
        
        
        /*arqPrincipal.close();
        arqLocal.close();*/
        index.close();
    }

    public static void syso(Object o) {
        System.out.println(o);
    }

    public static RandomAccessFile intercala(RandomAccessFile Raf1, RandomAccessFile Raf2, int n) throws IOException {
        RandomAccessFile Raf3 = new RandomAccessFile("Intercal" + n, "rw");
        LeIndex li1 = new LeIndex();
        LeIndex li2 = new LeIndex();
        Raf1.seek(0);
        Raf2.seek(0);
        Raf3.seek(0);
        li1.leIndex(Raf1);
        li2.leIndex(Raf2);

        while (Raf1.getFilePointer() < Raf1.length() && Raf2.getFilePointer() < Raf2.length()) {
            if (li1.getNIS() < li2.getNIS()) {
                li1.escreveIndex(Raf3);
                li1.leIndex(Raf1);
            } else {
                li2.escreveIndex(Raf3);
                li2.leIndex(Raf2);
            }
        }

        while (Raf1.getFilePointer() < Raf1.length()) {
            li1.escreveIndex(Raf3);
            li1.leIndex(Raf1);
        }

        while (Raf2.getFilePointer() < Raf2.length()) {
            li2.escreveIndex(Raf3);
            li2.leIndex(Raf2);
        }

        return Raf3;
    }
    /*  dados:
        0  UF 
        1  CódigoSIAFI Mun 
        2  Nome Município
        3  Código Função	
        4  Código Subfunção
        5  Código Programa	
        6  Código Ação	
        7  NIS Favorecido	
        8  Nome Favorecido	
        9  Fonte-Finalidade
        10 Valor Parcela	
        11 Mês Competência
     */
}
