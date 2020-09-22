# Seguran-a-Inform-tica-trabalho

O objetivo trabalho é fazer um sistema para cifrar e decifrar mensagens, a cifra de uma mensagem deve ser feita a partir de uma palavra-passe e esta deve garantir que as tentativas de decifra do respetivo criptograma demoram sempre, no mínimo, 15 segundos. 

Utilizador:

1.	O utilizador deve colocar uma pergunta para a qual o destinatário da mensagem deve conhecer a resposta 
2.	O utilizador tem que escrever a mensagem; 
3.	gerar um número aleatório n com 128 bits; 
4.	juntar a palavra-passe e o número aleatório, e calcular repetidamente o valor de Se- cure Hash Algorithm 256 (SHA256) dessa junção e valores de hash, parando quando o tempo de execução for de 15 segundos ou mais, guardando o resultado final e o número de vezes que calculou o SHA256. 

Servidor/Destinatário:

1.	Pedir o nome do ficheiro onde está o criptograma (ou este deve ser-lhe dado como parâmetro); 
2.	Mostrar a pergunta ao utilizador e pedir a resposta; 
3. Tentar decifrar o criptograma, voltando a calcular o valor de hash iterativamente com os                                                    parâmetros fornecidos (i.e., número de iterações necessárias, palavra-passe e nú- mero aleatório). 
				
