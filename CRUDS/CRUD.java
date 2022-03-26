import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.ByteArrayOutputStream;

public class CRUD {
    
    private RandomAccessFile arq;
    private final String nomeArq = "Clubes.db";

   /**
   * Construtor da Classe
   */
    public CRUD() throws IOException {
    this.arq = new RandomAccessFile(nomeArq, "rw");
    if (arq.length() == 0) {
        arq.writeInt(0);
    }
}

 /**
     * Insere Clube no arquivo
     * 
     * @param clube Clube a ser inserido
     */
    public int create(Clube objeto) throws IOException {
        arq.seek(0);
        int ultimoID = arq.readInt();
        objeto.setId(ultimoID + 1);
        arq.seek(0);
        arq.writeInt(objeto.getId());

        // cria byteArray do objeto
        arq.seek(arq.length());
        byte[] b = objeto.toByteArray();

        // escreve L�pide
        arq.writeByte(' ');

        // escreve Tamanho do array
        arq.writeInt(b.length);

        // escreve Array
        arq.write(b);

        return objeto.getId();

    }

    /**
     * 
     * 
     * @param id Id do clube a ser lido
     * @return Objeto lido
     */
    public Clube read(String id) throws IOException {
        arq.seek(4);
        // pular cabecalho

        byte lapide;
        byte[] b;
        int tam;
        Clube objeto;
        while (arq.getFilePointer() < arq.length()) {
            lapide = arq.readByte();
            tam = arq.readInt();
            b = new byte[tam];
            arq.read(b);
            if (lapide != '*') {
                objeto = new Clube();
                objeto.fromByteArray(b);
                // System.out.println(objeto.getId());
                if (objeto.getNome() == id) {
                    return objeto;
                }
            }
        }

        return null;
    }

    /**
     * Atualiza um clube inserido no arquivo
     * 
     * @param clube clube a ser atualizado
     * @return booleano caso exista clube no arquivo
     */
    public boolean update(Clube novoObjeto) throws IOException {
        arq.seek(4);
        long pos;
        byte lapide;
        byte[] b;
        byte[] novoB;
        int tam;
        Clube objeto;
        while (arq.getFilePointer() < arq.length()) {
            pos = arq.getFilePointer();
            lapide = arq.readByte();
            tam = arq.readInt();
            b = new byte[tam];
            arq.read(b);
            if (lapide != '*') {
                objeto = new Clube();
                objeto.fromByteArray(b);
                if (objeto.getId() == novoObjeto.getId()) {
                    novoB = novoObjeto.toByteArray();
                    if (novoB.length < tam) {
                        arq.seek(pos + 5);
                        arq.write(novoB);
                    } else {
                        arq.seek(pos);
                        arq.writeByte('*');
                        arq.seek(arq.length());
                        arq.writeByte(' ');
                        arq.writeInt(novoB.length);
                        arq.write(novoB);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Deleta clube do arquivo
     * 
     * @param id Id do clube a ser removido
     */
    public boolean delete(int id) throws IOException {
        arq.seek(4);
        // pular cabecalho
        long pos;
        byte lapide;
        int tam;
        byte[] b;
        Clube objeto;
        while (arq.getFilePointer() < arq.length()) {
            pos = arq.getFilePointer();
            lapide = arq.readByte();
            tam = arq.readInt();
            b = new byte[tam];
            arq.read(b);
            if (lapide != '*') {
                objeto = new Clube();
                objeto.fromByteArray(b);
                if (objeto.getId() == id) {
                    arq.seek(pos);
                    arq.writeByte('*');
                    return true;
                }
            }
        }

        return false;
    }
    
    /**
     * Verifica se arq ja foi criado
     * 
     * @return true caso arq != null
     */
    public boolean getArqIsNull() {
        return (arq == null) ? false : true;
    }
    public void readAll() throws IOException {
        arq.seek(4);
        //pular cabeçalho

        byte lapide;
        byte[] b;
        int tam;
        Clube objeto;
        while(arq.getFilePointer() < arq.length()) {
            lapide = arq.readByte();
            tam = arq.readInt();
            b = new byte[tam];
            arq.read(b);
            if(lapide != '*') {
                objeto = new Clube();
                objeto.fromByteArray(b);
                System.out.println(objeto.imprimeClube());
            }
        }

    }

}