# API-FATEC-2-SEM.-2024 - SPRINT 4

<html>
<body>

![image](https://github.com/API-2-Semestre/API-FATEC-2-SEM.-2024/blob/Sprint2/DOCUMENTOS/BANNER%20CYBERNEXUS.jpg)
 


## **ÍNDICE**
_______________________________________________________________________________

| 1. [SOBRE 📖](#SOBRE) |
|:---------------------:|

|2. [EQUIPE 👨‍💼](#EQUIPE) |
|:-------:|

|3. [SPRINT BACKLOG 🗃️](#BACKLOG) |
|:-------:|


| 4. [DOCUMENTAÇÕES 📚](#DOCUMENTAÇÕES) |
|:-------------------------------------:|

| 5. [TECNOLOGIAS UTILIZADAS 🔧](#TECNOLOGIAS) |
|:--------------------------------------------:|


##  <a name="SOBRE"></a> **SOBRE 📖**
_______________________________________________________________________________

O desenvolvimento da primeira sprint foi focado na entrega de todas as interfaces do sistema, que servirão como pontos de interação para professores e alunos na inclusão de informações. A equipe se dedicou a criar um ambiente intuitivo, facilitando o acesso e a usabilidade.

Nesta sprint, foram abordados os seguintes pontos:

1. **Desenvolvimento do Ambiente de Acesso do Professor**: Criamos uma interface robusta que permite ao professor gerenciar as turmas, cadastrar critérios de avaliação e visualizar os grupos de alunos. Isso proporciona um controle efetivo sobre o andamento das avaliações.

2. **Desenvolvimento do Ambiente de Acesso do Aluno**: As interfaces para os alunos foram projetadas para que eles possam cadastrar suas informações e participar ativamente das avaliações. A simplicidade e a clareza na navegação foram priorizadas para garantir uma experiência agradável.

3. **Gestão Geral dos Alunos**: Implementamos uma tela de gestão que permite visualizar e editar as informações dos alunos cadastrados, assegurando que os dados estejam sempre atualizados e acessíveis.

4. **Cadastro de Turmas e Critérios**: O sistema agora conta com funcionalidades para o cadastro de turmas e critérios de avaliação, permitindo que professores adaptem as avaliações conforme as necessidades dos projetos.

5. **Diagrama MER**: Na documentação, incluímos os diagramas de Modelo Entidade-Relacionamento (MER) para proporcionar uma visão clara da estrutura do banco de dados e das relações entre as entidades.

**Próximos Passos**: Na próxima sprint, planejamos implementar a lógica de avaliação e relatórios, bem como aprimorar as funcionalidades já existentes, incorporando feedback dos usuários para uma melhor experiência.
 

## <a name="EQUIPE"></a> **EQUIPE 👨‍💼** 
_______________________________________________________________________________

| **INTEGRANTES**| **FUNÇÃO**                                                       | **NETWORKS** |**IDS**|
|-------|------------------------------------------------------------------|-------|--------|
| DIEGO SALES GAMERO JOÃO| ![Static Badge](https://img.shields.io/badge/DEV-blue)           | <a href="https://github.com/DiegoSGamero" target="_blank"><img src="https://img.shields.io/badge/-black?style=social&logo=github&label=github&color=black" target="_blank"></a>| ![Image](https://github.com/user-attachments/assets/3c28b9a0-d16b-4fa2-9b41-f4678eb8959f)|
|  JOÃO PEDRO VENTURA | ![Static Badge](https://img.shields.io/badge/PRODUCT_OWNER-red)  | <a href="https://github.com/jaupventur" target="_blank"><img src="https://img.shields.io/badge/-black?style=social&logo=github&label=github&color=black" target="_blank"></a>| ![Image](https://github.com/user-attachments/assets/4bf968bd-8879-47d7-a0ec-522768f831dd)|
|  TIAGO AUGUSTO SEIXAS  | ![Static Badge](https://img.shields.io/badge/DEV-blue)           | <a href="https://github.com/TiagoAugustoSeixas" target="_blank"><img src="https://img.shields.io/badge/-black?style=social&logo=github&label=github&color=black" target="_blank"></a>|![Image](https://github.com/user-attachments/assets/d3c8ecb3-562d-408b-9122-a35133830aa9)|
|  TIAGO SANTOS | ![Static Badge](https://img.shields.io/badge/DEV-blue)           | <a href="https://github.com/tiago17santos" target="_blank"><img src="https://img.shields.io/badge/-black?style=social&logo=github&label=github&color=black" target="_blank"></a>|![Image](https://github.com/user-attachments/assets/e39515e8-763e-4a17-befd-04cff4d20c9c)|
|  VICTOR SOUZA | ![Static Badge](https://img.shields.io/badge/SCRUM_MASTER-black) | <a href=" https://github.com/victor4486 " target="_blank"><img src="https://img.shields.io/badge/-black?style=social&logo=github&label=github&color=black" target="_blank"></a>| ![image](https://github.com/user-attachments/assets/442d8d83-06b9-4313-a0da-15f24c83c593)


## <a name="BACKLOG"></a> **SPRINT BACKLOG 🗃️** 
_______________________________________________________________________________

|                                                                                       **SPRINT BACKLOG**                                                                                        |  **PRIORIDADE**  | **STATUS** 
|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:----------------:|:----------:|
|                                                                       DESENVOLVIMENTO DO SISTEMA DE MANEJAMENTO DE ALUNOS                                                                       | PRIORIDADE MÉDIA |     ✅      |
|                                                                             CRIAÇÃO DO PRINT DE RELATÓRIO DA SPRINT                                                                             | PRIORIDADE ALTA  |     ✅      |
|                                                                             CRIAR DROPDOWN NA TELA VISÃO DE GRUPOS                                                                              | PRIORIDADE MÉDIA |     ✅      |
|                                                                        LÓGICA PARA INCLUSÃO DE DATAS VÁLIDAS NAS SPRINTS                                                                        | PRIORIDADE MÉDIA |     ✅      |
|                                                                         LÓGICA PARA BOTÕES DE SPRINTS FICAREM INATIVAS                                                                          | PRIORIDADE MÉDIA |     ✅      |
|                                                              CRIAÇÃO DE LÓGICA E FUNÇÃO DE GERAR RELATÓRIOS CSV (GERENCIAR ALUNOS)                                                              | PRIORIDADE MÉDIA |     ✅      |
|                                                                                    AJUSTAR LAYOUT DAS TELAS                                                                                     | PRIORIDADE BAIXA |     ✅      |
|                                                    CRIAÇÃO DO TAMANHO DO HISTÓRICO DE CRITÉRIOS COM A BARRA DE ROLAGEM (CRITERIOS E SPRINTS                                                     | PRIORIDADE BAIXA |     ✅      |
| CRIAÇÃO DE LÓGICA PARA IMPEDIR A EXCLUSÃO DO CRITÉRIO APÓS O FINAL DA PRIMEIRA SPRINT CRIAÇÃO DE LÓGICA PARA IMPEDIR A EXCLUSÃO DO CRITÉRIO APÓS O FINAL DA PRIMEIRA SPRINT (CADASTRO DE TURMA) | PRIORIDADE MÉDIA |     ✅      |
|                                       VERIFICAR A QUANTIDADE DE SPRINTS PARA MOSTRAR OS BOTÕES COM AS DETERMINADAS SPRINTS NA TELA (PONTUAÇÃO DOS GRUPOS)                                       | PRIORIDADE BAIXA |     ✅      |
|                                     REALIZAR FUNÇÃO PARA CLICAR NOS BOTÕES DAS SPRINTS E MOSTRAR OS PONTOS ATRIBUIDOS PELO PROFESSOR (PONTUAÇÃO DOS GRUPOS)                                     | PRIORIDADE BAIXA |     ✅      |
|                                                                  CRIAR LÓGICA PARA MUDAR OS ALUNOS DE GRUPO (GERENCIAR ALUNOS)                                                                  | PRIORIDADE MÉDIA |     ✅      |
|                                                                CRIAR LÓGICA PARA OS PONTOS NÃO FICAREM NEGATIVOS (AREA DO ALUNO)                                                                | PRIORIDADE MÉDIA |     ✅      |
|                                                                        CRIAÇÃO DE LÓGICA PARA "ESQUECI A SENHA" (LOGIN)                                                                         | PRIORIDADE MÉDIA |     ✅      |
|                                                                         LÓGICA PARA BLOQUEIO DE SPRINT PARA O PROFESSOR                                                                         | PRIORIDADE MÉDIA |     ✅      |

## <a name="DOCS"></a>  **DOCUMENTAÇÕES 📚** 
_______________________________________________________________________________

 [**BURNDOWN**](https://github.com/API-2-Semestre/API-FATEC-2-SEM.-2024/tree/main)

 ![image](https://github.com/API-2-Semestre/API-FATEC-2-SEM.-2024/blob/Sprint3/DOCUMENTOS/Burndown%20Sprint3.jpg)
 
 
 [**MODELAGEM DE BANCO DE DADOS**](https://github.com/API-2-Semestre/API-FATEC-2-SEM.-2024/tree/main)

 ![image](https://github.com/API-2-Semestre/API-FATEC-2-SEM.-2024/blob/Sprint3/DOCUMENTOS/MODELAGEM_DE_DADOS_SPRINT3.jpg)


 [**PROTÓTIPO DE TELAS**](https://www.figma.com/design/QGUqQzvNf3rkLnSx3C5mAM/PACER-V2?node-id=0-1&t=bUoGKdfa1E2TV2fU-1)
 

##  <a name="TECNOLOGIAS"></a>  **TECNOLOGIAS UTILIZADAS 🔧**
_______________________________________________________________________________



![image](https://github.com/API-2-Semestre/API-FATEC-2-SEM.-2024/blob/Sprint2/DOCUMENTOS/TECNOLOGIAS%20UTILIZADAS.jpg)




</body>
</html>
