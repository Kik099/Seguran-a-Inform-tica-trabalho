import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

/**
 *
 * @author kiko
 */
public class Funcoes {
    public static void GuardarPergunta(String Path1,String pergunta){
        
        String Path=Path1+"\\Pergunta.txt";
        try (FileWriter writer = new FileWriter(Path);
              BufferedWriter bw = new BufferedWriter(writer)) {

            bw.write(pergunta);
            bw.close();

        } catch (IOException e) {
        }
    }
    
     
      public static String LerPergunta(String Path1) 
{
         String Path=Path1+"\\Pergunta.txt";
    StringBuilder contentBuilder = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new FileReader(Path))) 
    {
 
        String sCurrentLine;
        while ((sCurrentLine = br.readLine()) != null) 
        {
            contentBuilder.append(sCurrentLine).append("\n");
        }
    } 
    catch (IOException e) 
    {
        e.printStackTrace();
    }
    return contentBuilder.toString();
}
      
       public static void gerarIV(String Path1) throws IOException { //ivFile e o caminho do ficheiro onde se quer guardar o IV
        SecureRandom srandom = new SecureRandom(); //inicializar o objecto SecureRandom

        byte[] iv = new byte[16]; //128 bits -> 16bytes
        srandom.nextBytes(iv); //gerar para o array de bytes
        
        String Path=Path1+"\\IV.txt";
        try (FileOutputStream fos = new FileOutputStream(Path)) {
          fos.write(iv);
   //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
}
      
    }
      
       public static byte[] LerIV(String Path1) throws FileNotFoundException, IOException{
          
           FileInputStream fis = null;
           String Path=Path1+"\\IV.txt";
            File file = new File(Path);
             byte[] bArray = new byte[(int) file.length()];
        try{
            fis = new FileInputStream(file);
            fis.read(bArray);
            fis.close();        
            
        }catch(IOException ioExp){
        
        }
        return bArray;
           
       }
       
       public static byte[] HmacJuncaoEnc(String resposta, byte[] iv,String Path) throws FileNotFoundException, NoSuchAlgorithmException, UnsupportedEncodingException, IOException{
           byte[] mac =null;
           int cnt=0;
           String Path1=Path+"\\Iteracoes.txt";
           byte[] res=resposta.getBytes("UTF-8");
           
           byte[] juncao=new byte[res.length + iv.length];
           System.arraycopy(res, 0, juncao, 0, res.length);
           System.arraycopy(iv, 0, juncao,res.length,iv.length);
           MessageDigest digest = MessageDigest.getInstance("SHA-256");
           
            long end = System.currentTimeMillis()+15000;
            while(System.currentTimeMillis() < end) {
            digest.update(juncao);
            cnt=cnt+1;
            }
        byte[] keyBytes = digest.digest(juncao);
  
            File fac = new File(Path1);
        try (FileWriter write = new FileWriter(fac)) {
            write.write(""+cnt);
        }
     
        
           return keyBytes;
   //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
}
           
       
       public static int LerIteracao(String Path) throws IOException{
           int aux;
           String Path1=Path+"\\Iteracoes.txt";
           File f;
           Scanner scan = null;
                try{
                      f = new File(Path1);
                      scan = new Scanner(f);
                    }
        	catch(Exception e){
                System.exit(0);
                    	}
	 aux=scan.nextInt();
         return aux;
       }

        public static byte[]    HmacJuncaoDesenc(String resposta, byte[] iv,String Path,int num) throws FileNotFoundException, NoSuchAlgorithmException, UnsupportedEncodingException, IOException{
           
           byte[] res=resposta.getBytes("UTF-8");
           byte[] juncao=new byte[res.length + iv.length];
           System.arraycopy(res, 0, juncao, 0, res.length);
           System.arraycopy(iv, 0, juncao,res.length,iv.length);
           MessageDigest digest = MessageDigest.getInstance("SHA-256");
            int aux=0;
            while(aux < num) {
                digest.update(juncao);
                aux=aux+1;
            }
             byte[] keyBytes = digest.digest(juncao);
             return keyBytes;
           
       }
       
       
       
       public static void GuardarEncryp(byte[] encrypted,String Path1) throws FileNotFoundException, IOException{
           
           String Path=Path1+"\\Encripetado.aes-128-cbc";
           
            try (FileOutputStream fos = new FileOutputStream(Path)) {
          fos.write(encrypted);
           
       }
        }    
      
       public static byte[] LerEncryp(String Path1) throws FileNotFoundException, IOException{
           
           String Path=Path1+"\\Iteracoes.aes-128-cbc";
            byte  [] encrypted=null;
            File f;
            String aux;
            Scanner scan = null;
                try{
                  f = new File(Path);
                   scan = new Scanner(f);
                    }
              catch(Exception e){
                  System.exit(0);
                    }
             aux=scan.toString();
              return aux.getBytes();
        }    
       
        public static void lerParaAES(Cipher ci, String Path) {
            String entrada=Path+"\\Encripetado.aes-128-cbc";
            String saida=Path+"\\desencriptado.txt";
            
        try (FileInputStream in = new FileInputStream(entrada);
                FileOutputStream out = new FileOutputStream(saida)) {
            byte[] bufferEntrada = new byte[1024]; //buffer de 1024 bytes
            int len;
            while ((len = in.read(bufferEntrada)) != -1) { //
                byte[] bufferSaida = ci.update(bufferEntrada, 0, len);
                if (bufferSaida != null) {
                    out.write(bufferSaida);
                }
            }
            byte[] bufferSaida = ci.doFinal(); //concluir a operacao
            if (bufferSaida != null) {
                out.write(bufferSaida);
            }

        } catch (FileNotFoundException ex) {
          
        } catch (IOException | IllegalBlockSizeException | BadPaddingException ex) {
               }
    }
         public static void GuardarMensagemProvisoria(String Path1,String pergunta){
        
        String Path=Path1+"\\Mensagem.txt";
        try (FileWriter writer = new FileWriter(Path);
              BufferedWriter bw = new BufferedWriter(writer)) {

            bw.write(pergunta);

        } catch (IOException e) {
        }
    }
       
            public static String LerMensagem(String Path1) {
        
            String Path=Path1+"\\desencriptado.txt";
            StringBuilder contentBuilder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(Path))) 
                 {
 
                       String sCurrentLine;
                        while ((sCurrentLine = br.readLine()) != null) 
                                  {
                               contentBuilder.append(sCurrentLine).append("\n");
                                     }
                     } 
             catch (IOException e) 
                 {
                 e.printStackTrace();
                     }
            return contentBuilder.toString();
}
       
         public static void GuardarHmac(String Path1,String hmac){
        
        String Path=Path1+"\\HmacOriginal.txt";
        try (FileWriter writer = new FileWriter(Path);
              BufferedWriter bw = new BufferedWriter(writer)) {

            bw.write(hmac);

        } catch (IOException e) {
        }
    }
    
    public static void GuardarHmac2(String Path1,String hmac){
        
        String Path=Path1+"\\Hmac2.txt";
        try (FileWriter writer = new FileWriter(Path);
              BufferedWriter bw = new BufferedWriter(writer)) {

            bw.write(hmac);

        } catch (IOException e) {
        }
    }
      
            
    public static String LerHmac(String Path1){
        
        String Path=Path1+"\\HmacOriginal.txt";
    StringBuilder contentBuilder = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new FileReader(Path))) 
    {
 
        String sCurrentLine;
        while ((sCurrentLine = br.readLine()) != null) 
        {
            contentBuilder.append(sCurrentLine).append("\n");
        }
    } 
    catch (IOException e) 
    {
        e.printStackTrace();
    }
    return contentBuilder.toString();
}
       
    
    public static String LerHmac2(String Path1){
        
            String Path=Path1+"\\Hmac2.txt";
    StringBuilder contentBuilder = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new FileReader(Path))) 
    {
 
        String sCurrentLine;
        while ((sCurrentLine = br.readLine()) != null) 
        {
            contentBuilder.append(sCurrentLine).append("\n");
        }
    } 
    catch (IOException e) 
    {
        e.printStackTrace();
    }
    return contentBuilder.toString();
}
       
    public static String LerMensagemEApagar(String Path1){
        
            String Path=Path1+"\\Mensagem.txt";
    StringBuilder contentBuilder = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new FileReader(Path))) 
    {
 
        String sCurrentLine;
        while ((sCurrentLine = br.readLine()) != null) 
        {
            contentBuilder.append(sCurrentLine).append("\n");
        }
    } 
    catch (IOException e) 
    {
        e.printStackTrace();
    }
    File file = new File(Path);
    file.delete();
    return contentBuilder.toString();
}
       

    }


  
 
