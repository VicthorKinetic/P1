Documentação

Alunos: Edinaldo Correia / Victhor Capasso

•	Exercício Ímpar; 

•	Arquitetura escolhida: Mestre e escravo (atores)

•	Linguagem: Scala (Akka)

•	Perspectiva: 

Iremos usar o modelo de mestre e escravo simulando 9 máquinas sendo 1 mestre e 8 escravos, para dividir uma máquina
para todos os processos de cada um dos usuários, e utilizando o mestre para executar as tarefas gerais do exercício.

Como a primeira parte do exercício consiste da criação de 8 arquivos contendo os dados de todos os processos de cada
usuário, cada escravo ficara responsável de organizar a lista de processos e recursos de seu respectivo usuário.

Na segunda parte, cada escravo será responsável por calcular sua própria média de tempo gasto por processo e enviar 
de volta ao mestre a sua média e o total de tempo gasto e de processos, para que o mestre calcule a média geral.

A terceira parte utiliza do mesmo processo da segunda, cada escravo calcula sua própria média de uso de cpu e 
de memória, e envia de volta ao mestre sua média além dos totais para o cálculo da média geral.

Depois de receber os dados de todos os escravos, o mestre irá calcular as médias referentes aos exercícios anteriores,
e criar os arquivos com os dados necessários.

Na parte final, o mestre irá reutilizar dos processos de divisão de usuário para criar um arquivo que guarda todos
eles e o PID de cada processo executado por ele. Finalizando o exercício e criando ao todo 11 arquivos.

•	Desenvolvimento:

A aplicação do modelo foi feita com sucesso, e foram usadas como base a perspectiva anotada a cima.

Não houve grandes alterações em relação à arquitetura planejada, sendo apenas feitas pequenas mudanças no código 
durante o desenvolvimento, para adicionar pequenas melhorias ou corrigir problemas mínimos que surgiram.

Algumas dessas alterações foram a implementação de uma variável contendo todas as linhas do arquivo, 
ao invés do programa reler o mesmo diversas vezes, e a criação do arquivo PID sendo transferido para o escravo, 
para escreve-lo enquanto ele faz os processos de média referentes ao exercício 2 e 3.

•	Conclusão:

Como dito anteriormente, não tiveram muitas mudanças no planejado, e o exercício conclui com os 11 arquivos criados
e sem muita dificuldade.

Pelo exercício proposto, a arquitetura de mestre e escravo foi escolhida por sua simplicidade, e foi mais que 
o suficiente para o executar sem muitos gargálos, não necessitando de algo mais complexo como cliente e servidor 
ou peer to peer.
