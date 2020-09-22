

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author kiko
 */
public class Principal {
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws java.security.NoSuchAlgorithmException
     */
    public static void main(String[] args) throws IOException, FileNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        // TODO code application logic here
       
        File directory = new File("Encriptar"); 
        String Path=directory.getPath();
         if (directory.exists()!=true) {
          directory.mkdir();
        } 
       encriptar(Path);
       desencriptar(Path);
         
    }
    
    public static void encriptar(String Path) throws IOException, FileNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
        //Guardar a pergunta 
        System.out.print("Introduza a Pergunta: \n");
        BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in)); 
        String pergunta = reader.readLine();
        Funcoes.GuardarPergunta(Path, pergunta);
        String perg=Funcoes.LerPergunta(Path);
       
       //Introducao da resposta
       System.out.print("\nIntroduza a resposta:\n");
       BufferedReader readerPergunta =  
                   new BufferedReader(new InputStreamReader(System.in)); 
       String respostaAux = readerPergunta.readLine();
       String resposta=respostaAux.toLowerCase();
       
       //Introducao da mensagem
       System.out.print("\n\nIntroduza a Mensagem:\n"); 
       BufferedReader readerMensagem = new BufferedReader(new InputStreamReader(System.in)); 
       String mensagem= readerMensagem.readLine();
       
       //Gerar Iv, guardar esse mesmo iv e de seguida Ler o iv 
       Funcoes.gerarIV(Path);
       byte[] ivspec = Funcoes.LerIV(Path);
       IvParameterSpec iv = new IvParameterSpec(ivspec);
       
       //Juntar o IV com a resposta, daqui sai a key para encriptar
       byte[] keyBytes,key;
       keyBytes=Funcoes.HmacJuncaoEnc(resposta,ivspec,Path);
       key = Arrays.copyOf(keyBytes, 16); 
       SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
       
        //Iniciar a encriptação e guardar essa mesma
         Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec,iv);
           byte[] bufferSaida = cipher.doFinal(mensagem.getBytes()); 
         Funcoes.GuardarEncryp(bufferSaida,Path);
         
         //Funcoes para calcular o Hmac
         Funcoes.GuardarMensagemProvisoria(Path,mensagem);
         String mes=Funcoes.LerMensagemEApagar(Path);
         String hmac=perg+Arrays.toString(ivspec)+mes;
         Mac mac; 
         final String HMAC_SHA512 = "HmacSHA512";
         mac = Mac.getInstance(HMAC_SHA512);     
         mac.init(secretKeySpec);
         byte [] macAux = mac.doFinal(hmac.getBytes("UTF-8"));
         String  result = bytesToHex(macAux);
         Funcoes.GuardarHmac(Path,result);
        
}
    public static void desencriptar(String Path) throws IOException, FileNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
        
        // Ler a pergunta e guardar essa mesma pergunta numa String para achar o Hmac no fim
        System.out.print("\n\n");  
        String pergunta=Funcoes.LerPergunta(Path);
        System.out.print(pergunta);
        
        //Introduzir a resposta
          System.out.print("\nIntroduza a resposta:\n");
       BufferedReader readerPergunta =  new BufferedReader(new InputStreamReader(System.in)); 
       String respostaAux = readerPergunta.readLine();
       String resposta=respostaAux.toLowerCase();
        
       //LerIv do ficheiro 
        byte[] ivspec=Funcoes.LerIV(Path);
         IvParameterSpec iv = new IvParameterSpec(ivspec);
         
        //Ler numero de iteracoes feitas na encriptação e juntar a resposta com o IV lido anteriormente, para acharmos a nova key
        int iteracao = Funcoes.LerIteracao(Path);
        byte[] keyBytes = Funcoes.HmacJuncaoDesenc(resposta, ivspec, Path,iteracao);
        byte[] key;
        key = Arrays.copyOf(keyBytes, 16); 
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        
        //Iniciar desencriptação
        Cipher cil;
        cil = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cil.init(Cipher.DECRYPT_MODE, secretKeySpec,iv);
        Funcoes.lerParaAES(cil, Path);
         
        //Guardar a mensagem desencriptada num ficheiro, para de seguida
        // o lermos e calcularmos o Hmac, se este for igual ao Original mostra a mensagem
        // senao apaga o ficheiro
         
         String mes=Funcoes.LerMensagem(Path);
         
         //Calcular Hmac
         String hmac=Funcoes.LerPergunta(Path)+Arrays.toString(ivspec)+Funcoes.LerMensagem(Path);
         Mac mac; 
         final String HMAC_SHA512 = "HmacSHA512";
            mac = Mac.getInstance(HMAC_SHA512);     
            mac.init(secretKeySpec);
             byte [] macAux = mac.doFinal(hmac.getBytes("UTF-8"));
            String  result = bytesToHex(macAux);
            Funcoes.GuardarHmac2(Path,result);
             String Hmac=Funcoes.LerHmac2(Path);
            String HmacOriginal=Funcoes.LerHmac(Path);
            
            //Verificar Integridade
            if(Hmac.equals(HmacOriginal)){
                 System.out.print("\n\n Integridade mantida\n\n Mensagem:\n");
                 System.out.print(mes);
            }
            else{
                 System.out.print("\n\n Integridade corrompida, fiheiro vai ser eliminado ");
                 File file = new File(Path+"\\desencriptado.txt");
                 file.delete();
            }
            
    }
      public static String bytesToHex(byte[] bytes) 
    {
        final  char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) 
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    }

    
