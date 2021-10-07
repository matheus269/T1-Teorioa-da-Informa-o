# TRABALHO GA - TEORIA DA INFORMA��O

## MEMBROS: Kevin Lima, Pedro Henrique Gomes Viegas e Matheus Rocha

Como Rodar a aplica��o:

Dentro da pasta executor, rodar o **exec.bat**
Que contem a seguinte instru��o:

![images/bat.png](images/bat.png)

ou 

Abrir o projeto e rodar a classe **Coders.java**

---

Com o servi�o funcionando, abrir a collection:

em **collection/Coders & NoiseTreatment.postman_collection.json**

ou acessar a url: http://localhost:8080/swagger-ui.html#/

## Coders

Para os acessos do Coders: Golomb, Elias-Gamma, Fibonacci, Un�ria e Delta

Para mandar a requisi��o:
-
![images/postman_exemplo.gif](images/postman_exemplo.gif)


Todas as requisi��es s�o chamadas POST
-

http://localhost:8080/coders/encoder/:tipoEncode

Encoder
-
Via Path passar o tipo de codifica��o que ser empregada

Via header
file -> onde � o path absoluto do arquivo a ser encodado


Decoder
-

http://localhost:8080/coders/decoder

Via header
file -> onde � o path absoluto do arquivo a ser decodado


## Tratamento de Ruido

Encoder: 
-

http://localhost:8080/noise-treatment/encoder

Via header
file -> onde � o path absoluto do arquivo a ser encodado


Decoder: 
-
http://localhost:8080/noise-treatment/decoder

Via header
file -> onde � o path absoluto do arquivo a ser decodado


# Informa��es da implenta��o

#### T1 - Codifica��es Golomb, Elias-Gamma, Fibonacci, Un�ria e Delta


Foi feita a sepa��o de coders eles ir�o ter sempre a implementa��o de um encoder e um decoder.


Para a codifica��o/decodifica��o Fibonacci: 
-
utilizou-se o valor ASCII 235 para representar o valor 0,
pois Fibonnaci n�o consegue empregar corretamente o valor 0.

Foi empregado nas decodifica��es/codifica��es: 
-
Delta - foi utilizado apenas opera��es BitSet

Fibonacci - apenas opera��es bitwise

Golumb - foi Utilizado BitSet e opera��es bitwise

Unary - foi Utilizado opera��es bitwise

Elias Gamma - foi Utilizado opera��es bitwise e BitSet

## Referencias

#### Bitwise Java - https://www.geeksforgeeks.org/bitwise-operators-in-java/
#### Logbase 2 java - https://www.techiedelight.com/calculate-log-base-2-in-java/
#### BitSet To Array byte - https://www.javatpoint.com/post/java-bitset-tobytearray-method
#### BitWise operator - https://www.javatpoint.com/bitwise-operator-in-java
#### BitWise Docs -https://docs.oracle.com/javase/7/docs/api/java/util/BitSet.html

# Implementa��o do T2 

## Tratamento de Ru�dos - Hamming

Informa��es sobre a implementa��o
-

## CRC 8
  
  Nos 2 primeiros bits do cabe�alho � calculado o CRC8.
  Para o calculo foi utilizado c�digo pronto encontrado na internet pelos integrantes do Grupo o nome da Classe � **CRC8** e est� com seus direitos marcados.

  Quando n�o for possivel fazer a decodifica��o dos valores ser� retornado uma Exce��o do tipo **InvalidCRCException**.


## Hamming

  O codigo hamming � calculado para o restante dos bites de entrada. 

  Foi Utilizado BitSet j� que tem rapido acesso aos bites para convers�o e tambem pela facil adpta��o do grupo ao seu uso.

Obs:
-
N�o foi implementada uma valida��o para a extens�o dos arquivos.



