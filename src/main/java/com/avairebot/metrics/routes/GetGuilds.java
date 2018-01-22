package com.avairebot.metrics.routes;

import com.avairebot.contracts.metrics.SparkRoute;
import com.avairebot.metrics.Metrics;
import net.dv8tion.jda.core.entities.Guild;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

public class GetGuilds extends SparkRoute {

    public GetGuilds(Metrics metrics) {
        super(metrics);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.type("application/json");

        String[] ids = request.params("ids").split(",");

        JSONObject root = new JSONObject();
        for (String id : ids) {
            try {
                Guild guildById = metrics.getAvaire().getGuildById(Long.parseLong(id));
                if (guildById == null) {
                    root.put(id, JSONObject.NULL);
                    continue;
                }

                JSONObject guild = new JSONObject();

                guild.put("id", guildById.getId());
                guild.put("name", guildById.getName());
                guild.put("region", guildById.getRegion().getName());
                guild.put("icon", guildById.getIconUrl());

                JSONObject owner = new JSONObject();
                owner.put("id", guildById.getOwner().getUser().getId());
                owner.put("username", guildById.getOwner().getUser().getName());
                owner.put("discriminator", guildById.getOwner().getUser().getDiscriminator());
                owner.put("avatar", guildById.getOwner().getUser().getEffectiveAvatarUrl());

                guild.put("owner", owner);
                root.put(id, guild);
            } catch (NumberFormatException e) {
                root.put(id, JSONObject.NULL);
            }
        }

        return root;
    }
}
