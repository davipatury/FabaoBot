/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.modules;

import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.commands.music.*;
import net.davipatury.fabaobot.commands.Command;
import net.davipatury.fabaobot.controllers.CommandController;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONObject;

/**
 *
 * @author Davi
 */
public class MusicModule extends Module {

    public MusicModule(CommandController commandController) {
        super(commandController);
    }

    @Override
    public String getName() {
        return "Music";
    }

    @Override
    public Module generateCommands() {
        commands = new Command[]{
            new PlayCommand(this),
            new SkipCommand(this),
            new NowPlayingCommand(this),
            new VolumeCommand(this),
            new QueueCommand(this),
            new RemoveCommand(this)
        };
        return this;
    }
    
    @Override
    public boolean canProcess(final MessageReceivedEvent event, final Command command, final String[] parameters, final FabaoBot bot) {
        return event.getGuild() != null;
    }
    
    @Override
    public JSONObject customizeOptions(final JSONObject options) {
        final JSONObject voiceChannels = new JSONObject();
        final JSONObject textChannels = new JSONObject();
        commandController.getBot().getJDA().getGuilds().forEach(guild -> {
            voiceChannels.put(guild.getId(), guild.getVoiceChannels().get(0).getId());
            textChannels.put(guild.getId(), guild.getPublicChannel());
        });
        return options.put("voice_channels", voiceChannels).put("text_channels", textChannels);
    }
}
