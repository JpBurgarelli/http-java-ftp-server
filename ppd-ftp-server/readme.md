# Servidor FTP Simples em Java

## Descrição

Este projeto implementa um servidor FTP básico em Java, utilizando a classe `ServerSocket` para comunicação com os clientes. A implementação segue as especificações da RFC 959, oferecendo as funcionalidades essenciais do protocolo FTP, como autenticação de usuários e listagem de arquivos.

### Funcionalidades Implementadas

1. **Conexão de Controle:**

   - O servidor escuta na porta **2121** e envia uma mensagem de boas-vindas ao cliente:
     `"220 Bem-vindo ao Servidor FTP Simples"`.
2. **Autenticação:**

   - **Comando `USER <username>`**: Responde com `"331 Password required"` para um nome de usuário válido (no exemplo, "user"). Se o nome for inválido, responde com `"530 Invalid username"`.
   - **Comando `PASS <password>`**: Responde com `"230 User logged in"` se a senha estiver correta (senha padrão: "pass"). Caso contrário, responde com `"530 Login incorrect"`.
3. **Comando LIST (Modo Passivo):**

   - Após login, o servidor inicia uma nova conexão de dados em modo passivo, abre uma nova porta aleatória e envia uma lista de arquivos: `"le1.txt\r\nle2.txt\r\nle3.txt\r\n"`.
   - Responde com `"226 Transfer complete"` após enviar a lista de arquivos.
4. **Comandos Não Reconhecidos:**

   - Responde com `"500 Syntax error, command unrecognized"` para comandos não reconhecidos.
5. **Comando QUIT:**

   - Encerra a conexão com o cliente com a resposta `"221 Goodbye"`.

### Requisitos Técnicos

- O servidor deve ser implementado utilizando a classe `ServerSocket` do Java.
- A comunicação é feita via TCP utilizando sockets.
- O servidor escuta na **porta 2121**.
- Validação de comandos:
  - Comandos não reconhecidos resultam na resposta `"500 Syntax error, command unrecognized"`.
- O servidor é multithread, permitindo atender múltiplos clientes simultaneamente.

### Como Rodar

1. Compile os arquivos Java:
   ```bash
    telnet localhost 2121
    220 Bem-vindo ao Servidor FTP Simples
    USER user
    331 Password required
    PASS pass
    230 User logged in
    LIST
    227 Entering Passive Mode (127,0,0,1,23,45)
    le1.txt
    le2.txt
    le3.txt
    226 Transfer complete
    QUIT
    221 Goodbye

   ```

