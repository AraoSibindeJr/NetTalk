#  JavaConnect

**JavaConnect** é um projecto académico desenvolvido em Java que demonstra a comunicação entre computadores utilizando **Sockets** e **Threads** com **interface gráfica (Swing)**.  
O sistema permite a troca de mensagens em tempo real entre um **Servidor** e um **Cliente**, mostrando como ocorre a comunicação bidirecional em rede.

##  Objectivo do Projeto

O objectivo é compreender e aplicar conceitos fundamentais de:
- Comunicação cliente-servidor usando **TCP/IP**;
- Uso de **Threads** para comunicação simultânea (envio e recepção);
- Integração entre **rede e interface gráfica** em Java;
- Estruturação modular de um projeto real.

##  Estrutura de Pacotes

````

comunicacao_sockets/
│
├── app/
│   ├── MainServidor.java      # Classe principal do Servidor
│   ├── MainCliente.java       # Classe principal do Cliente
│
├── gui/
│   ├── ServidorUI.java        # Interface gráfica do Servidor
│   ├── ClienteUI.java         # Interface gráfica do Cliente
│
├── Rede/
│   ├── Servidor.java          # Lógica de conexão do lado do Servidor
│   ├── Cliente.java           # Lógica de conexão do lado do Cliente
│
├──  ReadMe.md
└── utils/
├── Config.java            # Configurações gerais (IP, porta, etc.)
├── Log.java               # Sistema de logs e mensagens de debug

````

---

##  Tecnologias Utilizadas

| Tecnologia | Função |
|-------------|--------|
| **Java 17+** | Linguagem de programação principal |
| **Swing** | Criação da interface gráfica |
| **Sockets (java.net)** | Comunicação em rede |
| **Threads (java.lang.Thread)** | Execução simultânea |
| **Git & GitHub** | Versionamento e colaboração |

---

## Funcionalidades

- Comunicação em tempo real entre cliente e servidor  
- Envio e receção de mensagens simultâneas  
- Interface gráfica intuitiva  
- Logs de comunicação (envio/receção)  
- Suporte a múltiplos clientes (versão futura)  

---


---

## Equipa de Desenvolvimento

| Nome                       | GitHub                                                          |
| -------------------------- | --------------------------------------------------------------- |
| **Xheymom Flavio Bucuane** | [@XheymonFlavioBucuane](https://github.com/XheymonFlavioBucuane) |
| **Arão Sibinde Júnior**    | [@AraoSibindeJunior](https://github.com/AraoSibindeJr)       |

---

## Conceitos Envolvidos

* Programação Orientada a Objetos (POO)
* Comunicação em rede (TCP/IP)
* Concorrência com Threads
* Estrutura modular de software
* Interface Gráfica (GUI)
* Manipulação de fluxos (`InputStream` / `OutputStream`)

---
*“A comunicação é o coração da tecnologia — e os sockets são o seu pulso.”*


