package net.davipatury.fabaobot.commands;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import net.davipatury.fabaobot.FabaoBot;
import net.davipatury.fabaobot.FabaoUtils;
import net.davipatury.fabaobot.modules.Module;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Davi
 */
public class EvalCommand extends Command {

    public EvalCommand(Module module) {
        super(module);
    }
    
    @Override
    public void processCommand(final MessageReceivedEvent event, final String[] params, final FabaoBot bot) {
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        String word = FabaoUtils.arrayToString(params);

        if(word.contains("getToken") || word.contains("bot_token")) {
            channel.sendMessage("Safado!").queue();
            return;
        }
        
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

        String script = 
                "var imports = new JavaImporter(com.mashape.unirest.http.Unirest, java.net.URLEncoder, org.json.JSONObject, org.apache.commons.io.IOUtils);" +
                "function execute() {\n" +
                "  with (imports) {" +
                "    "+word+"\n" +
                "  }};";

        engine.put("bot", bot);
        engine.put("event", event);
        engine.put("message", message);
        engine.put("command", this);
        engine.put("params", params);
        engine.put("channel", channel);

        engine.put("guild", event.getGuild());
        engine.put("selfMember", event.getGuild().getSelfMember());
        engine.put("member", event.getMember());

        engine.put("selfUser", bot.getJDA().getSelfUser());

        engine.put("author", message.getAuthor());

        engine.put("jda", bot.getJDA());

        try {
            engine.eval(script);
            String str = String.valueOf(engine.eval("execute();"));
            if(word.contains("return")) {
                channel.sendMessage("Output: \n```\n" + str + "\n```").queue();
            }
            message.addReaction("\u2705").queue();
        } catch (ScriptException e) {
            channel.sendMessage("**Exception:** \n```\n" + e.getMessage() + "```").queue();
            message.addReaction("\u274C").queue();
        }
    }
	
    @Override
    public String getName() {
        return "eval";
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[]{Permission.ADMINISTRATOR};
    }
	
    @Override
    public String[] getAliases() {
        return new String[]{"evaluate"};
    }

    @Override
    public boolean verifyParameters(String[] parameters) {
        return (parameters.length >= 1);
    }
    
}
