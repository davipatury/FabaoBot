# FabaoBot

Bot criado em Java com o objetivo primário de **prover memes para a humanidade**.

 * Comandos para adicionar, remover e ver memes.
 * PermissionController: controle onde e como os usuários podem usar cada comando
 * Sistema de atualização/restart.*

*: É necessário o uso de um script de incialização específico, veja mais abaixo.


## Update System

Para os comandos de update/restart funcionarem, é necessário o uso dos scripts abaixo:

<details> 
  <summary>Windows</summary>
  
  [Download](https://github.com/davipatury/FabaoBot/releases/download/v1.2/start.bat)
   ```bash
@echo off

java -jar FabaoBot.jar

if %ERRORLEVEL% EQU 2 (
   echo Restarting required...
   call start.bat
)

if %ERRORLEVEL% EQU 3 (
   echo Updated required, restarting...
   del "FabaoBot.jar"
   ren FabaoBot-updated.jar FabaoBot.jar
   call start.bat
)
```
</details>

<details> 
  <summary>Linux</summary>
   
   [Download](https://github.com/davipatury/FabaoBot/releases/download/v1.2/start.sh)
   ```bash
#!/bin/bash

java -jar FabaoBot.jar

if [ $? -eq 2 ]; then
   echo Restarting required...
   source start.sh
fi

if [ $? -eq 3 ]; then
   echo Updated required, restarting...
   rm "FabaoBot.jar"
   mv FabaoBot-updated.jar FabaoBot.jar
   source start.sh
if
```
</details>

## PermissionController

Atualmente só é possível administrar as permissões de cada comando editando o arquivo `data/permission.json`, em breve existirá comandos para controle de permissões.

#### Lista de permissões

 * ADMINISTRATOR
 * ALL_PERMISSIONS
 * BAN_MEMBERS
 * CREATE_INSTANT_INVITE
 * KICK_MEMBERS
 * MANAGE_CHANNEL
 * MANAGE_EMOTES
 * MANAGE_PERMISSIONS
 * MANAGE_ROLES
 * MANAGE_SERVER
 * MANAGE_WEBHOOKS
 * MESSAGE_ADD_REACTION
 * MESSAGE_ATTACH_FILES
 * MESSAGE_EMBED_LINKS
 * MESSAGE_EXT_EMOJI
 * MESSAGE_HISTORY
 * MESSAGE_MANAGE
 * MESSAGE_MENTION_EVERYONE
 * MESSAGE_READ
 * MESSAGE_TTS
 * MESSAGE_WRITE
 * NICKNAME_CHANGE
 * NICKNAME_MANAGE
 * VIEW_AUDIT_LOGS
 * VOICE_CONNECT
 * VOICE_DEAF_OTHERS
 * VOICE_MOVE_OTHERS
 * VOICE_MUTE_OTHERS
 * VOICE_SPEAK
 * VOICE_USE_VAD